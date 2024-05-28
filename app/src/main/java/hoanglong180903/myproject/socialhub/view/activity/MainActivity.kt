package hoanglong180903.myproject.socialhub.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.databinding.ActivityMainBinding
import hoanglong180903.myproject.socialhub.viewmodel.MessageViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var viewModel: MessageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolBar)

        bottomNavigationView = findViewById(R.id.bottomNavView)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(
            bottomNavigationView, navController
        )

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navNews -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.navCreate -> {
                    navController.navigate(R.id.createFragment)
                    true
                }
                R.id.navMessage -> {
                    navController.navigate(R.id.messageFragment)
                    true
                }
                R.id.navUser -> {
                    navController.navigate(R.id.userFragment)
                    true
                }
                else -> false
            }
        }
        if (savedInstanceState == null) {
            navController.navigate(R.id.homeFragment)
            bottomNavigationView.selectedItemId = R.id.navNews
        }
        init()

    }
    private fun init(){
        viewModel = ViewModelProvider(this)[MessageViewModel::class.java]
    }
    override fun onStart() {
        super.onStart()
        viewModel.fetchPresence("Online")
    }
    override fun onResume() {
        super.onResume()
        viewModel.fetchPresence("Online")
    }
    override fun onPause() {
        viewModel.fetchPresence("Offline")
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        viewModel.fetchPresence("Offline")
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.fetchPresence("Offline")
    }
}