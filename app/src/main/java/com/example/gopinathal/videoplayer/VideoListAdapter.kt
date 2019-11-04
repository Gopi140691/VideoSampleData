package com.example.gopinathal.videoplayer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class VideoListAdapter(val arrlist: ArrayList<String>): RecyclerView.Adapter<VideoListAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v);
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val txtTitle = itemView.findViewById<TextView>(R.id.txtTitle)

    }
    override fun getItemCount(): Int {
        return arrlist.size
    }
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.txtName?.text = arrlist[position]
       // holder?.txtTitle?.text = arrlist[position]

    }

    interface clickEvent{
        fun eventclick(string: String)
    }
}