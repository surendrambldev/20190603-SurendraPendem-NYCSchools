package com.jpmc.nycschools.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jpmc.nycschools.R


class MainActivity : AppCompatActivity() {

    /**
     * This is the NavigationController for nav_graph
     */
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setting staus bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window?.apply {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                statusBarColor = ContextCompat.getColor(context, R.color.colorLiteWhite)
            }
        }
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    /**
     * Setting all the requirement for BottomNavigation with Jetpack Navigation Component
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        navController = Navigation.findNavController(this, R.id.fragment_container)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when {
                destination.id == R.id.homeFragment -> bottomNavigationView.visibility = View.VISIBLE
                destination.id == R.id.searchFragment -> bottomNavigationView.visibility = View.GONE
                destination.id == R.id.schoolDetailsFragment -> bottomNavigationView.visibility = View.GONE
            }
        }
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            onNavDestinationSelected(item, navController)
        }
    }

    override fun onSupportNavigateUp() = if (::navController.isInitialized) navController.navigateUp() else false
}
