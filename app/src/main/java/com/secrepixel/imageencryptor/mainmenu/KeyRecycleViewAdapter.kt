package com.secrepixel.imageencryptor.mainmenu

import android.app.Application
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.secrepixel.imageencryptor.R
import com.secrepixel.imageencryptor.keyinfo.Key
import timber.log.Timber

/**
 * adapter for the main menu key recycle view
 */
class KeyRecycleViewAdapter(var onSelectKeyListener: OnSelectKeyListener) : RecyclerView.Adapter<KeyRecycleViewAdapter.ViewHolder>() {

    //list of keys to be displayed
    var data = listOf<Key>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    /**
     * creates a new ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createViewHolder(parent, viewType, onSelectKeyListener)
    }

    /**
     * assigns a key to an existing ViewHolder
     * based on scroll position
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindKey(data[position])
    }

    /**
     * returns the number of keys
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * custom view holder class used
     */
    class ViewHolder(view: View, var onSelectKeyListener: OnSelectKeyListener): RecyclerView.ViewHolder(view), OnDeselectKeyListener{
        private lateinit var key: Key;
        private val textView: TextView = view.findViewById(R.id.key_name_text_view)
        private val encryptButton: Button = view.findViewById(R.id.encrypt_button_key_list_item)
        private val decryptButton: Button = view.findViewById(R.id.decrypt_button_key_list_item)
        private val layout: ConstraintLayout = view.findViewById(R.id.constraint_layout_key_list_item)

        /**
         * binds a key to the view holder
         */
        fun bindKey(key: Key){
            this.key = key
            textView.setText(key.name)


            layout.setOnClickListener(){
                onSelectKeyListener.onSelectKey(key)
            }
            encryptButton.setOnClickListener(){
                //navigate to the WriteMessageFragment
                //pass the key that it will use for encryption
                Navigation.findNavController(this.itemView).navigate(MainMenuFragmentDirections.actionMainMenuFragmentToWriteMessageFragment(this.key))
            }
            //check if the key has a private exponent
            if(key.privateExponent!=null){
                decryptButton.setOnClickListener(){
                    //navigate to the DecryptMessageFragment
                    //and pass the key to it
                    Navigation.findNavController(this.itemView).navigate(MainMenuFragmentDirections.actionMainMenuFragmentToDecryptMessageFragment(this.key))
                }
            }else{
                //disable the button
                decryptButton.isEnabled = false
                decryptButton.isClickable = false
                decryptButton.setTextColor(ContextCompat.getColor(decryptButton.context, R.color.disabledTextColor))
            }

        }

        companion object{
            /**
             * creates a new ViewHolder
             */
            fun createViewHolder(parent: ViewGroup, viewType: Int, onSelectKeyListener: OnSelectKeyListener): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.key_list_item, parent, false)
                val viewHolder = ViewHolder(view, onSelectKeyListener)
                onSelectKeyListener.addOnDeselectKeyListener(viewHolder)
                return viewHolder
            }
        }

        /**
         * if the view holder currently holds this key, it will change its color so its no longer selected
         */
        override fun onDeselectKey(key: Key?) {
            if(this.key==key){
                markSelected()
            }else{
                markDeselected()
            }
        }

        private fun markSelected(){
            layout.setBackgroundColor(ContextCompat.getColor(layout.context.applicationContext, R.color.secondaryColor))
            textView.setTextColor(ContextCompat.getColor(layout.context.applicationContext, R.color.secondaryTextColor))
        }
        private fun markDeselected(){
            layout.setBackgroundColor(Color.TRANSPARENT)
            textView.setTextColor(ContextCompat.getColor(layout.context.applicationContext, R.color.primaryTextColor))
        }
    }
}