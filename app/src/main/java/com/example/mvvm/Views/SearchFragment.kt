package com.example.mvvm.Views

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.Adapter.UserAdapter
import com.example.mvvm.R
import com.example.mvvm.ViewModel.ViewModelClass

class SearchFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var userAdapter: UserAdapter

    val factory by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(context?.applicationContext as Application)
    }
    val viewmodel by lazy {
        ViewModelProvider(requireActivity(),factory)[ViewModelClass::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView=view.findViewById(R.id.recycler_users)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=LinearLayoutManager(context)

        viewmodel.getUsers()
        viewmodel.users.observe(requireActivity(), Observer { users->
            userAdapter=UserAdapter(users,viewmodel)
            recyclerView.adapter=userAdapter
        })
    }
}