package com.mikhail.vyakhirev.presentation.main_activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.databinding.ActivityMainBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory by instance<MainActivityViewModelFactory>()
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)

        binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
                .apply {
                    this.viewmodel = viewModel
                    this.lifecycleOwner = this@MainActivity
                }

        val bottomNavigation: BottomNavigationView = binding.bottomNav
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

//        setSupportActionBar(binding.toolbar)

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