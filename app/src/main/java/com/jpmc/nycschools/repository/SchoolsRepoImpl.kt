package com.jpmc.nycschools.repository

import com.jpmc.nycschools.model.School
import com.jpmc.nycschools.model.Score
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * SchoolsRepoImpl fetches the data from API
 */
class SchoolsRepoImpl(private val apiFactory: ApiFactory) : SchoolsRepo {

    /**
     * fetches School data
     * @param schoolId      schoolId(dbn) to get specific school details
     * @return returns List of schools object
     */
    override suspend fun fetchSchoolAsync(schoolId: String): List<School>? {
        return withContext(Dispatchers.IO) {
            val deferredResponse = apiFactory.schoolsApi.fetchSchoolAsync(schoolId)
            val response = deferredResponse.await()
            if (response.isSuccessful) response.body() else null
        }
    }

    /**
     * fetches School SAT scores data
     * @param schoolId      schoolId(dbn) to gets pecific school SAT score details
     * @return returns list of schools object
     */
    override suspend fun fetchScoreAsync(schoolId: String): List<Score>? {
        return withContext(Dispatchers.IO) {
            val deferredResponse = apiFactory.schoolsApi.fetchScoreAsync(schoolId)
            val response = deferredResponse.await()
            if (response.isSuccessful) response.body() else null
        }
    }

    /**
     * fetches search results data
     * @param query   search term
     * @return returns list of schools object
     */
    override suspend fun fetchSearchResultsAsync(query: String): List<School>? {
        return withContext(Dispatchers.IO) {
            val deferredResponse = apiFactory.schoolsApi.fetchSearchResultsAsync(query)
            val response = deferredResponse.await()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
    }
}