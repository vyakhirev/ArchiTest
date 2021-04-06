package com.mikhail.vyakhirev.presentation.adapters

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.data.model.PhotoItem
import com.mikhail.vyakhirev.databinding.ListRowBinding


class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var binding: ListRowBinding? = null

    fun bind(item: PhotoItem) {
        binding = ListRowBinding.bind(itemView)

        binding?.photoIV?.loadImageFromLink(item.getFlickrImageLink())

        binding?.titleTV?.setTextColor(Color.parseColor("#0972C5"))
        binding?.titleTV?.text = item.title

        binding?.favorStar?.setImageResource(if (item.isFavorite)
            R.drawable.ic_star_on else R.drawable.ic_star_off
        )
//        if (item.width_n.isNullOrEmpty())
//            itemView.photo_IV.setHeightRatio(calculateHeightRatio(item.width_n!!, item.height_n!!))
//
    }
    private fun ImageView.loadImageFromLink(link: String?) {
        if (!link.isNullOrEmpty()) {
            Glide.with(context.applicationContext)
                .load(link)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(this)
        }
    }

}