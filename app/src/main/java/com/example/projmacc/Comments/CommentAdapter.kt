package com.example.projmacc.Comments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.projmacc.R

class CommentAdapter (var mCtx: Context, var resources:Int, var items:List<CommentsModel>):
    ArrayAdapter<CommentsModel>(
    mCtx,resources,items
){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View =layoutInflater.inflate(resources,null)

        val email: TextView =view.findViewById(R.id.personEmail)
        val comment: TextView = view.findViewById(R.id.comment)

        var mItem:CommentsModel = items[position]
        comment.text = mItem.comment
        email.text = mItem.email

        return view
    }
}