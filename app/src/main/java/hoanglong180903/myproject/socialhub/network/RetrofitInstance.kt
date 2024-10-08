package hoanglong180903.myproject.socialhub.network

import hoanglong180903.myproject.socialhub.utils.Contacts
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Contacts.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitShopping by lazy {
        Retrofit.Builder()
            .baseUrl(Contacts.BASE_URL_SHOPPING)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: DeezerApiService by lazy {
        retrofit.create(DeezerApiService::class.java)
    }

    val api_shopping : ShoppingApiService by lazy {
        retrofitShopping.create(ShoppingApiService::class.java)
    }

}