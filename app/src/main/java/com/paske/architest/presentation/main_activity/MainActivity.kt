package com.paske.architest.presentation.main_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.paske.architest.R
import com.paske.architest.databinding.ActivityMainBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory by instance<MainActivityViewModelFactory>()
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    private fun setupNavigation() {
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNav)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//        bottomNavigation.findViewById<BottomNavigationItemView>(R.id.action_search).visibility=
//            View.INVISIBLE
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
//            val nav_Controller = findNavController(R.id.nav_host_fragment)
            when (it.itemId) {

                R.id.action_list -> {
//                    listPhotoMediator.openListPhotoScreen(
//                        R.id.fragmentContainer,
//                        supportFragmentManager
//                    )
                    return@OnNavigationItemSelectedListener true
                }

                R.id.action_favoritesPhoto -> {
//                    favoritesPhotoMediator.openFavoritesPhotoScreen(
//                        R.id.fragmentContainer,
//                        supportFragmentManager
//                    )
                    return@OnNavigationItemSelectedListener true
                }

                R.id.action_settings -> {
//                    nav_Controller.navigate(R.id.BigPhotoFragment)
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}