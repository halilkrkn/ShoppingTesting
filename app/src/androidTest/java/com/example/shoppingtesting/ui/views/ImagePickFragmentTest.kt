package com.example.shoppingtesting.ui.views

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.shoppingtesting.R
import com.example.shoppingtesting.adapters.ImageAdapter
import com.example.shoppingtesting.getOrAwaitValue
import com.example.shoppingtesting.launchFragmentHiltContainer
import com.example.shoppingtesting.repositories.FakeShoppingRepositoryAndroidTest
import com.example.shoppingtesting.ui.viewmodels.ShoppingViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@HiltAndroidTest
@MediumTest
@ExperimentalCoroutinesApi
class ImagePickFragmentTest {

     @get:Rule
     var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: FragmentFactory


    @Before
    fun setup() {
        hiltRule.inject()
    }

    // Burada ImageAdapter üzerinden image'lere click özellik getirdik ve bu durumdan sonra setImageUrl'e ilgili string değeri atayıp geri tuşu ile AddShoppingItemFragment'e dönüş işlemini test eden bir senaryo.
    // Burada oluşturmuş olduğumuz bir RecyclerView'daki bir öğeye tıklama olayını test etmek istiyoruz.
    // Buradaki test senaryomuzda bir floatActionButton'a tıklama olayı gibi view'ları test etmiyoruz/simüle etmiyoruz.
    // O yüzden build.gradle(app) dosyamıza Viewları test etmemize olanak sağlayan Espresso kütüphanesinde var olan androidTestImplementation("androidx.test.espresso:espresso-contrib:3.4.0") implementation'ınını ekliyoruz.
    // Burada implematation olarak eklediğimiz library ile sadece RecyclerView'da click özelliği olan viewlara tıklama özelliğini test/simüle eder.
    // Burada RecyclerView'ımız olaran ImageAdapter'e bir item eklemek için imageUrl değişkeni oluşturup içerisine TEST adında string değeri veriyoruz.
    // Sonra launchFragmentHiltContainer içerisinde ImagePickFragment'ımız aracılığı ile imageAdapter'ımıza erişip  bu imageUrl değişkenimizi RecyclerView'ımızda item olarak ekletiyoruz.
    // Tüm ekleme işlemlerini sadece bu test classımızı üzerinden yaparak test ediyoruz.
    // RecyclerView'ımız ImagePickFragment içerisinde gözükmesi için ShoppingViewModelimize erişip içerisinede imageUrl'imizi vermek zorundayız.
    // O yüzden testViewModel adında bir değişken oluşturup ShoppingViewModel'imize eşitledik.
    @Test
    fun clickImage_popBackStackAndSetImageUrl() {
        val navController = mock(NavController::class.java)
        val imageUrl = "TEST"
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        launchFragmentHiltContainer<ImagePickFragment>(fragmentFactory = fragmentFactory){
            Navigation.setViewNavController(requireView(),navController)
            imageAdapter.images = mutableListOf(imageUrl)
            viewModel = testViewModel
        }

        // onView() içerisinde click özelliği getirdiğimiz view'ın yani fragment_image_pick.xml dosyamızdaki RecyclerView'ımızın id'sini çağırıyoruz.
        // Sonra ise RecyclerView üzerinden item'a tıklama olayını gerçekleştirmek için perform() fonk. içerisine RecyclerViewActions class'ından actionOnItemAtPosition fonk. çağırıp ImageAdapter'da oluşturduğumuz ImageViewHolder inner class'ımızı generic içerisine koyuyoruz.
        // Ve actionOnItemAtPosition fonk. içerisine ise RecyclerView içerisindeki liste itemlarının pozisyonunu veriyoruz(biz 0. itemı verdik) ve click() fonk. çağırıp ilgili itema/itemlara tıklama özelliği getiriyoruz.
        // Tıklama olayının olmasını istediğimiz pozisyonu 0 olarak seçerek click() fonk. ile ilk item'a tıklama özelliği getirdik.
        onView(withId(R.id.rvImages)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageAdapter.ImageViewHolder>(
                0,
                click()
            )
        )

        // verify() fonk. kullanarak popBackStack olayını test ediyoruz.
        verify(navController).popBackStack()

        // Truth kütüphane içerisindeki assertThat() fonk. ile de viewModel üzerinden image'in Url'ini veriyoruz ve bunu 56. satırdaki imageUrl değişkenimize eşitliyoruz.
        // Böylelikle PixabayApi'den bu RecyclerView'ımız için gerçekten bir şey göstermesini herhangi bir istekte bulunmak istemiyoruz.
        // Çünkü test senaryolarında herhangi bir api çağrısı yapmak istemiyoruz ve imageUrl'ini biz kendimiz manuel olarak  veriyoruz  böylelikle sanki apiden gelen image'in url'i geliyormuş gibi test etmiş oluyoruz.
        assertThat(testViewModel.curImageUrl.getOrAwaitValue()).isEqualTo(imageUrl)
    }

    /*
        - Espresso ile test yaparken RecyclerView'ımız üzerinden görünüm testi yaptığımızdan bazı animasyonlar test işlemini öngrülemeyen sonuçlara götürdüğünden dolayı
        emülatörümüzün animasyon işlemlerini devre dışı(disable) yapmalıyız ki test işlemimiz doğru bir şekilde çalışsın.
        - O yüzdem bu animasyonları disable etmek için terminalden üç tane komut işlemi gireceğiz bunlar;
            - adb shell settings put global window_animation_scale 0
            - adb shell settings put global transition_animation_scale 0
            - adb shell settings put global animator_duration_scale 0
          komutları ile emülatör üzerinden animasyonları devre dışı bırakabiliriz.
            - İlgili komutlar için;
                https://stackoverflow.com/questions/43060792/how-do-you-disable-animations-at-the-system-level-for-an-android-device-emulator

       - Eğer terminaldem adb ile sorun yaşarsanız ilgili çözümü kullanabilirsiniz.
            https://stackoverflow.com/questions/20564514/adb-is-not-recognized-as-an-internal-or-external-command-operable-program-or
     */

}