package com.jpmc.nycschools.repository

import com.jpmc.nycschools.constants.AppConstants
import com.jpmc.nycschools.model.School
import com.jpmc.nycschools.model.Score
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SchoolsApi {
    @GET("s3k6-pzi2.json")
    fun fetchSchoolsAsync(@Query("${'$'}offset") offset: Int, @Query("${'$'}limit") limit: Int, @Header("X-App-Token") appToken: String = AppConstants.API_TOKEN): Deferred<Response<List<School>>>

    @GET("s3k6-pzi2.json")
    fun fetchSchoolAsync(@Query("dbn") schoolId: String, @Header("X-App-Token") appToken: String = AppConstants.API_TOKEN): Deferred<Response<List<School>>>

    @GET("s3k6-pzi2.json")
    fun fetchSearchResultsAsync(@Query("${'$'}where") query: String, @Header("X-App-Token") appToken: String = AppConstants.API_TOKEN): Deferred<Response<List<School>>>

    @GET("f9bf-2cp4.json")
    fun fetchScoreAsync(@Query("dbn") schoolId: String, @Header("X-App-Token") appToken: String = AppConstants.API_TOKEN): Deferred<Response<List<Score>>>
}