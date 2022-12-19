# MVVM Projesi ile Derinlemesine Android Test Driven Development(A-TDD)
- Bu repo'da Android Testing üzerinden Tüm Test işlemleri nasıl yapılır bu konuda öğrendiklerimi sizlere yorum satırlarıyla anlatmaya çalıştım.
- Bir MVVM projesi üzerinden Unit Test, Integration Test ve UI Test'in nasıl yapıldığını yorum satırları ile anlatmaya çalıştım. Ama bunun üzerine Medium'dan seri de oluşturacağım.
- Eğer doğrundan kodlar üzerinden kendi yorum satırlarım aracılığı ile incelemek/öğrenmek istiyorsanız Branches'ları takip ederek aşamalı bir şekilde ilerleyebilirsiniz. 

## Android'de TDD Nedir/Neden Kullanılır?
- Bir uygulamayı test etmek, uygulama geliştirme sürecinin ayrılmaz bir parçasıdır. Uygulamanıza yönelik testleri tutarlı bir şekilde çalıştırarak, kullanıcının önüne sürmeden önce uygulamanızın doğruluğunu, işlevsel davranışını ve kullanılabilirliğini doğrulayabilirsiniz.
- Testing'in bir diğer amacıda uygulama geliştirme sürecinde oluşabilecek hataları yakalayıp ileriki aşamalarda oluşabilecek hataları engellemektir. Bu şekilde iş yükünden ve zaman kayıplarından kurtulmuş oluruz.
#### ->Android TDD'de Ana Prensip(Main Principle):
 - Bir test uygulamasında fonksiyonun uygulanmasından önce test senaryoları yazılır.( sadece Unit Testler için)
 - Daha içeriği olmayan bir fonksiyon tanımlanır.
 - Fonksiyonu test etmesi gereken test senaryoları yazılır. Bu test senaryoları her zaman ilk başta başarız olması beklenir. Çünkü bu fonksiyonda herhangi bir içerik yoktur.
 - Testlerin geçmesi için fonksiyon mantığı(içeriği) yazılır.
 - Test senaryosu başına yalnızca tek bir iddiaya sahip olunması gerekir.
 - Başarısız bir test vakasının nedenini hemen bilmek isteriz.
 - Bir test senaryosunda birden fazla test iddiası olamaz.

#### -> Android'de TDD'in Avantajları: 
- Test, uygulama geliştirme sürecinin ayrılmaz bir parçasıdır.
- Uygulamaya yönelik testleri tutarlı bir şekilde çalıştırarak, tüm kullanıcının önüne çıkmadan önce uygulamanın doğruluğunu, işlevsel davranışlarını ve kullanabilirliğini test etmiş oluruz.
- Daha hızlı ve daha tekrarlanabilir testler gerçekleştirilmesinde yardımcı olur.
- Uygulama üzerindeki hatalar hakkında hızlı geri bildirim sağlaması.
- Uygulamayı geliştirme döngüsünde hataları erken tespit etme
- Uygulama içerisindeki kodların daha güvenli kodlar olmasını ve kodları yeniden düzenlenebilir halde kodu optimize etmemize olanacak sağlar.
- Uygulama üzerinde kararlı bir şekilde uygulamayı geliştirme hızını arttırır ve en aza indirgemeyi sağlar.
- kaynaklar:
 https://developer.android.com/training/testing
 
 #### -> Android'de Bir TDD Stratejisi Tanımlama:
 - Normal şartlar altında uygulamanızdaki her kod satırını, uygulamanızın uyumlu olduğu her cihazda test etmeniz gerekir. Ama bu durum pratik olamayacak kadar yavaş ve maliyetlidir. (Böyle bir şeyi hiçbir şirket istemez tabi.)
 - <b>İyi bir Test Stratejisinde</b>, bir testin;
   - 1. <b> Doğruluğu </b>
   - 2. <b> Hızı </b>
   - 3. <b> Güvenirliliği </b>
 
    arasında uygun bir denge bulunur.
 - Uygulamanızda oluşturmuş olduğunuz testleri JVM üzerinden düşük doğruluk testleri çalıştırılabilir ama daha hızlıdır.
 - Emülatör veya fiziksel cihazların kendisinde daha yüksek doğruluk testler çalıştırılabilir ama daha yavaştır.
 - Bu nedendele yüksek doğruluk testleri genellikle daha yavaş olduğundan daha fazla kaynak gerektirir. O yüzden her testi yüksek doğruluk test'i içerisinde yapılmamalıdır.  

#### -> Flaky Testler (Kesintili Testler):
- Doğru tasarlanmış ve uygulanmış test çalışmalarında bile hatalar olabilir.
- Örneğin, gerçek bir cihazda test çalıştırırken, testin ortasında otomatik güncelleme başlayabilir ve başarısız olmasına neden olabilir.
- Yani test çalışması bazen başarılı bazen başarısız sonuç veriyorsa bu Flaky Test'tir.

#### -> Android'de Test Stratejisi Tasarlama:
- **Scope (Kapsam)** 
  - Yani Test Senaryosunun Kapsamı, test etmek istediğimiz fonksiyondaki gerçek kodun ne kadarının tek bir test senaryosu tarafından kapsandığını belirler.
- **Speed (Hız)**
  - Bu sadece test senaryosunun ne kadar hızlı çalıştığı anlamına gelir.
  - Test ne kadar hızlı çalışırsa test senaryoları o kadar sık çalışır ve daha çok hata bılur. 
- **Fidelity (Uygunluk)**
  - Test senaryosunun gerçek bir senaryoya ne kadar yakın doğrulukta olduğu anlamına gelir.
  - Yani test edilen kodun bir kısmı bir ağ isteği yapması gerekiyorsa test kodu bu ağ isteğini gerçekten yapıyor mu yoksa sonucu taklit mi ediyor? Test gerçekten ağ ile iletişim kuruyorsa bu daha yüksek uygunluğa sahip olduğu anlama geliyor.

## Android'de 'Test'edilebilir Mimari:
- Test edilebilir bir uygulama mimarisi ile kod, farklı bölümleri tek başına kolayca test etmenize izin veren bir yapı izler.
- Test edilebilir mimarilerin daha iyi <b>okunabilirlik, sürdürülebilirlik, ölçeklenebilirlik ve yeniden kullanıbilirlik</b> gibi avantajları vardır.
#### -> Test Edilemeyen bir Mimari:
- Daha büyük, daha yavaş, daha düzensiz testler üretir. Unit(Birim) Testi yapılamayan sınıfların daha büyük Entegrasyon(Integration) Test'leri veya UI Test'leri kapsamında olması gerekir.
- Farklı test senaryoları daha az test fırsatı oluşturur. Yani daha büyük testler daha yavaştır. Bu nedenle bir uygulamanın tüm olası durumlarını test etmek gerçekçi değildir.
  
#### -> Test'leri Ayrıştırma Yaklaşımı: 
- Bir fonksiyonun, sınıfın veya modülün bir kısmı diğerlerinden ayrıştırılabilirse, test etmek daha kolay ve daha etkilidir. 
- Bu Uygulama Ayrıştırması olarak bilinir ve Test Edilebilir Mimari için en önemli kavramdır.
- **Yaygın Ayrıştırma Teknikleri:**
  - Bir uygulamada Presentation, Domain ve Data Layer(katmanlarının) ayırılması.
  - Activity ve Fragments'lar gibi büyük bağımlılıkları olan entities'lere logic eklemekten kaçınılmalıdır. Bu sınıflar frameworklerin giriş noktaları olarak kullanılması ve UI(Kullanıcı Arabirimi) ve Business Logic'leri yani örn. ViewModel, Domain Layer gibi başka bir yere taşımak.
  - Business Logic içeren sınıflarda doğrudan framework bağımlılıklarından(dependencies) kaçınılmalı. Örneğin, ViewModel içinde Android Context yapılarının kullanmaması.
  - Bağımlılıkların değiştirilmesini kolaylaştırın. Bir Dependency Injection(DI) kullanılması.

## Peki, İyi Bir Test Ne Yapar?
- İyi bir test senaryosunda hangi doğru özelliklere sahip olunması gerektiğini bilmek/düşünmek gerek.
- Ve iyi bir Test Senaryosu 3 karakteristik özelliğe sahip olmalıdır;
  - **Scope(Kapsam)**
  - **Speed(Hız)**
  - **Fidelity(Uygunluk)**
- Kesintili(Flaky) bir test olmamalıdır. Yani test bazen başarılı veya bazen başarısız olmamalıdır. 
- Asla bir testin sonucunu başka bir testin sonucuna bağla hale getirilmemelidir. Yani Her zaman testlerinizin bağımsız olduğundan ve başka bir testin senaryosuna bağlı olmadığından emin olunmalıdır.
- Herhangi bir test senaryosunun başka bir test senaryosunun sonucunu etkilemediğinden emin olunması gerekir. 

## Test İzolasyonu ve Bağımlılıkları
- Bir class'ı veya bir yapıyı test etmek istenildiğinde bu testler tekil olarak yapılmalıdır.
- Örneğin, Bir ViewModel'i test etmek için Android Frameworklerine yani Android Bileşenlerine bağlı olmaması gerektiği için Emülatör veya fiziksel cihazda başlatılması gerekmez.
- Ama bununla birlikte, test edilen yapının veya class'ın çalışması için başka yapılara/sınıflara bağımlı olabilir. Mesela, bir ViewModel çalışması için başka bir Data Yapısına/Class'ına bağlı olabilir.
- Bu yüzden Test edilen bir yapıya bağımlılık sağlanması gerektiğinde yaygın olarak **Test Doubles(Test Çifti veya Test Nesnesi)** oluşturulur.
#### -> Test Doubles(Test Çiftler/Nesnesi) Nedir?
- **Test Çiftileri**, uygulamanızdaki bileşen gibi görünen ve hareket eden nesnelerdir, ancak testinizde belirli bir davranış veya veri sağlamak için oluşturulur.
- En önemli avantajı, testlerini daha hızlı ve daha basit hale getirmeleridir.
#### -> Test Doubles(Test Çiftler/Nesnesi) Türleri:
- Birçok çeşit Test Doubles'lar vardır.
- Bunların en çok kullanılanı **Fake, Mock, Stub** dır. 
- **Bunlar;**
  - **Fake**, Gerçek bir projedeki çalışan bir class'ın test dizinlerinde kullanılabilmesi için fake bir class'ı oluşturulur. 
    - Çünkü ilgili class'ın gerçek projedeki class'tan bağımsız bir yapıda olması beklenir ve bir test çifti oluşturulur. 
    - Örneğin, Bir repository için FakeRepository oluşturulur ki test dizinlerinde senaryolarına uygun bir şekilde fonksiyonları yazılır.
    - Bir başka örnekte, uygulama için veritabanı kullanımı için kullanılır. Diğer test çiftlerine kıyasla sistemin gerçek davranışına daha yakındır.
    - Kod içerisinde [Fake Test Doubles Kullanımı](https://github.com/halilkrkn/ShoppingTesting/blob/main/app/src/test/java/com/example/shoppingtesting/repositories/FakeShoppingRepository.kt#L18:~:text=//%20Burada%20fake%20bir%20repository%20olu%C5%9Fturuyoruz.%20Yani%20asl%C4%B1nda%20test%20doubles%20olu%C5%9Fturuyoruz.) olayını inceleyebilirsiniz.
  - **Mock**, Yazılan bir kodun veya bir class'ın bağımlı olduğu objelerin sahte bir referansını oluşturmamıza olanak sağlayan bir test double'dır. 
    - Yani bu şekilde ilgili nesneyi mock sayesinde taklit ederek sahte bir nesnesi oluştulan yapı test edilen sistemin yapılması beklenen işlemlerin yapılıp/yapılmadığını doğrulamak olarak tanımlanabilir. 
    - Bu yapıyı kullanabilmek için Mockito framework'ü projeye implement edilmelidir.
    -  Kod içerisinde [Mock Test Doubles Kullanımı](https://github.com/halilkrkn/ShoppingTesting/blob/f0007e1240cc6cdb9fc1e4a8c7c17bb132da1b35/app/src/androidTest/java/com/example/shoppingtesting/ui/views/ShoppingFragmentTest.kt#L54:~:text=Mock%20asl%C4%B1nda%20bir%20nevi%20test%20double%27d%C4%B1r%20yani%20test%20bir%20test%20dubl%C3%B6r%C3%BCd%C3%BCr/sim%C3%BClasyonudur.) olayını inceleyebilirsiniz.
  - **Stub**, Genellikle bir bağımlılığın gerçek projedeki uygulamasını değiştirmek için sabit veya döndürülmesi istenilen değerleri yapılandıran bir test double'dır. 
    - Yani,bir yöntem çağrıldığında hazır bir yanıt döndürür. Mock'lara ve Fake'e çok benzer.
  - **Dummy**, En basit test double'dır. Testin kendisiyle çok fazla bir ilgisi olmasa da tek amacı argüman olarak kullanılmasıdır. 
    - Yani genellikle zorunlu parametreleri doldurmak için kullanılır ve başka hiçbir şey için kullnılmaz.
  - **Spy**, Spy çalışmak için işlevsel bir uygulama kullanması ve daha sonra doğrulama veya iddia için kullanılabilecek daha karmaşık durumları kaydedebilen bir test double'dır. 
    - En kafa karıştırıcı test double'dır.
  - **Shadow**, Aynı Robolectic'de kullanılan bir Fake test double'dır.

## Android'de Testing Türleri:

#### -> Android'de Farklı Test Türleri Mevcuttur:
- <b> Functional Testing: </b> Uygulamanın yapması gerekenleri yapıp/yapmadığını test etme
- <b> Performance Testing: </b>  Uygulamanın yapması gerekenleri hızlı ve verimli bir şekilde yapıp/yapmadığını test etme
- <b> Accessibility Testing: </b>  Uygulamanın Erişilebilirlik Hizmetleri iyi çalışıp/çalışmadığını test etme
- <b> Compatibility Testing: </b>  Uygulamanın her cihaz ve API düzeylerinde uyumlu çalışıp/çalışmadığını test etme

#### -> Android'de Testler Boyutlarına/Seviyelerine Türlere Sahiptir:
<img align="center" src="https://developer.android.com/static/training/testing/fundamentals/test-scopes.png" alt="halilkrkn" height="400" width="500" />

#### -> <b> Küçük(Small) Seviyeli Testler - Unit Test (Birim Test): </b> 
- Uygulama içerisinde bir methodu(fonksiyonu) veya bir sınıf'ı(class'ı) gibi çok küçük bir bölümün testini yapar. 
- Yani kısaca uygulamamızdaki tek bileşenleri test eder. Genellikle class'ları.
- Bu Android'deki tüm test işlemlerinin %70'ini oluşturmaktadır.
- Unit Testler'de JUnit, Truth, Mocking gibi frameworkler kullanılır.
- <b> -> Unit Testler Local ve Instrumented Unit(Birim) Test olarak ikiye ayrılır; </b>
  - <b> -> Local Unit Test: </b> 
Sadece yerel ortamda çalışan birim testlerdir. Testlerin yürütme süresini en aza indirgemek için JVM(Java Virtual Machine) üzerinden çalışır. Buradaki testler JVM üzerinden test işlemlerini yapmak için test dizinin içerisinde yazılır.
  - <b> -> Instrumented Unit Test: </b> Bu test'te Android'in bileşenlerine(bağlamlarına) dayanan bir testtir. Yani Android üzerindeki context yapılarına/bileşenlerine erişimine sahip olup cihaz veya emülatör üzerinden çalışan birim testlerdir. Local birimlere göre doğruluk oranı daha yüksektir ama bir cihaz veya emülatör üzerinden test koştuğu için daha yavaştır. Burdaki testler Android Bileşenlerine erişmek için androidTest dizinin içerisinde yazılır.                                             

#### <b> -> Orta(Medium) Seviyeli Testler - Integration Test (Entegresyon Testi): </b> 
- Integration testler aradadır ve iki veya daha fazla birim arasındaki entegrasyonu kontrol eder.
- Yani uygulamadaki farklı bileşenlerin arasındaki etkileşimi test eder. Örn: Fragmentler gibi.
- Bu Android'deki tüm test işlemlerinin %20'ini oluşturmaktadır.
- Bu testler bir cihaz veya emülatörde çalıştığı için Unit(Birim) Testlere göre daha yavaş çalışırlar.
- Integration Testlerde Robolectric veya ActivityScenario sınıfları kullanılmaktadır.
- Buradaki testler'de androidTest dizininde yazılır.

#### <b> -> Büyük(Large) Seviyeli Testler - UI (End-to-End) Test: </b> 
- Bir kullanıcının uygulamanın tüm ekran veya kullanıcı akışı gibi daha büyük bölümlerini aynı anda doğrulayan testlerdir.
- Yani, Uygulamanın birçok veya tüm bileşenlerinin birlikte iyi çalışıp/çalışmadığını ve kullanı arayüzünün olması gerektiği gibi görünüp/görünmediğini kontrol eden testlerdir.
- Uygulamayu Uçtan uca baştan sona kontrol eden testlerdir.
- Bu testlerin doğruluk oranı diğerlerine göre daha yüksektir çünkü gerçek hayattaki kullanımı simüle eder. 
- Bu Android'deki tüm test işlemlerinin %10'unu oluşturmaktadır.
- UI Testlerde Espresso kullanılmaktadır. Espresso bir UI Test Framework'üdür.

#### <b> -> Bir Android Projesindeki Test Dizinleri: </b>
- Android Studio'daki tipik bir proje, yürütme ortamlarına bağlı olarak testleri tutan iki dizin içerir. 
- <b> androidTest dizini: </b> Gerçek veya sanal cihazlarda çalışan testleri içermelidir. Bu tür testler Android Bileşenlerine erişebilmek için, Integration Testleri ve UI(E2E) Testleri ve JVM'in tek başına uygulamanın işlevselliğini doğrulayamadığı diğer testleri içerir.
- <b> test dizini: </b> Birim testleri yerel makinenizde çalışan testleri içermelidir. Yukarıdaki androidTest'in aksine bunlar yerel bir JVM üzerinde çalışan testlerdir.

## Gerekli olan Unit(Birim) Testler:
- Bir uygulamayı hazırlarken yapılması beklenen Unit Testler vardır. 

### <b> Unit Test Olaylarını Yaparken; </b>
- <b> ViewModel'ler </b> veya <b> Sunucular'ın </b> Unit Testlerinin yapılması gerekir.
- <b> Data Layers (Veri Katmanı) </b> içerisindeki <b>Data Source</b> ve özellikle <b> Repository'</b>lerin Unit Testlerinin yapılması gerekir. Ve bu testler Data Layer(Veri Katmanı) için çoğu platformdan bağımsız olmalıdır. Yani test dizinleri içerisinde ki data layer ile proje içerisindeki data layer birbirinden bağımsız olmalıdır. Bunu yapmak için <b> Test Doubles (Test Çiftleri)</b> kullanırız. Bu Test Doubles'lar aslında testlerdeki repository kullanımının, veritabanı modüllerinin ve API'den gelen kaynakların asıl proje içerindekinden bağımsız olarak değiştirilmesini sağlar. Bu Test Doubles'lara ilerde değineceğim.
- <b> Domain Layer(Katmanı) </b>'ında Unit Testlerinin diğer platformlardan bağımsız olarak testlerinin yapılması gerekir.  
- <b> Utility Classes'lar</b> için de Unit Testlerinin yapılması gerekir.

## Unit Test'te Uç Vakaları(Edge Cases) Test Etme:
- Unit(Birim) Testler, hem normal hem de uç durumlarda olan test vakalarına odaklanmalıdır.
- Uç Test Vakaları, test cihazlarının daha büyük testlerin yakalamasında pek mümkün olmadığı nadir vaka durumlarıdır.
- ### <b>Bu Uç Vakalar(Edge Cases); </b>
- Negatif sayılar, sıfır ve sınıf koşullarını kullanan matematik işlemleri
- Tüm olabilecek ağ bağlantısı hataları
- JSON gibi hatalı biçimlendirilmiş bozuk veriler
- Bir dosyayı kaydederken tam depolama simülasyonu
- Bir işlemin ortasında yeniden oluşturulan objectler ( Cihazın döndürdüğümüzde ekranda gerçekleşen activity gibi)

## Kaçınılması Gereken Unit Testler:
- Düşük değerleri nedeniyle bazım birim testlerinden kaçınılmalıdır.
- Kendi kodunuz olmayan, Framework'ler ve Library'lerin doğru çalışıp/çalışmadığını doğrulayan testlerden kaçınılmalıdır.
- Activity, Fragments ve Services gibi framework giriş noktalarında iş mantığı bulunmamaktadır. Bu nedenle Unit Testi öncelik olmamalıdır/kaçınılmalıdır. 
- Activity'ler için Unit Testlerin çok az değeri vardır bu yüzden çoğunlukla framework kodunu kapsarlar ve daha ilgili bir kurulum gerektirirler. O yüzden UI Testler gibi araçlı testler bu Activity, Fragment gibi sınıfları kapsayabilir. 

## UI (End-to-End) Testler:
- Kullanılması gereken birkaç UI Test türü vardır.  Bunlar; 
- ### <b>Screen UI Testler;</b>
- Kritik kullanıccı etkileşimlerini tek bir ekranda kontrol eder. 
- Buton'lara tıklamak, from'ları yazmak ve görünür durumları kontrol etmek gibi eylemleri gerçekleştirirler. 
- Ekran başına bir test sınıfı iyi bir başlangıç noktasıdır.
- ### <b>User Flow Test veya Navigation Testler;</b>
- En kapsayıcı test yollarındandır.
- Bu testler, uygulama içerisinde tüm akışları gezinen bir kullanıcıyı simüle eder. 
- Bunlar, başlatma sırasında run-time caches'lerini kontrol etmek için kullanılan yararlı testlerdir. 

## Proje İçerisinde Kullanılan Önemli Class'lar:

### Test Klasörlerindeki [LiveDataUtilAndroidTest](https://github.com/halilkrkn/ShoppingTesting/blob/main/app/src/androidTest/java/com/example/shoppingtesting/LiveDataUtilAndroidTest.kt) ve [LiveDataUtilTest](https://github.com/halilkrkn/ShoppingTesting/blob/main/app/src/test/java/com/example/shoppingtesting/LiveDataUtilTest.kt) Class'ları: 
- Bu class'lar testing içerisinde livedata yapılarının kullanımı için çok önemli ve kullanılması gerekir. 
- İlgili test senaryolarında kullanılan **get:Rule annotation'ı** altında **InstantTaskExecutorRule()** sınıfı yani aslında LiveDataUtilTest ve LiveDataAndroidUtilTest class'larını çağırmış olduk.
- Bu InstantTaskExecutorRule() kullanımı sayesinde LiveData yapılarının kullanımı test ortamlarında sağlanmış oluyor.
- Yani test ortamında gözlemlenebilir yapıdaki LiveData kullanımını sağlatmış olduk. Çünkü test ortamında observe olarak verileri LiveData'yı gözlemlemek için bu Google'ın oluşturmuş olduğu yapıyı kullandık.
- Aslında "Run tasks synchronously" işlemi yani görevleri eşzamanlı olarak(senkronize)  çalıştırma işlemi yapar
- Ve bu işlem JVM üzerindeki testlerde kullanılır.
- İlgili kaynaklar:
	 - [Unit-testing LiveData and Other Common Observability Problems](https://medium.com/androiddevelopers/unit-testing-livedata-and-other-common-observability-problems-bb477262eb04)
- LiveDataUtilTest Class'ının kullanımına android'in kendi github hesabından ulaşabilirsiniz:
  - [LiveDataUtil Kurulumu - InstantTaskExecutorRule](https://github.com/android/architecture-components-samples/blob/master/LiveDataSample/app/src/test/java/com/android/example/livedatabuilder/util/LiveDataTestUtil.kt#L28:~:text=util.concurrent.TimeoutException-,/**,*/,-fun%20%3CT)
  - [Unit Test'te LiveData ve InstantTaskExecutorRule Kullanımı](https://github.com/android/architecture-components-samples/blob/0905d0e307ef457a4c37511a542edfe3bdb4d2a3/LiveDataSample/app/src/test/java/com/android/example/livedatabuilder/LiveDataViewModelTest.kt#L48)

### Test klasörü içerisindeki shoppintTesting Klasörüne Coroutine'ler sayesinde Dispatchers'ların yönetimi için [MainDispatcherCoroutineRule](https://github.com/halilkrkn/ShoppingTesting/blob/main/app/src/test/java/com/example/shoppingtesting/MainDispatcherCoroutineRule.kt) Class'ı: 
 - Bu Sınıfta gerçek proje içerisinde örneğin viewModelimizi test ederken repositoryde coroutine'leri kullandığımızdan dolayı ve repository'de fonksiyonları coroutine yapılarından olan suspend fonksiyon ile fonksiyonları oluşturduğumuz için ViewModel'de test senarylarının çalışması için bir kural yazıldı.
 - MainDispatcherCoroutineRule Class'ında Coroutine'ler sayesinde Dispatchers'ların yönetimi için özellikle main Dispatchers'ların test dosyasında yönetebilmek için de oluşturuldu.
 - Eğer gerekli olarak bu kuralı eklemeseydik build işlemi yaparken test senaryolarımız çalışmıyor ve hatalarla karşılaşırdık.
 - Bu durum androidTest dosyası içerisinde olsaydı eğer böyle bir kurala ihtiyacımız olmayabilirdi. Çünkü bu klasör de zaten Android Componentlerine(Bileşenlerine-Bağlamına) sahip olunduğundan dolayı bir cihaz üzerinden test edildiğin de bunu o cihaz içerisindeki erişimlerden halledebiliyoruz. 
 - Buradaki kuralı da Test dosyası içerisindeki viewModelTest class'ımıza **get:Rule** yaparak **MainDispatcherCoroutineRule** class'ını çağırıyoruz.
 - İlgili MainDispatcherCoroutineRule kuralı zaten android'in kendi github hesabından ve developer.android.com'dan detayları öğrenebilirsiniz.
   - [MainDispatcherCoroutineRule Class'ı](https://github.com/android/snippets/blob/e714d659d8dceb331a8d8436caa88dc12a8e8cf3/kotlin/src/test/kotlin/com/example/android/coroutines/testing/HomeViewModelTestUsingRule.kt#L33-L56)
   - [Testing Kotlin Coroutines on Android](https://developer.android.com/kotlin/coroutines/test)

### androidTest Klasörü içerisindeki shoppingTesting klasörüne Dagger-Hilt için [DaggerHiltTestRunner'ı](https://github.com/halilkrkn/ShoppingTesting/blob/main/app/src/androidTest/java/com/example/shoppingtesting/DaggerHiltTestRunner.kt) ve shoppingTesting klasörünün içindeki di klasörüne de [TestAppModule Class'ı](https://github.com/halilkrkn/ShoppingTesting/blob/main/app/src/androidTest/java/com/example/shoppingtesting/di/TestAppModule.kt) Eklendi:
- Test ortamında **Dagger-Hilt** kullanmak için androidTest Klasörüne ilk önce **DaggerHiltTestRunner** class'ını oluşturuyoruz.
- Çünkü Dagger-Hilt'i kullanabilmemiz için androidTest klasörü'müzde Hilt'i dahil edip çalıştırmamız için DaggerHiltTestRunner classı'nada AndroidJUnitRunner'ı çağırıp Unit Test işlemlerimizi yaptığımız Instrumentation katmanında AndroidJUnitRunner sınıfının işlevini sağlamış olduk. 
- Böylelikle Instrumentation katmanında Android Componentleri kullandığımız için Dagger-Hilt ile artık bu componentlerin yönetimi için module oluşturarak Dependency Injection işlemlerini androidTest klasörü içerisinde yapabilme imkanına eriştik.
- **DaggerHiltTestRunner class'ı** ile;
  - Bu sınıfa zaten AndroidJUnitRunner'ı da zaten inherite ettiği için aslında Hilt'i test işlemlerine dahil etmiş oluyoruz.   
  - Burada oluşturduğumuz bu sınıfı Test dosyalarına işlemlerine dahil etmek için build.gradle(app) de defaultConfig'deki androidTest içerisinde testlerin çalışması için olan testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"'ın içerisindeki adresin yerine burada oluşturduğumuz class'ın yolunu veriyoruz. 
  - Yani build.gradle(app) de artık testInstrumentationRunner "com.example.shoppingtesting.DaggerHiltTestRunner" şeklinde bir tanımlama ile Dagger-Hilt'i artık androidTest klasörü içerisinde kullabilmeyi sağladık.
  - ShoppingDaoTest class'ında ise @RunWith(AndroidJUnit4:class) annotation'unu çağırmıyoruz ve ShoppingDaoTest sınıfında RunWith annotation yerine @HiltAndroidTest annotation'ını kullanıyoruz.
  - Böylelikle bu kullandığımız annotation sayesinde Hilt'i test işlemlerine dahil etmiş oluyoruz ve artık hilt üzerinden testlerimizi koşturuyoruz.
  - Bu işlemleri daha detaylı olarak Android Developers'dan inceleyebilirsiniz.
    - [Hilt Testing Guide](https://developer.android.com/training/dependency-injection/hilt-testing)

 - DaggerHiltTestRunner'ı test işlemlerinde kullanımına hazır hale getirdiğimize göre artık androidTest klasöründe Hilt'i kullanabiliriz.
 - O yüzden androidTest klasörümüz içerisinden shoppingtesting klasörünün içerisine **gerçek projedeki gibi di klasörü** oluşturup bir object ifadedeki **TestAppModule.kt** dosyası oluşturuyoruz.
- androidTest klasöründe artık Dagger-Hilt'i gerçek proje klasörleri içerisinde kullandığımız gibi kullanabiliriz.
- Bu TestAppModule object'i içerisine gerçek projedeki AppModule içerisine yazdığımız işlemler gibi aynı kod işlevlerini yazıyoruz ve hemen hemen aynı annotation'ları kullanıyoruz. Mesela @Provides annotation gibi.
- **Ama TestAppModule içerisinde @Singleton Annotation'ını kullanılmıyor.**    
- Yani @Singleton test içerisinde kullanmıyoruz. Çünkü her test durumu için yeni bir örnek oluşturmak istiyoruz. Yani burada tekil olmasını istemiyoruz.
- androidTest dizini içerisindeki data/local klasörü içerisinde ShoppingDaoTest class'ımın içerisinde de **@get:Rule** altında bir değişkene **[HiltAndroidRule](https://github.com/halilkrkn/ShoppingTesting/blob/2e85d40a3085242cbbfb751a07402e81f141f655/app/src/androidTest/java/com/example/shoppingtesting/data/local/ShoppingDaoTest.kt#L43)** classını implemente ederiz.
- Bu şekilde test senaryolarımızın olduğu ShoppingDaoTest class'ında ise artık Dagger-Hilt'i rahatlıkla kullabiliriz. Yani Dependency Inject işlemlerini gerçekleştirebiliriz. 
- TestAppModule içerisindeki ilgili işlevleri fonksiyonları [ShoppingDaoTest Class](https://github.com/halilkrkn/ShoppingTesting/blob/main/app/src/androidTest/java/com/example/shoppingtesting/data/local/ShoppingDaoTest.kt)'ına inject yapabiliriz ve bağımlı hale getirebiliriz.


