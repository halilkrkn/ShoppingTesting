package com.example.shoppingtesting.ui.views

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.shoppingtesting.R
import com.example.shoppingtesting.adapters.ShoppingItemAdapter
import com.example.shoppingtesting.data.local.ShoppingItem
import com.example.shoppingtesting.getOrAwaitValue
import com.example.shoppingtesting.launchFragmentHiltContainer
import com.example.shoppingtesting.ui.viewmodels.ShoppingViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import javax.inject.Inject

// Integration Test yaptığımız için ve iki fragment arasındaki etkileşimi test etmek istiyoruz.
// Bu yüzden MediumTest annotation'unu kullandık.
// HiltAndroidTest annotation'unuda ekliyoruz çünkü bu test sınıfında hilt'i kullanmak istiyoruz.
@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ShoppingFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var testFragmentFactory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    // Navigation'ı yani fragmentler arasındaki sayfa geçişlerinin testlerini yapmak önemlidir.
    // Bu yüzden Navigation'ı test etmek için Mock kütüphanesini kullanıyoruz.
    // Mock aslında bir nevi test double'dır yani test bir test dublörüdür/simülasyonudur.
    @Test
    fun clickAddShoppingItemButton_navigateToAddShoppingItemFragment() {

        // Burada mockito kütüphanesini kullandık ve navController için sahte bir nesne oluşturmak istediğimiz sınıfı belirleyip mock içerisine atadık.
        // Yani böylelikle mock sayesinde navController'ın sahte bir nesnesini oluşturmuş olduk.
        val navController = mock(NavController::class.java)

        // İlgili fragmentlarımız üzerinden navigation işlemi yapmak için launchFragmentHiltContainer lambda fonksiyonunu çağırdık.
        // Ve bu ilgili fragment'ımızın navController'ını gerçekten bu sahte navController ile değiştirmek için bu lambda fonk. çağırdık.
        launchFragmentHiltContainer<ShoppingFragment> {

            // Test dosyamız içerisinde ilgili ShoppingFragment'imizde NavigationController işlevlerini sağladık.
            Navigation.setViewNavController(requireView(),navController)
        }

        // Şimdi ise ShoppingFragment içerisindeki Action Button'a tıklanma olayını test etmek için Espresso Kütüphanesini kullanıyoruz.
        // Espresso kütüphanesi Integration Test'ler kullanılan kullanıcı ara birimi yani UI'yı görünümü test eden bir kütüphanedir.

        // Ve ActionButton için Espresso kullanmak için sadece onView() üzerinde işlem yapıyoruz.
        // withId() ile de hangi görünümü kastettiğimizi belirlemek için o görünümün res dosyasındaki ilgili fragment'teki id'sini yazıyoruz.
        // En son ise Action Button için neyi test etmesi gerrektiğini söylüyoruz. Yani aşağıda click işlemi gerçekleştirilmesini test etmek istiyoruz.
        onView(withId(R.id.fabAddShoppingItem)).perform(click())

        // Şimdi NavController ile gezinme yaparken gerçekten doğru parametre ile çağrılıp/çağrılmadığını doğrulamak istiyoruz.
        // Bu doğrulama işlemini de Mockito'dan verify() fonk. ile gerçekleştiriyoruz.
        // Verify() fonksiyonunun içerisine mock ile oluşturduğumuz sahte bir navController nesnemizi atıyoruz ve ShoppingFragmenttan AddShoppingItemFragment'a navigate olup olmadığını kontrol ediyoruz.
        verify(navController).navigate(
            ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
        )

        // Bir çok senaryoda yararlı olan şeyleri ilgili senaryoya göre ne zaman ihtiyacımız olacağını belirleyebiliriz.
        // Bunuda Mockito'nun `when`() fonksiyonu sayesinde yapabiliriz.
        // Örneğin; navController ile popBackStack() durumunu kontrol edebiliriz. Ve burada thenReturn ilede true/false ile popBackStack işlevini test edebiliriz.
        // Yani aslında burda anlatılmak istenen Mockito nesnelerinin (navController gibi) return değelerini gerçekten değiştirebileceğimiz görmüş olduk.
        // Ama bu kod bloğu bu Test dosyammız için uygun olmadığı için kullanmıyoruz.
//        `when`(navController.popBackStack()).thenReturn(false)

    }

    // Uygulama içerisinde Swipe ile database'den veri silme olayının test senaryosu.
    @Test
    fun swipeShoppingItem_deleteItemDb() {
        val shoppingItems = ShoppingItem("TEST",1,1f,"TEST", 1)
        var testViewModel: ShoppingViewModel ?= null
        launchFragmentHiltContainer<ShoppingFragment> (fragmentFactory = testFragmentFactory){
            testViewModel = viewModel
            viewModel?.insertShoppingItemIntoDb(shoppingItems)
        }

        onView(withId(R.id.rvShoppingItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ShoppingItemAdapter.ShoppingItemViewHolder>(
                0,
                swipeLeft()
            )
        )

        assertThat(testViewModel?.shoppingItem?.getOrAwaitValue()).isEmpty()
    }

}