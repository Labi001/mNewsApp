package com.example.mnewsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ArticleEntity::class],
    version = 2,
    exportSchema = false
)
abstract class NewsDataBase: RoomDatabase() {

    abstract fun articleDao(): ArticleDao

}
