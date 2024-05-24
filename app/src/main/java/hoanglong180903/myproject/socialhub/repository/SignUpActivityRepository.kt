package hoanglong180903.myproject.socialhub.repository

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import hoanglong180903.myproject.socialhub.model.UserModel
import hoanglong180903.myproject.socialhub.view.activity.LoginActivity

class SignUpActivityRepository(val application: Application) {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val isSuccessful = MutableLiveData<Boolean>()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    fun requestSignUp(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    val firebaseUser: FirebaseUser = auth.currentUser!!
                    val userID = firebaseUser.uid
                    val document: DocumentReference = firestore.collection("Users").document(userID)
                    val hashMap: HashMap<String, String> = HashMap<String, String>()
                    hashMap["userid"] = userID
                    hashMap["username"] = username
                    hashMap["imageProfile"] = "default"
                    document.set(hashMap).addOnSuccessListener {
                        val intent = Intent(application, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        application.startActivity(intent)
                    }.addOnFailureListener {
                        Log.d("Signup","on failed: ${it.toString()}")
                    }
                    isSuccessful.value = it.isSuccessful
                } else {
                    Log.d("SignUp", "Dang ky that bai")
                    isSuccessful.value = false
                }
            }
    }
}