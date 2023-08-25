package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {
    @Query("SELECT * FROM ImageData")
    fun getAll(): List<ImageData>

    @Insert
    fun insertAll(vararg imageData: ImageData)
}