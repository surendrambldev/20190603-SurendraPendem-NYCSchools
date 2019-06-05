package com.jpmc.nycschools.repository

import com.jpmc.nycschools.model.School
import com.jpmc.nycschools.model.Score

interface SchoolsRepo {

    suspend fun fetchSchoolAsync(schoolId: String): List<School>?

    suspend fun fetchScoreAsync(schoolId: String): List<Score>?

    suspend fun fetchSearchResultsAsync(query: String): List<School>?
}