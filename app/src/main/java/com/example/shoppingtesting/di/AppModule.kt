package com.example.shoppingtesting.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shoppingtesting.R
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


// Burada aslında ilgili class'larımızın örneklerini(instance) oluşturuyoruz ve böylece her yere inject ederek bağımlılık kazandırmış oluyoruz.
// Aslında nerde bir constructer ile ilgili class'ları çekmemiz gerekiyorsa o class'ları AppModule'de insatancelarını oluşturup inject constructer ile bağımlılık kazandırmış oluyoruz.
// Tabi bu class içerisinde de inject işlemi ile ilgili class'larımızı inject edip bağımlılık kazandırmış oluyoruz.
// Buradaki class'ların instance'larını oluşturmak için @Singleton ve @Provides annotation'ı kullanıyoruz.
// Çünkü Singleton ile tekil bir yapıda olmasını, Provides ile de ilgili class'ların sağlayıcı bir yapıda olmasını sağlatıyoruz.
// Ve buradaki bir diğer önemli nokta ise oluşturulan fonksiyonlar provide ile başlaması.
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
    ) = DefaultShoppingRepository(dao, api) as ShoppingRepository


    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_baseline_image_search_24)
            .error(R.drawable.ic_baseline_image_search_24)
    )

    @Singleton
    @Provides
    fun provideShoppingDao(
        database: ShoppingItemDatabase
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayApi(): PixabayAPI {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(PixabayAPI::class.java)
    }

}