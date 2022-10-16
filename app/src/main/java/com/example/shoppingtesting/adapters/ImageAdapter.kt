package com.example.shoppingtesting.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.shoppingtesting.R
import com.example.shoppingtesting.other.OnItemClickListener
import kotlinx.android.synthetic.main.item_image.view.*
import javax.inject.Inject

class ImageAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(), OnItemClickListener {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_image,
                parent,
                false
            )
        )
    }

    // Burada ise PixabayApi'den gelen image'in url'ini manuel olarak test senaryolarımız için kendimiz image değişkeni adında bir MutableList oluşturduk.
    // İlgili url'i test senaryonumuz içerisinden kendimiz manuel olarak string bir değer olarak verebiliriz.
    // Yani images değişkenimize ImagePickFragmentTest class'ımızda erişip oradan ImageAdapter RecyclerView'ımıza string bir değer verebiliriz.
    var images: MutableList<String>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    // RecyclerView İçerisinde ilgili view'a click özelliği atmak için
    private var onItemClickListener: ((String) -> Unit)? = null
    override fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    // Burada RecyclerView üzerinde oluşacak herbir item'a click özelliği getirdik.
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val url = images[position]

        holder.itemView.apply {
            glide.load(url).into(ivShoppingImage)
            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(url)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }


}