package com.jpmc.nycschools.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jpmc.nycschools.R
import com.jpmc.nycschools.model.School
import com.jpmc.nycschools.utils.RecyclerDiffUtil
import java.util.*

/**
 * RecyclerViewAdapter for SearchView
 */
class RecyclerViewAdapter : RecyclerView.Adapter<SchoolsViewHolder>() {

    private val schools by lazy { ArrayList<School>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolsViewHolder {
        return SchoolsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recyler_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return this.schools.size
    }

    override fun onBindViewHolder(holder: SchoolsViewHolder, position: Int) {
        holder.bind(schools[position], true)
    }

    /**
     * Updating dataset for schools
     *
     * @param schoolsList latest schools data from API
     */
    fun updateData(schoolsList: List<School>) {
        val diffCallback = RecyclerDiffUtil(this.schools, schoolsList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.schools.clear()
        this.schools.addAll(schoolsList)
        diffResult.dispatchUpdatesTo(this)
    }
}