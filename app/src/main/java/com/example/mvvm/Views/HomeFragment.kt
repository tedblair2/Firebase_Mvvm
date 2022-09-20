package com.example.mvvm.Views

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.Adapter.HomeAdapter
import com.example.mvvm.R
import com.example.mvvm.ViewModel.ViewModelClass
import com.example.mvvm.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var items:RecyclerView
    lateinit var adapter: HomeAdapter


    val viewModel by lazy {
        ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory
            .getInstance(context?.applicationContext as Application))[ViewModelClass::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        items=view.findViewById(R.id.recycler_home)
        items.setHasFixedSize(true)
        items.layoutManager=LinearLayoutManager(context)

        viewModel.loggedout.observe(requireActivity(), Observer { status->
            if (status){
                val intent=Intent(context,MainActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        })
        viewModel.getPosts()
        viewModel.livePosts.observe(requireActivity(), Observer { posts->
            adapter=HomeAdapter(posts, requireContext(),viewModel)
            items.adapter=adapter
        })
    }
}