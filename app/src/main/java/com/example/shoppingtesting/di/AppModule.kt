package com.example.shoppingtesting.di

import android.content.Context
import androidx.room.Room
import com.example.shoppingtesting.data.local.ShoppingDao
import com.example.shoppingtesting.data.local.ShoppingItemDatabase
import com.example.shoppingtesting.data.remote.PixabayAPI
import com.example.shoppingtesting.other.Constants.BASE_URL
import com.example.shoppingtesting.other.Constants.DATABASE_NAME
import com.example.shoppingtesting.repositories.DefaultShoppingRepository
import com.example.shoppingtesting.repositories.ShoppingRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(context, ShoppingItemDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(
        dao: ShoppingDao,
        api: PixabayAPI
    ) = DefaultShoppingRepository(dao,api) as ShoppingRepository

    @Singleton
    @Provides
    fun provideShoppingDao(
        database: ShoppingItemDatabase
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayApi(): PixabayAPI {
        return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(PixabayAPI::class.java)
    }

}