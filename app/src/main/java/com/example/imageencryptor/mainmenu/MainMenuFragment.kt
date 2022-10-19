package com.example.imageencryptor.mainmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.imageencryptor.R
import com.example.imageencryptor.databinding.FragmentMainMenuBinding
import com.example.imageencryptor.keyinfo.Key


/**
 * This is the start fragment from which the user selects, or creates, a key
 */
class MainMenuFragment : Fragment(), OnSelectKeyListener{

    private lateinit var viewModel: MainMenuViewModel
    private lateinit var selectedKeyName: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //inflating the binding
        val binding: FragmentMainMenuBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)
        binding.setLifecycleOwner(this)

        //get the information from the binding
        selectedKeyName = binding.selectedKeyName
        //get the view model
        viewModel = ViewModelProvider(this).get(MainMenuViewModel::class.java)

        //set the recycle view adapter for the keys
        val keyRecycleViewAdapter = KeyRecycleViewAdapter(this)
        binding.keysListRecycleView.adapter = keyRecycleViewAdapter

        viewModel.keys.observe(viewLifecycleOwner, Observer {
            it?.let{
            keyRecycleViewAdapter.data = viewModel.keys.value!!
        }
        })

        //adding click listeners
        binding.addKeyFloatingButton.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.action_mainMenuFragment_to_addKeyFragment)
        }

        // return the binding root
        return binding.root
    }

    override fun onSelectKey(key: Key){
        viewModel.selectedKey = key
        selectedKeyName.text = key.name
    }
}