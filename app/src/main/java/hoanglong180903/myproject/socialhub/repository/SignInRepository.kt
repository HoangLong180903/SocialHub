package hoanglong180903.myproject.socialhub.repository

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.signin.internal.SignInClientImpl
import com.google.firebase.auth.FirebaseAuth
import hoanglong180903.myproject.socialhub.R

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