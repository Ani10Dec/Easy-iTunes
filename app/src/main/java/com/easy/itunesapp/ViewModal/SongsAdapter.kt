package com.easy.itunesapp.ViewModal

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.easy.itunesapp.Modal.Song
import com.easy.itunesapp.Modal.SongsDatabase
import com.easy.itunesapp.R

class SongsAdapter(val context: Context, val list: List<Song>) :
    RecyclerView.Adapter<SongsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.songsName)
        val artist = itemView.findViewById<TextView>(R.id.artistName)
        val genre = itemView.findViewById<TextView>(R.id.genre)
        val price = itemView.findViewById<TextView>(R.id.price)
        val img = itemView.findViewById<ImageView>(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = list[position]
        holder.title.text = current.trackName
        holder.artist.text = current.artistName
        holder.genre.text = current.primaryGenreName
        holder.price.text = "$ ${current.trackPrice}"
        Glide.with(context).load(current.artworkUrl100).error(R.drawable.ic_launcher_background)
            .into(holder.img)
        if (current.trackName == null) {
            current.trackName = "N/A"
        }
        if (current.artistName == null) {
            current.artistName = "N/A"
        }
        if (current.trackPrice == null) {
            current.trackPrice = 0.0
        }

        val song = Song(
            null,
            current.trackName,
            current.artistName,
            current.trackPrice,
            current.primaryGenreName,
            current.artworkUrl100
        )
        DBAsyncInsert(context, song).execute().get()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class DBAsyncInsert(val context: Context, val song: Song) :
        AsyncTask<Void, Void, Boolean>() {
        private val db =
            Room.databaseBuilder(context, SongsDatabase::class.java, "songs-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {
            db.getDao().insert(song)
            db.close()
            return true
        }
    }
}