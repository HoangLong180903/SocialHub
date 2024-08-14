package hoanglong180903.myproject.socialhub.repository

import android.R
import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class ProfileRepository(application: Application) {
    private val user = Firebase.auth.currentUser

    private val auth = FirebaseAuth.getInstance()
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    val isSuccessful = MutableLiveData<Boolean>()
    private val database = FirebaseDatabase.getInstance()
    fun updateUser(uid: String, name: String, selectedImage: Uri? = null) {
        if (selectedImage != null) {
            val reference: StorageReference = storage.reference.child("Profiles").child(user!!.uid)
            reference.putFile(selectedImage).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reference.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        val lastMgsObj = HashMap<String, Any>()
                        lastMgsObj["name"] = name
                        lastMgsObj["image"] = imageUrl
                        database.getReference("users").child(uid).updateChildren(lastMgsObj)
                            .addOnSuccessListener {
                                if (task.isSuccessful) {
                                    Log.d("update", "update thanh cong")
                                    isSuccessful.value = task.isSuccessful
                                } else {
                                    Log.d("update", "update khong thanh cong")
                                    isSuccessful.value = false
                                }
                            }
                    }
                }
            }
        }
    }
}