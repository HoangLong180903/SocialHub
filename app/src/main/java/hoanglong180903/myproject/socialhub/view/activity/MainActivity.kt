package hoanglong180903.myproject.socialhub.view.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.databinding.ActivityMainBinding
import hoanglong180903.myproject.socialhub.viewmodel.MessageViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var viewModel: MessageViewModel
    var database: FirebaseDatabase? = null
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
                R.id.navShopping -> {
                    navController.navigate(R.id.shoppingFragment)
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
        getToken()
        getFCMToken()
    }
    private fun init(){
        viewModel = ViewModelProvider(this)[MessageViewModel::class.java]
    }

    private fun getToken(){
        //init
        database = FirebaseDatabase.getInstance()
        FirebaseMessaging.getInstance()
            .token
            .addOnSuccessListener { token ->
                val map = HashMap<String, Any>()
                map["token"] = token
                database!!.reference
                    .child("users")
                    .child(FirebaseAuth.getInstance().uid!!)
                    .updateChildren(map)
            }
    }

    private fun getFCMToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful){
                val token = it.result
                Log.i("My token",token)
            }
        }
    }

}