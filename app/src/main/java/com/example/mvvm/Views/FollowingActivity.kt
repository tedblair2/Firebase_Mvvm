package com.example.mvvm.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.Adapter.UserAdapter
import com.example.mvvm.R
import com.example.mvvm.ViewModel.ViewModelClass
import com.example.mvvm.databinding.ActivityFollowingBinding

class FollowingActivity : AppCompatActivity() {
    lateinit var binding:ActivityFollowingBinding

    val factory by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
    }
    val viewmodel by lazy {
        ViewModelProvider(this,factory)[ViewModelClass::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFollowingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarFollowing)
        binding.toolbarFollowing.navigationIcon=ContextCompat.getDrawable(this,R.drawable.ic_back)
        binding.toolbarFollowing.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.recyclerFollowing.setHasFixedSize(true)
        binding.recyclerFollowing.layoutManager=LinearLayoutManager(this@FollowingActivity)

        val userid=intent.getStringExtra("userid")!!
        viewmodel.getFollowing(userid)
        viewmodel.userFollowing.observe(this, Observer { following->
            val userAdapter= UserAdapter(following,viewmodel)
            binding.recyclerFollowing.adapter=userAdapter
        })
    }
}