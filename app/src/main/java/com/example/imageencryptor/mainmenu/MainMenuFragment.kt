package com.example.imageencryptor.mainmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.imageencryptor.R
import com.example.imageencryptor.databinding.FragmentMainMenuBinding
import com.example.imageencryptor.keyinfo.KeyDatabase
import com.example.imageencryptor.keyinfo.KeyRecycleViewAdapter
import com.example.imageencryptor.writemessage.WriteMessageViewModel
import kotlinx.coroutines.Dispatchers


/**
 * This is the start fragment from which the user selects, or creates, a key
 */
class MainMenuFragment : Fragment() {

    private lateinit var viewModel: MainMenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //inflating the binding
        val binding: FragmentMainMenuBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)
        binding.setLifecycleOwner(this)
        //get the database
        var keyDatabase = KeyDatabase.getInstance(this.requireContext()).keyDatabaseDao
        //getting the application
        val application = this.requireActivity().application
        //create the view model factory
        val viewModelFactory = MainMenuViewModelFactory(application, keyDatabase)
        //get the view model
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainMenuViewModel::class.java)

        //set the recycle view adapter for the keys
        binding.keysListRecycleView.adapter = viewModel.keyRecycleViewAdapter

        viewModel.keys.observe(viewLifecycleOwner, Observer {
            it?.let{
            viewModel.keyRecycleViewAdapter.data = viewModel.keys.value!!
        }
        })

        //adding click listeners
        binding.addKeyFloatingButton.setOnClickListener(){
            viewModel.generateAndInsertNewKeyToDatabase("Vasya")
        }

        // return the binding root
        return binding.root
    }

}