package hoanglong180903.myproject.socialhub.view.fragment

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import hoanglong180903.myproject.socialhub.utils.BundleUtils
import hoanglong180903.myproject.socialhub.adapter.ActiveStatusUserAdapter
import hoanglong180903.myproject.socialhub.adapter.MessageAdapter
import hoanglong180903.myproject.socialhub.databinding.FragmentMessageBinding
import hoanglong180903.myproject.socialhub.interfaces.ItemMessageOnClick
import hoanglong180903.myproject.socialhub.model.UserModel
import hoanglong180903.myproject.socialhub.view.activity.ChatActivity
import hoanglong180903.myproject.socialhub.viewmodel.MessageViewModel

class MessageFragment : Fragment() , ItemMessageOnClick{
    lateinit var binding: FragmentMessageBinding
    private lateinit var viewModel: MessageViewModel
    private var navController: NavController? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val title = SpannableString("Message")
        title.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, 0)
        activity?.title = title
        getData()
    }
    private fun getData() {
        viewModel = ViewModelProvider(this)[MessageViewModel::class.java]
        viewModel.users.observe(viewLifecycleOwner, Observer { users ->
            binding.rcStatusUser.adapter = ActiveStatusUserAdapter(users)
        })
        viewModel.users.observe(viewLifecycleOwner, Observer { users ->
            binding.rcChatUser.adapter = MessageAdapter(users,this)
            binding.rcChatUser.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        })
        viewModel.fetchUsers()
    }

    override fun onClickItemToChat(model: UserModel) {
        val bundle = Bundle().apply {
//            putString("name", model.name)
//            putString("email", model.email)
//            putString("userId", model.id)
//            putString("profileImage", model.image)
//            putString("token",model.token)
            BundleUtils.bundleData(model, this)
        }
        val intent = Intent(context, ChatActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
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
        viewModel.fetchPresence("Offline")
        super.onPause()
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