package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import hoanglong180903.myproject.socialhub.repository.ChangePasswordRepository

class ChangePasswordViewModel(application: Application) : AndroidViewModel(application){
    private val repository = ChangePasswordRepository(application)
    val isSuccessful : LiveData<Boolean>
    init {
        isSuccessful = repository.isSuccessful
    }
    fun updateEmail(uid: String ,email: String , password : String , newPassword : String) {
        repository.updateEmail(uid, email, password,newPassword)
    }
}