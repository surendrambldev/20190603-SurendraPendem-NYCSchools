package com.jpmc.nycschools.utils

import androidx.recyclerview.widget.DiffUtil
import com.jpmc.nycschools.model.School

/**
 * DiffUtil class for smoothly handling  dataset changes to RecyclerView
 */
class RecyclerDiffUtil(private val oldSchools: List<School>, private val newSchools: List<School>) :
    DiffUtil.Callback() {

    override fun getOldListSize() = oldSchools.size

    override fun getNewListSize() = newSchools.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldSchools[oldItemPosition] == newSchools[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldSchools[oldItemPosition].schoolId == newSchools[newItemPosition].schoolId
    }
}