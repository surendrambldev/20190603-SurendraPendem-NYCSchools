package com.jpmc.nycschools.views.adapters

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jpmc.nycschools.model.School
import com.jpmc.nycschools.model.State

/**
 * RecyclerViewHolder for SchoolsRecyclerView
 */
class SchoolsRecyclerAdapter : PagedListAdapter<School, RecyclerView.ViewHolder>(schoolsDiffCallback) {

    private val REGULAR_VIEW = 1
    private val FOOTER_VIEW = 2
    private var state = State.LOADING

    /**
     * Setting viewHolder for viewTypes
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == REGULAR_VIEW) SchoolsViewHolder.create(
            parent
        ) else RecyclerFooterViewHolder.create(
            parent
        )
    }

    /**
     * Binding viewtoHolder
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (REGULAR_VIEW) {
            getItemViewType(position) -> (holder as SchoolsViewHolder).bind(getItem(position), false)
            else -> (holder as RecyclerFooterViewHolder).bind(state)
        }
    }


    /**
     * Returns type of viewType
     *
     * @return 1 if viewType is regular view else 0.
     */
    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) REGULAR_VIEW else FOOTER_VIEW
    }

    companion object {
        //DiffUtil callback for PagedListAdapter
        val schoolsDiffCallback = object : DiffUtil.ItemCallback<School>() {
            override fun areItemsTheSame(oldItem: School, newItem: School): Boolean {
                return oldItem.schoolId == newItem.schoolId
            }

            override fun areContentsTheSame(oldItem: School, newItem: School): Boolean {
                return oldItem == newItem
            }
        }
    }

    /**
     * Determines view has a footer
     *
     * @return true if data state is in loading or error
     */
    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    /**
     * Update the state
     */
    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }
}