package hoanglong180903.myproject.socialhub.view.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.databinding.ActivityChangePasswordBinding
import hoanglong180903.myproject.socialhub.utils.Functions
import hoanglong180903.myproject.socialhub.viewmodel.ChangePasswordViewModel

class ChangePasswordActivity : AppCompatActivity() {
    lateinit var binding : ActivityChangePasswordBinding
    lateinit var viewModel : ChangePasswordViewModel
    private lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        getDataBundle()
        goBack()
    }

    private fun init(){
        viewModel = ViewModelProvider(this)[ChangePasswordViewModel::class.java]
        loadingDialog = Functions.showLoadingDialog(this)
    }

    private fun goBack(){
        binding.changeBtnCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getDataBundle(){
        val bundle = intent.extras
        if (bundle != null) {
            val name = bundle.getString("name")
            val email = bundle.getString("email")
            val password = bundle.getString("password")
            val userId = bundle.getString("userId")
            val profileImage = bundle.getString("profileImage")
            val token = bundle.getString("token")
            requestChangePassword(userId.toString(),email.toString(),password.toString())
        }
    }

    private fun requestChangePassword(uid : String ,email : String , password : String){
        binding.changeBtnSave.setOnClickListener {
            if (binding.changeEdConfirmPassword.text.toString() == binding.changeEdNewPassword.text.toString() && binding.changeEdOldPassword.text.toString() == password){
                loadingDialog.show()
                viewModel.updateEmail(uid,email,password,binding.changeEdNewPassword.text.toString())
            }else if (binding.changeEdConfirmPassword.text.toString().isEmpty() || binding.changeEdNewPassword.text.toString().isEmpty()){
                Toast.makeText(this,"Request cannot be empty",Toast.LENGTH_SHORT).show()
            }else if (binding.changeEdConfirmPassword.text.toString() != binding.changeEdNewPassword.text.toString()){
                Toast.makeText(this,"Password must match",Toast.LENGTH_SHORT).show()
            }else if(binding.changeEdOldPassword.text.toString() != password){
                Toast.makeText(this,"Current password must match",Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isSuccessful.observe(this, Observer {
            var message = ""
            message = if (it) {
                loadingDialog.dismiss()
                binding.changeEdNewPassword.setText("")
                binding.changeEdConfirmPassword.setText("")
                "Update password successful"
            } else {
                loadingDialog.dismiss()
                "Update password failed"
            }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        })
    }

}