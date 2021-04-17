package com.easy.itunesapp.Modal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        @ColumnInfo(name = "artistName")
        var artistName: String,
        @ColumnInfo(name = "trackName")
        var trackName: String,
        @ColumnInfo(name = "price")
        var trackPrice: Double,
        @ColumnInfo(name = "genre")
        val primaryGenreName: String,
        @ColumnInfo(name = "img")
        val artworkUrl100: String
)