package com.example.imageencryptor.mainmenu

import com.example.imageencryptor.keyinfo.Key

interface OnSelectKeyListener {

    fun onSelectKey(key: Key)
    fun addOnDeselectKeyListener(onDeselectKeyListener: OnDeselectKeyListener)
}