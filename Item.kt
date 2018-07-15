package com.example.mengujua.statusmutablelivedata

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "items")
data class Item(@PrimaryKey val id: Int, val name: String)