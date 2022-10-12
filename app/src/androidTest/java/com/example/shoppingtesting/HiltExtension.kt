package com.example.shoppingtesting

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

// Bu HiltExtension kotlin dosyası sadece bir uzatma fonksiyonu olacak.
// Aslında sadece Dagger-Hilt için bir tür extension fonksiyonu olan bir fonksiyon.
// Bu yüzden onu buraya koyacağız ve daha sonra bu extension fonksiyonu her yerden çağırabiliriz.
// inline fun = kotlin'de bir lambda fonksiyonunu(işlevini) daha verimli hale getirmenin bir yoludur.
// Çünkü derleyici(compiler) bunu gerçek bir fonkiyon olarak görmez. Bunun yerine işlevin kodunu alır ve bu işlevi çağırdığımız yere koyar.
// Bu fonksiyonun her yerde kullanılması için generic fonksiyon olarak oluşturuyoruz.
// reified: Genel olarak T türünün miras olarak aldığı tüm sınıf bilgilerine erişebileceğimiz anlamına gelir.
// Bu generic fonksyion içerisindeki fragmentFactory parametresi sayesinde böylece fragmentlarımıza constructor injection kullanmamıza izin verir.
// Kaynaklar:
// https://github.com/android/architecture-samples/blob/dev-hilt/app/src/androidTest/java/com/example/android/architecture/blueprints/todoapp/HiltExt.kt#L38
// https://gist.github.com/mitchtabian/047aa7c353b6f91c6caf01e6faa1e1fe
@ExperimentalCoroutinesApi
inline fun <reified T : Fragment> launchFragmentHiltContainer(
    fragmentArgs: Bundle? = null,
    themeResId: Int = androidx.fragment.testing.R.style.FragmentScenarioEmptyFragmentActivityTheme,
    fragmentFactory: FragmentFactory? = null,
    crossinline action: T.() -> Unit = {}
) {

    // Bu alanda ilk önce fragmentimizi eklemek istediğimiz MainActivity'mizi başlatan INTENT işlemini oluşturmak.
    // Yani burada bizim test için oluşturduğumuz MainActivity'miz olan HiltTestActivity'i tanımlıyoryz.
    // Bu activity burada MainActivity olarak hizmet ettiğinden dolayı bu çalışan test senaryosunda da tek aktivite budur.
    // Yani çalışan tüm test senaryolarımız için HiltTestActivity class'ımızı MainActivity gibi hizmet etmesini sağlıyoruz.
    val mainActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY,themeResId)


    // Burada ise fragment kullanımı için ActivityScrenario kullanıyoruz.
    // Ve buraya HiltTestActivity ve oluşturduğumuz mainActivityIntent'i ekleyebiliriz bu aktivity için.
    // Burada bu yüzden HiltTestActivity'mizi bura başlatıyoruz ve sonra senaryo yerine gerçek fragmentimizi oluuşturacağız ve onu ekleyeceğiz.
    // Daha sonra bu activity'e kendi fragmentimizi ekleyeceğiz bu yüzden FragmentFactory parametremizi çağırarak Fragmentlerimizin yönetimini sağlatmış oluyoruz.
    ActivityScenario.launch<HiltTestActivity>(mainActivityIntent).onActivity { activity ->
        fragmentFactory?.let { FragmentFactory ->
            activity.supportFragmentManager.fragmentFactory = FragmentFactory
        }

        // Bu alanda fragment'larımızı oluşturuyoruz. Ve fragmentlarımızı oluşturduktan sonra bu fragment'lara referans almak için fragment diye bir değişkene atadık.
       val fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )
        // burada ise fragmentlar'ın argümanlarını kullanabilmemiz için;
        fragment.arguments = fragmentArgs


        // En son olarak, oluşturulan fragment'i başlatmak için olağan bir fragment işlemi gerçekleştiririz.
        activity.supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()

        //Burada parametreler içerisinde olan action fonksiyonlu parametremizde lambda fonksiyonumuzu kullanmak ve bu fragmentlarımızla çağırmak istiyoruz.
        // Böylece lambda fonksiyonumuz yerine oluşturdğumuz fragment değişkeni için bir referansımız olur.
        // Yani ragmentlarımızın class'larını T tipinin yerine koyuyoruz buradaki kodlar çalışarak fragmentlarımızı kullanabiliyoruz.
        // Yani bu kod parçası ilgili fragmentlara referans olacak.
        (fragment as T).action()

    }
}