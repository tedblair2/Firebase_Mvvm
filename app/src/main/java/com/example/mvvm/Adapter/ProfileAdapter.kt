package com.example.mvvm.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.Model.Posts
import com.example.mvvm.R
import com.example.mvvm.ViewModel.ViewModelClass
import com.example.mvvm.Views.DetailsActivity
import com.squareup.picasso.Picasso

class ProfileAdapter(
    private val postitems:ArrayList<Posts>,
    private val context: Context):RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.profile_items,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post=postitems[position]

        Picasso.get().load(post.imagelink).placeholder(R.drawable.not_available).into(holder.image)

        holder.image.setOnClickListener {
            val intent=Intent(context,DetailsActivity::class.java)
            intent.putExtra("postid",post.postid)
            intent.putExtra("userid",post.publisherid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return postitems.size
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val image:ImageView=itemView.findViewById(R.id.profile_item)

    }
}