package com.jpmc.nycschools

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jpmc.nycschools.repository.SchoolsRepo
import com.jpmc.nycschools.viewmodels.SchoolDetailsViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

private class SchoolDetailsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var schoolsRepo: SchoolsRepo
    @Mock
    private lateinit var schoolDetailsViewModel: SchoolDetailsViewModel

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        preConditions()
        schoolDetailsViewModel = SchoolDetailsViewModel(schoolsRepo)
    }

    private fun preConditions() {

        runBlocking { whenever(schoolsRepo.fetchSchoolAsync(any())).thenReturn(emptyList()) }
    }
/*
    @Test
    suspend fun fetchSchoolDetails(schoolId: String) {
        val schoolsList = schoolsRepo.fetchSchoolAsync(schoolId)
        if (!schoolsList.isNullOrEmpty()) _school.postValue(schoolsList[0]) else _school.postValue(null)
    }*/
}