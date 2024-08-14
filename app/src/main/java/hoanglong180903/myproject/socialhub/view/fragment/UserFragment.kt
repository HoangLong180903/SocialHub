package hoanglong180903.myproject.socialhub.view.fragment

import android.content.Intent
import android.graphics.Typeface
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
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.view.Change
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.databinding.FragmentUserBinding
import hoanglong180903.myproject.socialhub.view.activity.ChangePasswordActivity
import hoanglong180903.myproject.socialhub.view.activity.ProfileActivity
import hoanglong180903.myproject.socialhub.view.activity.RegistrationActivity
import hoanglong180903.myproject.socialhub.viewmodel.UserViewModel

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    lateinit var viewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = SpannableString("User")
        title.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, 0)
        activity?.title = title
        initView()
        logOut()
        fetchInfoUser(FirebaseAuth.getInstance().uid.toString())
    }

    private fun initView() {
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
    }

    private fun logOut() {
        binding.userBtnLogOut.setOnClickListener {
            viewModel.logOut()
            val intent = Intent(requireActivity(), RegistrationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun fetchInfoUser(uid: String) {
        viewModel.fetchUser(uid)
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                binding.userTvName.text = user.name
                binding.userTvEmail.text = user.email
                if (user.image == "No image") {
                    binding.userImgUser.setImageResource(R.drawable._144760)
                }else{
                    Glide.with(this)
                        .load(user.image)
                        .placeholder(R.drawable._144760)
                        .error(R.drawable._144760)
                        .into(binding.userImgUser)
                }
                binding.userBtnEditProfile.setOnClickListener {
                    val bundle = Bundle().apply {
//                        BundleUtils.bundleData(user)
                        putString("name", user.name)
                        putString("email", user.email)
                        putString("password",user.password)
                        putString("userId", user.id)
                        putString("profileImage", user.image)
                        putString("token", user.token)
                    }
                    val intent = Intent(context, ProfileActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }

                binding.userBtnChangePassword.setOnClickListener {
                    val bundle = Bundle().apply {
//                        BundleUtils.bundleData(user)
                        putString("name", user.name)
                        putString("email", user.email)
                        putString("password",user.password)
                        putString("userId", user.id)
                        putString("profileImage", user.image)
                        putString("token", user.token)
                    }
                    val intent = Intent(context, ChangePasswordActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
                binding.userBtnHelp.setOnClickListener {
                    Toast.makeText(requireContext(),"Not available",Toast.LENGTH_LONG).show()
                }
                binding.userBtnSetting.setOnClickListener {
                    Toast.makeText(requireContext(),"Not available",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        fetchInfoUser(FirebaseAuth.getInstance().uid.toString())
    }
}