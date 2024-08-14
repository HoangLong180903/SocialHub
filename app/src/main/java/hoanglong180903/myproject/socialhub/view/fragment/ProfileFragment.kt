package hoanglong180903.myproject.socialhub.view.fragment

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.databinding.FragmentProfileBinding
import hoanglong180903.myproject.socialhub.utils.Functions
import hoanglong180903.myproject.socialhub.view.activity.MainActivity
import hoanglong180903.myproject.socialhub.view.activity.ProfileActivity
import hoanglong180903.myproject.socialhub.viewmodel.ProfileViewModel


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    var selectedImage: Uri? = null
    private var navController: NavController? = null
    lateinit var viewModel: ProfileViewModel
    lateinit var loadingDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        getDataBundle()
    }

    private fun initView(view: View) {
        navController = Navigation.findNavController(view)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        loadingDialog = Functions.showLoadingDialog(requireContext())
    }

    private fun getDataBundle() {

        arguments?.let { bundle ->
            val name = bundle.getString("name")
            val email = bundle.getString("email")
            val password = bundle.getString("password")
            val userId = bundle.getString("userId")
            val profileImage = bundle.getString("profileImage")
            val token = bundle.getString("token")

            binding.profileEdName.setText(name)
            binding.profileEdEmail.setText(email)
            binding.profileEdPassword.setText(password)
            if (profileImage == "No image") {
                binding.profileImgUser.setImageResource(R.drawable._144760)
            } else {
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
        binding.profileBtnCancel.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun requestUpdateUser(uid: String) {
        binding.profileBtnSave.setOnClickListener {
            loadingDialog.show()
            viewModel.updateUser(uid, binding.profileEdName.text.toString(), selectedImage)
        }
        viewModel.isSuccessful.observe(viewLifecycleOwner, Observer {
            var message = ""
            if (it) {
                message = "Update profile successful"
                activity?.finish()
            } else {
                message = "Update profile failed"
            }
            loadingDialog.dismiss()
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