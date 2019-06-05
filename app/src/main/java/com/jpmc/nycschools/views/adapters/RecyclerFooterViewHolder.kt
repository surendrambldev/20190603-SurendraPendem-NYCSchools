package com.jpmc.nycschools.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jpmc.nycschools.R
import com.jpmc.nycschools.model.State
import kotlinx.android.synthetic.main.recycler_footer_item.view.*

/**
 * RecyclerViewHolder for recycler footer view
 */
class RecyclerFooterViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(status: State?) {
        itemView.progress_bar.visibility = if (status == State.LOADING) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        fun create(parent: ViewGroup): RecyclerFooterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_footer_item, parent, false)
            return RecyclerFooterViewHolder(view)
        }
    }
}