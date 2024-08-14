package hoanglong180903.myproject.socialhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.listener.FavoriteClickItemListener
import hoanglong180903.myproject.socialhub.model.Categories
import hoanglong180903.myproject.socialhub.model.Favorite

class CategoriesAdapter(private var context: Context) :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {
    private var categories_list: List<Categories> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categories, parent, false)
        return CategoriesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val item = categories_list[position]
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)
        holder.itemView.startAnimation(animation)
        holder.nameCategories.text = item.name_brand

        Glide.with(holder.itemView.context).load(item.image_brand)
            .into(holder.imageCategories)
    }

    override fun getItemCount() : Int { return categories_list.size }

    class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageCategories: ImageView = itemView.findViewById(R.id.item_categories_imgView)
        val nameCategories: TextView = itemView.findViewById(R.id.item_categories_tvName)
    }

    fun submitList(favoriteList: List<Categories>) {
        categories_list = favoriteList
        notifyDataSetChanged()
    }

}
