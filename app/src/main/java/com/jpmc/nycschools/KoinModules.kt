package com.jpmc.nycschools

import android.app.Application
import com.jpmc.nycschools.repository.*
import com.jpmc.nycschools.viewmodels.HomeViewModel
import com.jpmc.nycschools.viewmodels.SchoolDetailsViewModel
import com.jpmc.nycschools.viewmodels.SearchViewModel
import org.koin.android.ext.android.startKoin
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

/**
 * KoinModules will init Koin KTX here for the project
 */
object KoinModules {

    /**
     * init koin with available Modules
     */
    fun startKoin(application: Application) {
        application.startKoin(application, listOf(homeView, detailsView, searchView))
    }

    /**
     * HomeViewModule for all decencies needed for HomeFragment
     */
    val homeView: Module = module {
        single { ApiFactory(RetrofitFactory) }
        single { SchoolsDatasourceFactory(apiFactory = get()) }
        viewModel { HomeViewModel(schoolsDatasourceFactory = get()) }
    }

    /**
     * DetailsViewModule for all decencies needed for SchoolDetailsFragment
     */
    val detailsView: Module = module {
        single<SchoolsRepo> { SchoolsRepoImpl(apiFactory = get()) }
        viewModel { SchoolDetailsViewModel(schoolsRepo = get()) }
    }

    /**
     * SearchViewModule for all decencies needed for SearchFragment
     */
    val searchView: Module = module {
        viewModel { SearchViewModel(schoolsRepo = get()) }
    }


}