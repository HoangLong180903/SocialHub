package hoanglong180903.myproject.socialhub.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devlomi.circularstatusview.CircularStatusView
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.model.Stories
import hoanglong180903.myproject.socialhub.model.UserStories
import omari.hamza.storyview.StoryView
import omari.hamza.storyview.callback.StoryClickListeners
import omari.hamza.storyview.model.MyStory


class StoriesAdapter (private val users: List<UserStories>) : RecyclerView.Adapter<StoriesAdapter.StoriesViewModel>() {

    class StoriesViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageStories : ImageView = itemView.findViewById(R.id.item_stories_imgUrl)
        val imageProfile : ImageView = itemView.findViewById(R.id.item_stories_imgUser)
        val circularStatusView : CircularStatusView = itemView.findViewById(R.id.circular_status_view)
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
        }else{
            Glide.with(holder.itemView.context)
                .load(item.profileImage)
                .placeholder(R.drawable._144760)
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
                    .placeholder(R.drawable._144760)
                    .error(R.drawable._144760)
                    .into(holder.imageStories)
            } else {
                holder.imageStories.setImageResource(R.drawable._144760)
            }
        }

        holder.circularStatusView.setPortionsCount(item.statusList!!.size)
        holder.circularStatusView.setPortionsColor(Color.BLUE)
        holder.circularStatusView.setOnClickListener(View.OnClickListener {
            val myStories = ArrayList<MyStory>()
            for (status in item.statusList) {
                myStories.add(MyStory(status!!.imageUrl))
            }
            holder.circularStatusView.setPortionsColor(Color.GRAY)

            StoryView.Builder((holder.itemView.context as AppCompatActivity?)!!.supportFragmentManager)
                .setStoriesList(myStories)
                .setStoryDuration(5000)
                .setTitleText(item.name)
                .setSubtitleText("")
                .setTitleLogoUrl(item.profileImage)
                .setStoryClickListeners(object : StoryClickListeners {
                    override fun onDescriptionClickListener(position: Int) {
                        // your action
                    }

                    override fun onTitleIconClickListener(position: Int) {
                        // your action
                    }
                }) // Optional Listeners
                .build() // Must be called before show method
                .show()
        })
    }

    override fun getItemCount() = users.size

}