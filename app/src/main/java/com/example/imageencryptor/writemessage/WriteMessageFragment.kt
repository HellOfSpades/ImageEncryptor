package com.example.imageencryptor.writemessage

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.imageencryptor.R
import com.example.imageencryptor.databinding.FragmentWriteMessageBinding
import com.example.imageencryptorlibrary.encryption.PPKeyImageEncryptor
import timber.log.Timber
import java.math.BigInteger


/**
 * This fragment is where the user will make their encrypted image
 */
class WriteMessageFragment : Fragment() {

    //fragments view model
    private lateinit var viewModel: WriteMessageViewModel

    //fragments binding
    private lateinit var binding: FragmentWriteMessageBinding

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //=====restoring previous state====
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState)
        }
        //=======Extract the key==================
        var args = WriteMessageFragmentArgs.fromBundle(requireArguments())
        var key = args.key
        //=====Initializing Fragment Properties======
        //initializing the binding
        binding = FragmentWriteMessageBinding.inflate(layoutInflater)
        //initializing the view model
        viewModel = ViewModelProvider(this).get(WriteMessageViewModel::class.java)
        viewModel.binding = binding
        viewModel.activity = activity
        viewModel.imageEncryptor = PPKeyImageEncryptor()
        //viewModel.imageEncryptor.setPublicKey(BigInteger(key.modulus), BigInteger(key.publicExponent))
        viewModel.imageEncryptor.setPublicKey(
            BigInteger(key.modulus),
            BigInteger(key.publicExponent)
        )

        //initialing listeners
        binding.chooseImageButton.setOnClickListener { this.onClickChooseImage() }
        binding.makeImageButton.setOnClickListener {
            this.onClickMakeImage()
            Navigation.findNavController(it)
                .navigate(R.id.action_writeMessageFragment_to_mainMenuFragment)
        }

        //other initializations
        binding.keyUsedTextView.text = getString(R.string.key_used)+" "+key.name

        return binding.root
    }

    /**
     * Launcher to retrieve image from library
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private var retrieveImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK && result.data?.data != null) {
                val data: Uri? = result.data!!.data
                if (data != null) {
                    viewModel.setPicture(data)
                    binding.previewImageView.setImageURI(viewModel.getPicture())
                    binding.symbolsLeftTextView.text = getString(R.string.symbols_left)+" "+viewModel.symbolCapacity.toString()
                }
            }
        }

    /**
     * activates when the choose image button is pressed
     * is opens the users gallery for him to find the image they want to encrypt
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun onClickChooseImage() {
        Timber.i("choose image buttom clicked")
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        retrieveImageResultLauncher.launch(intent)
    }

    /**
     * encrypts the message into the image and saves it
     */
    fun onClickMakeImage() {
        viewModel.encrypt(binding.inputMessageTextView.text.toString(), "savedImage.png")
    }

    /**
     * saves the current state of the fragment
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (viewModel.getPicture() != null) {
            outState.putParcelable("output_image", viewModel.getPicture())
        }
    }

    /**
     * restores the previous state of the fragment
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun restoreInstanceState(savedInstanceState: Bundle) {

        //TODO fix this
//        var image = savedInstanceState.get("output_image") as Uri?
//        if (image != null) {
//            viewModel.setPicture(image)
//            binding.previewImageView.setImageURI(viewModel.getPicture())
//        }

    }
}