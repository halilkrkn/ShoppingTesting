package com.example.shoppingtesting.di

import android.content.Context
import androidx.room.Room
import com.example.shoppingtesting.data.local.ShoppingItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

// Burda oluşturduğumuz sınıf sayesinde AndroidJUnitRunner çağırdık ve ShoppingDaoTest sınıfında RunWith annotation'u içerisinde bu sınıfı kullanacağız.
// Bu sınıfta zaten AndroidJUnitRunner'ı çağırdığımız için ShoppingDaoTest sınıfında hiç bir sorun olmayacaktır. Böylelikle Hilt'i Test işlemlerine dahil etmiş oluyoruz.
@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    //  @Singleton test içerisinde singleton'ı kullanmıyoruz. Çünkü her test durumu için yeni bir örnek oluşturmak istiyoruuz. Yani burada tekil olmasını istemiyoruz.
    //  Burda inMemoryDatabaseBuilder'ı kullanıyoruz. Çünkü veritabanını oluşturmamız gerekiyor ama inMemoryDatabaseBuilder ile fake bir database oluşturuyoruz.
    //  Aslında inMemoryDatabaseBuilder ile verileri gerçek bir veritabanının içeriside değilde, yalnızca verileri Ram'de tutacağımız anlamına geliyor.
    //  Bu şekilde test daha sağlıklı ve güvenli bir şekilde yapılmış olacak. Böylelikle her test senaryosu için gerçek veritabanını oluşturulmayacak.
    // Bu allowMainThreadQueries() fonk. ile veritabanına Main thread'den daha iyi erişmemize izin veriyoruz yani veritabanımıza erişimize izin veriyoruz, genellikle bir room veritabanı veya herhangi bir veritabanı kullanıyorsa
    // bu veritabanına bir arkaplan Thread'den erişmek ve bu veri tabanı aracılığıyla yazmak ve okumak için kullanıyoruz. Çünkü thread'ler birbirini manipüle edebilir ve test işlemini zorlaştırır.
    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context,ShoppingItemDatabase::class.java)
            .allowMainThreadQueries()
            .build()
}