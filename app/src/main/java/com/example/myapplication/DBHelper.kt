package com.example.myapplication

import android.content.Context
import androidx.room.*

@Database(entities = [ImageData::class],version = 1)
@TypeConverters(DataConverter::class)
abstract class DBHelper: RoomDatabase() {

    abstract fun dao() : ImageDao

    companion object{
        @Volatile
        private var INSATNCE: DBHelper? = null
        fun getDatabase(context : Context) : DBHelper{
            if (INSATNCE == null){
                synchronized(this){
                    INSATNCE = Room.databaseBuilder(context,DBHelper::class.java,"img.db").fallbackToDestructiveMigration().allowMainThreadQueries().build()
                }
            }
            return INSATNCE!!
        }
    }
}

