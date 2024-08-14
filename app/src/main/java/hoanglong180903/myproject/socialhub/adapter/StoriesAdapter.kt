package hoanglong180903.myproject.socialhub.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.devlomi.circularstatusview.CircularStatusView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.model.Stories
import hoanglong180903.myproject.socialhub.model.UserStories
import omari.hamza.storyview.StoryView
import omari.hamza.storyview.callback.StoryClickListeners
import omari.hamza.storyview.model.MyStory
import omari.hamza.storyview.utils.StoryViewHeaderInfo
import java.util.Date


class StoriesAdapter(private val users: List<UserStories>) :
    RecyclerView.Adapter<StoriesAdapter.StoriesViewModel>() {

    class StoriesViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageStories: ImageView = itemView.findViewById(R.id.item_stories_imgUrl)
        val imageProfile: ImageView = itemView.findViewById(R.id.item_stories_imgUser)
        val progressLoading : ProgressBar = itemView.findViewById(R.id.item_stories_progress)
        val circularStatusView: CircularStatusView =
            itemView.findViewById(R.id.circular_status_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewModel {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stories, parent, false)
        return StoriesViewModel(itemView)
    }

    override fun onBindViewHolder(holder: StoriesViewModel, position: Int) {
        val item = users[position]
        if (item.profileImage == "No image") {
            holder.imageProfile.setImageResource(R.drawable._144760)
        } else {
            Glide.with(holder.itemView.context)
                .load(item.profileImage)
                .error(R.drawable._144760)
                .into(holder.imageProfile)
        }
        if (item.statusList != null && item.statusList.size > 0) {
            val lastStatus: Stories = item.statusList[item.statusList.size - 1]!!
            Glide.with(holder.itemView.context)
                .load(item.profileImage)
                .placeholder(R.drawable._144760)
                .error(R.drawable._144760)
                .into(holder.imageProfile)
            if (lastStatus.imageUrl != null) {
                Glide.with(holder.itemView.context)
                    .load(lastStatus.imageUrl)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            holder.progressLoading.visibility = View.GONE
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
                    .into(holder.imageStories)
            } else {
                holder.imageStories.setImageResource(R.drawable._144760)
            }
        }

        holder.circularStatusView.setPortionsCount(item.statusList!!.size)
        holder.circularStatusView.setPortionsColor(Color.BLUE)
        holder.circularStatusView.setOnClickListener(View.OnClickListener {
            val sortedStatusList = item.statusList.sortedByDescending { it!!.timeStamp }
            val myStories = ArrayList<MyStory>()
            for (status in sortedStatusList) {
                myStories.add(MyStory(status!!.imageUrl))
            }
            val headerInfoArrayList = ArrayList<StoryViewHeaderInfo>()
            for (story in sortedStatusList) {
                headerInfoArrayList.add(
                    StoryViewHeaderInfo(
                        item.name,
                        getTimeAgo(story!!.timeStamp),
                        item.profileImage
                    )
                )
            }
            holder.circularStatusView.setPortionsColor(Color.GRAY)
            StoryView.Builder((holder.itemView.context as AppCompatActivity?)!!.supportFragmentManager)
                .setStoriesList(myStories)
                .setStoryDuration(5000)
                .setTitleText(item.name)
                .setSubtitleText("")
                .setHeadingInfoList(headerInfoArrayList)
                .setTitleLogoUrl(item.profileImage)
                .setStoryClickListeners(object : StoryClickListeners {
                    override fun onDescriptionClickListener(position: Int) {
                        // your action
                    }

                    override fun onTitleIconClickListener(position: Int) {
                        // your action
                    }
                }) // Optional Listeners
                .build()
                .show()
        })
    }

    override fun getItemCount() = users.size

    private fun getTimeAgo(timestamp: Long): String {
        val currentTime = System.currentTimeMillis()
        return if (timestamp < currentTime) {
            val difference = currentTime - timestamp
            when {
                difference < DateUtils.MINUTE_IN_MILLIS -> {
                    "Just now"
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
                    "$days days ago"
                }
            }
        } else {
            "In the future"
        }
    }
}