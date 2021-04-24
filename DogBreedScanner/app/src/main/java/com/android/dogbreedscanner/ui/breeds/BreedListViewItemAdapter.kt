package com.android.dogbreedscanner.ui.breeds

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.dogbreedscanner.R
import com.android.dogbreedscanner.databinding.AdapterListViewItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-23.
 * </prev>
 */
class BreedListViewItemAdapter(
    private val mapItems: HashMap<String, BreedsViewModel.BreedListUiData>,
    private var clickListener: (Int, Int) -> Unit
) : RecyclerView.Adapter<BreedListViewItemAdapter.ListViewItemViewHolder>() {

    private var listAdapterItems = arrayListOf<BreedsViewModel.BreedListUiData>()

    class ListViewItemViewHolder(
        private val binding: AdapterListViewItemBinding,
        val clickListener: (Int, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BreedsViewModel.BreedListUiData) {

            val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

            binding.apply {
                itemNameStr = item.name

                Glide.with(itemImage.context)
                    .load(item.url)
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .apply(RequestOptions().centerCrop().placeholder(R.drawable.image_large))
                    .into(itemImage)

                root.setOnClickListener {
                    clickListener(item.id, absoluteAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: AdapterListViewItemBinding =
            AdapterListViewItemBinding.inflate(layoutInflater, parent, false)
        return ListViewItemViewHolder(itemBinding, clickListener)
    }

    override fun getItemCount(): Int = listAdapterItems.size

    override fun onBindViewHolder(holder: ListViewItemViewHolder, position: Int) {
        holder.bind(listAdapterItems[position])
    }

    fun addMoreItems(newItems: HashMap<String, BreedsViewModel.BreedListUiData>) {
        val positionStart = this.itemCount
        this.mapItems.putAll(newItems)
        this.listAdapterItems.addAll(newItems.values)
        notifyItemRangeInserted(positionStart, listAdapterItems.size)
    }

    fun orderListAlphabetically() {
        this.listAdapterItems = arrayListOf()
        this.listAdapterItems.addAll(mapItems.toSortedMap().values)
        notifyDataSetChanged()
    }

    fun getMapItems() = mapItems
}