package com.mikhail.vyakhirev.presentation.main_activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.databinding.ActivityMainBinding
import com.mikhail.vyakhirev.presentation.list_fragment.ListMyFragment
import org.kodein.di.Copy
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.android.di
import org.kodein.di.android.retainedSubDI
import org.kodein.di.instance

class MainActivity : AppCompatActivity(), DIAware {
//    override val di by di()
override val di by retainedSubDI(closestDI(), copy = Copy.All) {
}
    private val factory by instance<MainActivityViewModelFactory>()
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.bottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
//            val nav_Controller = findNavController(R.id.nav_host_fragment)
            when (it.itemId) {
                R.id.action_list -> {
                    openFragment(ListMyFragment())
//                    openFragment(ListMyFragment())
                    Toast.makeText(this, "Kan!", Toast.LENGTH_SHORT).show()

                    return@OnNavigationItemSelectedListener true
                }

                R.id.action_favoritesPhoto -> {
                    return@OnNavigationItemSelectedListener true
                }

                R.id.action_settings -> {
//                    nav_Controller.navigate(R.id.BigPhotoFragment)
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }

}