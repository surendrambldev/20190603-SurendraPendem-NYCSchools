package com.jpmc.nycschools

import com.jpmc.nycschools.repository.SchoolsRepo
import com.jpmc.nycschools.viewmodels.SchoolDetailsViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SchoolDetailViewModelTest : BaseTest() {

    @Mock
    private lateinit var schoolsRepo: SchoolsRepo
    private lateinit var schoolDetailsViewModel: SchoolDetailsViewModel

    override fun init() {
        MockitoAnnotations.initMocks(this)
        schoolDetailsViewModel = SchoolDetailsViewModel(schoolsRepo)
    }

    @Test
    fun fetchDataMethodShouldTriggerRepoToFetchData() {
        runBlocking {
            schoolDetailsViewModel.fetchData("27Q475")
            Thread.sleep(100)
            verify(schoolsRepo, times(1)).fetchSchoolAsync(any())
            Thread.sleep(100)
            verify(schoolsRepo, times(1)).fetchScoreAsync(any())
        }
    }

}