package com.mfriend.djapp.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mfriend.djapp.R

/**
 * Activity for all the fragments
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity)
    }
}
