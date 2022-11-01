package com.example.imageencryptor.mainmenu

import android.R.attr.button
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.imageencryptor.MainActivity
import com.example.imageencryptor.R
import com.example.imageencryptor.databinding.FragmentMainMenuBinding
import com.example.imageencryptor.keyinfo.Key


/**
 * This is the start fragment from which the user selects his key
 * or navigates to other operations
 */
class MainMenuFragment : Fragment(), OnSelectKeyListener {

    private lateinit var viewModel: MainMenuViewModel
    private lateinit var selectedKeyName: TextView
    private var onDeselectKeyListeners = ArrayList<OnDeselectKeyListener>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //inflating the binding
        val binding: FragmentMainMenuBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)
        binding.setLifecycleOwner(this)

        //get the information from the binding
        selectedKeyName = binding.selectedKeyName
        //get the view model
        viewModel = ViewModelProvider(this).get(MainMenuViewModel::class.java)

        //set the recycle view adapter for the keys
        val keyRecycleViewAdapter = KeyRecycleViewAdapter(this)
        binding.keysListRecycleView.adapter = keyRecycleViewAdapter

        viewModel.keys.observe(viewLifecycleOwner, Observer {
            it?.let {
                keyRecycleViewAdapter.data = viewModel.keys.value!!
            }
        })

        //adding click listeners
        binding.addKeyFloatingButton.setOnClickListener() {
            Navigation.findNavController(it)
                .navigate(R.id.action_mainMenuFragment_to_addKeyFragment)
        }
        binding.keyDetailsButton.setOnClickListener() {
            val popupMenu = PopupMenu(this.requireContext(), it)
            popupMenu.getMenuInflater().inflate(R.menu.selected_key_properties, popupMenu.menu)

            if (viewModel.selectedKey!!.privateExponent == null) {
                popupMenu.menu.findItem(R.id.private_key).setEnabled(false)
            }

            popupMenu.setOnMenuItemClickListener {


                if (it.itemId == R.id.public_key) {
                    Navigation.findNavController(this.requireView()).navigate(
                        MainMenuFragmentDirections.actionMainMenuFragmentToKeyDetailsPublicKeyFragment(
                            viewModel.selectedKey!!
                        )
                    )
                } else if (it.itemId == R.id.private_key) {
                    Navigation.findNavController(this.requireView()).navigate(
                        MainMenuFragmentDirections.actionMainMenuFragmentToKeyDetailsPrivateKeyFragment(
                            viewModel.selectedKey!!
                        )
                    )
                }
                true
            }
            popupMenu.show()
        }
        binding.mainMenuDelleteKeyButton.setOnClickListener {
            viewModel.onClickDeleteKeyButton()
        }

        // return the binding root
        return binding.root
    }

    /**
     * called when user taps one of the keys
     */
    override fun onSelectKey(key: Key) {
        //notify that the previous key was deselected

        onDeselectKeyListeners.forEach() {
            it.onDeselectKey(viewModel.selectedKey)
        }

        //change the selected key
        viewModel.selectedKey = key
        selectedKeyName.text = key.name
        //change the color of the text view so its no longer a warning text
        selectedKeyName.setTextColor(resources.getColor(R.color.primaryTextColor))
    }

    /**
     * add a new listener for when a key is deselected
     */
    override fun addOnDeselectKeyListener(onDeselectKeyListener: OnDeselectKeyListener) {
        onDeselectKeyListeners.add(onDeselectKeyListener)
    }


}
