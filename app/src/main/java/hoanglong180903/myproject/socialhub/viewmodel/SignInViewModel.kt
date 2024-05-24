package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import hoanglong180903.myproject.socialhub.repository.SignInRepository

class SignInViewModel (application: Application) : AndroidViewModel(application) {
    private val repository = SignInRepository(application)
    val isSuccessful : LiveData<Boolean>
    init {
        isSuccessful = repository.isSuccessful
    }
    //request login in firebase
    fun requestLogin(email : String , password: String) {
        repository.requestLogin(email,password)
    }

}