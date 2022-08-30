/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.shoppingtesting

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Gets the value of a [LiveData] or waits for it to have one, with a timeout.
 *
 * Use this extension from host-side (JVM) tests. It's recommended to use it alongside
 * `InstantTaskExecutorRule` or a similar mechanism to execute tasks synchronously.
 */

/*
 // LiveData'yı Testing işlemlerinde kullanacabilmek için Google tarafından yazılan bir methoddur.
 // Burda LiveData için bir getOrAwaitValue adında fonksiyon mevcut.
 // Bu fonksiyon sayesinde livedata genişliyor ve böylelikle bu fonksiyonu livedata nesnelerinde kullanmamıza olanak sağlıyor.
 // Buradaki fonksiyon parametrelere de sahip bu parametreler time, timeUnit, afterObserve parametreleridir.
 // time parametresi 2'e eşittir. Burda iki saniyeye ayarlanmış bir zaman aşımı oluşturulmuştur.
 // timeUnit parametresinde ise time'ın tipi belirtilmiş ve saniye cinsinden zaman aşımı oluşturulması sağlanmıştır.
 // afterObserve parametresi ise Observable bir işlem yapıldıktan sonraki bir fonksiyondur.
 // Yani bu fonksiyonun yaptığı şey, Bu fonksiyonu çağırdığımız bu livedata bir değer döndürene kadar bekleyecek, bu nedenle bu aslında varsayılan olarak
 2 saniyeden uzun sürerse, bu fonksiyon içerisindeki zaman aşımmı TimeoutException'ı devreye girecek. Bu durum şuan kullandığımız database de olmayacak.
 // Ama bu durumu apiden gelen verileri livedata ile gözlemlediğimizde karşımıza çıkabilir.
 */
@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    try {
        afterObserve.invoke()

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

    } finally {
        this.removeObserver(observer)
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}