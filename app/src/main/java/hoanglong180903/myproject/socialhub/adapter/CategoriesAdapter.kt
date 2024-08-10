package hoanglong180903.myproject.socialhub.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.model.Categories
import hoanglong180903.myproject.socialhub.model.Track

class CategoriesAdapter (private var context: Context) :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {
    private var categories_list: List<Categories> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categories, parent, false)
        return CategoriesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val item = categories_list[position]
        Glide.with(holder.itemView.context).load(item.image_brand)
            .into(holder.categories_imageView)
        holder.categories_tvName.text = item.name_brand
    }

    override fun getItemCount() = categories_list.size

    class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categories_imageView: ImageView = itemView.findViewById(R.id.item_categories_imgView)
        val categories_tvName: TextView = itemView.findViewById(R.id.item_categories_tvName)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(categories_list: List<Categories>) {
        this.categories_list = categories_list
        notifyDataSetChanged()
    }
}