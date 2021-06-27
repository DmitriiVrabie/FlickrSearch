package com.dvrabie.flickrsearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class SearchAdapter : ListAdapter<Image, SearchAdapter.ImageViewHolder>(SearchDiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Image) {
            val imageView = itemView as ImageView
            Picasso.get()
                .load(item.url)
                .resize(400, 400)
                .centerCrop()
                .into(itemView)
        }
    }

    object SearchDiffUtil : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean =
            oldItem == newItem

    }

}