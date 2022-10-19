package com.example.shoppingtesting.ui.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.shoppingtesting.adapters.ImageAdapter
import javax.inject.Inject

// Fragmentlarımızda constructor inject işlemini kullanabilmek için FragmentFactory class'ını oluşturuyoruz.
// Ve bu aslında sadece field inject olarak değil, bağımlılıkları constructor inject işlemini test etmeye gelince tercih edilen bir yoldur.
// Yani eğer ilgili fragment'ımız constructor inject ile bir bağımlılığa sahipse bu tür fragmentlar içerisinde durumlarda Fragmentlarımızı test edebilmek için bu yöntem tercih edilir.
// Çünkü temelde sadece fragment'lar oluşturabilir ve constructor'da farklı bağımlılıkları geçirebiliriz. Ama bunu sadece fragmentlar üzerinden testlerini gerçekleştiremeyiz.
// O yüzden fragmentlarımız üzerinde constructor inject ile bağımlılık varsa Fragmentlarımızı da test etmek istiyorsak Fragment Factory'ye ihtiyacımız var.
class FragmentFactory @Inject constructor(
    private val imageAdapter: ImageAdapter,
    private val glide: RequestManager
) : FragmentFactory() {

    // Instantiate - Somutlaştır/ örneklendirme
    // Burada bir fragment gerçekten başlatıldığında ve tüm fragmentlarımız için bir fragmentFactory istediğimizi için instantiate içerisine ilgili fragment'lar return ediyoruz.
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            AddShoppingItemFragment::class.java.name -> AddShoppingItemFragment(glide)
            else -> super.instantiate(classLoader, className)

        }

    }

}