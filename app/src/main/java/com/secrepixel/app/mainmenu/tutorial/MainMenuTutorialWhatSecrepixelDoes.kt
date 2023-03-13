package com.secrepixel.app.mainmenu.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.secrepixel.app.databinding.FragmentMainMenuTutorialWhatSecrepixelDoesBinding
import timber.log.Timber


class MainMenuTutorialWhatSecrepixelDoes : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("here!!!")
        val binding = FragmentMainMenuTutorialWhatSecrepixelDoesBinding.inflate(inflater)
        return binding.root
    }
}