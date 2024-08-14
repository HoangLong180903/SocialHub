package hoanglong180903.myproject.socialhub.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallFragment
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.databinding.ActivityChatBinding
import hoanglong180903.myproject.socialhub.view.fragment.ChatFragment


class ChatActivity : AppCompatActivity() {
    lateinit var binding : ActivityChatBinding
    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        val bundle = intent.extras
        if (bundle != null) {
            val chatFragment = ChatFragment()
            chatFragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, chatFragment)
                .commit()
        }
    }
}