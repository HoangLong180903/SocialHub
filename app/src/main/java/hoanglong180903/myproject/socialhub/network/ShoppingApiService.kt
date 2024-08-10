package hoanglong180903.myproject.socialhub.network

import hoanglong180903.myproject.socialhub.responses.AlbumResponse
import hoanglong180903.myproject.socialhub.responses.CategoriesResponse
import hoanglong180903.myproject.socialhub.responses.ProductResponse
import hoanglong180903.myproject.socialhub.responses.TracksResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface ShoppingApiService {
    @GET
    suspend fun getCategories(@Url url: String): CategoriesResponse


    @GET
    suspend fun getProduct(@Url url: String): ProductResponse
}