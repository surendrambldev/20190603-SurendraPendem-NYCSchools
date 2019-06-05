package com.jpmc.nycschools.repository

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jpmc.nycschools.constants.AppConstants
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * RetrofitFactory generates retrofit clients
 */
object RetrofitFactory {

    private fun createHttpClient(): OkHttpClient {
        val standardConnectionSpec = Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS)
        val builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS
        builder.connectionSpecs(standardConnectionSpec)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .followRedirects(false)
            .followSslRedirects(false)
            .addInterceptor(logging)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .writeTimeout(30000, TimeUnit.MILLISECONDS)
        return builder.build()
    }


    fun retrofitSchools(): Retrofit {
        val converterFactoryDateFormat =
            GsonConverterFactory.create(GsonBuilder().create())
        return Retrofit.Builder()
            .addConverterFactory(converterFactoryDateFormat)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(AppConstants.BASE_URL)
            .client(createHttpClient())
            .build()
    }
}