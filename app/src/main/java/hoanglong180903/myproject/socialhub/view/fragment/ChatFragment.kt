package hoanglong180903.myproject.socialhub.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import hoanglong180903.myproject.socialhub.adapter.UserAdapter
import hoanglong180903.myproject.socialhub.databinding.FragmentChatBinding
import hoanglong180903.myproject.socialhub.viewmodel.ChatFragmentViewModel

class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    private lateinit var viewModel: ChatFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater,container,false)
        init()
        getData()
        return binding.root
    }
    private fun init(){
        viewModel = ViewModelProvider(this)[ChatFragmentViewModel::class.java]
    }
    private fun getData(){

        viewModel.users.observe(viewLifecycleOwner, Observer { users ->
            // Cập nhật UI với danh sách người dùng
            binding.rcStatusUser.adapter = UserAdapter(users)
        })

        viewModel.fetchUsers()
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchPresence("Online")
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPresence("Online")
    }
    override fun onPause() {
        super.onPause()
        viewModel.fetchPresence("Offline")
    }

    override fun onStop() {
        super.onStop()
        viewModel.fetchPresence("Offline")
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.fetchPresence("Offline")
    }

}