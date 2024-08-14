package hoanglong180903.myproject.socialhub.model


data class Album (
    val id: Long,
    val title: String,
    val duration : Int,
    val is_loved_track : Boolean,
    val picture : String,
    val creator: Creator
)