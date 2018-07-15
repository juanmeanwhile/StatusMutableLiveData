package com.example.mengujua.statusmutablelivedata

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.adidas.checkout.db.ItemDao
import com.example.mengujua.statusmutablelivedata.Resource.Companion.STATUS_ERROR
import com.example.mengujua.statusmutablelivedata.Resource.Companion.STATUS_SUCCESS
import com.example.mengujua.statusmutablelivedata.Resource.Companion.STATUS_LOADING
import java.util.*
import java.util.concurrent.Executor

class ItemRepository(private val itemDao: ItemDao, private val ioExecutor: Executor) {

    //Used to generate data for this example
    private val random = Random()

    fun getItems(): LiveData<Resource<List<Item>>> {

        //This is the returned LiveData
        val result = MediatorLiveData<Resource<List<Item>>>()

        //When new data comes from db, return success and the info
        result.addSource(itemDao.getItems(), object : android.arch.lifecycle.Observer<List<Item>> {
            var isFirst = true
            override fun onChanged(items : List<Item>?) {
                items?.let {
                    //We want to display those first items coming from the db as still loading
                    result.value = if (isFirst) Resource.loading(items) else Resource.success(items)
                    isFirst = false
                }
            }
        })


        //LiveData for status
        val status = MutableLiveData<Int>()

        //When the status changes, update result with the corresponding status
        result.addSource(status) { st: Int? ->
            st?.let {
                when (it) {
                    STATUS_LOADING -> result.value = Resource.loading(result.value?.data)

                    STATUS_ERROR -> result.value = Resource.error(result.value?.data)

                    STATUS_SUCCESS -> {
                        result.value?.data?.let {
                            result.value = Resource.success(it)
                        }
                    }
                    else -> {//nothing in this example }
                    }
                }
            }
        }

        //Lets simulate the network call
        status.value = STATUS_LOADING
        ioExecutor.execute {
            Thread.sleep(5000)
            val variation = random.nextInt(1000)

            val serverItems = listOf(Item(1, "First $variation"), Item(2, "Second $variation"), Item(3, "Third $variation"))
            itemDao.insertItems(serverItems)
        }

        return result
    }
}