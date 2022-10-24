package com.example.shoppingtesting.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shoppingtesting.R
import com.example.shoppingtesting.ui.views.FragmentFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Oluşturduğumuz FragmentFactory'i MainActivity de supportFragmentManager.fragmentFactory ile çağırıyoruz.
    // Böylelikle Fragmentlarımızı MainActivity üzerinde tanımladık ve çalışmasını sağlattık.
    // Eğer bu tanımlamayı yapmasaydık hata alıyorduk ve uygulama çöküyordu.
    // İlgili Hata:
    // androidx.fragment.app.Fragment$InstantiationException: Unable to instantiate fragment com.example.shoppingtesting.ui.views.ShoppingFragment: could not find Fragment constructor
    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        setContentView(R.layout.activity_main)
    }
}