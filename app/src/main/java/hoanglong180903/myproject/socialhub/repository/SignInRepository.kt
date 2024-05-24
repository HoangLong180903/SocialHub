package hoanglong180903.myproject.socialhub.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class SignInRepository (val application: Application) {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val isSuccessful = MutableLiveData<Boolean>()
    fun requestLogin(email : String , password: String) {
        //call firebase service here
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    isSuccessful.value = it.isSuccessful
                }else{
                    isSuccessful.value = false
                }
            }
    }
}