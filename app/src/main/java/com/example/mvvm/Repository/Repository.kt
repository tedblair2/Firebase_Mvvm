package com.example.mvvm.Repository

import android.app.Application
import android.app.ProgressDialog
import android.media.Image
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.mvvm.Model.Comment
import com.example.mvvm.Model.Posts
import com.example.mvvm.Model.Users
import com.example.mvvm.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class Repository(private val application: Application) {

    val mutableLiveData: MutableLiveData<FirebaseUser> = MutableLiveData<FirebaseUser>()
    private val authentication= Firebase.auth
    val loggedout:MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val database=Firebase.database.reference
    val storage=Firebase.storage
    val dialog:MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val posts:MutableLiveData<ArrayList<Posts>> = MutableLiveData<ArrayList<Posts>>()
    val publisher:MutableLiveData<Users> = MutableLiveData<Users>()
    val comments:MutableLiveData<ArrayList<Comment>> = MutableLiveData<ArrayList<Comment>>()
    val saved:MutableLiveData<ArrayList<Posts>> = MutableLiveData<ArrayList<Posts>>()
    val userPosts:MutableLiveData<ArrayList<Posts>> = MutableLiveData<ArrayList<Posts>>()
    val postCount:MutableLiveData<String> = MutableLiveData<String>()
    val followersCount:MutableLiveData<String> = MutableLiveData<String>()
    val followingCount:MutableLiveData<String> = MutableLiveData<String>()
    val postData:MutableLiveData<Posts> = MutableLiveData<Posts>()
    val users:MutableLiveData<ArrayList<Users>> = MutableLiveData<ArrayList<Users>>()

    init {
        if (authentication.currentUser!= null){
            mutableLiveData.postValue(authentication.currentUser)
        }
    }

    fun registerUser(email:String,password:String,name:String){
        dialog.postValue(true)
        authentication.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if (task.isSuccessful){
                mutableLiveData.postValue(authentication.currentUser)
                addUser(email,authentication.currentUser!!.uid,name)
                dialog.postValue(false)

            }else{
                Toast.makeText(application,task.exception!!.message,Toast.LENGTH_SHORT).show()
                dialog.postValue(false)
            }
        }
    }
    fun login(email: String,password: String){
        dialog.postValue(true)
        authentication.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
            if (task.isSuccessful){
                mutableLiveData.postValue(authentication.currentUser)
                dialog.postValue(false)
            }else{
                Toast.makeText(application,task.exception!!.message,Toast.LENGTH_SHORT).show()
                dialog.postValue(false)

            }
        }
    }
    fun signOut(){
        authentication.signOut()
        loggedout.postValue(true)
    }
    fun addUser(email: String,userid:String,name:String){
        val map= hashMapOf<String,Any>()
        map["email"]=email
        map["userid"]=userid
        map["name"]=name
        map["profile"]="null"
        database.child("Users").child(userid).setValue(map).addOnCompleteListener { task->
            if (task.isSuccessful){
                Toast.makeText(application,"Registration Successful",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(application,task.exception!!.message,Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun getUserData(userData:MutableLiveData<Users>,userid: String){
        database.child("Users").child(userid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Users::class.java)
                userData.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun addPost(description:String,uri: Uri){
        dialog.postValue(true)
        val ref=database.child("Posts")
        val postid=ref.push().key!!
        val publisherid=authentication.currentUser!!.uid

        val storageref=storage.getReference("Images/${Calendar.getInstance().timeInMillis}")

        storageref.putFile(uri).addOnSuccessListener {
            storageref.downloadUrl.addOnCompleteListener { task->
                if (task.isSuccessful){
                    val imagelink= task.result.toString()
                    val post=Posts(postid,publisherid,description,imagelink)

                    ref.child(postid).setValue(post).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(application,"Upload successful",Toast.LENGTH_SHORT).show()
                            dialog.postValue(false)
                        }else{
                            Toast.makeText(application,task.exception!!.message,Toast.LENGTH_SHORT).show()
                            dialog.postValue(false)
                        }
                    }
                }else{
                    Toast.makeText(application,task.exception!!.message,Toast.LENGTH_SHORT).show()
                    dialog.postValue(false)
                }
            }
        }
    }
    fun getPosts(list1: ArrayList<String>){
        database.child("Posts").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list= arrayListOf<Posts>()
                list.clear()
                for (post in snapshot.children){
                    val item=post.getValue(Posts::class.java)
                    for (id in list1){
                        if (item!!.publisherid == id)
                            list.add(item)
                    }
                }
                posts.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun getPublisher(publisherid:String,image:ImageView,name:TextView){
        database.child("Users").child(publisherid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Users::class.java)
                name.text=user!!.name

                if (user.profile.equals("null")){
                    image.setImageResource(R.drawable.profile)
                }else{
                    Picasso.get().load(user.profile).into(image)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun getPublisher1(publisherid:String,name:TextView){
        database.child("Users").child(publisherid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Users::class.java)
                name.text=user!!.name

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun addComment(comment:String,postid:String){
        dialog.postValue(true)
        val comments=database.child("Comments")
        val commentid=comments.push().key!!
        val publisherid=authentication.currentUser!!.uid

        val post=Comment(publisherid,comment,commentid,postid)

        comments.child(commentid).setValue(post).addOnCompleteListener { task->
            if (task.isSuccessful){
                Toast.makeText(application,"Comment added successfully",Toast.LENGTH_SHORT).show()
                dialog.postValue(false)
            }else{
                Toast.makeText(application,task.exception!!.message,Toast.LENGTH_SHORT).show()
                dialog.postValue(false)
            }
        }
    }
    fun getComments(postid:String){
        database.child("Comments").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list= arrayListOf<Comment>()
                list.clear()
                for (item in snapshot.children){
                    val comment=item.getValue(Comment::class.java)
                    if (comment!!.postid.equals(postid)){
                        list.add(comment)
                    }
                }
                comments.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun noOfComments(postid: String,txt:TextView){
        database.child("Comments").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var count=0
                for (item in snapshot.children){
                    val comment=item.getValue(Comment::class.java)
                    if (comment!!.postid.equals(postid)){
                        count +=1
                    }
                }
                txt.text="View $count comments"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun addLike(postid: String){
        database.child("Likes").child(postid).child(authentication.currentUser!!.uid).setValue(true)
    }

    fun removeLike(postid: String){
        database.child("Likes").child(postid).child(authentication.currentUser!!.uid).removeValue()
    }

    fun noOfLikes(postid: String,txt: TextView){
        database.child("Likes").child(postid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val numberOfLikes=snapshot.childrenCount.toString()
                txt.text="$numberOfLikes likes"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun isLiked(postid: String,like: ImageView){
        database.child("Likes").child(postid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(authentication.currentUser!!.uid).exists()){
                    like.tag="liked"
                    like.setImageResource(R.drawable.ic_liked)
                }else{
                    like.tag="like"
                    like.setImageResource(R.drawable.ic_like)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun savePost(postid: String){
        database.child("Saved").child(authentication.currentUser!!.uid).child(postid).setValue(true)
    }
    fun removePost(postid: String){
        database.child("Saved").child(authentication.currentUser!!.uid).child(postid).removeValue()
    }
    fun isSaved(postid: String,save:ImageView){
        database.child("Saved").child(authentication.currentUser!!.uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(postid).exists()){
                    save.tag="saved"
                    save.setImageResource(R.drawable.ic_saved)
                }else{
                    save.tag="save"
                    save.setImageResource(R.drawable.ic_save)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getSaved(userid: String){
        database.child("Saved").child(userid).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list= arrayListOf<String>()
                list.clear()
                for (child in snapshot.children){
                    list.add(child.key!!)
                }
                getData(list)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getData(list1: ArrayList<String>) {
        database.child("Posts").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list= arrayListOf<Posts>()
                list.clear()
                for (child in snapshot.children){
                    val post=child.getValue(Posts::class.java)
                    for (item in list1){
                        if (item == post!!.postid){
                            list.add(post)
                        }
                    }
                }
                saved.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    fun getUserPosts(userid:String){
        database.child("Posts").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list= arrayListOf<Posts>()
                list.clear()
                for(child in snapshot.children){
                    val post=child.getValue(Posts::class.java)
                    if (post!!.publisherid.equals(userid)){
                        list.add(post)
                    }
                }
                userPosts.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun noOfPosts(userid: String){
        database.child("Posts").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var count=0
                for (child in snapshot.children){
                    val post=child.getValue(Posts::class.java)
                    if (post!!.publisherid.equals(userid)){
                        count+=1
                    }
                }
                postCount.postValue(count.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun followUser(userid: String){
        database.child("Follows").child(authentication.currentUser!!.uid)
            .child("following").child(userid).setValue(true)
        database.child("Follows").child(userid).child("followers")
            .child(authentication.currentUser!!.uid).setValue(true)
    }
    fun unfollowUser(userid: String){
        database.child("Follows").child(authentication.currentUser!!.uid)
            .child("following").child(userid).removeValue()
        database.child("Follows").child(userid).child("followers")
            .child(authentication.currentUser!!.uid).removeValue()
    }
    fun isFollowing(userid: String,btn:Button){
        database.child("Follows").child(authentication.currentUser!!.uid)
            .child("following").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(userid == authentication.currentUser!!.uid){
                        btn.visibility=View.GONE
                    }else if (snapshot.child(userid).exists()){
                        btn.text="following"
                    }else{
                        btn.text="Follow"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun noOfFollwers(userid: String){
        database.child("Follows").child(userid)
            .child("followers").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count=snapshot.childrenCount.toString()

                    followersCount.postValue(count)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun noFollowing(userid: String){
        database.child("Follows").child(userid)
            .child("following").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count=snapshot.childrenCount.toString()

                    followingCount.postValue(count)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }

    fun getFollowers(){
        database.child("Follows").child(authentication.currentUser!!.uid)
            .child("following").addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list= arrayListOf<String>()
                    list.clear()
                    for (child in snapshot.children){
                        list.add(child.key!!)
                    }
                    getPosts(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
    fun postDetails(postid: String){
        database.child("Posts").child(postid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val post=snapshot.getValue(Posts::class.java)
                postData.postValue(post)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun publisherDetails(publisherid: String){
        database.child("Users").child(publisherid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user=snapshot.getValue(Users::class.java)
                publisher.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun uploadProfilePic(uri: Uri){
        dialog.postValue(true)
        val data=database.child("Users").child(authentication.currentUser!!.uid)
        val ref=storage.getReference("Profile/${Calendar.getInstance().timeInMillis}")
        ref.putFile(uri).addOnSuccessListener {
            ref.downloadUrl.addOnCompleteListener { task->
                if (task.isSuccessful){
                    val imagelink=task.result.toString()
                    val map= hashMapOf<String,Any>()
                    map["profile"]=imagelink
                    data.updateChildren(map)
                    dialog.postValue(false)
                }else{
                    Toast.makeText(application,task.exception!!.message,Toast.LENGTH_SHORT).show()
                    dialog.postValue(false)
                }
            }
        }
    }
    fun updateUser(name:String){
        val map= hashMapOf<String,Any>()
        map["name"]=name
        database.child("Users").child(authentication.currentUser!!.uid).updateChildren(map)
    }

    fun getUsers(){
        database.child("Users").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list= arrayListOf<Users>()
                list.clear()
                for (child in snapshot.children){
                    val user=child.getValue(Users::class.java)
                    if (!user!!.userid.equals(authentication.currentUser!!.uid)){
                        list.add(user)
                    }
                }
                users.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getFollowing(userid: String,users: MutableLiveData<ArrayList<Users>>){
        database.child("Follows").child(userid).child("following")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list= arrayListOf<String>()
                    for (child in snapshot.children){
                        list.add(child.key!!)
                    }
                    gettingUsers(list,users)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun userFollowers(userid: String,users: MutableLiveData<ArrayList<Users>>){
        database.child("Follows").child(userid).child("followers")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list= arrayListOf<String>()
                    list.clear()
                    for (child in snapshot.children){
                        list.add(child.key!!)
                    }
                    gettingUsers(list,users)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun gettingUsers(list1: ArrayList<String>, users:MutableLiveData<ArrayList<Users>>){
        database.child("Users").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list= arrayListOf<Users>()
                for(child in snapshot.children){
                    val user=child.getValue(Users::class.java)
                    for (id in list1){
                        if (user!!.userid.equals(id)){
                            list.add(user)
                        }
                    }
                }
                users.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}