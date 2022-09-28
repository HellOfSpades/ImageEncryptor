package com.example.imageencryptor.encryption

import android.app.PendingIntent.getActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.imageencryptor.R
import com.example.imageencryptor.mainmenu.MainMenuFragmentDirections
import timber.log.Timber

class KeyRecycleViewAdapter() : RecyclerView.Adapter<KeyRecycleViewAdapter.ViewHolder>() {
    var data = listOf<Key>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindKey(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var key: Key;
        private val textView: TextView = view.findViewById(R.id.key_name_text_view)
        private val encryptButton: Button = view.findViewById(R.id.encrypt_button_key_list_item)
        private val decryptButton: Button = view.findViewById(R.id.decrypt_button_key_list_item)

        fun bindKey(key: Key){
            this.key = key
            textView.setText(key.name)
            encryptButton.setOnClickListener(){
                Timber.i("Encrypt button pressed with key "+key.name)
                Navigation.findNavController(this.itemView).navigate(MainMenuFragmentDirections.actionMainMenuFragmentToWriteMessageFragment(this.key))
            }
            decryptButton.setOnClickListener(){
                Timber.i("Decrypt button pressed with key "+key.name)
            }
        }

        companion object{
            fun createViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.key_list_item, parent, false)
                return ViewHolder(view)
            }
        }
    }
}