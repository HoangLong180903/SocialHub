package hoanglong180903.myproject.socialhub.view.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.ViewModelProvider
import hoanglong180903.myproject.socialhub.databinding.ActivityLoginBinding
import hoanglong180903.myproject.socialhub.viewmodel.LoginActivityViewModel

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: LoginActivityViewModel
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        login()
    }

    private fun initView(){
        viewModel = ViewModelProvider(this)[LoginActivityViewModel::class.java]
    }

    @SuppressLint("SuspiciousIndentation")
    private fun login(){
        binding.tvRegister.setOnClickListener {
            viewModel.intentRegister()
        }
        binding.btnLogin.setOnClickListener {
            Log.d("login", "Login here")
            viewModel.requestLogin(binding.edUsername.text.toString(),binding.edPassword.text.toString())
        }
        //khi dang nhap thanh cong hoac tha bai
        viewModel.isSuccessful.observe(this, Observer {
        var message = ""
            if (it){
                message = "Dang nhap thanh cong"
            }else{
                message = "Dang nhap that bai"
            }
            Toast.makeText(application,message,Toast.LENGTH_LONG).show()
        })
    }
}