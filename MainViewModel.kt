package com.example.mengujua.statusmutablelivedata

import android.arch.lifecycle.ViewModel

class MainViewModel(val repo: ItemRepository) : ViewModel() {

    fun getItems() = repo.getItems()

}