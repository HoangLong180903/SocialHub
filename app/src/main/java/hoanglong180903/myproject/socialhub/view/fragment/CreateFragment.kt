package hoanglong180903.myproject.socialhub.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.databinding.FragmentCreateBinding


class CreateFragment : Fragment() {
    lateinit var binding : FragmentCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateBinding.inflate(layoutInflater, container, false)
        val title = SpannableString("Create")
        title.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, 0)
        activity?.title = title
        return binding.root
    }


}