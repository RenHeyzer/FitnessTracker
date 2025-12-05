package com.azim.fitness

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.azim.fitness.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<SaveDailyResultViewModel> {
        SaveDailyResultViewModelFactory(
            preferencesHelper = container.preferencesHelper,
            userRepository = container.userRepository,
            exercisesRepository = container.exercisesRepository,
            dailyResultRepository = container.dailyResultRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.startIfNeeded()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainFragment -> showBottomNavigation()
                R.id.caloriesFragment -> showBottomNavigation()
                R.id.calendarFragment -> showBottomNavigation()
                R.id.profileFragment -> showBottomNavigation()
                else -> hideBottomNavigation()
            }
        }

        if (savedInstanceState == null) {
            defineStartDestination(navController)
        }
    }

    private fun defineStartDestination(navController: NavController) {
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        with(container.preferencesHelper) {
            when {
                isAuthorized && isGoalsDefined -> navGraph.setStartDestination(R.id.mainFragment)
                isAuthorized && !isGoalsDefined -> navGraph.setStartDestination(R.id.goalsFragment)
                !isAuthorized && !isGoalsDefined -> navGraph.setStartDestination(R.id.registrationFragment)
            }
        }

        navController.graph = navGraph
    }

    private fun hideBottomNavigation() {
        binding.bottomNavigation.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }
}