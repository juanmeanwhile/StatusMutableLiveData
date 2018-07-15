package com.adidas.checkout.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.mengujua.statusmutablelivedata.Item

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(items: List<Item>)


    @Query("SELECT * FROM items ORDER BY id ")
    fun getItems(): LiveData<List<Item>>

}