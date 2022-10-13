package com.example.imageencryptor.keykcreation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.imageencryptor.R
import com.example.imageencryptor.databinding.FragmentAddKeyBinding
import com.example.imageencryptor.mainmenu.MainMenuViewModel


class AddKeyFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAddKeyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_key, container, false)
        val viewModel = ViewModelProvider(this).get(AddKeyViewModel::class.java)

        //setting on click listeners
        binding.generateNewKeyButton.setOnClickListener(){
            viewModel.generateAndInsertNewKeyToDatabase(binding.addedKeyNameEditText.text.toString())
            Navigation.findNavController(it).navigate(R.id.action_addKeyFragment_to_mainMenuFragment)
        }
        // Inflate the layout for this fragment
        return binding.root
    }
}