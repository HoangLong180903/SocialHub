package hoanglong180903.myproject.socialhub.adapter

import android.R.attr.data
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.interfaces.ItemPostOnClick
import hoanglong180903.myproject.socialhub.model.Posts
import omari.hamza.storyview.utils.StoryViewHeaderInfo


class PostAdapter (private val users: List<Posts>,
                   private val listener: ItemPostOnClick
) : RecyclerView.Adapter<PostAdapter.StoriesViewModel>() {
    var mProcessLike : Boolean = false
    val database = FirebaseDatabase.getInstance()
    var mUid : String = FirebaseAuth.getInstance().currentUser!!.uid
    private var likeRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("Likes")
    private var posRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("Posts")
    class StoriesViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgUser : ImageView = itemView.findViewById(R.id.item_post_imgUser)
        val tvName : TextView = itemView.findViewById(R.id.item_post_tvName)
        val tvTime : TextView = itemView.findViewById(R.id.item_post_tvTime)
        val imgPost : ImageView = itemView.findViewById(R.id.item_post_imgPost)
        val tvEmotions : TextView = itemView.findViewById(R.id.item_post_tvEmotions)
        val tvComment : TextView = itemView.findViewById(R.id.item_post_tvComment)
        val tvTitle : TextView = itemView.findViewById(R.id.item_post_title)
        val iconLike : ImageView = itemView.findViewById(R.id.item_post_iconLike)
        val iconComment : ImageView = itemView.findViewById(R.id.item_post_iconComment)
        val progressLoading : ProgressBar = itemView.findViewById(R.id.item_post_progress)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewModel {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return StoriesViewModel(itemView)
    }

    override fun onBindViewHolder(holder: StoriesViewModel, position: Int) {
        val item = users[position]
        if (item.uImage == "No image") {
            holder.imgUser.setImageResource(R.drawable._144760)
        }else{
            Glide.with(holder.itemView.context)
                .load(item.uImage)
//                .placeholder(R.drawable._144760)
                .error(R.drawable._144760)
                .into(holder.imgUser)
        }
        holder.tvName.text = item.uName
        holder.tvTime.text = getTimeAgo(item.pTime)
        holder.tvTitle.text = item.pTitle
        Glide.with(holder.itemView.context)
            .load(item.pImage)
            .placeholder(R.drawable.ic_loading)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressLoading.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressLoading.visibility = View.GONE
                    return false
                }
            })
            .error(R.drawable._144760)
            .into(holder.imgPost)
        holder.tvEmotions.text = "${item.pLikes} Like"
        holder.tvComment.text = "${item.pComments} Comment"
        holder.iconLike.setOnClickListener {
            val pLikes : Int =  item.pLikes!!.toInt()
            mProcessLike = true
            val postIde : String = item.pId.toString()
            // Get the generated key
            database.reference.child("Likes")
                .addValueEventListener(object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (mProcessLike) {
                            if (snapshot.child(postIde).hasChild(mUid)) {
                                posRef.child(postIde).child("pLikes").setValue(""+(pLikes - 1))
                                likeRef.child(postIde).child(mUid).removeValue()
                                mProcessLike = false
                            } else {
                                posRef.child(postIde).child("pLikes").setValue(""+(pLikes + 1))
                                likeRef.child(postIde).child(mUid).setValue("Liked")
                                mProcessLike = false
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

        }
        holder.iconComment.setOnClickListener {
            listener.onClickComment(item.pTime.toString())
        }
        setLike(holder, item.pId.toString())
    }

    override fun getItemCount() = users.size

    private fun getTimeAgo(timestamp: Long): String {
        val currentTime = System.currentTimeMillis()
        return if (timestamp < currentTime) {
            val difference = currentTime - timestamp
            when {
                difference < DateUtils.MINUTE_IN_MILLIS -> {
                    "vừa xong"
                }
                difference < DateUtils.DAY_IN_MILLIS -> {
                    DateUtils.getRelativeTimeSpanString(
                        timestamp,
                        currentTime,
                        DateUtils.MINUTE_IN_MILLIS
                    ).toString()
                }
                else -> {
                    val days = (difference / DateUtils.DAY_IN_MILLIS).toInt()
                    "$days ngày trước"
                }
            }
        } else {
            "In the future"
        }
    }


    private fun setLike(holder: StoriesViewModel , postKey:String){
        database.reference.child("Likes").addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(postKey).hasChild(mUid)){
                    //thay icon
                    holder.iconLike.setImageResource(R.drawable._107845)
                }else{
                    holder.iconLike.setImageResource(R.drawable._590951_200)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}