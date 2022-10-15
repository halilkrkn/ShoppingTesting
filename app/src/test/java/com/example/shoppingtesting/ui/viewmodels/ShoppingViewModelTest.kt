package com.example.shoppingtesting.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shoppingtesting.MainDispatcherCoroutineRule
import com.example.getOrAwaitValue
import com.example.shoppingtesting.other.Constants
import com.example.shoppingtesting.other.Status
import com.example.shoppingtesting.repositories.FakeShoppingRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    // Coroutine Main Dispatcher, yalnızca gerçek bir uygulama senaryosunda mevcut olan ana döngüye dayandığında testlerde sahip olmadığımız Main Dispatcher'ı kullandığını görebilirsiniz.
    // Gerçek bir uygulama başlatırsak ancak burada testlerimizi jvm üzerinde çalıştırdığımız test setimizin içinde olduğumuzdan, o gerçek uyguulama ortamına sahip değiliz.
    // Dolayısıyla Main Dispatcher'a da erişimimiz yok.
    // Testlerimizi androidTest dizinimizde(dosyası içerisinde) yazsaydık bu bir sorun olmadı çünkü o zaman testlerimizi gerçek bir android cihaz üzerinde test etmiş olurduk.
    // Ama burada yani test dizimiz(dosyamız) içerisinde halledebilmemiz için kendi JUnit kuralı (get:Rule) tanımlamamız gerekiyor.
    // Buradaki MainDispatcherCoroutineRule() sınıfını kendimiz oluşturduk ve test dosyası içerisinde. Bu dosya sayesinde MainDispatcher kuralı oluşturduk ve bu kuralı ShoppingViewModelTest'imizde object'ini oluşturduk.
    @get:Rule
    var mainDispatcherCoroutineRule = MainDispatcherCoroutineRule()



    // LiveData kullanımınu Test ortamında kullanabilmemiz için get:Rule ile bir kural oluşturuyoruz.
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ShoppingViewModel

    // Burada test senaryolarının herbirinden önce yürütülen işlemler için Before annotations'ını kullanıyorduk.
    // O yüzden burada tüm test senaryolarımızdan önce tekrar tekrar başlatmamak için viewmodel'imizi setup fonk.nunda başlatıyoruz..
    // Burada normalde DefaultShoppingRepository class'ını kullanamamız gerekirdi ama o zaman Db ve Api ye ihtiyacı vardı. Buda test senaryosunda kullanılmasının çok istenilen bir şey değil
    // Çünkü Unit Testlerimizde Db ve Api çağrısı yapmak fazlaca zaman alır. UUnit Testlerimizde amacımız hızlı işlemlerle ilgili durumları test etmektir.
    // O yüzden burda Test dosyamız içerisinde repositories dosyasında oluşturduğumuz FakeShoppingRepository'i kullanacaz.
    // Bu FakeShoppingRepository'de db işlemlerlerini liste yapıları sayesinde yaparak ve api işlemlerinide ağ hatalarını kontrol edecek bir fonksiyonlarla
    // gerçek repository'inin yapması gerektiği işlemleri simüle ederek yaptık.
    // Bu şekilde Unit Test'imiz daha hızlı sonuçlarla test senaryolarımızı test edebileceğiz.
    @Before
    fun setup() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }


    // Burada database'e veri ekleme işleminde viewModeli test etmek için istenilen test senoryosu.
    // DB'ye veri eklemek için eğer herhangi alan/alanlar boş ise ise test senaryomuz ERROR döndürecektir
    @Test
    fun `insert shopping item with empty field, returns error`() {

        viewModel.insertShoppingItem("name","","3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }



    // DB'ye veri eklemek için eğer bir name alanı 20 karakterden fazla ise test senaryomuz ERROR döndürecektir
    @Test
    fun `insert shopping item with too long name, returns error`() {
        // Burada name max uzunluğu aşması durumu için for loop içerisin koşul verdik.
        val name = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH + 1){
                append(1)
            }
        }

        viewModel.insertShoppingItem(name,"5","3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    // DB'ye veri eklemek için eğer bir price alanı 10 karakterden fazla ise test senaryomuz ERROR döndürecektir
    @Test
    fun `insert shopping item with too long price, returns error`() {
        // Burada price max uzunluğu aşması durumu için for loop içerisin koşul verdik.
        val price = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH + 1){
                append(1)
            }
        }

        viewModel.insertShoppingItem("name","5",price)

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    // DB'ye veri eklemek için eğer bir amount alanı çok fazla karakterden oluşuyor ise test senaryomuz ERROR döndürecektir
    @Test
    fun `insert shopping item with too high amount, returns error`() {
        // Burada name max uzunluğu aşması durumu için for loop içerisin koşul verdik.
        viewModel.insertShoppingItem("name","99999999999999999999999999","3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }


    // DB'ye veri eklemek için eğer tüm elemanlar doğru bir şekilde DB'ye eklendi ise test senaryomuz SUCCESS dönderecektir
    @Test
    fun `insert shopping item with valid input, returns success`() {
        // Burada name max uzunluğu aşması durumu için for loop içerisin koşul verdik.
        viewModel.insertShoppingItem("name","5","3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
}
























