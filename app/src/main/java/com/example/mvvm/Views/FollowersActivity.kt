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
import com.example.mvvm.databinding.ActivityFollowersBinding

class FollowersActivity : AppCompatActivity() {
    lateinit var binding: ActivityFollowersBinding

    val factory by lazy{
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
    }
    val viewmodel by lazy {
        ViewModelProvider(this,factory)[ViewModelClass::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFollowersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarFollowers)
        binding.toolbarFollowers.navigationIcon=ContextCompat.getDrawable(this,R.drawable.ic_back)
        binding.toolbarFollowers.setNavigationOnClickListener {
            onBackPressed()
        }

        val userid=intent.getStringExtra("userid")!!

        binding.recyclerFollowers.setHasFixedSize(true)
        binding.recyclerFollowers.layoutManager=LinearLayoutManager(this)

        viewmodel.getFollowers(userid)
        viewmodel.userFollowers.observe(this, Observer { followers->
            val userAdapter=UserAdapter(followers,viewmodel)
            binding.recyclerFollowers.adapter=userAdapter
        })
    }
}