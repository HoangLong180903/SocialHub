package hoanglong180903.myproject.socialhub.repository

import android.app.Application
import android.net.Uri
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import hoanglong180903.myproject.socialhub.model.Posts
import hoanglong180903.myproject.socialhub.model.UserModel
import java.util.Date
import java.util.Objects

class CreateRepository(val application: Application){
    private val database = FirebaseDatabase.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()


    fun createPosts(
        selectImage : Uri,
        user: UserModel,
        title : String,
    ) {
        val date = Date()
        //upload image firebase storage
        val reference = storage.reference.child("Posts").child(date.time.toString() + "")
        reference.putFile(selectImage).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { uri ->
                    val hashMap = HashMap<String, Any>()
                    hashMap["pId"] = date.time.toString()
                    hashMap["pTitle"] = title
                    hashMap["pImage"] =uri.toString()
                    hashMap["pTime"] =  date.time
                    hashMap["uid"] = user.id
                    hashMap["uImage"] = user.image
                    hashMap["uName"] = user.name
                    hashMap["pLikes"] = "0"
                    database.getReference("Posts")
                        .child(date.time.toString())
                        .setValue(hashMap)
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                Log.d("create posts","Them thanh cong")
                            }else{
                                Log.d("create posts","Them khong thanh cong")
                            }
                        }
                }
            }
        }
    }

    fun getUserData(uid: String, callback: (UserModel?) -> Unit) {
        database.getReference("users").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                callback(user)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }


}