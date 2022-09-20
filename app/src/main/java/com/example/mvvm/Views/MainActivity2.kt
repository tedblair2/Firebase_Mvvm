package com.example.mvvm.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm.R
import com.example.mvvm.ViewModel.ViewModelClass
import com.example.mvvm.databinding.ActivityMain2Binding
import com.google.android.gms.common.api.internal.LifecycleCallback.getFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        getFragment(HomeFragment())

        binding.bottomnav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> getFragment(HomeFragment())
                R.id.search ->getFragment(SearchFragment())
                R.id.add -> getFragment(AddFragment())
                R.id.profile ->{
                    val bundle=Bundle()
                    bundle.putString("publisherid",Firebase.auth.currentUser!!.uid)
                    val fragment=ProfileFragment()
                    fragment.arguments=bundle
                    getFragment(fragment)
                }
            }
            true
        }
    }

    private fun getFragment(fragment:Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.framelayout,fragment).commit()
    }

}