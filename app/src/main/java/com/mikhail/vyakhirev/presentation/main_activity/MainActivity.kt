package com.mikhail.vyakhirev.presentation.main_activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.Copy
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.android.retainedSubDI
import org.kodein.di.instance

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //    override val di by di()
//    override val di by retainedSubDI(closestDI(), copy = Copy.All) {
//    }
//    private val factory by instance<MainActivityViewModelFactory>()
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navHostFragment.navController)

        binding.bottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_list -> {
                    navController.popBackStack()
                    navController.navigate(R.id.listMyFragment)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.action_favoritesPhoto -> {
                    navController.popBackStack()
                    navController.navigate(R.id.favoritesFragment)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.action_settings -> {
                    navController.popBackStack()
                    navController.navigate(R.id.settingsFragment)
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_search, menu)
//        val searchItem: MenuItem = menu.findItem(R.id.action_search)
//        val searchView: SearchView = searchItem.actionView as SearchView
//
//        searchView.setOnQueryTextListener(
//            DebouncingQueryTextListener(
//                this.lifecycle
//            ) { newText ->
//                newText?.let {
////                    if (it.isEmpty()) {
////                        viewModel.resetSearch()
////                    } else {
//                        viewModel.searchMovies(it)
////                    }
//                }
//            }
//        )
//        return true
//    }
//
//    internal class DebouncingQueryTextListener(
//        lifecycle: Lifecycle,
//        private val onDebouncingQueryTextChange: (String?) -> Unit
//    ) : SearchView.OnQueryTextListener {
//        var debouncePeriod: Long = 500
//
//        private val coroutineScope = lifecycle.coroutineScope
//
//        private var searchJob: Job? = null
//
//        override fun onQueryTextSubmit(query: String?): Boolean {
//            return false
//        }
//
//        override fun onQueryTextChange(newText: String?): Boolean {
//            searchJob?.cancel()
//            searchJob = coroutineScope.launch {
//                newText?.let {
//                    delay(debouncePeriod)
//                    onDebouncingQueryTextChange(newText)
//                }
//            }
//            return false
//        }
//    }
}