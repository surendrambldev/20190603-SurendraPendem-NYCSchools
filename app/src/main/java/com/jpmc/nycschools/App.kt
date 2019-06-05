package com.jpmc.nycschools

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KoinModules.startKoin(this)
    }
}