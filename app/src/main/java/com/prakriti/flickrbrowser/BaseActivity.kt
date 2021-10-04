package com.prakriti.flickrbrowser

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

// internal constants don't have to be available outside this project
internal const val FLICKR_QUERY = "FlickrQuery"
internal const val PHOTO_TRANSFER = "PhotoTransfer"

open class BaseActivity: AppCompatActivity() { // open keyword makes the class extendable, not by default

    private val TAG = "BaseActivity"

    // fn for showing toolbar and if activity should have home button or not
    internal fun activateToolbar(enableHome: Boolean) {
        Log.i(TAG, "activateToolbar called")
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar // inflate toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(enableHome) // enable home button
    }

}