package com.mikhail.vyakhirev.presentation.main_activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)

//Crushlitics
//        val i= 10 / 0

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navHostFragment.navController)

        var isLogged = false

        val facebookToken=AccessToken.getCurrentAccessToken()
        if(facebookToken != null && !facebookToken.isExpired)
            isLogged = true

        if (!isLogged) {
            navController.navigate(R.id.loginFragment)
            binding.bottomNav.visibility=View.GONE
        }
        else{
            binding.bottomNav.visibility=View.VISIBLE
            binding.bottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        }

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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        callbackManager.onActivityResult(requestCode, resultCode, data)
//        super.onActivityResult(requestCode, resultCode, data)
//    }

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