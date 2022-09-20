package com.example.mvvm.ViewModel

import android.app.Application
import android.net.Uri
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm.Model.Comment
import com.example.mvvm.Model.Posts
import com.example.mvvm.Model.Users
import com.example.mvvm.Repository.Repository
import com.google.firebase.auth.FirebaseUser
import org.w3c.dom.Text

class ViewModelClass(application: Application): AndroidViewModel(application) {
    val repository:Repository= Repository(application)
    val mutableLiveData:MutableLiveData<FirebaseUser> = repository.mutableLiveData
    val loggedout:MutableLiveData<Boolean> = repository.loggedout
    val dialog:MutableLiveData<Boolean> = repository.dialog
    val userData = MutableLiveData<Users>()
    val liveData: LiveData<Users> = userData
    val posts:MutableLiveData<ArrayList<Posts>> = repository.posts
    val livePosts:LiveData<ArrayList<Posts>> = posts
    val comments:MutableLiveData<ArrayList<Comment>> = repository.comments
    val liveComments:LiveData<ArrayList<Comment>> = comments
    val savedPosts: MutableLiveData<ArrayList<Posts>> = repository.saved
    val savedLiveData: LiveData<ArrayList<Posts>> = savedPosts
    val userPosts:MutableLiveData<ArrayList<Posts>> = repository.userPosts
    val userLiveData:LiveData<ArrayList<Posts>> = userPosts
    val postCount:MutableLiveData<String> = repository.postCount
    val followersCount:MutableLiveData<String> =repository.followersCount
    val followingCount:MutableLiveData<String> = repository.followingCount
    val userDetails:MutableLiveData<Users> = repository.publisher
    val postDetails:MutableLiveData<Posts> =repository.postData
    val users:MutableLiveData<ArrayList<Users>> = repository.users
    val userFollowers= MutableLiveData<ArrayList<Users>>()
    val userFollowing= MutableLiveData<ArrayList<Users>>()

    fun registerUser(email:String,password:String,name:String){
        repository.registerUser(email,password,name)
    }
    fun loggin(email: String,password: String){
        repository.login(email, password)
    }
    fun signout(){
        repository.signOut()
    }
    fun getUserData(userid: String){
        repository.getUserData(userData,userid)
    }
    fun addPost(description:String,uri: Uri){
        repository.addPost(description,uri)
    }
    fun getPosts(){
        repository.getFollowers()
    }
    fun getPublisher(publisherid:String,image:ImageView,text:TextView){
        repository.getPublisher(publisherid,image,text)
    }
    fun getPub(publisherid: String,text: TextView){
        repository.getPublisher1(publisherid,text)
    }
    fun addComment(comment:String,postid:String){
        repository.addComment(comment,postid)
    }
    fun getComments(postid:String){
        repository.getComments(postid)
    }
    fun addLike(postid: String){
        repository.addLike(postid)
    }
    fun removeLike(postid: String){
        repository.removeLike(postid)
    }
    fun noOfLikes(postid: String,txt: TextView){
        repository.noOfLikes(postid,txt)
    }
    fun isLiked(postid: String,like:ImageView){
        repository.isLiked(postid,like)
    }
    fun noOfComment(postid: String,txt:TextView){
        repository.noOfComments(postid,txt)
    }
    fun savePost(postid: String){
        repository.savePost(postid)
    }
    fun removePost(postid: String){
        repository.removePost(postid)
    }
    fun isSaved(postid: String,save:ImageView){
        repository.isSaved(postid,save)
    }
    fun getSaved(userid: String){
        repository.getSaved(userid)
    }
    fun userPosts(userid: String){
        repository.getUserPosts(userid)
    }
    fun noOfPosts(userid:String){
        repository.noOfPosts(userid)
    }
    fun followUser(userid: String){
        repository.followUser(userid)
    }
    fun unFollowUser(userid: String){
        repository.unfollowUser(userid)
    }
    fun isFollowing(userid: String,btn:Button){
        repository.isFollowing(userid,btn)
    }
    fun noOfFollowers(userid: String){
        repository.noOfFollwers(userid)
    }
    fun noFollowing(userid: String){
        repository.noFollowing(userid)
    }
    fun getPostDetails(postid: String){
        repository.postDetails(postid)
    }
    fun getPublisherDetails(publisherid: String){
        repository.publisherDetails(publisherid)
    }
    fun updateProfilePic(uri: Uri){
        repository.uploadProfilePic(uri)
    }
    fun updateUserData(name: String){
        repository.updateUser(name)
    }
    fun getUsers(){
        repository.getUsers()
    }
    fun getFollowers(userid: String){
        repository.userFollowers(userid,userFollowers)
    }
    fun getFollowing(userid: String){
        repository.getFollowing(userid,userFollowing)
    }
}