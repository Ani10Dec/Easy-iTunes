package com.easy.itunesapp.Modal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SongsDao {
    @Insert
    fun insert(song: Song)

    @Query("SELECT * FROM songs ORDER BY id ASC")
    fun getSongsList(): List<Song>
}