package com.example.imageencryptor.decryptmessage

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.imageencryptor.R
import com.example.imageencryptor.databinding.FragmentDecryptMessageBinding
import com.example.imageencryptor.databinding.FragmentWriteMessageBinding
import com.example.imageencryptor.writemessage.WriteMessageFragmentArgs
import com.example.imageencryptorlibrary.encryption.PPKeyImageEncryptor
import timber.log.Timber
import java.math.BigInteger


class DecryptMessageFragment : Fragment() {

    lateinit var binding: FragmentDecryptMessageBinding
    lateinit var viewModel: DecryptMessageViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //=======Extract the key==================
        var args = WriteMessageFragmentArgs.fromBundle(requireArguments())
        var key = args.key

        //initializing the view model
        viewModel = ViewModelProvider(this).get(DecryptMessageViewModel::class.java)
        viewModel.key = key
        viewModel.activity = activity
        viewModel.imageEncryptor = PPKeyImageEncryptor()
        viewModel.imageEncryptor.setPublicPrivateKey(BigInteger(key.modulus),BigInteger(key.publicExponent),BigInteger(key.privateExponent))

        //initializing the binding
        binding = FragmentDecryptMessageBinding.inflate(layoutInflater)

        //add click listeners
        binding.chooseImageButtonDecryptMessageView.setOnClickListener(){
            onClickChooseImage()
        }
        binding.decryptButtonDecryptMessageView.setOnClickListener(){
            val decrypted = viewModel.decrypt();
            var message: String
            if(decrypted==null){
                message = "can't extract message from this image"
            }else{
                message = decrypted
            }
            Toast.makeText(this.context,  message, Toast.LENGTH_SHORT).show()
        }

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
                    binding.decryptedImagePreview.setImageURI(viewModel.getPicture())
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onClickChooseImage() {
        Timber.i("choose image buttom clicked")
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        retrieveImageResultLauncher.launch(intent)
    }

}