package com.example.shoppingtesting.ui.views

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.shoppingtesting.R
import com.example.shoppingtesting.other.Status
import com.example.shoppingtesting.ui.viewmodels.ShoppingViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_shopping_item.*
import javax.inject.Inject

@AndroidEntryPoint
class AddShoppingItemFragment @Inject constructor(
    val glide: RequestManager
) : Fragment(R.layout.fragment_add_shopping_item) {

    lateinit var viewModel: ShoppingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        subscribeToObserves()

        // Add Butonuna tıklandığında kullanıcının editText'e girmiş olduğu verileri database'e insert işlemi yaptırıyoruz ve database'e ekliyoruz.
        btnAddShoppingItem.setOnClickListener {
            viewModel.insertShoppingItem(
                etShoppingItemName.text.toString(),
                etShoppingItemAmount.text.toString(),
                etShoppingItemPrice.text.toString()
            )
        }

        // Burada apiden resim seçmek için ImagePickFragment'a yönlendiriyoruz ve buradan apiden bir resim url'i seçiyoruz yani ilgili resmi çekiyoruz.
        ivShoppingImage.setOnClickListener {
            findNavController().navigate(
                AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
            )

            // Burada apiden resim seçmek için ImagePickFragment'a yönlendirildikten sonra geri AddShoppingFragment'a gelince tekrar ImagePickFragment'a gittiğimizde seçmiş olduğumuz api deki image gözükmesin diye url'ini sıfırladık.
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.setCurImageUrl("")
                    findNavController().popBackStack()
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(callback)
        }
    }

    // Viewmodel'den gelen verileri Observe ediyoruz.
    private fun subscribeToObserves() {
        // Apiden gelen image imageView'da gösteriyoruz.
        viewModel.curImageUrl.observe(viewLifecycleOwner) {
            glide.load(it)
                .into(ivShoppingImage)
        }

        // Database'e eklenen verilerin durumlarına göre (success, error, loading gibi) snackbar da durum bilgilendirmesi işlemi yapıyoruz
        viewModel.insertShoppingItemStatus.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { resourceResult ->
                when (resourceResult.status) {
                    Status.SUCCESS -> {
                        Snackbar.make(
                            requireActivity().rootLayout,
                            "Added Shopping Item",
                            Snackbar.LENGTH_LONG
                        ).show()
                        findNavController().popBackStack()
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                            requireActivity().rootLayout,
                            resourceResult.message ?: "An unknown error occurred",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {
                        /* NO-OP */
                    }
                }
            }
        }
    }
}