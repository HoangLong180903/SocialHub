package hoanglong180903.myproject.socialhub.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_table")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val song: String,

    val singer: String,

    val preview: String,

    val image: String,
):Serializable