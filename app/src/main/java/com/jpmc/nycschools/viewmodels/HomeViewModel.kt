package com.jpmc.nycschools.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jpmc.nycschools.model.School
import com.jpmc.nycschools.model.State
import com.jpmc.nycschools.repository.SchoolsDataSource
import com.jpmc.nycschools.repository.SchoolsDatasourceFactory


class HomeViewModel(private val schoolsDatasourceFactory: SchoolsDatasourceFactory) :
    ViewModel() {

    /**
     * liveData object for showShimmer value
     */
    val showShimmer: LiveData<Boolean>
        get() = Transformations.switchMap<SchoolsDataSource, Boolean>(
            schoolsDatasourceFactory.schoolsData,
            SchoolsDataSource::showShimmer
        )

    /**
     * liveData object for schoolsList value
     */
    var schoolsList: LiveData<PagedList<School>>
    private val pageSize = 5

    /**
     * initializing configuration for paging mechanism
     */
    init {
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize)
            .setEnablePlaceholders(false)
            .build()
        schoolsList = LivePagedListBuilder<Int, School>(schoolsDatasourceFactory, config).build()
    }

    /**
     * @returns liveData object for state value
     */
    fun getState(): LiveData<State> = Transformations
        .switchMap<SchoolsDataSource, State>(schoolsDatasourceFactory.schoolsData, SchoolsDataSource::dataState)

    /**
     * @returns true if list is empty
     */
    fun listIsEmpty(): Boolean {
        return schoolsList.value?.isEmpty() ?: true
    }
}