package com.example.mengujua.statusmutablelivedata

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import com.adidas.checkout.db.ItemDatabase
import com.example.mengujua.statusmutablelivedata.Resource.Companion.STATUS_ERROR
import com.example.mengujua.statusmutablelivedata.Resource.Companion.STATUS_LOADING
import com.example.mengujua.statusmutablelivedata.Resource.Companion.STATUS_SUCCESS
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var listAdapter : ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = ItemDatabase.getInstance(this)
        val viewModelFactory = ViewModelFactory(ItemRepository(database.itemDao(), Executors.newSingleThreadExecutor()))
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getItems().observe(this, Observer {
            it?.let { res : Resource<List<Item>> ->
                Log.d("TAG", it.toString())

                //if there is data, always display it
                it.data?.let {
                    listAdapter.submitList(it)
                }

                when (res.status) {
                    STATUS_LOADING ->  {
                        progressBar.visibility = VISIBLE
                        error.visibility = GONE
                    }
                    STATUS_ERROR -> {
                        progressBar.visibility = GONE
                        error.visibility = VISIBLE
                    }
                    STATUS_SUCCESS -> {
                        progressBar.visibility = GONE
                        error.visibility = GONE
                    }
                }
            }
        })

        listAdapter = ItemsAdapter()
        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.apply {
            setHasFixedSize(true)
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    class ItemsAdapter : RecyclerView.Adapter<ItemHolder>() {
        private var data : List<Item> = ArrayList()

        fun submitList(items: List<Item>) {
            data = items
            notifyDataSetChanged()
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
        }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            holder.name.text = data[position].name
        }

    }

    class ItemHolder(val view: View) : RecyclerView.ViewHolder(view)  {
        val name: TextView = itemView.findViewById(R.id.name)

    }
}
