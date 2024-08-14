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
import hoanglong180903.myproject.socialhub.model.Comment
import hoanglong180903.myproject.socialhub.model.UserModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CommentAdapter (private val users: List<Comment>) : RecyclerView.Adapter<CommentAdapter.UserViewHolder>() {
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageUser : ImageView = itemView.findViewById(R.id.item_comment_imgUser)
        val tvName : TextView = itemView.findViewById(R.id.item_comment_tvName)
        val tvTitle : TextView = itemView.findViewById(R.id.item_comment_tvTitleComment)
        val tvTime : TextView = itemView.findViewById(R.id.item_comment_tvTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = users[position]
        if (item.uImage == "No image") {
            holder.imageUser.setImageResource(R.drawable._144760)
        }else{
            Glide.with(holder.itemView.context)
                .load(item.uImage)
                .placeholder(R.drawable._144760) // Ảnh placeholder khi tải ảnh
                .error(R.drawable._144760) // Ảnh hiển thị khi có lỗi
                .into(holder.imageUser)
        }
        holder.tvName.text = item.uName
        holder.tvTitle.text = item.comment
        holder.tvTime.text = convertEpochToDateTime(item.timestamp)
    }

    override fun getItemCount() = users.size
    private fun convertEpochToDateTime(epochTime: Long): String {
        val date = Date(epochTime)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }
}