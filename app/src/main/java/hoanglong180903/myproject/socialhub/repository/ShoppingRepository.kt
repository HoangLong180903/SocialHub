package hoanglong180903.myproject.socialhub.repository

import android.app.Application
import hoanglong180903.myproject.socialhub.network.RetrofitInstance
import hoanglong180903.myproject.socialhub.responses.AlbumResponse
import hoanglong180903.myproject.socialhub.responses.CategoriesResponse
import hoanglong180903.myproject.socialhub.responses.ProductResponse
import hoanglong180903.myproject.socialhub.responses.TracksResponse


class ShoppingRepository (application: Application){
    suspend fun getCategories(url: String): CategoriesResponse {
        return RetrofitInstance.api_shopping.getCategories(url)
    }

    suspend fun getProducts(url: String) : ProductResponse{
        return RetrofitInstance.api_shopping.getProducts(url)
    }
}