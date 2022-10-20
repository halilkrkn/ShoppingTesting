package com.example.shoppingtesting.ui.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppingtesting.R
import com.example.shoppingtesting.adapters.ImageAdapter
import com.example.shoppingtesting.other.Constants.GRID_SPAN_COUNT
import com.example.shoppingtesting.ui.viewmodels.ShoppingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_image_pick.*
import javax.inject.Inject

@AndroidEntryPoint
class ImagePickFragment @Inject constructor(
    val imageAdapter: ImageAdapter
): Fragment(R.layout.fragment_image_pick) {

    lateinit var viewModel: ShoppingViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)

        imageAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setCurImageUrl(it)
        }
    }

    private fun setupRecyclerView() {
        rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(),GRID_SPAN_COUNT)
        }
    }
}