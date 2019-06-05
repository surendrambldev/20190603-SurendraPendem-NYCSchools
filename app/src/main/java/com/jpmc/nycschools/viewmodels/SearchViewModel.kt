package com.jpmc.nycschools.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jpmc.nycschools.model.School
import com.jpmc.nycschools.repository.SchoolsRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SearchViewModel(private val schoolsRepo: SchoolsRepo) : ViewModel() {

    /**
     * This is the job for all coroutines started by this ViewModel.
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     * Since we pass viewModelJob, you can cancel all coroutines
     * launched by scope by calling viewModelJob.cancel()
     */
    private val scope = CoroutineScope(Dispatchers.IO + viewModelJob)

    /**
     * liveData object for isLoading value
     */
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    /**
     * liveData object for schoolsList value
     */
    private var _schoolsList = MutableLiveData<List<School>>()
    val schools: LiveData<List<School>>
        get() = _schoolsList

    /**
     * liveData object for noresults
     */
    private var _hasNoResults = MutableLiveData<Boolean>()
    val hasNoResults: LiveData<Boolean>
        get() = _hasNoResults

    /**
     * fetches search data from Repo layer
     *
     * @param query search term
     */
    fun search(query: String) {
        scope.launch {
            _isLoading.postValue(true)
            val formattedQuery = formatQuery(query)
            val schoolsList = schoolsRepo.fetchSearchResultsAsync(formattedQuery)
            if (schoolsList.isNullOrEmpty()) {
                _hasNoResults.postValue(true)
            } else {
                _schoolsList.postValue(schoolsList)
                _hasNoResults.postValue(false)
            }
            _isLoading.postValue(false)
        }
    }

    /**
     * Formats queryString to match with API input format
     *
     * @param query search term
     *
     * @return a formatted query parameter
     */
    fun formatQuery(query: String): String {
        return "school_name like '%${query.capitalize()}%'"
    }
}