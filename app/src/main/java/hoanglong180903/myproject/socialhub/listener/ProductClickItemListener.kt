package hoanglong180903.myproject.socialhub.listener

import hoanglong180903.myproject.socialhub.model.Favorite
import hoanglong180903.myproject.socialhub.model.Product
import hoanglong180903.myproject.socialhub.model.Track

interface ProductClickItemListener {
    fun onItemClickBuyNow(product: Product)
}