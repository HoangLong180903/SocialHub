package hoanglong180903.myproject.socialhub.view.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.permissionx.guolindev.PermissionX
import com.zegocloud.uikit.plugin.invitation.ZegoInvitationType
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.event.CallEndListener
import com.zegocloud.uikit.prebuilt.call.event.ErrorEventsListener
import com.zegocloud.uikit.prebuilt.call.event.SignalPluginConnectListener
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallInvitationData
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoUIKitPrebuiltCallConfigProvider
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import com.zegocloud.uikit.service.express.IExpressEngineEventHandler
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.adapter.ChatAdapter
import hoanglong180903.myproject.socialhub.databinding.FragmentChatBinding
import hoanglong180903.myproject.socialhub.utils.Contacts
import hoanglong180903.myproject.socialhub.viewmodel.ChatViewModel
import im.zego.zegoexpress.constants.ZegoRoomStateChangedReason
import org.json.JSONObject
import timber.log.Timber
import java.util.Date


class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding
    private var navController: NavController? = null
    private lateinit var viewModel: ChatViewModel
    private val senderUid = FirebaseAuth.getInstance().uid
    private val REQUEST_IMAGE_CAPTURE = 1
    var senderRoom: String = ""
    var receiverRoom: String = ""
    var token : String = ""
    var nameFCM : String = ""
    private var imageBitmap: Bitmap? = null
    var database : FirebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var functions : Contacts
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        getDataBundle()
        actionButton()
    }

    private fun initView(view: View) {
        navController = Navigation.findNavController(view)
    }

    private fun getDataBundle() {
        arguments?.let { bundle ->
            val name = bundle.getString("name")
            val email = bundle.getString("email")
            val userId = bundle.getString("userId")
            val profileImage = bundle.getString("profileImage")
            var tokenBundle = bundle.getString("token")
            senderRoom = senderUid + userId
            receiverRoom = userId + senderUid
            token = tokenBundle.toString()
            nameFCM = name.toString()
            binding.tvTopChat.text = name
            if (profileImage == "No image") {
                binding.chatProfile.setImageResource(R.drawable._144760)
            } else {
                Glide.with(this)
                    .load(profileImage)
                    .placeholder(R.drawable._144760)
                    .error(R.drawable._144760)
                    .into(binding.chatProfile)
            }
            val appID: Long = 853918929
            val appSign: String = "f866272dd0272b300fc2bdceb85bce66d0de06f91b9bc8f313c84e1ebe460c4b"

            initCallInviteService(appID, appSign, FirebaseAuth.getInstance().uid.toString(), "User_${FirebaseAuth.getInstance().uid.toString()}")

            initVoiceButton(userId.toString())

            initVideoButton(userId.toString())

            offlineUsePermission()
            requestSendMessage(userId.toString(), token,name.toString())
            getData(senderRoom)
        }
    }

    private fun actionButton() {
        binding.iconBack.setOnClickListener {
            activity?.finish()
        }
        binding.iconPhotoImage.setOnClickListener {
            val mIntent = Intent()
            mIntent.action = Intent.ACTION_GET_CONTENT
            mIntent.type = "image/*"
            startActivityForResult(mIntent, 25)
        }
        binding.iconCamera.setOnClickListener {
            openGallery()
        }

        binding.iconAddFile.setOnClickListener {
            binding.lnCameraPhotoImage.visibility = View.VISIBLE
            binding.iconAddFile.visibility = View.GONE
        }

        binding.iconRecord.setOnClickListener {

        }
    }

    private fun requestSendMessage(receiver: String , token : String , name:String) {
        val date = Date()
        val senderRoom = senderUid + receiver
        val receiverRoom = receiver + senderUid
        binding.imageSend.setOnClickListener {
            viewModel.sendMessage(
                binding.edChating.text.toString(),
                senderUid!!,
                date.time,
                senderRoom,
                receiverRoom,
                token,
                requireContext(),
                name
            )
            binding.edChating.setText("")
        }
    }

    private fun getData(senderRoom: String) {
        viewModel.users.observe(viewLifecycleOwner, Observer { users ->
            binding.rcChat.adapter = ChatAdapter(users)
            binding.rcChat.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )

        })
        viewModel.fetchMessage(senderRoom)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 25) {
            if (resultCode == Activity.RESULT_OK) {
                if (data!!.data != null) {
                    viewModel.sendPhotoGallery(data, "Photo", senderUid!!, senderRoom, receiverRoom,token,
                        requireContext(), nameFCM
                    )
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            viewModel.sendPhotoGallery(
                data!!, "camera", senderUid!!, senderRoom, receiverRoom,token,
                requireContext(), nameFCM
            )
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun openRecord(){

    }

    private fun initVideoButton(idUserListener : String) {
        binding.iconVideoCall.setIsVideoCall(true)
        binding.iconVideoCall.resourceID = "zego_data"
        binding.iconVideoCall.setOnClickListener { view ->
            val targetUserID = idUserListener.toString()
            val split = targetUserID.split(",")
            val users = ArrayList<ZegoUIKitUser>()
            for (userID in split) {
                println("userID=$userID")
                val userName = "User_${userID}"
                users.add(ZegoUIKitUser(userID, userName))
            }
            binding.iconVideoCall.setInvitees(users)
        }

    }

    private fun initVoiceButton(idUserListener : String) {
        binding.iconPhoneCall.setIsVideoCall(false)
        binding.iconPhoneCall.resourceID = "zego_data"
        val targetUserID = idUserListener.toString()
        val split = targetUserID.split(",")
        val users = ArrayList<ZegoUIKitUser>()
        for (userID in split) {
            println("userID=$userID")
            val userName = "User_${userID}"
            users.add(ZegoUIKitUser(userID, userName))
        }
        binding.iconPhoneCall.setInvitees(users)
    }

    private fun initCallInviteService(
        appID: Long,
        appSign: String,
        userID: String,
        userName: String
    ) {
        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig().apply {
            provider =
                ZegoUIKitPrebuiltCallConfigProvider { invitationData -> getConfig(invitationData) }
        }

        ZegoUIKitPrebuiltCallService.events.errorEventsListener =
            ErrorEventsListener { errorCode, message -> Timber.d("onError() called with: errorCode = [$errorCode], message = [$message]") }

        ZegoUIKitPrebuiltCallService.events.invitationEvents.pluginConnectListener =
            SignalPluginConnectListener { state, event, extendedData -> Timber.d("onSignalPluginConnectionStateChanged() called with: state = [$state], event = [$event], extendedData = [$extendedData]") }

        ZegoUIKitPrebuiltCallService.init(
            requireActivity().application,
            appID,
            appSign,
            userID,
            userName,
            callInvitationConfig
        )

        ZegoUIKitPrebuiltCallService.events.callEvents.callEndListener =
            CallEndListener { callEndReason, jsonObject -> Timber.d("onCallEnd() called with: callEndReason = [$callEndReason], jsonObject = [$jsonObject]") }

        ZegoUIKitPrebuiltCallService.events.callEvents.setExpressEngineEventHandler(object :
            IExpressEngineEventHandler() {
            override fun onRoomStateChanged(
                roomID: String,
                reason: ZegoRoomStateChangedReason,
                errorCode: Int,
                extendedData: JSONObject
            ) {
                Timber.d("onRoomStateChanged() called with: roomID = [$roomID], reason = [$reason], errorCode = [$errorCode], extendedData = [$extendedData]")
            }
        })
    }

    private fun getConfig(invitationData: ZegoCallInvitationData): ZegoUIKitPrebuiltCallConfig {
        val isVideoCall = invitationData.type == ZegoInvitationType.VIDEO_CALL.value
        val isGroupCall = invitationData.invitees.size > 1
        return when {
            isVideoCall && isGroupCall -> ZegoUIKitPrebuiltCallConfig.groupVideoCall()
            !isVideoCall && isGroupCall -> ZegoUIKitPrebuiltCallConfig.groupVoiceCall()
            !isVideoCall -> ZegoUIKitPrebuiltCallConfig.oneOnOneVoiceCall()
            else -> ZegoUIKitPrebuiltCallConfig.oneOnOneVideoCall()
        }
    }

    override fun onDetach() {
        super.onDetach()
        ZegoUIKitPrebuiltCallService.endCall()
    }

    private fun offlineUsePermission() {
        PermissionX.init(this).permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
            .onExplainRequestReason { scope, deniedList ->
                val message =
                    "We need your consent for the following permissions in order to use the offline call function properly"
                scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
            }.request { _, _, _ -> }
    }


}


