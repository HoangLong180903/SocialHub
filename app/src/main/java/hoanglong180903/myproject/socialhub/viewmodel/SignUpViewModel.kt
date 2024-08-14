package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import hoanglong180903.myproject.socialhub.repository.SignUpRepository

class SignUpViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SignUpRepository(application)
    val isSuccessful : LiveData<Boolean>
    init {
        isSuccessful = repository.isSuccessful
    }
    fun requestSignUp(username: String , email : String , password: String) {
        repository.requestSignUp(username, email,password)
    }

}