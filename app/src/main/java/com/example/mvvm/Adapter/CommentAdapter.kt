package com.example.mvvm.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.Model.Comment
import com.example.mvvm.R
import com.example.mvvm.ViewModel.ViewModelClass
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text

class CommentAdapter(private val commentlist:ArrayList<Comment>
    , private val viewmodel:ViewModelClass):RecyclerView.Adapter<CommentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.comment_items,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment=commentlist[position]

        holder.txt.text=comment.comment

        viewmodel.getPublisher(comment.publisherid!!,holder.image,holder.publisher)
    }

    override fun getItemCount(): Int {
        return commentlist.size
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val image:CircleImageView=itemView.findViewById(R.id.comment_pic)
        val publisher:TextView=itemView.findViewById(R.id.comment_publisher)
        val txt:TextView=itemView.findViewById(R.id.comment)

    }
}