package com

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

// Burada sadece Main Dispatcher olmayan özel bir Dispatcherı kullanacağız.
// Buraya yazılan bu kural ihtiyaç duyduğumuz tüm test sınıflarımızda tüm projelerimizde kullanabiliriz.
@ExperimentalCoroutinesApi
class MainDispatcherCoroutineRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
): TestWatcher() {
// TestWatcher, test kuralı arayüzünü uyguluyor ve bu sınıfı JUnit testleri için gerçek bir kural yapan  sınıftır.

    // Bu kural ile bir Coroutine başlattığımızda aslında bu kural aktifse ve orada bir sorgu başlatırsak o zaman Dispatcher'ları kullanmak istiyoruz.
    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    // Bu kural ile başlatmış olduğumuz coroutine kuralımızda yaptığımız şeyleri geri alırız yani resetleriz varsayılan hale getiririz..
    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}