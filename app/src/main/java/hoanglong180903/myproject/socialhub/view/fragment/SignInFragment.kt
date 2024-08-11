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
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
    private var RC_SIGN_IN = 100
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
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
        binding.loginTvForgotPassword.setOnClickListener(View.OnClickListener {
            navController?.navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
        })
        initView()
        login()
        loginWithGoogle()
        forgotPassword()
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

        viewModel.isCheckEmailVerified.observe(viewLifecycleOwner, Observer {
            var text_message = ""
           if (it){
               text_message = "Logged in successfully"
           }else{
               text_message = "Please verify your email first."
           }
            loadingDialog.dismiss()
            Toast.makeText(context, text_message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun loginWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_string))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        binding.lnSignInWithGoogle.setOnClickListener {
            val signIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signIntent, RC_SIGN_IN)
        }
    }

    private fun forgotPassword() {
//        binding.loginTvForgotPassword.setOnClickListener {
//            loadingDialog.show()
//            FirebaseAuth.getInstance()
//                .sendPasswordResetEmail(
//                    "hoanglong180903@gmail.com"
//                )
//                .addOnSuccessListener {
//                    loadingDialog.dismiss()
//                    Toast.makeText(context, "Send Email Successful", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener {
//                    loadingDialog.dismiss()
//                    it.message?.let { it1 -> Log.d("forgot password", it1) }
//                }
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.d("Sign in google", "Google sign in failed $e")
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user: FirebaseUser = mAuth.currentUser!!
                    Toast.makeText(context, user.email, Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d("Sign in google", "sign in with credential failed ${it.exception}")

                }
            }.addOnFailureListener {
                Log.d("Sign in google", "sign in with credential failed ${it.message}")
            }
    }
}