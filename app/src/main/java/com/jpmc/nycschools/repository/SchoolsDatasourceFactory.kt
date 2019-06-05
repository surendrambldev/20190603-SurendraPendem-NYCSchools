package com.jpmc.nycschools.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.jpmc.nycschools.model.School


class SchoolsDatasourceFactory(private val apiFactory: ApiFactory) : DataSource.Factory<Int, School>() {

    private var _schools = MutableLiveData<SchoolsDataSource>()
    val schoolsData: LiveData<SchoolsDataSource>
        get() = _schools

    override fun create(): DataSource<Int, School> {
        val schoolsDataSource = SchoolsDataSource(apiFactory)
        _schools.postValue(schoolsDataSource)
        return schoolsDataSource
    }

}