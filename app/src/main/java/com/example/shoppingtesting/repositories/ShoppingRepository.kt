package com.example.shoppingtesting.repositories

import androidx.lifecycle.LiveData
import com.example.shoppingtesting.data.local.ShoppingItem
import com.example.shoppingtesting.data.remote.responses.ImageResponse
import com.example.shoppingtesting.other.Resource
import retrofit2.Response

interface ShoppingRepository {

    // Room Database'için
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItem(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>


    // Api için
    suspend fun searchForImages(searchQuery:String): Resource<ImageResponse>
}