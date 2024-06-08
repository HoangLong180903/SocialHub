package hoanglong180903.myproject.socialhub.view.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.databinding.FragmentSignUpBinding
import hoanglong180903.myproject.socialhub.utils.Functions
import hoanglong180903.myproject.socialhub.viewmodel.SignUpViewModel


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: SignUpViewModel
    private var navController: NavController? = null
    private lateinit var loadingDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view);
        initView()
        signUp()
        goToSignIn()
    }
    private fun initView(){
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        loadingDialog = Functions.showLoadingDialog(requireContext())
    }

    private fun signUp(){
        binding.btnSignUp.setOnClickListener {
            if (binding.edConfirmPassword.text.toString() == binding.edPassword.text.toString()){
                viewModel.requestSignUp(
                    binding.edName.text.toString(),
                    binding.edEmail.text.toString(),
                    binding.edPassword.text.toString()
                )
                loadingDialog.show()
            }else if (binding.edConfirmPassword.text.toString().isEmpty() || binding.edPassword.text.toString().isEmpty()){
                Toast.makeText(context,"Request cannot be empty",Toast.LENGTH_SHORT).show()
            }else if (binding.edConfirmPassword.text.toString() != binding.edPassword.text.toString()){
                Toast.makeText(context,"Password must match",Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isSuccessful.observe(viewLifecycleOwner, Observer {
            var message = ""
             if (it) {
                 message = "Account registration successful"
                 binding.edName.setText("")
                 binding.edEmail.setText("")
                 binding.edPassword.setText("")
                 binding.edConfirmPassword.setText("")
            } else {
                 message = "Account registration failed"
            }
            loadingDialog.dismiss()
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        })
    }

    private fun goToSignIn(){
        binding.tvHaveAccount.setOnClickListener(View.OnClickListener {
            navController?.navigate(R.id.action_signUpFragment_to_signInFragment)
        })
    }
}