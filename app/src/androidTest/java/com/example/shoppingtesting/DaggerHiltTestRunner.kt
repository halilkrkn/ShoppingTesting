package com.example.shoppingtesting

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

// Bu androidTest dizinindeki bu sınıfımızda Dagger-Hilt'i Test işlemlerine dahil etmek için ilk etapta yapmamız gereken herşey zaten bu sınıfta tanımladık.
// Burada oluşturduğumuz bu sınıfı Test dosyalarına işlemlerine dahil etmek için build.gradle(app) de defaultConfig'deki androidTest içerisinde testlerin çalışması için olan
// testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"'ın içerisindeki adresin yerine burada oluşturduğumuz sınıfın yolunu veriyoruz.
// Bu sınıfa zaten AndroidJUnitRunner'ı da zaten inherite ettiği için aslında Hilt'i test işlemlerine dahil etmiş oluyoruz.
class DaggerHiltTestRunner: AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?, // Bu sınıf adı bizim gerçek uygulamamızın sınıf adıdır. Bu nedenle manifests'de tanımladığımız ve HiltAndroidApp annotation'ını kullandığımız ShoppingApplication sınıfıdır.
        context: Context?
    ): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
//        HiltTestApplication::class.java.name ile HiltAndroidApp annatotion sayesinde kaydettiğimiz ShoppingApplication Sınıfımızı  çağırmış olduk.
    }
}

