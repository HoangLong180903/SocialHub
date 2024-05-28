package hoanglong180903.myproject.socialhub.view.fragment

import android.Manifest
import android.app.Activity
import android.content.ContentValues
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
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.adapter.ChatAdapter
import hoanglong180903.myproject.socialhub.databinding.FragmentChatBinding
import hoanglong180903.myproject.socialhub.viewmodel.ChatViewModel
import java.util.Date


class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding
    private var navController: NavController? = null
    private lateinit var viewModel: ChatViewModel
    private val senderUid = FirebaseAuth.getInstance().uid
    private val REQUEST_IMAGE_CAPTURE = 1
    var senderRoom: String = ""
    var receiverRoom: String = ""
    private var imageBitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            val token = bundle.getString("token")
            senderRoom = senderUid + userId
            receiverRoom = userId + senderUid
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
            requestSendMessage(userId.toString())
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
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }

        binding.iconAddFile.setOnClickListener {
            binding.lnCameraPhotoImage.visibility = View.VISIBLE
            binding.iconAddFile.visibility = View.GONE
        }
    }

    private fun requestSendMessage(receiver: String) {
        val date = Date()
        val senderRoom = senderUid + receiver
        val receiverRoom = receiver + senderUid
        binding.imageSend.setOnClickListener {
            viewModel.sendMessage(
                binding.edChating.text.toString(),
                senderUid!!,
                date.time,
                senderRoom,
                receiverRoom
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
                    viewModel.sendPhotoGallery(data, "Photo", senderUid!!, senderRoom, receiverRoom)
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let {
//                imageView.setImageURI(it)
            }
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf<String>(
                    Manifest.permission.CAMERA
                ), 100
            )
        }
    }

}


