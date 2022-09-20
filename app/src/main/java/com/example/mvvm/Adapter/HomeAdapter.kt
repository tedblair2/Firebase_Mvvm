package com.example.mvvm.Adapter

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.Model.Posts
import com.example.mvvm.R
import com.example.mvvm.ViewModel.ViewModelClass
import com.example.mvvm.Views.CommentActivity
import com.example.mvvm.Views.ProfileFragment
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.security.PrivateKey

class HomeAdapter(private val postList:ArrayList<Posts>,
                  private val context: Context,
                  private val viewmodel:ViewModelClass,
                  ):RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.home_items,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post=postList[position]

        Picasso.get().load(post.imagelink).placeholder(R.drawable.not_available).into(holder.image)
        holder.description.text=" ${post.description}"

        viewmodel.getPublisher(post.publisherid!!,holder.profile,holder.name)

        viewmodel.getPub(post.publisherid,holder.user)

        holder.comments.setOnClickListener {
            val intent=Intent(context,CommentActivity::class.java)
            intent.putExtra("postid",post.postid)
            intent.putExtra("userid",post.publisherid)
            context.startActivity(intent)
        }
        holder.cmmt.setOnClickListener {
            val intent=Intent(context,CommentActivity::class.java)
            intent.putExtra("postid",post.postid)
            intent.putExtra("userid",post.publisherid)
            context.startActivity(intent)
        }
        viewmodel.noOfComment(post.postid!!,holder.comments)
        viewmodel.isLiked(post.postid,holder.like)
        viewmodel.isSaved(post.postid,holder.save)

        holder.like.setOnClickListener {
            if (holder.like.tag.equals("like")){
                viewmodel.addLike(post.postid)
            }else{
                viewmodel.removeLike(post.postid)
            }
        }
        viewmodel.noOfLikes(post.postid,holder.likes)

        holder.save.setOnClickListener {
            if (holder.save.tag.equals("save")){
                viewmodel.savePost(post.postid)
            }else{
                viewmodel.removePost(post.postid)
            }
        }
        holder.profile.setOnClickListener {
            val activity=it.context as AppCompatActivity
            val bundle=Bundle()
            bundle.putString("publisherid",post.publisherid)
            val fragment=ProfileFragment()
            fragment.arguments=bundle
            activity.supportFragmentManager.beginTransaction().replace(R.id.framelayout,fragment)
                .addToBackStack(null).commit()
        }
        holder.name.setOnClickListener {
            val activity=it.context as AppCompatActivity
            val bundle=Bundle()
            bundle.putString("publisherid",post.publisherid)
            val fragment=ProfileFragment()
            fragment.arguments=bundle
            activity.supportFragmentManager.beginTransaction().replace(R.id.framelayout,fragment)
                .addToBackStack(null).commit()
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val image:ImageView=itemView.findViewById(R.id.home_image)
        val user:TextView=itemView.findViewById(R.id.home_user)
        val description:TextView=itemView.findViewById(R.id.home_description)
        val comments:TextView=itemView.findViewById(R.id.comment_count)
        val profile:CircleImageView=itemView.findViewById(R.id.home_profile)
        val name:TextView=itemView.findViewById(R.id.home_name)
        val like:ImageView=itemView.findViewById(R.id.like_image)
        val cmmt:ImageView=itemView.findViewById(R.id.comment_image)
        val save:ImageView=itemView.findViewById(R.id.save_image)
        val likes:TextView=itemView.findViewById(R.id.home_likes)
    }
}