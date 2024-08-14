package hoanglong180903.myproject.socialhub.view.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.databinding.FragmentForgotPasswordBinding
import hoanglong180903.myproject.socialhub.utils.Functions

class ForgotPasswordFragment : Fragment() {
    private lateinit var binding : FragmentForgotPasswordBinding
    private lateinit var auth: FirebaseAuth
    lateinit var loadingDialog : Dialog
    private var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        actionButton()
    }

    private fun init(view : View){
        auth = Firebase.auth
        loadingDialog = Functions.showLoadingDialog(requireContext())
        navController = Navigation.findNavController(view);
    }

    private fun actionButton(){
        binding.forgotBtnSearch.setOnClickListener {
            loadingDialog.show()
            auth.sendPasswordResetEmail(binding.forgotEdEmail.text.toString().trim())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        loadingDialog.dismiss()
                        Toast.makeText(context, "Check your email", Toast.LENGTH_LONG).show()
                        navController?.navigate(R.id.action_forgotPasswordFragment_to_signInFragment)
                    } else {
                        Toast.makeText(context, task.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }

        binding.forgotBtnCancel.setOnClickListener {
            binding.forgotEdEmail.setText("")
            navController?.navigate(R.id.action_forgotPasswordFragment_to_signInFragment)
        }
    }
}