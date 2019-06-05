package com.jpmc.nycschools

import com.jpmc.nycschools.repository.SchoolsRepo
import com.jpmc.nycschools.viewmodels.SearchViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class SearchViewModelTest : BaseTest() {

    @Mock
    private lateinit var schoolsRepo: SchoolsRepo
    private lateinit var searchViewModel: SearchViewModel

    override fun init() {
        MockitoAnnotations.initMocks(this)
        searchViewModel = SearchViewModel(schoolsRepo)
    }

    @Test
    fun searchMethodShouldTriggerRepoToFetchData() {
        runBlocking {
            searchViewModel.search("hil")
            Thread.sleep(100)
            verify(schoolsRepo, times(1)).fetchSearchResultsAsync(any())
        }
    }

    @Test
    fun formatQueryShouldFormatSearchTermToMatchWithApiQueryFormat() {
        runBlocking {
            val expected = "school_name like '%Hil%'"
            val actual = searchViewModel.formatQuery("hil")
            Assert.assertEquals(expected, actual)
        }
    }
}