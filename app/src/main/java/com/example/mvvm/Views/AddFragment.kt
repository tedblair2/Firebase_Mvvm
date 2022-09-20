package com.example.mvvm.Views

import android.app.Activity.RESULT_OK
import android.app.Application
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm.R
import com.example.mvvm.ViewModel.ViewModelClass
import com.example.mvvm.databinding.FragmentAddBinding
import com.google.android.material.textfield.TextInputLayout

class AddFragment : Fragment() {
    lateinit var image:ImageView
    lateinit var description:TextInputLayout
    lateinit var post:TextView
    lateinit var imageuri:Uri
    lateinit var dialog:ProgressDialog

    val CODE=3

    val viewmodel by lazy {
        ViewModelProvider(requireActivity(),ViewModelProvider.AndroidViewModelFactory
            .getInstance(context?.applicationContext as Application))[ViewModelClass::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image=view.findViewById(R.id.image_select)
        description=view.findViewById(R.id.description)
        post=view.findViewById(R.id.post)
        dialog= ProgressDialog(context)
        dialog.setMessage("Uploading...")

        selectImage()

        viewmodel.dialog.observe(requireActivity(), Observer { status->
            if (status){
                dialog.show()
            }else{
                dialog.dismiss()
                description.editText?.text=null
                image.setImageResource(R.drawable.not_available)
            }
        })

        post.setOnClickListener {
            val description_txt=description.editText!!.text.toString()
            if (TextUtils.isEmpty(description_txt)){
                description.error="Provide a short description"
            }else{
                viewmodel.addPost(description_txt,imageuri)
            }
        }
    }
    private fun selectImage(){
        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent,CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE && resultCode == RESULT_OK && data != null){
            imageuri= data.data!!
            image.setImageURI(imageuri)
        }
    }
}