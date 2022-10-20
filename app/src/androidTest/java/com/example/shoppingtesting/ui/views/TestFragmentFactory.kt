package com.example.shoppingtesting.ui.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.example.shoppingtesting.adapters.ImageAdapter
import com.example.shoppingtesting.adapters.ShoppingItemAdapter
import com.example.shoppingtesting.repositories.FakeShoppingRepositoryAndroidTest
import com.example.shoppingtesting.ui.viewmodels.ShoppingViewModel
import javax.inject.Inject


class TestFragmentFactory @Inject constructor(
    private val imageAdapter: ImageAdapter,
    private val glide: RequestManager,
    private val shoppingItemAdapter: ShoppingItemAdapter
) : FragmentFactory() {

    // Instantiate - Somutlaştır/ örneklendirme
    // Burada bir fragment gerçekten başlatıldığında ve tüm fragmentlarımız için bir fragmentFactory istediğimizi için instantiate içerisine ilgili fragment'lar return ediyoruz.
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            AddShoppingItemFragment::class.java.name -> AddShoppingItemFragment(glide)
            ShoppingFragment::class.java.name -> ShoppingFragment(
                shoppingItemAdapter,
                ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
            )
            else -> super.instantiate(classLoader, className)

        }

    }

}