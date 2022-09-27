package com.example.shoppingtesting.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingtesting.data.local.ShoppingItem
import com.example.shoppingtesting.data.remote.responses.ImageResponse
import com.example.shoppingtesting.other.Constants
import com.example.shoppingtesting.other.Event
import com.example.shoppingtesting.other.Resource
import com.example.shoppingtesting.repositories.ShoppingRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val  shoppingItem = repository.observeAllShoppingItem()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _curImageUrl = MutableLiveData<String>()
    val curImageUrl: LiveData<String> = _curImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    // Apiden alacağımız resimlerin url'i
    fun setCurImageUrl(url: String) {
        _curImageUrl.value = url
    }

    // database'den veri silme
    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    // database'e veri ekleme
    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }


    // Database'e veri ekleme işlemi yaptık ve herbir veri için gereken koşularıda yazdık.
    fun insertShoppingItem(name: String, amount: String, price: String) {

        if (name.isEmpty() || amount.isEmpty() || price.isEmpty()) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("The fields must not be empty",null)))
            return
        }

        if (name.length > Constants.MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue((Event(Resource.error("The name of the item must not exceed ${Constants.MAX_NAME_LENGTH} characters", null))))
            return
        }

        if (price.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("The price of the item must not exceed ${Constants.MAX_PRICE_LENGTH} characters", null)))
            return
        }

        val amountString = try {
            amount.toInt()
        }catch (e: Exception){
            _insertShoppingItemStatus.postValue(Event(Resource.error("Please enter a valid amount", null)))
            return
        }

        val shoppingItem = ShoppingItem(name,amountString,price.toFloat(),"")
        insertShoppingItemIntoDb(shoppingItem)
        setCurImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    // Apiden gelen veri için gereken koşulu ve işlemleri yazdık.
    fun searchForImage(imageQuery: String) {
        if (imageQuery.isEmpty()) {
            return
        }
        _images.postValue(Event(Resource.loading(null)))
        viewModelScope.launch {
            val response = repository.searchForImages(imageQuery)
            _images.value = Event(response)
        }
    }

}