package com.example.mvvm.Views

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm.R
import com.example.mvvm.ViewModel.ViewModelClass
import com.example.mvvm.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var dialog:ProgressDialog

    val viewmodel:ViewModelClass by lazy {
        ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory
        .getInstance(this@MainActivity.application!!))[ViewModelClass::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialog= ProgressDialog(this)
        dialog.setMessage("Loading...")

        viewmodel.dialog.observe(this, Observer { show->
            if (show){
                dialog.show()
            }else{
                dialog.dismiss()
            }
        })

        viewmodel.mutableLiveData.observe(this, Observer { user->
            if (user != null){
                startActivity(Intent(this,MainActivity2::class.java))
                finish()
            }
        })
        binding.registerTxt.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        binding.loginBtn.setOnClickListener {
            val email_txt=binding.email.editText!!.text.toString()
            val password_txt=binding.password.editText!!.text.toString()

            if (TextUtils.isEmpty(email_txt)){
                binding.email.error="Please provide an email"
            }else if(TextUtils.isEmpty(password_txt)){
                binding.password.error="Please provide your password"
            }else{
                viewmodel.loggin(email_txt,password_txt)
            }
        }
    }
}