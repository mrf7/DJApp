package com.mfriend.djapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit


class ApiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity)
        val apiToken = intent.getStringExtra(KEY_API_TOKEN) ?: throw IllegalStateException()
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.container, ApiFragment.newInstance(apiToken))
                addToBackStack("api")
            }
        }
    }

    companion object {
        const val KEY_API_TOKEN = "api_token"
    }
}
