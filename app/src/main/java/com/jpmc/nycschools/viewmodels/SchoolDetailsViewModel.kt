package com.jpmc.nycschools.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jpmc.nycschools.model.School
import com.jpmc.nycschools.model.Score
import com.jpmc.nycschools.repository.SchoolsRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SchoolDetailsViewModel(private val schoolsRepo: SchoolsRepo) : ViewModel() {
    /**
     * This is the job for all coroutines started by this ViewModel.
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by SchoolDetailsViewModel.
     */
    private val scope = CoroutineScope(Dispatchers.IO + viewModelJob)


    /**
     * liveData object for showShimmer value
     */
    private var _showShimmer = MutableLiveData<Boolean>()
    val showShimmer: LiveData<Boolean>
        get() = _showShimmer

    /**
     * liveData object for school value
     */
    private var _school = MutableLiveData<School?>()
    val school: LiveData<School?>
        get() = _school

    /**
     * liveData object for score value
     */
    private var _score = MutableLiveData<Score?>()
    val score: LiveData<Score?>
        get() = _score

    private lateinit var tempSchoolId: String

    /**
     * triggers school details data
     *
     * @param schoolId SchoolID(dbn)
     */
    fun fetchData(schoolId: String) {
        if (::tempSchoolId.isInitialized && schoolId == tempSchoolId) return else
            scope.launch {
                tempSchoolId = schoolId
                _showShimmer.postValue(true)
                fetchSchoolDetails(schoolId)
                fetchScoreDetails(schoolId)
                _showShimmer.postValue(false)
            }
    }

    /**
     * fetches school details data from Repo layer
     *
     * @param schoolId SchoolID(dbn)
     */
    private suspend fun fetchSchoolDetails(schoolId: String) {
        val schoolsList = schoolsRepo.fetchSchoolAsync(schoolId)
        if (!schoolsList.isNullOrEmpty()) _school.postValue(schoolsList[0]) else _school.postValue(null)
    }

    /**
     * fetches school SAT score details data from Repo layer
     *
     * @param schoolId SchoolID(dbn)
     */
    private suspend fun fetchScoreDetails(schoolId: String) {
        val schoolsList = schoolsRepo.fetchScoreAsync(schoolId)
        if (!schoolsList.isNullOrEmpty()) _score.postValue(schoolsList[0]) else _score.postValue(null)
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}