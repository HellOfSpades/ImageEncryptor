package com.secrepixel.app.succesfullycreatedimage.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.secrepixel.app.R


class SuccessfullyCreatedImageTutorialCongratulations : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_successfully_created_image_tutorial_congratulations,
            container,
            false
        )
    }
}