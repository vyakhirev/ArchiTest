package com.mikhail.vyakhirev.presentation.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.utils.DiffUtilCallBack


class ListAdapter(
//    private var items: List<PhotoItem>
) : PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.list_row) {
            ListViewHolder.create(parent)
        } else {
            SeparatorViewHolder.create(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.Photo -> R.layout.list_row
            is UiModel.SeparatorItem -> R.layout.separator_view_item
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        uiModel.let {
            when (uiModel) {
                is UiModel.Photo -> (holder as ListViewHolder).bind(uiModel.photoItem)
                is UiModel.SeparatorItem -> (holder as SeparatorViewHolder).bind(uiModel.description)
            }
        }
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val binding = ListRowBinding.inflate(inflater, parent, false)
//        return ListViewHolder(binding.root)
//    }
//
//    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        val item = getItem(position)
//        if (holder is ListViewHolder && item != null)
//            holder.bind(item)
//    }

//    override fun getItemCount()= items.size

//    fun updateItems(newItems:List<PhotoItem>){
//       items=newItems
//       notifyDataSetChanged()
//    }

//    companion object {
//        private val COMPARATOR = object : DiffUtil.ItemCallback<UiModel>() {
//            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
//                return (oldItem is UiModel.Photo && newItem is UiModel.Photo &&
//                        oldItem.photoItem.id == newItem.photoItem.id) ||
//                        (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem &&
//                                oldItem.description == newItem.description)
//            }
//
//            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
//                oldItem == newItem
//        }
//    }


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