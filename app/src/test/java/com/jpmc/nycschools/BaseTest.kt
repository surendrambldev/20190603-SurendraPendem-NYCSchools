package com.jpmc.nycschools

import org.junit.Before
import org.koin.java.standalone.KoinJavaStarter
import org.koin.test.AutoCloseKoinTest

open class BaseTest : AutoCloseKoinTest() {
    @Before
    open fun init() {
        KoinJavaStarter.startKoin(listOf(KoinModules.homeView, KoinModules.searchView, KoinModules.detailsView))
    }
}