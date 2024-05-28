package hoanglong180903.myproject.socialhub.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.databinding.FragmentProfileBinding
import hoanglong180903.myproject.socialhub.viewmodel.ProfileViewModel


class ProfileFragment : Fragment() {
    lateinit var binding : FragmentProfileBinding
    var selectedImage: Uri? = null
    private var navController: NavController? = null
    lateinit var viewModel : ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        getDataBundle()
    }

    private fun initView(view : View){
        navController = Navigation.findNavController(view)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
    }
    private fun getDataBundle(){

        arguments?.let { bundle ->
            val name = bundle.getString("name")
            val email = bundle.getString("email")
            val userId = bundle.getString("userId")
            val profileImage = bundle.getString("profileImage")
            val token = bundle.getString("token")

            binding.profileEdName.setText(name)
            binding.profileEdEmail.setText(email)
            if (profileImage == "No image") {
                binding.profileImgUser.setImageResource(R.drawable._144760)
            }else{
                Glide.with(this)
                    .load(profileImage)
                    .placeholder(R.drawable._144760)
                    .error(R.drawable._144760)
                    .into(binding.profileImgUser)
            }
            requestUpdateUser(userId!!)
        }
        binding.profileImgUser.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 45)
        }
    }
    private fun requestUpdateUser(uid :String){
        binding.profileBtnSave.setOnClickListener {
            viewModel.updateUser(uid,binding.profileEdName.text.toString(),selectedImage)
        }
        viewModel.isSuccessful.observe(viewLifecycleOwner, Observer {
            var message = ""
            if (it) {
                message = "update thanh cong"
                activity?.finish()
            } else {
                message = "Update that bai"
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (data.data != null) {
                binding.profileImgUser.setImageURI(data.data)
                selectedImage = data.data
            }
        }
    }



}