package com.example.imageencryptor.mainmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imageencryptor.R
import com.example.imageencryptor.databinding.FragmentMainMenuBinding
import com.example.imageencryptor.encryption.Key
import com.example.imageencryptor.encryption.KeyRecycleViewAdapter
import com.example.imageencryptorlibrary.encryption.PPKeyImageEncryptor
import timber.log.Timber


/**
 * This is the start fragment from which the user navigates to all others
 */
class MainMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentMainMenuBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)


        //TEST CODE, WILL BE REMOVED LATER
        var imageEncryptor1 = PPKeyImageEncryptor()
        imageEncryptor1.makeKeyPair(2048)
        var imageEncryptor2 = PPKeyImageEncryptor()
        imageEncryptor2.makeKeyPair(2048)
        var imageEncryptor3 = PPKeyImageEncryptor()
        imageEncryptor3.makeKeyPair(2048)
        var imageEncryptor4 = PPKeyImageEncryptor()
        imageEncryptor4.makeKeyPair(2048)

        var key1 = Key("key 1",
            imageEncryptor1.getPublicKey()!!.modulus,
            imageEncryptor1.getPublicKey()!!.publicExponent,
            imageEncryptor1.getPrivateKey()!!.privateExponent)
        var key2 = Key("key 2",
            imageEncryptor2.getPublicKey()!!.modulus,
            imageEncryptor2.getPublicKey()!!.publicExponent,
            imageEncryptor2.getPrivateKey()!!.privateExponent)
        var key3 = Key("key 3",
            imageEncryptor3.getPublicKey()!!.modulus,
            imageEncryptor3.getPublicKey()!!.publicExponent,
            imageEncryptor3.getPrivateKey()!!.privateExponent)
        var key4 = Key("key 4",
            imageEncryptor4.getPublicKey()!!.modulus,
            imageEncryptor4.getPublicKey()!!.publicExponent,
            imageEncryptor4.getPrivateKey()!!.privateExponent)

        var keyList = listOf<Key>(key1, key2, key3, key4)

        Timber.i(key1.name)
        Timber.i(key2.name)
        Timber.i(key3.name)
        Timber.i(key4.name)
        Timber.i(keyList.size.toString())
        //END OF TEST CODE

        var keyRecycleViewAdapter = KeyRecycleViewAdapter();
        keyRecycleViewAdapter.data = keyList//ADDING THE TESTING KEYS
        binding.keysListRecycleView.setHasFixedSize(true)
        Timber.i("adding adapter")
        binding.keysListRecycleView.adapter = keyRecycleViewAdapter
        Timber.i("added adapter")

        // Inflate the layout for this fragment
        return binding.root
    }

}