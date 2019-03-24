package com.mfriend.djapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.models.Playlist
import kaaes.spotify.webapi.android.models.PlaylistSimple
import kaaes.spotify.webapi.android.models.UserPrivate
import kotlinx.android.synthetic.main.activity_main.*
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class TestingStuff : AppCompatActivity(), View.OnClickListener {

    // static members stored in companion object
    companion object {
        val CLIENT_ID = "bc77027fdfd54c0091c11fcc1895c5dd"
        val REDIRECT_URI = "http://com.mfriend.djapp/callback"
        val REQUEST_CODE = 420
    }

    private lateinit var mSpotifyApi: SpotifyApi
    private var mPlaylists: List<PlaylistSimple>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSpotifyApi = SpotifyApi()
        Log.d("MainAct", "Create")
        when {
            intent?.action == Intent.ACTION_SEND -> {
                if ("text/*" == intent.type) {

                    Log.d("MRF", "Shared link")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val builder = AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)

        builder.setScopes(
            arrayOf(
                "streaming",
                "playlist-read-private",
                "playlist-modify-private",
                "playlist-modify-public"
            )
        )
        val request = builder.build()

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)
    }

    override fun onClick(v: View?) {
        val service = mSpotifyApi.service
//        service.getMyPlaylists(object : Callback<Pager<PlaylistSimple>> {
//            override fun success(t: Pager<PlaylistSimple>?, response: Response?) {
//                mPlaylists = t?.items
//                Log.d("MRF", "" + t?.items?.map { it.name })
//            }
//
//            override fun failure(error: RetrofitError?) {
//                Log.d("MRF", "User error: " + error?.message)
//            }
//
//        })
        // Options for playlist
        val options = HashMap<String, Any>()
        options["name"] = "TestPlaylist"
        options["public"] = true

        service.getMe(object : Callback<UserPrivate> {
            override fun success(t: UserPrivate?, response: Response?) {
                service.createPlaylist(t?.display_name, options, object : Callback<Playlist> {
                    override fun success(t: Playlist?, response: Response?) {
                        Log.d("MRF", t?.toString())
                    }

                    override fun failure(error: RetrofitError?) {
                        Log.d("MRF", error?.message)
                    }

                })
            }

            override fun failure(error: RetrofitError?) {
                Log.d("MRF", "FAiled: " + error?.message)
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response: AuthenticationResponse =
                data?.extras?.getBundle("EXTRA_AUTH_RESPONSE")?.getParcelable("response") ?: return
            when (response.type) {
                // Response was successful and contains auth token
                AuthenticationResponse.Type.TOKEN -> {
                    Log.d("MRF", "succ login: " + response.accessToken)
                    mSpotifyApi.setAccessToken(response.accessToken)
                }

                // Auth flow returned an error
                AuthenticationResponse.Type.ERROR -> {
                    Log.d("MRF", "error: " + response.error)
                }
                else -> {
                    return
                }
            }
        }
    }
}
