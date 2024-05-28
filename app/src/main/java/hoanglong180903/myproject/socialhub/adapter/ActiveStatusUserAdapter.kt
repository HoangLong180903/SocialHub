package hoanglong180903.myproject.socialhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.model.UserModel


class ActiveStatusUserAdapter (private val users: List<UserModel>) : RecyclerView.Adapter<ActiveStatusUserAdapter.UserViewHolder>() {
    private val database = FirebaseDatabase.getInstance()
    var status: String? = null
    private var lastPosition = -1
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageStatus : ImageView = itemView.findViewById(R.id.item_asu_imageStatus)
        val imageProfile : ImageView = itemView.findViewById(R.id.item_asu_imageProfile)
        val tvName : TextView = itemView.findViewById(R.id.item_asu_tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_active_status_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        if (user.image == "No image") {
            holder.imageProfile.setImageResource(R.drawable._144760)
        }else{
            Glide.with(holder.itemView.context)
                .load(user.image)
                .placeholder(R.drawable._144760) // Ảnh placeholder khi tải ảnh
                .error(R.drawable._144760) // Ảnh hiển thị khi có lỗi
                .into(holder.imageProfile)
        }
        holder.tvName.text = user.name
        checkUserStatus(user.id,holder.imageStatus)
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_out_right)
        holder.itemView.startAnimation(animation)
    }

    override fun getItemCount() = users.size

    private fun checkUserStatus(userId: String, imageView: ImageView) {
        database.getReference("Presence").child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    status = snapshot.getValue(String::class.java)
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