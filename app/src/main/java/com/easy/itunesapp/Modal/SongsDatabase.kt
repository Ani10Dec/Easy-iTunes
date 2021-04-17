package com.easy.itunesapp.Modal

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class SongsDatabase : RoomDatabase() {
    abstract fun getDao(): SongsDao
}