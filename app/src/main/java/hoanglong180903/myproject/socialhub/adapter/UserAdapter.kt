package hoanglong180903.myproject.socialhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.model.UserModel

class UserAdapter(private val users: List<UserModel>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val presenceRef: DatabaseReference? = null
    var status: String? = null
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.image)
        val imageView : ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_active_status, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        if (user.profileImage == "default") {
            holder.image.setImageResource(R.drawable._144760)
        }else{
            Glide.with(holder.itemView.context)
                .load(user.profileImage)
                .placeholder(R.drawable.ic_launcher_foreground) // Ảnh placeholder khi tải ảnh
                .error(R.drawable.ic_launcher_foreground) // Ảnh hiển thị khi có lỗi
                .into(holder.image)
        }
        checkUserStatus(user.uid,holder.imageView)
    }

    override fun getItemCount() = users.size

    private fun checkUserStatus(userId: String, imageView: ImageView) {
        presenceRef?.child(userId)?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    status = snapshot.getValue<String>(String::class.java)
                    if ("Online" == status) {
                        imageView.visibility = View.VISIBLE
                    } else {
                        imageView.visibility = View.GONE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}