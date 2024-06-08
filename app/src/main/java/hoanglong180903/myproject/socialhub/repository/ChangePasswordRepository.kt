package hoanglong180903.myproject.socialhub.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChangePasswordRepository(application: Application) {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    val isSuccessful = MutableLiveData<Boolean>()
    private var database : FirebaseDatabase = FirebaseDatabase.getInstance()
    fun updateEmail(uid: String ,email: String , password : String , newPassword : String) {
        val credentials = EmailAuthProvider.getCredential(
            email,
            password
        )
        currentUser!!.reauthenticate(credentials).addOnSuccessListener {
            currentUser.updatePassword(
                newPassword.toString()
            ).addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    Log.d("Change password", "Change password successful")
                    val updatePassword = HashMap<String, Any>()
                    updatePassword["password"] = newPassword
                    database.getReference("users").child(uid).updateChildren(updatePassword)
                        .addOnSuccessListener {
                            Log.d("update", "update thanh cong")
                        }.addOnFailureListener {
                            Log.d("update", "update khong thanh cong")
                        }
                    isSuccessful.value = it.isSuccessful
                } else {
                    Log.d("Change password", "Change password failed")
                    isSuccessful.value = false
                }
            }
        }.addOnFailureListener {
            isSuccessful.value = false
        }
    }
}