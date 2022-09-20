package com.example.mvvm.Views

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.Adapter.ProfileAdapter
import com.example.mvvm.R
import com.example.mvvm.ViewModel.ViewModelClass
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {
    lateinit var userposts:RecyclerView
    lateinit var savedposts:RecyclerView
    lateinit var profile:CircleImageView
    lateinit var posts:ImageView
    lateinit var saved:ImageView
    lateinit var postsCount:TextView
    lateinit var followers:TextView
    lateinit var following:TextView
    lateinit var name:TextView
    lateinit var btn:Button
    lateinit var profileAdapter: ProfileAdapter

    val factory by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(context?.applicationContext as Application)
    }
    val viewmodel by lazy {
        ViewModelProvider(requireActivity(),factory)[ViewModelClass::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userid= arguments?.getString("publisherid")!!

        userposts=view.findViewById(R.id.posts)
        savedposts=view.findViewById(R.id.saved)
        profile=view.findViewById(R.id.profile_image)
        posts=view.findViewById(R.id.profile_posts)
        saved=view.findViewById(R.id.profile_saved)
        postsCount=view.findViewById(R.id.noOfPosts)
        followers=view.findViewById(R.id.followers)
        following=view.findViewById(R.id.following)
        name=view.findViewById(R.id.profile_name)
        btn=view.findViewById(R.id.btn_edit)

        following.setOnClickListener {
            val intent=Intent(context,FollowingActivity::class.java)
            intent.putExtra("userid",userid)
            startActivity(intent)
        }
        followers.setOnClickListener {
            val intent=Intent(context,FollowersActivity::class.java)
            intent.putExtra("userid",userid)
            startActivity(intent)
        }

        if (userid== Firebase.auth.currentUser!!.uid){
            btn.text="Edit Profile"
        }else{
            viewmodel.isFollowing(userid,btn)
        }

        btn.setOnClickListener {
            if (btn.text.equals("Edit Profile")){
                startActivity(Intent(context,EditActivity::class.java))
            }else if (btn.text.equals("Follow")){
                viewmodel.followUser(userid)
            }else{
                viewmodel.unFollowUser(userid)
            }
        }

        posts.setOnClickListener {
            userposts.visibility=View.VISIBLE
            savedposts.visibility=View.GONE
        }
        saved.setOnClickListener {
            userposts.visibility=View.GONE
            savedposts.visibility=View.VISIBLE
        }

        userposts.setHasFixedSize(true)
        userposts.layoutManager=GridLayoutManager(context,3)
        viewmodel.userPosts(userid)
        viewmodel.userLiveData.observe(requireActivity(), Observer { posts->
            profileAdapter= ProfileAdapter(posts,requireContext())
            userposts.adapter=profileAdapter
        })

        savedposts.setHasFixedSize(true)
        savedposts.layoutManager=GridLayoutManager(context,3)
        viewmodel.getSaved(userid)
        viewmodel.savedLiveData.observe(requireActivity(), Observer { list->
            profileAdapter= ProfileAdapter(list,requireContext())
            savedposts.adapter=profileAdapter
        })

        viewmodel.getUserData(userid)
        viewmodel.liveData.observe(requireActivity(), Observer { user->
            name.text=user.name
            if (user.profile.equals("null")){
                profile.setImageResource(R.drawable.profile)
            }else{
                Picasso.get().load(user.profile).into(profile)
            }
        })

        viewmodel.noOfPosts(userid)
        viewmodel.postCount.observe(requireActivity(), Observer { count->
            postsCount.text=count
        })

        viewmodel.noOfFollowers(userid)
        viewmodel.followersCount.observe(requireActivity(), Observer { count->
            followers.text=count
        })
        viewmodel.noFollowing(userid)
        viewmodel.followingCount.observe(requireActivity(), Observer { count->
            following.text=count
        })

    }


}