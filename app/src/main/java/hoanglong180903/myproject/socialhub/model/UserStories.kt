package hoanglong180903.myproject.socialhub.model

class UserStories(
    var name: String = "",
    var profileImage: String = "",
    val lastUpdated: Long = 0,
    val statusList: ArrayList<Stories?>? = null
) {
}