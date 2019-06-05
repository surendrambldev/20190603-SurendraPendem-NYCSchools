package com.jpmc.nycschools.repository

/**
 * ApiFactory generates retrofit interface for APIs
 */
class ApiFactory(private val retrofitFactory: RetrofitFactory) {
    val schoolsApi: SchoolsApi
        get() = retrofitFactory.retrofitSchools().create(SchoolsApi::class.java)
}