package com.example.shoppingtesting

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint


// Test senaryolarımız için fragmentlarımızı ekleyeceğimiz Activity HiltTestActivity class'ı olacak.
// Aynı gerçek proje üzerindeki MainActivity'imiz üzerinden fragmentlarmızı oluşturduğumuz gibi burda da test senaryolarımız için bir HiltTestActivity'imiz üzerinden Fragmentlarımızı oluşturabiliriz.
// @AndroidEntryPoint annotation'u ile aynı gerçek projedeki gibi buurda verdikki Dagger-Hilt ile tek bir activity üzerinden birden fazla fragmentimizi test dosyalarımız üzerinden de testlerini sağlayabileceğiz.
// Ama gerçek projede oludğu gibi gerçek dosyamızdaki manifest dosyamıza eklemeyeceğiz çünkü bu yalnızca test durumlarımız için kullandığımız bir Activity'dir.
// O yüzden test dosyalarımız için kullanacağımız için AndroidManifest.xml dosyamızı yeni oluştuduğumuz debug dosyamız içerine kopyalayıp yapıştırıyoruz.
// Bu debug dosyamıza kopyaladığımız AndroidManifest.xml dosyamızın içerise activity olarak android:name'e HiltTestActivity'i ekliyoruz. Böylelikle test dosyalarımız içerisinde HiltTestActivity'imizin kullanımını sağlatmış oluyoruz.
// Ve ek olarak da android:exported= false yani dışa aktarmayı false olarak yapıyoruz ki bu sadece bu paketteki bu HiltTestActivity'sine dışarıdan değil de temelde erişebileceğimiz anlamına gelir.
@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity()