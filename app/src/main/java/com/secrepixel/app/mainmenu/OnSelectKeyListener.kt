package com.secrepixel.app.mainmenu

import com.secrepixel.app.keyinfo.Key

interface OnSelectKeyListener {

    fun onSelectKey(key: Key)
    fun addOnDeselectKeyListener(onDeselectKeyListener: OnDeselectKeyListener)
}