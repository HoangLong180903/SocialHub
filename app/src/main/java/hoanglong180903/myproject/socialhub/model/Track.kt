package hoanglong180903.myproject.socialhub.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class Track(
    val id: Int,
    val title: String,
    val link: String,
    val duration: Int,
    val preview: String,
    val artist: Artist
) : Serializable{
    constructor(id: Int, title: String, preview: String, artistName : String , artistPicture: String) : this(
        id,
        title,
        "", // Default value for link
        0,  // Default value for duration
        preview,
        Artist(0, artistName, artistPicture) // Default value for artist
    )
}