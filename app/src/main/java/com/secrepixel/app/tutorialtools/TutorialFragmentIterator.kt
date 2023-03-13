package com.secrepixel.app.tutorialtools

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class TutorialFragmentIterator(val fragments: Array<Fragment>) {
    var currentFragmentIndex = -1;

    public fun hasNext(): Boolean{
        return currentFragmentIndex<fragments.size-1;
    }

    public fun next(): Fragment{
        currentFragmentIndex++;
        return fragments[currentFragmentIndex];
    }
}