package hoanglong180903.myproject.socialhub.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import hoanglong180903.myproject.socialhub.Helper.AppInfo
import hoanglong180903.myproject.socialhub.Helper.CreateOrder
import hoanglong180903.myproject.socialhub.adapter.CategoriesAdapter
import hoanglong180903.myproject.socialhub.adapter.ProductAdapter
import hoanglong180903.myproject.socialhub.databinding.FragmentShoppingBinding
import hoanglong180903.myproject.socialhub.listener.ProductClickItemListener
import hoanglong180903.myproject.socialhub.model.Product
import hoanglong180903.myproject.socialhub.utils.Contacts
import hoanglong180903.myproject.socialhub.view.activity.MainActivity
import hoanglong180903.myproject.socialhub.viewmodel.ShoppingViewModel
import hoanglong180903.myproject.socialhub.viewmodelFactory.ShoppingViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener


class ShoppingFragment : Fragment(), ProductClickItemListener {
    private lateinit var binding: FragmentShoppingBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var viewModel: ShoppingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        ZaloPaySDK.init(AppInfo.APP_ID, Environment.SANDBOX)

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
        getData()
    }

    private fun initView() {
        viewModel = ViewModelProvider(
            this,
            ShoppingViewModelFactory(requireActivity().application)
        )[ShoppingViewModel::class.java]
    }

    private fun initVariable() {
        categoriesAdapter = CategoriesAdapter(requireContext())
        binding.shoppingRcCategories.adapter = categoriesAdapter
        binding.shoppingRcCategories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        productAdapter = ProductAdapter(requireContext(), this)
        binding.shoppingRcProduct.adapter = productAdapter
        binding.shoppingRcProduct.apply {
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun getData() {
        binding.shoppingCategoriesProgress.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.fetchCategories(Contacts.URL_CATEGORIES)
            withContext(Dispatchers.Main) {
                viewModel.categories.observe(requireActivity(), Observer { categories ->
                    categories?.let {
                        categoriesAdapter.submitList(it)
                        binding.shoppingCategoriesProgress.visibility = View.GONE
                    }
                })
            }
        }
        //get product
        binding.shoppingProductProgress.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.fetchProducts(Contacts.URL_PRODUCTS)
            withContext(Dispatchers.Main) {
                viewModel.products.observe(requireActivity(), Observer { albums ->
                    albums.let {
                        productAdapter.submitList(albums)
                        binding.shoppingProductProgress.visibility = View.GONE
                    }
                })
            }
        }
    }

    override fun onItemClickBuyNow(product: Product) {
        val orderApi = CreateOrder()
        try {
            val data: JSONObject = orderApi.createOrder(100.toString())
            val code = data.getString("returncode")

            if (code == "1") {
                val token = data.getString("zptranstoken")
                ZaloPaySDK.getInstance().payOrder(
                    (context as Activity?)!!,
                    token,
                    "demozpdk://app",
                    object : PayOrderListener {
                        override fun onPaymentSucceeded(
                            transactionId: String,
                            transToken: String,
                            appTransID: String
                        ) {
                            Toast.makeText(
                                context,
                                "Thanh toán thành công",
                                Toast.LENGTH_SHORT
                            ).show();
                            val intent = Intent(context,MainActivity::class.java)
                            startActivity(intent)
                        }

                        override fun onPaymentCanceled(zpTransToken: String, appTransID: String) {
                            Toast.makeText(context, "Thanh toán bị hủy", Toast.LENGTH_SHORT).show()
                        }

                        override fun onPaymentError(
                            zaloPayError: ZaloPayError,
                            zpTransToken: String,
                            appTransID: String
                        ) {
                            Toast.makeText(context, "Thanh toán thất bại", Toast.LENGTH_SHORT)
                                .show()
                            Log.e("zalopay",""+zaloPayError)
                        }
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}