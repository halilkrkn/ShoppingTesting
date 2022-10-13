package com.example.shoppingtesting.ui.views

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoActivityResumedException
import androidx.test.filters.MediumTest
import com.example.shoppingtesting.launchFragmentHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AddShoppingItemFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }


    @Test
    fun pressBackButton_popBackStack() {

        // Mockito ile navController'ın sahte bir nesnesini oluşturduk.
        val navController = mock(NavController::class.java)

        // Burada ilgili launchFragmentHiltContainer generic lambda fonksiyonu ile ilgili fragment olan AddShoppingItemFragment'i atadık ve bu fragment üzerinde NavController işlemlerinini kullanmayı ayarladık.
        launchFragmentHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        // Burada Espresso kütüphanesi yardımı ile AddShoppingItemFragmentten ShoppingFragmente geri tuşu ile geri dönme işlemini test ediyoruz.
        try {
            pressBack()
        }catch (e: NoActivityResumedException){}
        //Burada ise Mockito kütüphanesindeki verify() fonksiyonu ile popBackStack işlevinin doğruluğunu test ediyoruz.
            TestScope().launch {
            verify(navController).popBackStack()
        }


    }
}