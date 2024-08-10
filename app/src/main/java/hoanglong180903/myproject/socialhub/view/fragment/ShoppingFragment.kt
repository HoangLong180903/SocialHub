package hoanglong180903.myproject.socialhub.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hoanglong180903.myproject.socialhub.adapter.CategoriesAdapter
import hoanglong180903.myproject.socialhub.databinding.FragmentShoppingBinding
import hoanglong180903.myproject.socialhub.utils.Contacts
import hoanglong180903.myproject.socialhub.viewmodel.CategoriesViewModel
import hoanglong180903.myproject.socialhub.viewmodel.DeezerViewModel
import hoanglong180903.myproject.socialhub.viewmodelFactory.CategoriesViewModelFactory
import hoanglong180903.myproject.socialhub.viewmodelFactory.DeezerViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShoppingFragment : Fragment() {

    private lateinit var binding : FragmentShoppingBinding
    private lateinit var categoriesAdapter : CategoriesAdapter
//    private val categoriesViewModel: CategoriesViewModel by lazy {
//        ViewModelProvider(
//            this,
//            CategoriesViewModelFactory(requireActivity().application)
//        )[CategoriesViewModel::class.java]
//    }
    private lateinit var categoriesViewModel : CategoriesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShoppingBinding.inflate(layoutInflater, container, false)
        val title = SpannableString("Shopping")
        title.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, 0)
        activity?.title = title
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initVariable()
    }

    private fun initView(){
        categoriesViewModel = ViewModelProvider(this, CategoriesViewModelFactory(requireActivity().application))[CategoriesViewModel::class.java]
    }

    //setup recyclerview
    private fun initVariable() {
        categoriesAdapter = CategoriesAdapter(requireContext())
        binding.shoppingRcCategories.adapter = categoriesAdapter
        binding.shoppingRcCategories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        getData()
    }

    private fun getData() {
        CoroutineScope(Dispatchers.IO).launch {
            categoriesViewModel.fetchCategories(Contacts.URL_CATEGORIES)
            withContext(Dispatchers.Main) {
                categoriesViewModel.categories.observe(requireActivity(), Observer { tracks ->
                    tracks?.let {
                        categoriesAdapter.submitList(it)
                    }
                })
            }
        }
    }

}