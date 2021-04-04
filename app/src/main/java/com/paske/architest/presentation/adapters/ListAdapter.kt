package com.paske.architest.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.paske.architest.data.model.PhotoItem
import com.paske.architest.databinding.ListRowBinding
import com.paske.architest.presentation.list_fragment.ListViewModel
import java.lang.IndexOutOfBoundsException



class ListAdapter (
    private val viewModel: ListViewModel,
    private var items: MutableList<PhotoItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListRowBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount()= items.size


}
//RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {
//    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(product: Basket.Request) {
//            val binding = DataBindingUtil.bind<ProductRowBinding>(itemView)?.apply {
//                this.viewmodel = viewModel
//                this.product = product
//            }
//
//            binding?.normalLayout?.setOnClickListener {
//                if(hasInternet(it)){
//                    viewModel.openProduct(product)
//                }
//            }
//
//            binding?.emptyLayout?.setOnClickListener {
//                if(hasInternet(it)){
//                    viewModel.openProduct(product)
//                }
//            }
//
//            binding?.normalLayout?.setOnLongClickListener {
//                viewModel.showProductDialog(product)
//                return@setOnLongClickListener true
//            }
//        }
//    }
//
//    fun getItem(position: Int): Basket.Request? {
//        return try {
//            items[position]
//        } catch (ex: IndexOutOfBoundsException) {
//            null
//        }
//
//    }
//
//    private val items = ArrayList<Basket.Request>()
//    fun submitList(newList: List<Basket.Request>) {
//        items.clear()
//        items.addAll(newList.sortedByDescending { it.createdAt })
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val binding = ProductRowBinding.inflate(inflater, parent, false)
//        return ProductViewHolder(binding.root)
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//        holder.bind(items[position])
//    }
//
//
//}