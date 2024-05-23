package hoanglong180903.myproject.socialhub.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
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
        return binding.root
    }


}