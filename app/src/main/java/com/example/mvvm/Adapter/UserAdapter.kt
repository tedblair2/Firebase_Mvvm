package com.example.mvvm.Adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.Model.Users
import com.example.mvvm.R
import com.example.mvvm.ViewModel.ViewModelClass
import com.example.mvvm.Views.ProfileFragment
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(private val userlist:ArrayList<Users>,
                  private val viewmodel:ViewModelClass):RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.user_items,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user=userlist[position]
        holder.name.text=user.name
        if (user.profile.equals("null")){
            holder.image.setImageResource(R.drawable.profile)
        }else{
            Picasso.get().load(user.profile).into(holder.image)
        }
        viewmodel.isFollowing(user.userid!!,holder.btn)
        holder.btn.setOnClickListener {
            if (holder.btn.text.equals("Follow")){
                viewmodel.followUser(user.userid)
            }else{
                viewmodel.unFollowUser(user.userid)
            }
        }
        holder.image.setOnClickListener {
            val activity=it.context as AppCompatActivity
            val bundle=Bundle()
            bundle.putString("publisherid",user.userid)
            val fragment=ProfileFragment()
            fragment.arguments=bundle
            activity.supportFragmentManager.beginTransaction().replace(R.id.framelayout,fragment)
                .addToBackStack(null).commit()
        }
        holder.name.setOnClickListener {
            val activity=it.context as AppCompatActivity
            val bundle=Bundle()
            bundle.putString("publisherid",user.userid)
            val fragment=ProfileFragment()
            fragment.arguments=bundle
            activity.supportFragmentManager.beginTransaction().replace(R.id.framelayout,fragment)
                .addToBackStack(null).commit()
        }
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val image:CircleImageView=itemView.findViewById(R.id.user_profile)
        val name:TextView=itemView.findViewById(R.id.username)
        val btn:Button=itemView.findViewById(R.id.btn_follow)
    }
}