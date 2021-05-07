package com.mikhail.vyakhirev.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.data.model.PhotoItem
import com.mikhail.vyakhirev.databinding.ListRowBinding
import com.mikhail.vyakhirev.utils.extensions.loadImageFromLink

class ListViewHolder(val binding: ListRowBinding) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ResourceAsColor")
    fun bind(item: PhotoItem) {
        binding.apply {
            photoIV.loadImageFromLink(item.getFlickrImageLink())

            titleTV.setTextColor(R.color.colorPrimary)
            titleTV.text = item.title

            favorStar.setImageResource(
                if (item.isFavorite)
                    R.drawable.ic_star_on
                else
                    R.drawable.ic_star_off
            )
        }

    }

//    private fun ImageView.loadImageFromLink(link: String?) {
//        if (!link.isNullOrEmpty()) {
//            Glide.with(context.applicationContext)
//                .load(link)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .dontAnimate()
//                .into(this)
//        }
//    }

    companion object {
        fun create(parent: ViewGroup): ListViewHolder {
            val binding = ListRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ListViewHolder(binding)
        }
    }
}