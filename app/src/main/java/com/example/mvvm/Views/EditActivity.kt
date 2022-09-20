package com.example.mvvm.Views

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm.R
import com.example.mvvm.ViewModel.ViewModelClass
import com.example.mvvm.databinding.ActivityEditBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class EditActivity : AppCompatActivity() {

    lateinit var binding:ActivityEditBinding
    val CODE=2
    lateinit var dialog:ProgressDialog

    val factory by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this@EditActivity.application)
    }
    val viewmodel by lazy {
        ViewModelProvider(this,factory)[ViewModelClass::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog= ProgressDialog(this)
        dialog.setMessage("Uploading...")

        setSupportActionBar(binding.toolbarEdit)
        binding.toolbarEdit.navigationIcon=ContextCompat.getDrawable(this,R.drawable.ic_back)
        binding.toolbarEdit.setNavigationOnClickListener {
            onBackPressed()
        }

        viewmodel.getUserData(Firebase.auth.currentUser!!.uid)

        viewmodel.dialog.observe(this, Observer { status->
            if (status){
                dialog.show()
            }else{
                dialog.dismiss()
            }
        })

        viewmodel.loggedout.observe(this, Observer { status->
            if (status){
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        })
        binding.btnLogout.setOnClickListener {
            viewmodel.signout()
        }

        viewmodel.liveData.observe(this, Observer { user->
            if (user.profile.equals("null")){
                binding.editProfile.setImageResource(R.drawable.profile)
            }else{
                Picasso.get().load(user.profile).into(binding.editProfile)
            }
            binding.editName.setText(user.name)
            binding.emailEdit.text=user.email
        })
        binding.editProfile.setOnClickListener {
            selectImage()
        }
        binding.saveEdit.setOnClickListener {
            val name=binding.editName.text.toString()
            if (TextUtils.isEmpty(name)){
                binding.edit.error="Field cannot be empty"
            }else{
                viewmodel.updateUserData(name)
            }
        }
    }

    fun selectImage(){
        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent,CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE && resultCode== RESULT_OK && data !=null){
            val uri=data.data!!
            viewmodel.updateProfilePic(uri)
        }
    }
}