package hoanglong180903.myproject.socialhub.view.fragment

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.adapter.PostAdapter
import hoanglong180903.myproject.socialhub.adapter.StoriesAdapter
import hoanglong180903.myproject.socialhub.databinding.FragmentHomeBinding
import hoanglong180903.myproject.socialhub.interfaces.ItemPostOnClick
import hoanglong180903.myproject.socialhub.model.UserModel
import hoanglong180903.myproject.socialhub.utils.BundleUtils
import hoanglong180903.myproject.socialhub.view.activity.ChatActivity
import hoanglong180903.myproject.socialhub.view.activity.DetailPostActivity
import hoanglong180903.myproject.socialhub.viewmodel.HomeViewModel

class HomeFragment : Fragment() , ItemPostOnClick {
    lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    var user = UserModel()
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = SpannableString("Home")
        title.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, 0)
        activity?.title = title
        initView()
        getData()
        fetchInfoUser(FirebaseAuth.getInstance().uid.toString())
        requestAddStories()
    }

    private fun initView() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    private fun getData() {
        viewModel.getUserStories()
        viewModel.users.observe(viewLifecycleOwner, Observer { users ->
            val sortedStatuses = users.sortedByDescending { it.lastUpdated }
            binding.rcStories.adapter = StoriesAdapter(sortedStatuses)
            binding.rcStories.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        })
        viewModel.getPost()
        viewModel.posts.observe(viewLifecycleOwner, Observer { users ->
            val sortedStatuses = users.sortedByDescending { it.pTime }
            binding.rcPost.adapter = PostAdapter(sortedStatuses,this)
            binding.rcPost.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            for (item in users){
                viewModel.deleteStoriesInADay(item.pTime)
            }
        })
    }

    private fun fetchInfoUser(uid: String) {
        viewModel.fetchUser(uid)
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                if (user.image == "No image") {
                    binding.imgUser.setImageResource(R.drawable._144760)
                }else{
                    Glide.with(this)
                        .load(user.image)
                        .placeholder(R.drawable._144760)
                        .error(R.drawable._144760)
                        .into(binding.imgUser)
                }
            }
        })
    }


    private fun requestAddStories(){
        database!!.reference.child("users")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(UserModel::class.java)!!
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        binding.cvAddStories.setOnClickListener {
            val mIntent = Intent()
            mIntent.type = "image/*"
            mIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(mIntent, 75)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (data.data != null) {
                viewModel.addUserStories(data,user)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchInfoUser(FirebaseAuth.getInstance().uid.toString())
    }

    override fun onClickReleaseEmotions(idPost: String, idUser: String) {
//        viewModel.updatePostEmotions(idPost,idUser)
    }

    override fun onClickComment(idPost: String) {
        val bundle = Bundle().apply {
//            putString("name", model.name)
//            putString("email", model.email)
//            putString("userId", model.id)
//            putString("profileImage", model.image)
            putString("idPost",idPost)
        }
        val intent = Intent(context, DetailPostActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }


}