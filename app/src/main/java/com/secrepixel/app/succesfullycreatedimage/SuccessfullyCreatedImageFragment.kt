package com.secrepixel.app.succesfullycreatedimage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.secrepixel.app.databinding.FragmentSuccessfullyCreatedImageBinding
import com.secrepixel.app.getUri
import com.secrepixel.app.writemessage.WriteMessageViewModel

/**
 * this fragment is responsible for displaying to the user that their image was saved correctly
 * it also used to easily send the image to other apps
 */
class SuccessfullyCreatedImageFragment : Fragment() {

    private lateinit var viewModel:SuccessfullyCreatedImageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //get the arguments
        var args = SuccessfullyCreatedImageFragmentArgs.fromBundle(requireArguments())
        var uri = args.uri
        //inflate the binding
        var binding = FragmentSuccessfullyCreatedImageBinding.inflate(inflater)
        //get the view model
        viewModel = ViewModelProvider(this).get(SuccessfullyCreatedImageViewModel::class.java)
        viewModel.setImage(uri)
        //set preview picture
        binding.displayImage.setImageURI(uri)
        //add listeners
        binding.goBackToMainMenuButton.setOnClickListener(){
            //navigate to the main menu
            Navigation.findNavController(it).navigate(SuccessfullyCreatedImageFragmentDirections.actionSuccessfullyCreatedImageFragmentToMainMenuFragment());
        }
        binding.shareImageButton.setOnClickListener(){
            //share image to another app
            startActivity(Intent.createChooser(viewModel.getShareImageIntent(), "Share file"))
        }

        //return the binding root
        return binding.root
    }
}