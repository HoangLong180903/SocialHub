package hoanglong180903.myproject.socialhub.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import hoanglong180903.myproject.socialhub.model.UserModel

class SignUpRepository(val application: Application) {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val isSuccessful = MutableLiveData<Boolean>()
    private val database = FirebaseDatabase.getInstance().getReference("users")
    fun requestSignUp(username: String, email: String, password: String) {
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { it ->
//                if (it.isSuccessful) {
//                    val firebaseUser: FirebaseUser = auth.currentUser!!
//                    val userID = firebaseUser.uid
//                    val token = database.push().key
//                    val user = UserModel(username, "No image", email,password, token, userID)
//                    database.child(userID).setValue(user).addOnCompleteListener {
//                        if (it.isSuccessful) {
//                            Log.d("log sign up", "Dang ky thanh cong")
//                        } else {
//                            Log.d("log sign up", "Dang ky ko thanh cong")
//
//                        }
//                    }
//                    isSuccessful.value = it.isSuccessful
//                } else {
//                    Log.d("SignUp", "Dang ky that bai")
//                    isSuccessful.value = false
//                }
//            }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful && user.isEmailVerified) {
                                Log.d("log sign up", "Verification email sent to $email")
                                val firebaseUser: FirebaseUser = auth.currentUser!!
                                val userID = firebaseUser.uid
                                val token = database.push().key
                                val user = UserModel(username, "No image", email, password, token, userID)
                                    database.child(userID).setValue(user).addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Log.d("log sign up", "Dang ky thanh cong")
                                    } else {
                                        Log.d("log sign up", "Dang ky ko thanh cong")
                                    }
                                }
                            } else {
                                Log.d("log sign up", "Failed to send verification email.")
                            }
                        }
                    isSuccessful.value = task.isSuccessful
                } else {
                    Log.d("log sign up", "Registration failed: ${task.exception?.message}")
                    isSuccessful.value = false
                }
            }
    }
}