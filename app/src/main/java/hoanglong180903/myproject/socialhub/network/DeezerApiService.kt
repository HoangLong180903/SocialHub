package hoanglong180903.myproject.socialhub.network

import hoanglong180903.myproject.socialhub.responses.AlbumResponse
import hoanglong180903.myproject.socialhub.responses.TracksResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface DeezerApiService {
    @GET
    suspend fun getTracks(@Url url: String): TracksResponse


    @GET
    suspend fun getAlbum(@Url url: String): AlbumResponse
}