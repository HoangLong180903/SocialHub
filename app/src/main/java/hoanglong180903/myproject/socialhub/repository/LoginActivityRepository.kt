package hoanglong180903.myproject.socialhub.repository

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import hoanglong180903.myproject.socialhub.view.activity.MainActivity
import hoanglong180903.myproject.socialhub.view.activity.SignUpActivity

class LoginActivityRepository(val application: Application) {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val isSuccessful = MutableLiveData<Boolean>()
    fun requestLogin(email : String , password: String) {
        //call firebase service here
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    isSuccessful.value = it.isSuccessful
                    val intent = Intent(application, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    application.startActivity(intent)
                }else{
                    isSuccessful.value = false
                }
            }
    }
    fun intentRegister(){
        val intent = Intent(application, SignUpActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    fun checkAuthCurrent(){

    }
}