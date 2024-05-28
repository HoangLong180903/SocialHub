package hoanglong180903.myproject.socialhub.utils

import android.os.Bundle
import hoanglong180903.myproject.socialhub.model.UserModel

class BundleUtils {
    companion object {
        fun bundleData(model: UserModel, bundle: Bundle) {
            bundle.putString("name", model.name)
            bundle.putString("email", model.email)
            bundle.putString("userId", model.id)
            bundle.putString("profileImage", model.image)
            bundle.putString("token", model.token)
        }
        fun getBundleData(bundle: Bundle): UserModel {
            val name = bundle.getString("name")
            val email = bundle.getString("email")
            val userId = bundle.getString("userId")
            val profileImage = bundle.getString("profileImage")
            val token = bundle.getString("token")

            return UserModel(name!!, email!!, userId!!, profileImage, token!!)
        }

    }
}