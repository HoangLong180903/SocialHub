package hoanglong180903.myproject.socialhub.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    lateinit var binding : ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (FirebaseAuth.getInstance().currentUser != null) {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
        getStart()
    }

    private fun getStart(){
        binding.splashBtnGetStart.setOnClickListener {
            Intent(this, RegistrationActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }
}