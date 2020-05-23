package com.example.sky.ui.discover


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sky.R
import com.example.sky.ui.discover.DiscoverSearchFragment.OnListFragmentInteractionListener
import com.example.sky.ui.discover.DiscoverSearchFragment.PlacesDiffCallBack
import com.example.sky.ui.discover.model.PlaceContent.PlaceItem
import kotlinx.android.synthetic.main.fragment_discover_search.view.*

/**
 * [RecyclerView.Adapter] that can display a [PlaceItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class DiscoverSearchItemRecyclerViewAdapter(
    private val mValues: List<PlaceItem>,
    private val mListener: OnListFragmentInteractionListener?
) : ListAdapter<PlaceItem, DiscoverSearchItemRecyclerViewAdapter.ViewHolder>(PlacesDiffCallBack()) {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as PlaceItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_discover_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mContentView.text = item.placeName
        Log.i("sdsdsdsasasas", item.placeName + "sds")
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
