package com.secrepixel.app.mainmenu

import com.secrepixel.app.keyinfo.Key

interface OnDeselectKeyListener {

    /**
     * key: the new key that was selected
     */
    fun onDeselectKey(key: Key?);
}
