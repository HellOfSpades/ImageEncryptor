package com.example.imageencryptor.encryption

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imageencryptor.R

class KeyAdapter : RecyclerView.Adapter<KeyAdapter.ViewHolder>() {

    var keys = listOf<Key>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return ViewHolder(layoutInflater.inflate(R.layout.key_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = "key"
    }

    override fun getItemCount(): Int {
        return keys.size
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val textView = view.findViewById<TextView>(R.id.tempTextView)

    }

}