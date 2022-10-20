package com.example.imageencryptor.mainmenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.imageencryptor.R
import com.example.imageencryptor.keyinfo.Key
import timber.log.Timber

class KeyRecycleViewAdapter(var onSelectKeyListener: OnSelectKeyListener) : RecyclerView.Adapter<KeyRecycleViewAdapter.ViewHolder>() {
    var data = listOf<Key>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createViewHolder(parent, viewType, onSelectKeyListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindKey(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(view: View, var onSelectKeyListener: OnSelectKeyListener): RecyclerView.ViewHolder(view){
        private lateinit var key: Key;
        private val textView: TextView = view.findViewById(R.id.key_name_text_view)
        private val encryptButton: Button = view.findViewById(R.id.encrypt_button_key_list_item)
        private val decryptButton: Button = view.findViewById(R.id.decrypt_button_key_list_item)
        private val layout: ConstraintLayout = view.findViewById(R.id.constraint_layout_key_list_item)

        fun bindKey(key: Key){
            this.key = key
            textView.setText(key.name)
            layout.setOnClickListener(){
                onSelectKeyListener.onSelectKey(key)
            }
            encryptButton.setOnClickListener(){
                Timber.i("Encrypt button pressed with key "+key.name)
                //navigate to the WriteMessage Fragment
                //pass the key that it will use for encryption
                Navigation.findNavController(this.itemView).navigate(MainMenuFragmentDirections.actionMainMenuFragmentToWriteMessageFragment(this.key))
            }
            decryptButton.setOnClickListener(){
                Timber.i("Decrypt button pressed with key "+key.name)
                //navigate to the DecryptMessage fragment
                //and pass the key to it
                Navigation.findNavController(this.itemView).navigate(MainMenuFragmentDirections.actionMainMenuFragmentToDecryptMessageFragment(this.key))
            }
        }

        companion object{
            fun createViewHolder(parent: ViewGroup, viewType: Int, onSelectKeyListener: OnSelectKeyListener): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.key_list_item, parent, false)
                return ViewHolder(view, onSelectKeyListener)
            }
        }
    }
}