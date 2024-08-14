package hoanglong180903.myproject.socialhub.repository

import android.app.Application
import hoanglong180903.myproject.socialhub.network.RetrofitInstance
import hoanglong180903.myproject.socialhub.responses.AlbumResponse
import hoanglong180903.myproject.socialhub.responses.TracksResponse


class DeezerRepository (application: Application){
    suspend fun getTracks(url: String): TracksResponse {
        return RetrofitInstance.api.getTracks(url)
    }

    suspend fun getAlbum(url: String) : AlbumResponse{
        return RetrofitInstance.api.getAlbum(url)
    }
}