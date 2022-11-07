package com.example.imageencryptor.mainmenu

import com.example.imageencryptor.keyinfo.Key

interface OnDeselectKeyListener {

    /**
     * key: the new key that was selected
     */
    fun onDeselectKey(key: Key?);
}
