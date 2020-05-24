package com.example.sky.ui.discover


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sky.databinding.FragmentDiscoverSearchItemBinding
import com.example.sky.ui.discover.model.PlaceContent.PlaceItem

/**
 * [RecyclerView.Adapter] that can display a [PlaceItem]
 */
class DiscoverSearchItemRecyclerViewAdapter(
    private val mListener: PlaceItemListener
) : ListAdapter<PlaceItem, DiscoverSearchItemRecyclerViewAdapter.ViewHolder>(PlacesDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, mListener)
    }

    class ViewHolder(val binding: FragmentDiscoverSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            placeItem: PlaceItem,
            mListener: PlaceItemListener
        ) {
            binding.place = placeItem
            binding.clickListener = mListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FragmentDiscoverSearchItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class PlaceItemListener(
    val clickListener: (placeId: String) -> Unit,
    val predictionIconClickListener: (placeName: String) -> Unit
) {
    fun onClick(placeItem: PlaceItem) = clickListener(placeItem.placeId)
    fun onPredictionIcon(placeItem: PlaceItem) = predictionIconClickListener(placeItem.placeName)
}

class PlacesDiffCallBack :
    DiffUtil.ItemCallback<PlaceItem>() {
    override fun areItemsTheSame(oldItem: PlaceItem, newItem: PlaceItem): Boolean {
        return oldItem.placeId == newItem.placeId
    }

    override fun areContentsTheSame(oldItem: PlaceItem, newItem: PlaceItem): Boolean {
        return oldItem == newItem
    }
}