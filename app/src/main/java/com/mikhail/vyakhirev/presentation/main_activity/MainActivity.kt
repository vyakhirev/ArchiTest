package com.mikhail.vyakhirev.presentation.main_activity

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.databinding.ActivityMainBinding
import com.mikhail.vyakhirev.utils.extensions.getCroppedBitmap
import com.mikhail.vyakhirev.utils.extensions.hasInternet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var accountIcon: MenuItem? = null
    private var accountName: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//Crushlitics
//        val i= 10 / 0

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        setSupportActionBar(binding.toolbar)
        // Get a support ActionBar corresponding to this toolbar and enable the Up button
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navHostFragment.navController)

        binding.bottomNav.visibility = View.VISIBLE
        binding.bottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
//        viewModel.loadFbUserData()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.account_menu, menu)
        accountIcon = menu?.findItem(R.id.account_icon)
//        supportActionBar?.title = "Search photo"


//        accountName = menu?.findItem(R.id.account_name)

//        viewModel.loadFbUserData()
//        viewModel.user.observe(this, {
//            accountName?.title = it.name
//            viewModel.loadFbUserData()
//        })
        setupAccountButton()
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupAccountButton() {
        val account = GoogleSignIn.getLastSignedInAccount(this)

//        var logged = false
//        viewModel.isLogged.observe(this,{
//            if (it)
//               logged = true
//        })

        viewModel.isMyAppLogin()
        accountIcon?.setOnMenuItemClickListener {
            if ((account != null && hasInternet(this)) ) {
                navController.navigate(R.id.userDetailsFragment)
                binding.bottomNav.visibility = View.GONE
            } else if (viewModel.isLogged.value == true)
            {
                navController.navigate(R.id.userDetailsFragment)
                binding.bottomNav.visibility = View.GONE
            }
            else {
                navController.navigate(R.id.loginFragment)
                binding.bottomNav.visibility = View.GONE
//                binding.bottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
            }
            return@setOnMenuItemClickListener true
        }

        if (account != null && hasInternet(this)) {
            CoroutineScope(Dispatchers.IO).launch {
                val drawable = getAccountImage(account.photoUrl)
                if (drawable != null) {
                    withContext(Dispatchers.Main) {
                        accountIcon?.icon = drawable
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            accountIcon?.tooltipText = account.displayName
                        }
                    }
                }

            }
        }
    }

    private fun getAccountImage(url: Uri?): Drawable? {
        return try {
            val connection = URL(url.toString()).openConnection() as HttpURLConnection
            connection.connect()
            val bitmap = BitmapFactory.decodeStream(connection.inputStream).getCroppedBitmap()
            BitmapDrawable(Resources.getSystem(), bitmap)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }

    }

    fun refreshToolbar(){

    }

    fun setBottomNavigationVisibility(visibility: Int) {
        // get the reference of the bottomNavigationView and set the visibility.
        binding.bottomNav.visibility = visibility
    }

}