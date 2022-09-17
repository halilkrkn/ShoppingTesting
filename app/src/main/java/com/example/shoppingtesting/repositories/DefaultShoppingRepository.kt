package com.example.shoppingtesting.repositories

import androidx.lifecycle.LiveData
import com.example.shoppingtesting.data.local.ShoppingDao
import com.example.shoppingtesting.data.local.ShoppingItem
import com.example.shoppingtesting.data.remote.PixabayAPI
import com.example.shoppingtesting.data.remote.responses.ImageResponse
import com.example.shoppingtesting.other.Resource
import com.example.shoppingtesting.other.Status
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItem(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItem()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImages(imageQuery: String): Resource<ImageResponse> {

        return try {
            val response = pixabayAPI.searchForImage(imageQuery)
            if (response.isSuccessful) {
                response.body()?.let { imageResponse ->
                    return@let Resource.success(imageResponse) // Apiden gelen verilerin hepsi başarılı ise bu kod bloğu çalışacak.
                } ?: Resource.error("An unknown error occured.", null)
            } else {
                Resource.error("An unknown error occured.", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }

}