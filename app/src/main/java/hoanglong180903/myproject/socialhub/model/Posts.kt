package hoanglong180903.myproject.socialhub.model

class Posts(
    var title : String? = "",
    var imageUrl: String? = "",
    val timeStamp: Long = 0,
    var userPost : String ? ="",
    val commentList: ArrayList<Comment?>? = null,
    val fillingList : ArrayList<ReleaseEmotions>? = null
) {

}