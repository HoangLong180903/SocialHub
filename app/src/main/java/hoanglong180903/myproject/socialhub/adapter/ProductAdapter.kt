package hoanglong180903.myproject.socialhub.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.listener.ProductClickItemListener
import hoanglong180903.myproject.socialhub.model.Product
import java.text.DecimalFormat


class ProductAdapter(private var context: Context, private val listener: ProductClickItemListener) :
    RecyclerView.Adapter<ProductAdapter.CategoriesViewHolder>() {
    private var products_list: List<Product> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_products, parent, false)
        return CategoriesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val item = products_list[position]
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)
        holder.itemView.startAnimation(animation)
        holder.nameProduct.text = item.name
        Glide.with(context)
            .load(item.image)
            .into(holder.imageProduct)

        val decimalFormat1 = DecimalFormat("#,##0")
        try {
            val tongTienGiamNumber: Double = item.price.toDouble()
            val formattedNumber: String = decimalFormat1.format(tongTienGiamNumber)
            holder.priceProduct.text = "$formattedNumber đ"
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        holder.ratingProduct.rating = item.rating
        holder.ratingProduct.stepSize = 0.5f


        holder.buyNowProduct.setOnClickListener {
            listener.onItemClickBuyNow(item)
        }
    }

    override fun getItemCount() : Int { return products_list.size }

    class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageProduct: ImageView = itemView.findViewById(R.id.item_product_imgView)
        val nameProduct: TextView = itemView.findViewById(R.id.item_product_tvName)
        val priceProduct: TextView = itemView.findViewById(R.id.item_product_tvPrice)
        val ratingProduct: RatingBar = itemView.findViewById(R.id.item_product_rbRating)
        val imageCartProduct: ImageView = itemView.findViewById(R.id.item_product_imgCart)
        val buyNowProduct : AppCompatButton = itemView.findViewById(R.id.item_product_btnBuyNow)
    }

    fun submitList(productList: List<Product>) {
        products_list = productList
        notifyDataSetChanged()
    }

}
