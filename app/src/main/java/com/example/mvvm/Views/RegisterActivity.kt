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
import com.example.mvvm.databinding.ActivityRegisterBinding
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    lateinit var dialog:ProgressDialog
    lateinit var binding: ActivityRegisterBinding

    val factory by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this@RegisterActivity.application!!)
    }
    val viewModel:ViewModelClass by lazy {
        ViewModelProvider(this,factory)[ViewModelClass::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog= ProgressDialog(this)
        dialog.setMessage("Loading...")

        viewModel.mutableLiveData.observe(this, Observer { user->
            if (user != null){
                startActivity(Intent(this,MainActivity2::class.java))
                finish()
            }
        })
        viewModel.dialog.observe(this, Observer { show->
            if (show){
                dialog.show()
            }else{
                dialog.dismiss()
            }

        })

        binding.loginTxt.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        binding.regBtn.setOnClickListener {
            val email_txt = binding.emailReg.editText!!.text.toString()
            val password_txt=binding.passwordReg.editText!!.text.toString()
            val name=binding.nameReg.editText!!.text.toString()

            if (TextUtils.isEmpty(email_txt)){
                binding.emailReg.error="Please provide your email"
            }else if (TextUtils.isEmpty(password_txt)){
                binding.passwordReg.error="Please provide a password"
            }else if(password_txt.length < 8){
                binding.passwordReg.error="Password has to have at least 8 characters"
            }else if(TextUtils.isEmpty(name)){
                binding.nameReg.error="Please provide your name"
            }else{
                viewModel.registerUser(email_txt,password_txt,name)
            }
        }
    }
}