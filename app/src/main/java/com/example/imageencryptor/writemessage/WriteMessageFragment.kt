package com.example.imageencryptor.writemessage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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

    //permission request codes
    private val WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: Int = 0

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        }
        binding.inputMessageTextView.addTextChangedListener(object: TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                binding.symbolsLeftTextView.text = getString(R.string.symbols_left)+" "+(viewModel.symbolCapacity-s.length)
            }
        })

        //other initializations
        binding.keyUsedTextView.text = getString(R.string.key_used)+" "+key.name

        //=====restoring previous state====
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState)
        }

        return binding.root
    }

    /**
     * Launcher to retrieve image from library, and update the necessary views
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private var retrieveImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK && result.data?.data != null) {
                val data: Uri? = result.data!!.data
                setPicture(data)
            }
        }

    /**
     * activates when the choose image button is pressed
     * is opens the users gallery for him to find the image they want to encrypt
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun onClickChooseImage() {
        Timber.i("choose image bottom clicked")
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        retrieveImageResultLauncher.launch(intent)
    }

    /**
     * encrypts the message into the image and saves it
     */
    fun onClickMakeImage() {

        //check if the user gave permission to write to external storage
        if(!hasWriteExternalStoragePermission()){
            Timber.i("permission denied")
            Toast.makeText(context, "can't save image to your phone without permission", Toast.LENGTH_SHORT).show()
            requestWriteExternalStoragePermission()
            return
        }

        var usersMessage = binding.inputMessageTextView.text.toString()

        var exceptionMessageToUser = viewModel.encrypt(usersMessage, "savedImage.png")

        if(exceptionMessageToUser!=null){
            Toast.makeText(context, exceptionMessageToUser, Toast.LENGTH_SHORT).show()
        }
        else Navigation.findNavController(binding.makeImageButton).navigate(R.id.action_writeMessageFragment_to_mainMenuFragment)
    }

    /**
     * check if the app has permission to write to external storage
     */
    fun hasWriteExternalStoragePermission(): Boolean{
        //check if the permission was already given before
        return ActivityCompat.checkSelfPermission(requireContext().applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
    }

    /**
     * request to give the WRITE_EXTERNAL_STORAGE permission
     */
    fun requestWriteExternalStoragePermission(){
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
    }

    /**
     * saves the current state of the fragment
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("output_image", viewModel.getPicture())
        outState.putString("incomplete_user_message", binding.inputMessageTextView.text.toString())
    }

    /**
     * restores the previous state of the fragment
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun restoreInstanceState(savedInstanceState: Bundle) {
        setPicture(savedInstanceState.get("output_image") as Uri?)
        var incompleteUserMessage = savedInstanceState.getString("incomplete_user_message")
        if(incompleteUserMessage!=null){
            binding.inputMessageTextView.setText(incompleteUserMessage)
        }
    }

    /**
     * set picture to be changed as well as all the visuals to display image information to the user
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun setPicture(data: Uri?){
        if (data != null) {
            //how many decimal places to show
            val decimalPlaces = 2.0

            //setting the picture in the view model
            viewModel.setPicture(data)
            //updating the text views to match the selected image
            binding.previewImageView.setImageURI(viewModel.getPicture())
            //creating uri cursor
            val cursor = requireActivity().contentResolver.query(viewModel.getPicture()!!, null, null, null, null)!!
            cursor.moveToFirst()
            //cursor indexes
            val nameColumnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeColumnIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            //setting the text Views
            binding.writeMessageFileNameTextView.text = getString(R.string.file_name)+" "+cursor.getString(nameColumnIndex)
            //there are 1048576 bytes in a mega byte
            binding.writeMessageFileSizeTextView.text = getString(R.string.file_size)+" "+
                    (Math.round(cursor.getDouble(sizeColumnIndex)/1048576*Math.pow(10.0,decimalPlaces))/Math.pow(10.0,decimalPlaces))+
                    getString(R.string.mb)
            binding.writeMessageFileTotalSymbolsTextView.text = getString(R.string.total_symbols)+" "+viewModel.symbolCapacity.toString()
            binding.symbolsLeftTextView.text = getString(R.string.symbols_left)+" "+(viewModel.symbolCapacity-binding.inputMessageTextView.text.length)
        }
    }
}