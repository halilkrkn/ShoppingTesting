package com.example.shoppingtesting.ui.views

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoActivityResumedException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.shoppingtesting.R
import com.example.shoppingtesting.data.local.ShoppingItem
import com.example.shoppingtesting.getOrAwaitValue
import com.example.shoppingtesting.launchFragmentHiltContainer
import com.example.shoppingtesting.repositories.FakeShoppingRepositoryAndroidTest
import com.example.shoppingtesting.ui.viewmodels.ShoppingViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AddShoppingItemFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }


    // AddShoppingItemFragment'den geri butonu ile olan işlevin çalışma durumunun senaryosu.
    @Test
    fun pressBackButton_popBackStack() {

        // Mockito ile navController'ın sahte bir nesnesini oluşturduk.
        val navController = mock(NavController::class.java)

        // Burada ilgili launchFragmentHiltContainer generic lambda fonksiyonu ile ilgili fragment olan AddShoppingItemFragment'i atadık ve bu fragment üzerinde NavController işlemlerinini kullanmayı ayarladık.
        launchFragmentHiltContainer<AddShoppingItemFragment>(fragmentFactory = fragmentFactory){
            Navigation.setViewNavController(requireView(), navController)
        }

        // Burada Espresso kütüphanesi yardımı ile AddShoppingItemFragmentten ShoppingFragmente geri tuşu ile geri dönme işlemini test ediyoruz.
        try {
            pressBack()
        } catch (e: NoActivityResumedException) {
        }
        //Burada ise Mockito kütüphanesindeki verify() fonksiyonu ile popBackStack işlevinin doğruluğunu test ediyoruz.
        TestScope().launch {
            verify(navController).popBackStack()
        }
    }


    // Burada Add Butonuna tıklandıktan sonra database'e girilen değerlerin eklenip/eklenilmediğini test eden UI test senaryosu.
    // Uygulama içerisinden Database'e gerçekten bir veri ekleyip/eklenilmediğini test ediyoruz.
    @Test
    fun clickInsertIntoDb_shoppingItemInsertedIntoDb() {
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        launchFragmentHiltContainer<AddShoppingItemFragment>(fragmentFactory = fragmentFactory) {
            viewModel = testViewModel
        }

        // Amacımız; Edittext'lerimize girdiğimiz değerleri ve Database'e eklemek için Add Butonunu Espresso ile simüle edip test etmek.
        onView(withId(R.id.etShoppingItemName)).perform(replaceText("shopping item"))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("5"))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("5.5"))
        onView(withId(R.id.btnAddShoppingItem)).perform(click())

        // Espresso ile UI testler/işlemler yapıldıktan sonra ise Edittext ile girilen değerlerin ve add butonu ile ilgili değerlerin database başarıyla eklenmesi durumunu da Truth kütüphanesi ile asserThat işlevine koyup test ediyoruz.
        // Şimdi düzenleme(replayText) metinlerimize geçerli değerleri girdiğimizden emin olduk ve butona tıklandığında bu değerlerin gerçekten veritabanına eklendiğini iddia etmek istiyoruz yani assertThat() işlemi yapıyoruz..
        assertThat(testViewModel.shoppingItem.getOrAwaitValue())
            .contains(ShoppingItem("shopping item", 5, 5.5f, ""))
    }
}