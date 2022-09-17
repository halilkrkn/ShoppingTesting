package com.example.shoppingtesting.data.remote

import com.example.shoppingtesting.BuildConfig
import com.example.shoppingtesting.data.remote.responses.ImageResponse
import com.example.shoppingtesting.data.remote.responses.ImageResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


// Burada Fotoğrafları search işlemi aratıp karşımıza getirmek için Pixabay'den api sorgusu yaptırdık.
interface PixabayAPI {
    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): Response<ImageResponse>
}