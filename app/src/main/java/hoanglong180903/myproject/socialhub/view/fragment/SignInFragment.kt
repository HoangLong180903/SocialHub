package hoanglong180903.myproject.socialhub.view.fragment

import android.content.Intent
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
import hoanglong180903.myproject.socialhub.view.activity.MainActivity
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.databinding.FragmentSignInBinding
import hoanglong180903.myproject.socialhub.viewmodel.SignInViewModel


class SignInFragment : Fragment() {
    private var navController: NavController? = null
    lateinit var binding : FragmentSignInBinding
    lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentSignInBinding.inflate(layoutInflater, container, false)
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
    }

    private fun initView(){
        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]
    }

    private fun login(){
        binding.btnLogin.setOnClickListener {
            viewModel.requestLogin(binding.edEmail.text.toString(),binding.edPassword.text.toString())
        }
        //khi dang nhap thanh cong hoac that bai
        viewModel.isSuccessful.observe(viewLifecycleOwner, Observer {
            var message = ""
            if (it){
                message = "Dang nhap thanh cong"
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }else{
                message = "Dang nhap that bai"
            }
            Toast.makeText(context,message,Toast.LENGTH_LONG).show()
        })
    }
}