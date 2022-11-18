package com.secrepixel.imageencryptor.mainmenu

import com.secrepixel.imageencryptor.keyinfo.Key

interface OnSelectKeyListener {

    fun onSelectKey(key: Key)
    fun addOnDeselectKeyListener(onDeselectKeyListener: OnDeselectKeyListener)
}