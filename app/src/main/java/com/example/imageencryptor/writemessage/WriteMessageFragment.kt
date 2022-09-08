package com.example.imageencryptor.writemessage

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.imageencryptor.databinding.FragmentWriteMessageBinding
import timber.log.Timber

/**
 * This fragment is where the user will make their encrypted image
 */
class WriteMessageFragment : Fragment() {

    private lateinit var viewModel: WriteMessageViewModel
    private lateinit var binding: FragmentWriteMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //=====Initializing Fragment Properties======
        //initializing the binding
        binding = FragmentWriteMessageBinding.inflate(layoutInflater)
        //initializing the view model
        viewModel = ViewModelProvider(this).get(WriteMessageViewModel::class.java)
        viewModel.binding = binding
        viewModel.activity = activity

        //=====restoring previous state====
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState)
        }

        //initialing listeners
        binding.chooseImageButton.setOnClickListener{this.onClickChooseImage()}

        return binding.root
    }

    /**
     * Launcher to retrieve image from library
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private var retrieveImageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == AppCompatActivity.RESULT_OK && result.data?.data !=null){
            val data: Uri? = result.data!!.data
            if (data != null) {
                viewModel.setPicture(data)
                binding.previewImageView.setImageURI(viewModel.getPicture())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onClickChooseImage(){

        Timber.i("choose image buttom clicked")
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        retrieveImageResultLauncher.launch(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if(viewModel.getPicture()!=null){outState.putParcelable("output_image", viewModel.getPicture())}

        Timber.i("saved instance")
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun restoreInstanceState(savedInstanceState: Bundle) {
        if(savedInstanceState!=null) {
            viewModel.setPicture(savedInstanceState.get("output_image") as Uri?)
            binding.previewImageView.setImageURI(viewModel.getPicture())
            Timber.i("restored instance")
        }
    }
}