package hoanglong180903.myproject.socialhub.view.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.adapter.ChatAdapter
import hoanglong180903.myproject.socialhub.databinding.FragmentCreateBinding
import hoanglong180903.myproject.socialhub.model.UserModel
import hoanglong180903.myproject.socialhub.utils.Functions
import hoanglong180903.myproject.socialhub.view.activity.MainActivity
import hoanglong180903.myproject.socialhub.view.activity.ProfileActivity
import hoanglong180903.myproject.socialhub.viewmodel.CreateViewModel


class CreateFragment : Fragment() {
    lateinit var binding : FragmentCreateBinding
    private var imageBitmap: Bitmap? = null
    private var selectedImageUri : Uri? = null
    lateinit var viewModel : CreateViewModel
    private var urlImageUser :  String = ""
    private var nameUser : String = ""
    lateinit var loadingDialog: Dialog
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateBinding.inflate(layoutInflater, container, false)
        val title = SpannableString("Create")
        title.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, 0)
        activity?.title = title
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        fetchInfoUser(FirebaseAuth.getInstance().uid.toString())
        pickerImageGallery()
    }

    private fun initView(view : View){
        viewModel = ViewModelProvider(this)[CreateViewModel::class.java]
        loadingDialog = Functions.showLoadingDialog(requireContext())
        navController = Navigation.findNavController(view);

    }

    private fun fetchInfoUser(uid: String) {
        viewModel.fetchUser(uid)
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                binding.createTvName.text = user.name
                if (user.image == "No image") {
                    binding.createImgUser.setImageResource(R.drawable._144760)
                }else{
                    Glide.with(this)
                        .load(user.image)
                        .placeholder(R.drawable._144760)
                        .error(R.drawable._144760)
                        .into(binding.createImgUser)
                }
            }
            urlImageUser = user!!.image
            nameUser = user.name
            requestAddPost(user)
        })
    }

    private fun pickerImageGallery(){
        binding.createLnImage.setOnClickListener {
            val mIntent = Intent()
            mIntent.action = Intent.ACTION_GET_CONTENT
            mIntent.type = "image/*"
            startActivityForResult(mIntent, 25)
        }
        binding.createImageUrl.setOnClickListener {
            val mIntent = Intent()
            mIntent.action = Intent.ACTION_GET_CONTENT
            mIntent.type = "image/*"
            startActivityForResult(mIntent, 25)
        }
    }

    private fun requestAddPost(user : UserModel){
        binding.createBtnPost.setOnClickListener {
            viewModel.createPost(selectedImageUri!!,user,binding.createEdStatus.text.toString())
            loadingDialog.show()
        }
        viewModel.isSuccessful.observe(viewLifecycleOwner, Observer {
            var message = ""
            if (it) {
                message = "Create posts successfully"
                binding.createEdStatus.setText("")
                selectedImageUri = null
                binding.createImageUrl.visibility = View.GONE
                binding.createLnImage.visibility = View.VISIBLE
                navController?.navigate(R.id.action_createFragment_to_homeFragment)
            } else {
                message = "Create posts failed"
            }
            loadingDialog.dismiss()
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 25) {
            if (resultCode == Activity.RESULT_OK) {
                if (data!!.data != null) {
                    binding.createImageUrl.visibility = View.VISIBLE
                    binding.createLnImage.visibility = View.GONE
                    selectedImageUri = data.data
                    selectedImageUri?.let {
                        binding.createImageUrl.setImageURI(it)
                    }
                }
            }
        }
    }

}