package com.secrepixel.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.secrepixel.app.tutorialtools.TutorialPreferenceKeys

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TutorialPreferenceKeys.resetAllTutorials(this)
        setContentView(R.layout.activity_main)
    }
}