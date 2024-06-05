package hoanglong180903.myproject.socialhub.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.adapter.ChatAdapter
import hoanglong180903.myproject.socialhub.adapter.CommentAdapter
import hoanglong180903.myproject.socialhub.databinding.ActivityDetailPostBinding
import hoanglong180903.myproject.socialhub.model.Posts
import hoanglong180903.myproject.socialhub.view.fragment.ChatFragment
import hoanglong180903.myproject.socialhub.viewmodel.DetailPostViewModel

class DetailPostActivity : AppCompatActivity() {
    lateinit var binding : ActivityDetailPostBinding
    private var database : FirebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var ref : DatabaseReference
    lateinit var query : Query
    lateinit var viewModel : DetailPostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        getDataBundle()
    }

    private fun init(){
        viewModel = ViewModelProvider(this)[DetailPostViewModel::class.java]
    }
    private fun getDataBundle(){
        val bundle = intent.extras
        if (bundle != null) {
            val postId = bundle.getString("idPost")
            ref = database.getReference("Posts")
            query = ref.orderByChild("pId").equalTo(postId)
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children){
                        val pId = ds.child("pId").getValue(String::class.java)
                        val pImage = ds.child("pImage").getValue(String::class.java)
                        val pLikes = ds.child("pLikes").getValue(String::class.java)
                        val pComments = ds.child("pComments").getValue(String::class.java)
                        val pTime = ds.child("pTime").getValue(Long::class.java)
                        val pTitle = ds.child("pTitle").getValue(String::class.java)
                        val uImage = ds.child("uImage").getValue(String::class.java)
                        val uName = ds.child("uName").getValue(String::class.java)
                        val uId = ds.child("uid").getValue(String::class.java)

                        if (uImage == "No image") {
                            binding.detailPostImgUser.setImageResource(R.drawable._144760)
                        } else {
                            Glide.with(applicationContext)
                                .load(uImage)
                                .placeholder(R.drawable._144760)
                                .error(R.drawable._144760)
                                .into(binding.detailPostImgUser)
                        }

                        binding.detailPostTvEmotions.text = "$pLikes Like"
                        binding.detailPostTvComment.text = "$pComments Comment"
                        binding.detailPostTvTime.text = "" + getTimeAgo(pTime!!)
                        binding.detailPostTitle.text = ""+ pTitle
                        Glide.with(applicationContext)
                            .load(pImage)
                            .placeholder(R.drawable._144760)
                            .error(R.drawable._144760)
                            .into(binding.itemPostImgPost)
                        binding.detailPostTvName.text = ""+ uName
                        fetchInfoUser(FirebaseAuth.getInstance().uid!!, pId.toString(),
                            uId.toString()
                        )
                        getData(postId.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Detail_post","Khong thanh cong")
                }

            })
        }
    }

    private fun requestComment(postIde : String  , uid :String , uName:String , uImage:String){
        binding.detailPostBtnComment.setOnClickListener {
            viewModel.sendComment(postIde ,binding.detailPostEdComment.text.toString(),uid,uName , uImage)
            viewModel.updateComment(postIde)
            binding.detailPostEdComment.setText("")
        }
    }

    private fun getData(postIde: String) {
        viewModel.users.observe(this, Observer { users ->
            binding.detailPostRcComment.adapter = CommentAdapter(users)
            binding.detailPostRcComment.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )

        })
        viewModel.getComment(postIde)
    }

    private fun getTimeAgo(timestamp: Long): String {
        val currentTime = System.currentTimeMillis()
        return if (timestamp < currentTime) {
            val difference = currentTime - timestamp
            when {
                difference < DateUtils.MINUTE_IN_MILLIS -> {
                    "vừa xong"
                }
                difference < DateUtils.DAY_IN_MILLIS -> {
                    DateUtils.getRelativeTimeSpanString(
                        timestamp,
                        currentTime,
                        DateUtils.MINUTE_IN_MILLIS
                    ).toString()
                }
                else -> {
                    val days = (difference / DateUtils.DAY_IN_MILLIS).toInt()
                    "$days ngày trước"
                }
            }
        } else {
            "In the future"
        }
    }

    private fun fetchInfoUser(uid: String , pId : String , uId : String) {
        viewModel.fetchUser(uid)
        viewModel.user.observe(this, Observer { user ->
            if (user != null) {
                requestComment(pId,uId,user.name, user.image)
            }
        })
    }
}