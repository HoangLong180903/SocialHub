package hoanglong180903.myproject.socialhub.view.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import hoanglong180903.myproject.socialhub.view.activity.MainActivity
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.databinding.FragmentSignInBinding
import hoanglong180903.myproject.socialhub.utils.Functions
import hoanglong180903.myproject.socialhub.viewmodel.SignInViewModel


class SignInFragment : Fragment() {
    private var navController: NavController? = null
    lateinit var binding: FragmentSignInBinding
    lateinit var viewModel: SignInViewModel
    private lateinit var loadingDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view);
        binding.tvRegister.setOnClickListener(View.OnClickListener {
            navController?.navigate(R.id.action_signInFragment_to_signUpFragment)
        })
        initView()
        login()
        loginWithGoogle()
    }

    private fun initView() {
        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]
        loadingDialog = Functions.showLoadingDialog(requireContext())
    }

    private fun login() {
        binding.btnLogin.setOnClickListener {
            viewModel.requestLogin(
                binding.edEmail.text.toString(),
                binding.edPassword.text.toString()
            )
            loadingDialog.show()
        }
        //khi dang nhap thanh cong hoac that bai
        viewModel.isSuccessful.observe(viewLifecycleOwner, Observer {
            var message = ""
            if (it) {
                message = "Logged in successfully"
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                message = "Logged in failed"
            }
            loadingDialog.dismiss()
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun loginWithGoogle() {
        binding.lnSignInWithGoogle.setOnClickListener {
            Toast.makeText(context, "This feature cannot be used yet", Toast.LENGTH_SHORT).show()
        }
    }

}