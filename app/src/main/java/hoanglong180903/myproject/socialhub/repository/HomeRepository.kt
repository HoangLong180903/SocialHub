package hoanglong180903.myproject.socialhub.repository

import android.app.Application
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import hoanglong180903.myproject.socialhub.model.Stories
import hoanglong180903.myproject.socialhub.model.UserModel
import hoanglong180903.myproject.socialhub.model.UserStories
import java.util.Date

class HomeRepository(application: Application) {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    fun getUserData(uid: String, callback: (UserModel?) -> Unit) {
        database.getReference("users").child(uid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                callback(user)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

    fun addUserStories(
        data: Intent,
        user: UserModel
    ){
        val storage = FirebaseStorage.getInstance()
        val date = Date()
        val reference = storage.reference.child("Statuses")
            .child(date.time.toString() + "")
        reference.putFile(data.data!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { uri ->
                    val userStatus = UserStories(
                        name = user.name,
                        profileImage = user.image,
                        lastUpdated = date.time
                    )
                    val objectHashMap = HashMap<String, Any>()
                    objectHashMap["name"] = userStatus.name
                    objectHashMap["profileImage"] = userStatus.profileImage
                    objectHashMap["lastUpdated"] = userStatus.lastUpdated
                    val imageUrl = uri.toString()
                    val status = Stories(imageUrl, userStatus.lastUpdated)
                    database.reference
                        .child("Stories")
                        .child(FirebaseAuth.getInstance().uid!!)
                        .updateChildren(objectHashMap)
                    database.reference.child("Stories")
                        .child(FirebaseAuth.getInstance().uid!!)
                        .child("Statuses")
                        .push()
                        .setValue(status)
                }
            }
        }
    }

    fun getUserStories(
        onSuccess: (List<UserStories>) -> Unit,
        onFailure: (DatabaseError) -> Unit,
    ) {
        database.reference.child("Stories").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userStoriesList = mutableListOf<UserStories>()
                    for (storySnapshot in snapshot.children) {
                        val statuses: ArrayList<Stories?> = ArrayList()
                        for (statusSnapshot in storySnapshot.child("Statuses").children) {
                            val sampleStatus: Stories? = statusSnapshot.getValue(Stories::class.java)
                            statuses.add(sampleStatus)
                        }
                        val userStory = UserStories(
                            name = storySnapshot.child("name").getValue(String::class.java)!!,
                            profileImage = storySnapshot.child("profileImage").getValue(String::class.java)!!,
                            lastUpdated = storySnapshot.child("lastUpdated").getValue(Long::class.java)!!,
                            statusList = statuses
                        )
                        userStoriesList.add(userStory)
                    }
                    onSuccess(userStoriesList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error)
            }
        })
    }
}