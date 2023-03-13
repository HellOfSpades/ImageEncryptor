package com.secrepixel.app.tutorialtools

import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.secrepixel.app.ImageEncryptorApplication

enum class TutorialPreferenceKeys (val key: String){
    WRITE_MESSAGE_FRAGMENT_TUTORIAL_KEY("SECREPIXEL_WRITE_MESSAGE_TUTORIAL_KEY"),
    SUCCESSFULLY_CREATED_IMAGE_FRAGMENT_TUTORIAL_KEY("SECREPIXEL_SUCCESSFULLY_CREATED_IMAGE_TUTORIAL_KEY"),
    MAIN_MENU_FRAGMENT_TUTORIAL_KEY("SECREPIXEL_MAIN_MENU_TUTORIAL_KEY"),
    ADD_KEY_FRAGMENT_TUTORIAL_KEY("SECREPIXEL_ADD_KEY_TUTORIAL_KEY"),
    DECRYPT_MESSAGE_FRAGMENT_TUTORIAL_KEY("SECREPIXEL_DECRYPT_MESSAGE_TUTORIAL_KEY")
    ;
    companion object{
        public fun resetAllTutorials(activity: Activity){
            for(tutorialKey in TutorialPreferenceKeys.values()){
                activity.getPreferences(AppCompatActivity.MODE_PRIVATE).edit()
                    .putBoolean(tutorialKey.key, true).apply()
            }
        }
    }
}