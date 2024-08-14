package hoanglong180903.myproject.socialhub.model

import java.io.Serializable

class UserModel(
    var name:String = "",
    var image:String = "",
    var email:String = "",
    var password: String = "",
    var token: String? = "",
    var id:String = ""

) : Serializable