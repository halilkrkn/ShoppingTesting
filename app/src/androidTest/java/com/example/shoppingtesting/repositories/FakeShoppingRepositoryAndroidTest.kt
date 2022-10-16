package com.example.shoppingtesting.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppingtesting.data.local.ShoppingItem
import com.example.shoppingtesting.data.remote.responses.ImageResponse
import com.example.shoppingtesting.other.Resource

// Burada fake bir repository oluşturuyoruz. Yani aslında test doubles oluşturuyoruz.
// Yani hem gerçek(default) hemde test dosyalarımız içerisinde fake reporsitory'imizi test etmek için. Bunuda ShoppingRepository'imiz ile çözüyoruz.
// Yani burada yapacağımız şey, gerçek veritabanımızı eden basit bir liste oluşturmak ve default repo'muzu simüle eden içinde sadece görebileceğimiz bir fonksiyona sahip olmak
// Api'den bir hata döndürmek istiyorsak veya istemiyorsak bir boolean değişkeni ile repository'imizi istediğimiz gibi kurabilir ve ardından gerçekten yapması gerekeni yapıp yapmadığını kontrol edebiliriz.
// Fake Reporsitory sınıfını burada gerçek repository'imiz gibi kullanıyoruz daha sonra ViewModel'imizi test etmek için yazıyoruz.
// Bu yüzden aslında test senaryolarımızda yalnızca ViewModel'imizi test edebilmemiz için bir repository'e ihtiyacımız var. O bu FakeShoppingRepository.
// Yani bu FakeShoppingRepository'de ViewModel'imizin uygun bir şekilde yanıt verip vermediğini kontrol edebilmek için DefaultShoppingRepository'imizin davranışını simüle ettik.
// Burada Api ile ilgili hiçbir şey test edilmiyor.

class FakeShoppingRepositoryAndroidTest: ShoppingRepository {

    // *** VERİTABANIMIZ İÇİN ***
    // Gerçek Repository interface'imiz olan ShoppingRepository içindeki fonksiyonlarda veritabanı için gereken işlevler olduğu için veritabanının ihtiyacı olan ShoppingItem classına ihtiyacımız var.
    // O sınıfımızı mutableList listemizin içerisine atadık ve liste haline dönüştürdük.
    // Amacımız ShoppingRepository Interface'imizdeki veritabanımız için oluşturulan fonksiyonlara ilgili değerleri verebilmek.
    private val shoppingItems = mutableListOf<ShoppingItem>()
    // Yukarıda oluşturduğumuz shoppingItems listemizi MutableLive data içerisine atadık.
    private val observableShoppingItems = MutableLiveData<List<ShoppingItem>>(shoppingItems)
    private val observableTotalPrice = MutableLiveData<Float>()


    //*** APİ'MİZ İÇİN ***
    // Ağ İsteklerini Kontrol edebilmek için shouldReturnNetworkError ile Ağ Hatalarını boolean bir değer olan false ile atadık. Bu şekilde fonksiyonun hata döndürüp döndürmeyeceğini belirlemek için
    // böylece ViewModel'imizde Network Hatası olmadığında olması gerektiği gibi davranıp davranmadığını veya bir hata varsa olması gerektiği gibi davranıp davranmadığını kontrol ediyoruz.
    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean){
         shouldReturnNetworkError = value
    }

    // Observable olan işlevlerin LiveData kullanımı için;
    // Veritabanına her veri eklediğinde ObservableShoppingItems'daki veriler kendini göncelleyecek ve Ürün fiyatları da güncellenecek.
    private fun refreshLiveData() {
        observableShoppingItems.postValue(shoppingItems)
        observableTotalPrice.postValue(getTotalPrice())
    }

    // Eklenen ürünlerin toplam fiyatlarının alınması
    private fun getTotalPrice(): Float {
        return shoppingItems.sumOf { it.price.toDouble() }.toFloat()
    }

    //***** ShoppingRepository interface'deki hem veritabanı için hemde api'miz için oluşturulan fonksiyonların işlevlerini yazacağız. *****
    
    // Yukarıdaki kodları buradaki fonksiyonların işlevsel hale getirebilmek için yazdık. Yani gerçek bir repositoryde işlem yapıyoruz gibi.
    // Veritabanımıza verileri ekledik ve yine refreshLiveData fonksiyonu ile ürün listeyi güncelledik
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    // Veritabanından verileri sildik ve yine refreshLiveData fonksiyonu ile ürün listeyi güncelledik
    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        refreshLiveData()
    }

    // Veritabanımızdaki verileri ürünleri observable bir şekilde listeledik.
    override fun observeAllShoppingItem(): LiveData<List<ShoppingItem>> {
        return observableShoppingItems
    }

    // Veritabanımızdaki ürünlerin fiyatlarını observable bir şekilde döndürdük.
    override fun observeTotalPrice(): LiveData<Float> {
        return observableTotalPrice
    }

    // Burada ise Api'den Görüntülüleri Seach etmek için gereken işlevleri yazdık.
    // Yukarıdaki shouldReturnNetworkError ile apimizde oluşabilecek ağ hatasını kontrolü için yaptık.
    override suspend fun searchForImages(searchQuery: String): Resource<ImageResponse> {
        return  if (shouldReturnNetworkError) {
            Resource.error("Error", null) // Eğer ağ hatası var ise Error Message'ı dönecek ve apideki isteğimizin başarısız olarak anlaşılacak.
        } else {
            Resource.success(ImageResponse(listOf(),0,0)) // Eğer Ağ Hatası yok ise ImageResponse'dakileri listeleyerek başarılı bir şekilde döndürecek.
        }
    }
}