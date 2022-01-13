package com.nft.maker.db

import androidx.room.RoomDatabase
import com.nft.maker.db.dao.UserDao


//@Database(entities = {} , version = 2)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object{
        const val DATABASE_NAME: String = "application_db"

    }
}