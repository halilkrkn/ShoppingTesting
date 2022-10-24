package com.example.shoppingtesting.ui.views

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppingtesting.R
import com.example.shoppingtesting.adapters.ImageAdapter
import com.example.shoppingtesting.other.Constants.GRID_SPAN_COUNT
import com.example.shoppingtesting.other.Constants.SEARCH_TIME_DELAY
import com.example.shoppingtesting.other.Status
import com.example.shoppingtesting.ui.viewmodels.ShoppingViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_image_pick.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImagePickFragment @Inject constructor(
    val imageAdapter: ImageAdapter
): Fragment(R.layout.fragment_image_pick) {

    lateinit var viewModel: ShoppingViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        subscribeToObserves()
        setupRecyclerView()


        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
//                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        viewModel.searchForImage(editable.toString())
                    }
                }
            }
        }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        var job: Job ?= null

        searchView.queryHint = "Search for Images..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                job?.cancel()
                job = lifecycleScope.launch {
//                    delay(SEARCH_TIME_DELAY)
                    viewModel.searchForImage(newText)
                }
                return true
            }
        })
    }

    private fun subscribeToObserves() {

        viewModel.images.observe(viewLifecycleOwner){ event ->

            event.getContentIfNotHandled()?.let { resourceResult ->
                when(resourceResult.status) {
                    Status.SUCCESS -> {
                        var url = resourceResult.data?.hits?.map { imageResult ->
                            imageResult.previewURL
                        }
                        imageAdapter.images = (url ?: mutableListOf()) as MutableList<String>
                        progressBar.visibility = View.GONE
                    }

                    Status.ERROR -> {
                        Snackbar.make(
                            requireActivity().rootLayout,
                            resourceResult.message ?: "An unknown error occured",
                            Snackbar.LENGTH_LONG
                        ).show()
                        progressBar.visibility = View.GONE
                    }

                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}