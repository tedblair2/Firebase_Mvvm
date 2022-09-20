package com.example.mvvm.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.Adapter.CommentAdapter
import com.example.mvvm.R
import com.example.mvvm.ViewModel.ViewModelClass
import com.example.mvvm.databinding.ActivityCommentBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class CommentActivity : AppCompatActivity() {
    lateinit var binding:ActivityCommentBinding

    val factory by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this@CommentActivity.application)
    }
    val viewModel by lazy {
        ViewModelProvider(this,factory)[ViewModelClass::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarComment)
        binding.toolbarComment.navigationIcon=ContextCompat.getDrawable(this,R.drawable.ic_back)
        binding.toolbarComment.setNavigationOnClickListener {
            onBackPressed()
        }

        val postid=intent.getStringExtra("postid")

        binding.recyclerComments.setHasFixedSize(true)
        binding.recyclerComments.layoutManager=LinearLayoutManager(this@CommentActivity)

        viewModel.getComments(postid!!)
        viewModel.liveComments.observe(this, Observer { comments->
            val adapter=CommentAdapter(comments,viewModel)
            binding.recyclerComments.adapter=adapter
        })

        viewModel.getUserData(Firebase.auth.currentUser!!.uid)
        viewModel.liveData.observe(this, Observer { user->
            if (user.profile.equals("null")){
                binding.commentProfile.setImageResource(R.drawable.profile)
            }else{
                Picasso.get().load(user.profile).into(binding.commentProfile)
            }
        })

        binding.commentSend.setOnClickListener {
            val comment=binding.commentTxt.text.toString().trim {it <= ' ' }

            if (TextUtils.isEmpty(comment)){
                binding.commentTxt.error="Field cannot be empty"
            }else{
                viewModel.addComment(comment, postid)
            }
        }
        viewModel.dialog.observe(this@CommentActivity, Observer { status->
            if (!status){
                binding.commentTxt.text=null
            }
        })
    }
}