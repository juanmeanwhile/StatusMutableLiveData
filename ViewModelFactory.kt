package com.example.mengujua.statusmutablelivedata

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

/**
 * Factory for ViewModels
 */
class ViewModelFactory(private val itemRepo: ItemRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> return MainViewModel(itemRepo) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}