package com.secrepixel.app.decryptmessage.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.secrepixel.app.R

class DecryptMessageTutorialDecryptingImage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_decrypt_message_tutorial_decrypting_image,
            container,
            false
        )
    }
}