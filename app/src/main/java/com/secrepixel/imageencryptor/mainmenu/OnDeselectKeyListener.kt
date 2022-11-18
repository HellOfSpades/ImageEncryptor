package com.secrepixel.imageencryptor.mainmenu

import com.secrepixel.imageencryptor.keyinfo.Key

interface OnDeselectKeyListener {

    /**
     * key: the new key that was selected
     */
    fun onDeselectKey(key: Key?);
}
