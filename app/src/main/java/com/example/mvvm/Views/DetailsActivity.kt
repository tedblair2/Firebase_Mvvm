package com.example.mvvm.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm.R
import com.example.mvvm.ViewModel.ViewModelClass
import com.example.mvvm.databinding.ActivityDetailsBinding
import com.squareup.picasso.Picasso

class DetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsBinding

    val factory by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
    }
    val viewmodel by lazy {
        ViewModelProvider(this,factory)[ViewModelClass::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postid=intent.getStringExtra("postid")!!
        val userid=intent.getStringExtra("userid")!!

        setSupportActionBar(binding.toolbarDetails)
        binding.toolbarDetails.navigationIcon=ContextCompat.getDrawable(this,R.drawable.ic_back)
        binding.toolbarDetails.setNavigationOnClickListener {
            onBackPressed()
        }

        viewmodel.getPostDetails(postid)
        viewmodel.postDetails.observe(this, Observer { post->
            Picasso.get().load(post.imagelink).into(binding.homeImage)
            binding.homeDescription.text=post.description
        })

        viewmodel.getPublisherDetails(userid)
        viewmodel.userDetails.observe(this, Observer { user->
            binding.homeName.text=user.name
            binding.homeUser.text=user.name

            if (user.profile.equals("null")){
                binding.homeProfile.setImageResource(R.drawable.profile)
            }else{
                Picasso.get().load(user.profile).into(binding.homeProfile)
            }
        })
        viewmodel.isSaved(postid,binding.saveImage)
        viewmodel.isLiked(postid,binding.likeImage)
        viewmodel.noOfComment(postid,binding.commentCount)
        viewmodel.noOfLikes(postid,binding.homeLikes)

        binding.commentImage.setOnClickListener {
            val intent=Intent(this,CommentActivity::class.java)
            intent.putExtra("postid",postid)
            intent.putExtra("userid",userid)
            startActivity(intent)
        }

        binding.commentCount.setOnClickListener {
            val intent=Intent(this,CommentActivity::class.java)
            intent.putExtra("postid",postid)
            intent.putExtra("userid",userid)
            startActivity(intent)
        }
        binding.likeImage.setOnClickListener {
            if (binding.likeImage.tag.equals("like")){
                viewmodel.addLike(postid)
            }else{
                viewmodel.removeLike(postid)
            }
        }
        binding.saveImage.setOnClickListener {
            if (binding.saveImage.tag.equals("save")){
                viewmodel.savePost(postid)
            }else{
                viewmodel.removePost(postid)
            }
        }
        binding.homeProfile.setOnClickListener {
            val bundle=Bundle()
            bundle.putString("publisherid",userid)
            val fragment=ProfileFragment()
            fragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.framelayout,fragment)
                .addToBackStack(null).commit()
        }
        binding.homeName.setOnClickListener {
            val bundle=Bundle()
            bundle.putString("publisherid",userid)
            val fragment=ProfileFragment()
            fragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.framelayout,fragment)
                .addToBackStack(null).commit()
        }
    }
}