package com.jpmc.nycschools.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.jpmc.nycschools.model.School
import com.jpmc.nycschools.model.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * SchoolsDataSource extends PageKeyedDataSource to provide dataSource to implement pagination
 */
class SchoolsDataSource(private val apiFactory: ApiFactory) : PageKeyedDataSource<Int, School>() {

    /**
     * data state LiveData
     */
    private var _state = MutableLiveData<State>()
    val dataState: LiveData<State>
        get() = _state

    /**
     * shimmer LiveData
     */
    private var _showShimmer = MutableLiveData<Boolean>()
    val showShimmer: LiveData<Boolean>
        get() = _showShimmer

    /**
     * loadInitial fetches initial dataset for Schools
     */
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, School>) {
        _showShimmer.postValue(true)
        _state.postValue(State.LOADING)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val deferredResponse =
                    apiFactory.schoolsApi.fetchSchoolsAsync(0, params.requestedLoadSize)
                val response = deferredResponse.await()
                val schools = response.body()
                if (response.isSuccessful && !schools.isNullOrEmpty()) {
                    _showShimmer.postValue(false)
                    _state.postValue(State.COMPLETED)
                    callback.onResult(schools, null, 5)
                } else {
                    _showShimmer.postValue(false)
                    _state.postValue(State.ERROR)
                }
            } catch (exception: Exception) {
                Log.e("SchoolsDataSource", "Got exception while fetching schools data")
                exception.printStackTrace()
            }

        }
    }

    /**
     * loadAfter fetches next dataset for Schools as we scroll through the list
     */
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, School>) {
        _state.postValue(State.LOADING)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val deferredResponse = apiFactory.schoolsApi
                    .fetchSchoolsAsync(params.key, params.requestedLoadSize)
                val response = deferredResponse.await()
                val schools = response.body()
                if (response.isSuccessful && !schools.isNullOrEmpty()) {
                    _state.postValue(State.COMPLETED)
                    callback.onResult(schools, params.key + 5)
                } else {
                    _state.postValue(State.ERROR)
                }
            } catch (exception: Exception) {
                Log.e("SchoolsDataSource", "Got exception while fetching schools data")
                exception.printStackTrace()
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, School>) {}
}