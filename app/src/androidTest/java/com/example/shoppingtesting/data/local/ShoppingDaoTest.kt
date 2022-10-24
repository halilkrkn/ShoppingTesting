package com.example.shoppingtesting.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.shoppingtesting.getOrAwaitValue
import com.example.shoppingtesting.launchFragmentHiltContainer
import com.example.shoppingtesting.ui.views.ShoppingFragment
import com.example.shoppingtesting.ui.views.TestFragmentFactory
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

// Coroutine içerisindeki runTest'iyi tamamen kullanabilmek için bu annotation'u kullandık.
// Juni4'ü androidTest içerisinde çalıştırmamız gerektiği için burda Android Bileşenlerine ihtiyaç duydukları için emulatörde çalışır.
// Normalde jvm üzerinde çalışmadığı için testin emülatorde çalışması gerekiyor o yüzden böyle bir annotation yazdık.
// JUnit'e burda yazdıklarımızın birim testler olduğunu ve sadece bir tane integrated test kullanacağımız için SmallTest annotation kullandık.
// Eğer bu sınıfta iki tane integrated test kullansaydık o zaman MediumTest kullacaktık.
// Eğer bu sınıfta birden fazla integrated test ve ui test ile ngn testleri yazılsaydı o zaman LargeTest kullanacaktık.
@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest // @RunWith(AndroidJUnit4::class) Buradaki annotation yerine @HiltAndroidTest annotation'ı kullanıyoruz.
class ShoppingDaoTest {


    // Livedata testte sorun yaratmaması için yani varsayılan olarak eşzamansız(asynchronous)ve burada RunTestte multiTreadleri blocklamak için
    // bu test sınıfı içerisine içerideki tüm kodu birbiri ardına çalıştırmak istediğimizi söylememiz gerekiyor. Bu nedenle temelde aynı trread'de (iş parçacığında) birbiri ardına çalışıyor.
    // Bunu sözde bir Rule yani bir kural oluşturarak üstesinden gelebiliyoruz. Yani sözde bir anlık görev yürütücü(çalıştırıcı) olacak.
    // Yani JUnit'in @get:Rule annotationunu kullandık ve bir değişkene InstantTaskExecutorRule() Sınıfını atadık.
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Burada Inject işlemlerimizi yapabilmemiz için ve hilt'i sağlıklı bir şekilde kullanabilmemiz için Hilt'in vermiş olduğu HiltAndroidRule'u tanımlamış olduk.
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db") // burada yazdımız Named ile TestAppModule'deki provideInMemoryDb'in kurulumunu kastetmiş olduk inject işlemi yaparak.
    lateinit var database: ShoppingItemDatabase

    @Inject
    lateinit var testFragmentFactory: TestFragmentFactory

    private lateinit var dao: ShoppingDao

    // Before annotation'u bu sınıfın içerisindeki her test senaryosundan önce çalıştırılacak.
    // Burada her test senaryosundan önce veritabanı tekrardan oluşturulur.
    // Yani burdaki fonksiyonda veritabanımızı oluşturarak erişim sağlıyoruz ve database içerisindeki dao muza ulaşarak oradaki fonksiyonları test edeceğiz.
    @Before
    fun setup() {
        // Database'imizin kurulumunu androidTest dizinimizin içerisinde di klasöründeki AppModule'ü ile buu sınıfa inject ettik ve kuurulum işlemini yaptırmış olduk.
        // Artık test senrayolarında kullanılabilir vaziyette.
        hiltRule.inject()
        dao = database.shoppingDao() // database içerisindeki dao'yu çağırdıkki dao interface'imize bu şekilde ulaştık.
    }

    // After annotation ile bu sınıfın içerisinde tüm test senaryoları bittikten sonraki yazılacak işlevi yazarız.
    // Yani burda tüm test senaryoları bittikten sonra veritabanını kapatıyoruz.
    @After
    fun teardown() {
        database.close() // veritabanımızı kapattık.
    }


    // Oluşturduğumuz debug dosyamız içerisine Dagger-Hilt ile test senaryolarımızın fragmentlarda kullanılması için gereken işlemleri/işlevleri yazdık.
    // Böylelikle gerçek projedeki gibi fragmentlar içinde Hilt kurulumu yaptık ama test klasörlerinde çalışması içib com(debug) içerisinden bir HiltTestActivity class'ı oluşturduk ve debug dosyası içerisindeki androidManifest.xml dosyasına atadık.
    // Sonra ise androidTest dosyamız içerisine de HiltExtension kotlin dosyası oluşturarak test durumlarımız için mainActivity olarak HiltActivity i gösterdik ve fragmentlarımızı da bu mainActivity'mizde kullanmamızı sağlattık.
    // Şuan launchFragmentHiltContainer generic fonksiyonumuz sayesinde gerçek projedeki ShoppingFragment'i tanımladık.
    // Ve inline bir fonksiyon olduğu içinde lambda fonksiyon olarak da ShoppingFragmenttaki kodlara ulaşabilmemize olanak sağladık. Yani ShoppingFragment'ta erişim sağladık.
    @Test
    fun testLaunchFragmentInHiltContainer(){
        launchFragmentHiltContainer<ShoppingFragment>(fragmentFactory = testFragmentFactory) {
        }
    }

    // Burada test durumlarında eşzamanlılık istemiyoruz, bu yüzden MultiThread'leri coroutines ile çalıştırmayı engellemeyi deneyeceğiz.
    // Çünkü ShoppingDao içerinde insert  ve delete fonksiyonlarımızı suspend fonk olarak tanımladık ki multithread'leri çalışmasını engeleyebiliriz(blocklayabiliriz).
    // Bunuda Coroutine'inin içerisinde olan ve test işlemleri için kullanılan runTest fonksiyonu ile yaptık. Çünkü burda insert ve delete işlemlerini coroutinelerden olan suspend işlevini kullandığımız için böyle bir şey yaptık.
    // Burada Database'e verilerin eklenmesini ve aynı zamanda observeAllShoppingItem ile de tüm verilerin listelenmesinin test senaryosunu oluşturduk.
    @Test
    fun insertShoppingItem() = runTest {

        // Burda manuel olarak veririlerimizi kendimizi girerek insertShoppingItem methodu sayesinde database'e verileri ekledik.
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", 1)
        dao.insertShoppingItem(shoppingItem)


        // getOrAwaitValue fonksiyonu androidTest dosyası içerisindeki Google'ın LiveData işlemlerini Test Senaryolarında kullanabilmemiz için oluşturduğu LiveDataUtilAndroidTest class'ından gelmektedir.
        // Livedata ile observable yani gözlemlenebilir fonksiyonları değişkenleri test edebilmemiz için LiveDataUtilAndroidTest class'ının içerisindeki fonksiyonun oluşturulduğu getOrAwaitValue fonksiyonu kullanmalıyız.
        val observeAllShoppingItem = dao.observeAllShoppingItem().getOrAwaitValue()


        //Testimizin geçerli olması için bir test senaryonu iddiası yani assert'ü oluşturmalıyız.
        // Bunu da google'ın oluşturmuş olduğu Truth kütüphanesi ile rahatlıkla yapabiliyoruz.
        assertThat(observeAllShoppingItem).contains(shoppingItem)
    }


    // DeleteShoppingItem - Database'deki verilerin silinmesinin ve aynı zamanda observable olan tüm verilerin listesinin test senaryosunu oluşturduk.
    @Test
    fun deleteShoppingItem() = runTest {
        val shoppingItem = ShoppingItem(
            "name1",
            1,
            1f,
            "url",
            1
        ) // ShoppingItem Class'ındaki verilere manuel olarak kendimiz verileri ekledik.
        dao.insertShoppingItem(shoppingItem) // Burada database'e yine manuel olarak eklenen verileri yani shoppingItem değişkenini vedik ve verileri database'e ekledik.
        dao.deleteShoppingItem(shoppingItem) // Burada ise database eklemiş olduğumuz verileri silmek için dao içerisindeki delete fonksiyonunu çağırdık.

        val observeAllShoppingItem = dao.observeAllShoppingItem()
            .getOrAwaitValue() // Burda ise eklenen ve sonrasında silinen verileri gözlemlemek için yine dao daki observeAllShoppingItem fonksiyonunu çağırdık.

        assertThat(observeAllShoppingItem).doesNotContain(shoppingItem) // Burda yukarı da yaptımız tüm işlemleri Truth kütüphanesi ile bir assertThat oluşturduk ve doesNotContain() fonksiyonuna shoppingItem'a eklediğimiz verileri içermemesini söyledik.

    }

    // Burada ise database'deki ürünlerin miktarı ile fiyatının toplam Maliyetinin sonucunun test senaryosu oluşturuldu.
    @Test
    fun observeTotalPriceSum() = runTest {
        val shoppingItem1 = ShoppingItem(
            "name1",
            1,
            10f,
            "url1",
            1
        ) // ShoppingItem Class'ındaki verilere manuel olarak kendimiz verileri ekledik.
        val shoppingItem2 = ShoppingItem(
            "name2",
            3,
            2.5f,
            "url2",
            2
        ) // ShoppingItem Class'ındaki verilere manuel olarak kendimiz verileri ekledik.
        val shoppingItem3 = ShoppingItem(
            "name3",
            0,
            35f,
            "url3",
            3
        ) // ShoppingItem Class'ındaki verilere manuel olarak kendimiz verileri ekledik.

        //Verileri database'e insert yaptık ve database'e ekledik.
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        // Database'deki tüm verilerin maliyetine ulaşmak için böyle bir değişken oluşturuldu.
        val observeTotalPriceSum = dao.observeTotalPrice().getOrAwaitValue()

        assertThat(observeTotalPriceSum).isEqualTo(1 * 10f + 3 * 2.5f + 0)

    }
}

























