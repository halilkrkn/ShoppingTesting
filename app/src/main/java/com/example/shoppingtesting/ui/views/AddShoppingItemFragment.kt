package com.example.shoppingtesting.ui.views

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.shoppingtesting.R
import com.example.shoppingtesting.ui.viewmodels.ShoppingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_image.*

@AndroidEntryPoint
class AddShoppingItemFragment : Fragment(R.layout.fragment_add_shopping_item) {

    lateinit var viewModel: ShoppingViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)

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
}