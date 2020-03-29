/*
  */
package com.mfriend.djapp.selectplaylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mfriend.djapp.R
import com.mfriend.djapp.spotifyapi.models.PlaylistDto

/**
 * Simple adapter for a playlist recylcer view
 *
 * Created by mfriend on 2020-01-05.
 */
class PlaylistAdapter(
    var items: List<PlaylistDto>,
    private val onItemClickListener: ((PlaylistDto) -> Unit)? = null
) : RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bindPlaylist(items[position], onItemClickListener)
    }
}

class PlaylistViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bindPlaylist(playlistDto: PlaylistDto, onItemClickListener: ((PlaylistDto) -> Unit)?) {
        view.findViewById<TextView>(R.id.tv_playlist_name).text = playlistDto.name
        view.findViewById<TextView>(R.id.tv_description).text = playlistDto.description
        view.setOnClickListener {
            if (onItemClickListener != null) {
                onItemClickListener(playlistDto)
            }
        }
    }
}