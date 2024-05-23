package hoanglong180903.myproject.socialhub.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hoanglong180903.myproject.socialhub.databinding.ActivitySignupBinding
import hoanglong180903.myproject.socialhub.viewmodel.SignUpActivityViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var viewModel: SignUpActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        signUp()
    }

    private fun initView(){
        viewModel = ViewModelProvider(this)[SignUpActivityViewModel::class.java]
    }

    private fun signUp(){
        binding.btnSignUp.setOnClickListener {
            viewModel.requestSignUp(
                binding.edUsername.text.toString(),
                binding.edEmail.text.toString(),
                binding.edPassword.text.toString()
            )
        }

        viewModel.isSuccessful.observe(this, Observer {
            var message = ""
            if (it) {
                message = "Dang ky thanh cong"
            } else {
                message = "Dang ky that bai"
            }
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        })
    }
}