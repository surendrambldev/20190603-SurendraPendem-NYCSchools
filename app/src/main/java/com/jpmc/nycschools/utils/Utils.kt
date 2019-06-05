package com.jpmc.nycschools.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager


object Utils {
    /**
     * Utility function to check internet connectivity
     */
    fun isConnectedToNetwork(context: Context): Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null
    }
}