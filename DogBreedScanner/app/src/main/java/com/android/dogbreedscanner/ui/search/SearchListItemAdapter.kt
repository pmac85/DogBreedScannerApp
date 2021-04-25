package com.android.dogbreedscanner.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.dogbreedscanner.databinding.AdapterSearchListViewItemBinding

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-25.
 * </prev>
 */
class SearchListItemAdapter(
    private var listItems: ArrayList<SearchViewModel.BreedSearchListUiData>,
    private var clickListener: (Int, Int) -> Unit
) : RecyclerView.Adapter<SearchListItemAdapter.SearchListItemViewHolder>() {

    class SearchListItemViewHolder(
        private val binding: AdapterSearchListViewItemBinding,
        val clickListener: (Int, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: SearchViewModel.BreedSearchListUiData) {

            binding.apply {
                this.item = listItem

                root.setOnClickListener {
                    clickListener(listItem.id, absoluteAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: AdapterSearchListViewItemBinding =
            AdapterSearchListViewItemBinding.inflate(layoutInflater, parent, false)
        return SearchListItemViewHolder(itemBinding, clickListener)
    }

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: SearchListItemViewHolder, position: Int) {
        holder.bind(listItems[position])
    }

    fun updateList(items: ArrayList<SearchViewModel.BreedSearchListUiData>) {
        listItems = items
        notifyDataSetChanged()
    }
}