package com.secrepixel.app.mainmenu.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.secrepixel.app.databinding.FragmentMainMenuTutorialWelcomeToSecrepixelBinding

class MainMenuTutorialWelcomeToSecrepixelFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainMenuTutorialWelcomeToSecrepixelBinding.inflate(inflater)
        return binding.root
    }
}