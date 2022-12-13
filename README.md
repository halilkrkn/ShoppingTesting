# MVVM Projesi ile Derinlemesine Android Testing
- Bu repo'da Android Testing üzerinden TÜm Test işlemleri nasıl yapılır bu konuda öğrendiklerimi sizlere yorum satırlarıyla anlatmaya çalıştım.
- Bir MVVM projesi üzerinden Unit Test, Integration Test ve UI Test'in nasıl yapıldığını yorum satırları ile anlatmaya çalıştım. Ama bunun üzerine Medium'dan seri de oluşturacağım.
- Eğer doğrundan kodlar üzerinden kendi yorum satırlarım aracılığı ile incelemek/öğrenmek istiyorsanız Branches'ları takip ederek aşamalı bir şekilde ilerleyebilirsiniz. 
- 
## Android'de Testing Nedir/Neden Kullanılır?
- Bir uygulamayı test etmek, uygulama geliştirme sürecinin ayrılmaz bir parçasıdır. Uygulamanıza yönelik testleri tutarlı bir şekilde çalıştırarak, kullanıcının önüne sürmeden önce uygulamanızın doğruluğunu, işlevsel davranışını ve kullanılabilirliğini doğrulayabilirsiniz.
- Testing'in bir diğer amacıda uygulama geliştirme sürecinde oluşabilecek hataları yakalayıp ileriki aşamalarda oluşabilecek hataları engellemektir. Bu şekilde iş yükünden ve zaman kayıplarından kurtulmuş oluruz.
#### Android'de Testing'in Avantajları: 
- Uygulama üzerindeki hatalar hakkında hızlı geri bildirim sağlaması.
- Uygulamayı geliştirme döngüsünde hataları erken tespit etme
- Uygulama içerisindeki kodların daha güvenli kodlar olmasını ve kodları yeniden düzenlenebilir halde kodu optimize etmemize olanacak sağlar.
- Uygulama üzerinde kararlı bir şekilde uygulamayı geliştirme hızını arttırır ve en aza indirgemeyi sağlar.
- kaynaklar:
 https://developer.android.com/training/testing
## Android'de Testing Türleri:
#### Android'de Farklı Test Türleri Mevcuttur:
- <b> Functional Testing: </b> Uygulamanın yapması gerekenleri yapıp/yapmadığını test etme
- <b> Performance Testing: </b>  Uygulamanın yapması gerekenleri hızlı ve verimli bir şekilde yapıp/yapmadığını test etme
- <b> Accessibility Testing: </b>  Uygulamanın Erişilebilirlik Hizmetleri iyi çalışıp/çalışmadığını test etme
- <b> Compatibility Testing: </b>  Uygulamanın her cihaz ve API düzeylerinde uyumlu çalışıp/çalışmadığını test etme

#### Android'de Testler Boyutlarına/Seviyelerine Türlere Sahiptir:
<img align="center" src="https://developer.android.com/static/training/testing/fundamentals/test-scopes.png" alt="halilkrkn" height="400" width="500" />

#### <b> Küçük(Small) Seviyeli Testler - Unit Test (Birim Test): </b> 
- Uygulama içerisinde bir methodu(fonksiyonu) veya bir sınıf'ı(class'ı) gibi çok küçük bir bölümün testini yapar. 
- Yani kısaca uygulamamızdaki tek bileşenleri test eder. Genellikle class'ları.
- Bu Android'deki tüm test işlemlerinin %70'ini oluşturmaktadır.
- Unit Testler'de JUnit, Truth, Mocking gibi frameworkler kullanılır.
- <b> Unit Testler Local ve Instrumented Unit(Birim) Test olarak ikiye ayrılır; </b>
- <b> Local Unit Test: </b> 
Sadece yerel ortamda çalışan birim testlerdir. Testlerin yürütme süresini en aza indirgemek için JVM(Java Virtual Machine) üzerinden çalışır. Buradaki testler JVM üzerinden test işlemlerini yapmak için test dizinin içerisinde yazılır.
- <b> Instrumented Unit Test: </b> Bu test'te Android'in bileşenlerine(bağlamlarına) dayanan bir testtir. Yani Android üzerindeki context yapılarına/bileşenlerine erişimine sahip olup cihaz veya emülatör üzerinden çalışan birim testlerdir. Local birimlere göre doğruluk oranı daha yüksektir ama bir cihaz veya emülatör üzerinden test koştuğu için daha yavaştır. Burdaki testler Android Bileşenlerine erişmek için androidTest dizinin içerisinde yazılır.
- 
#### <b> Orta(Medium) Seviyeli Testler - Integration Test (Entegresyon Testi): </b> 
- Integration testler aradadır ve iki veya daha fazla birim arasındaki entegrasyonu kontrol eder.
- Yani uygulamadaki farklı bileşenlerin arasındaki etkileşimi test eder. Örn: Fragmentler gibi.
- Bu Android'deki tüm test işlemlerinin %20'ini oluşturmaktadır.
- Bu testler bir cihaz veya emülatörde çalıştığı için Unit(Birim) Testlere göre daha yavaş çalışırlar.
- Integration Testlerde Robolectric veya ActivityScenario sınıfları kullanılmaktadır.
- Buradaki testler'de androidTest dizininde yazılır.

#### <b> Büyük(Large) Seviyeli Testler - UI (End-toEnd) Test: </b> 
- Bir kullanıcının uygulamanın tüm ekran veya kullanıcı akışı gibi daha büyük bölümlerini aynı anda doğrulayan testlerdir.
- Yani, Uygulamanın birçok veya tüm bileşenlerinin birlikte iyi çalışıp/çalışmadığını ve kullanı arayüzünün olması gerektiği gibi görünüp/görünmediğini kontrol eden testlerdir.
- Uygulamayu Uçtan uca baştan sona kontrol eden testlerdir.
- Bu testlerin doğruluk oranı diğerlerine göre daha yüksektir çünkü gerçek hayattaki kullanımı simüle eder. 
- Bu Android'deki tüm test işlemlerinin %10'unu oluşturmaktadır.
- UI Testlerde Espresso kullanılmaktadır. Espresso bir UI Test Frameworküdür.

#### <b> Bir Android Projesindeki Test Dizinleri: </b>
- Android Studio'daki tipik bir proje, yürütme ortamlarına bağlı olarak testleri tutan iki dizin içerir. 
- <b> androidTest dizini: </b> Gerçek veya sanal cihazlarda çalışan testleri içermelidir. Bu tür testler Android Bileşenlerine erişebilmek için, Integration Testleri ve UI(E2E) Testleri ve JVM'in tek başına uygulamanın işlevselliğini doğrulayamadığı diğer testleri içerir.
- <b> test dizini: </b> Birim testleri yerel makinenizde çalışan testleri içermelidir. Yukarıdaki androidTest'in aksine bunlar yerel bir JVM üzerinde çalışan testlerdir.


