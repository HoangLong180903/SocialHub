package hoanglong180903.myproject.socialhub.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.databinding.ActivityEditProfileBinding
import hoanglong180903.myproject.socialhub.view.fragment.ProfileFragment

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditProfileBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        val bundle = intent.extras
        if (bundle != null) {
            val profileFragment = ProfileFragment()
            profileFragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, profileFragment)
                .commit()
        }
    }
}