package com.easy.itunesapp.View

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.easy.itunesapp.Modal.Result
import com.easy.itunesapp.Modal.Song
import com.easy.itunesapp.Modal.SongsDatabase
import com.easy.itunesapp.Modal.SongsServices
import com.easy.itunesapp.R
import com.easy.itunesapp.ViewModal.SongsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var noDataText: TextView
    lateinit var searchName: EditText
    lateinit var btnSearch: Button
    lateinit var progressLayout: RelativeLayout
    lateinit var name: String
    lateinit var adapter: SongsAdapter
    var list = listOf<Song>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)
        noDataText = findViewById(R.id.tv_noDataFound)
        searchName = findViewById(R.id.et_search)
        btnSearch = findViewById(R.id.btn_search)
        progressLayout = findViewById(R.id.progress_layout)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnSearch.setOnClickListener {
            if (isOnline(this)) {
                if (checkForValidation()) {
                    name = searchName.text.toString()
                    progressLayout.visibility = View.VISIBLE
                    getSongsData()
                }
            } else {
                Toast.makeText(
                    this,
                    "You are offline data may not be updated",
                    Toast.LENGTH_SHORT
                ).show()
                val list = DBAsyncGet(this, list).execute().get()
                if (list.isEmpty()) {
                    Toast.makeText(this@MainActivity, "list empty", Toast.LENGTH_SHORT).show()
                } else {
                    adapter = SongsAdapter(this, list)
                    recyclerView.adapter = adapter
                }
            }
        }
    }

    private fun getSongsData() {
        val data = SongsServices.songsInstances.getSongsData(name)
        data.enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val songs = response.body()
                if (songs != null) {
                    progressLayout.visibility = View.INVISIBLE
                    noDataText.visibility = View.INVISIBLE
                    val adapter = SongsAdapter(this@MainActivity, songs.results)
                    recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.e("TAG", "Error in fetching Data: $t")
            }

        })
    }

    private fun isOnline(mContext: Context): Boolean {
        val cm = mContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    private fun checkForValidation(): Boolean {
        if (searchName.text.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    class DBAsyncGet(val context: Context, val list: List<Song>) :
        AsyncTask<Void, Void, List<Song>>() {
        private val db =
            Room.databaseBuilder(context, SongsDatabase::class.java, "songs-db").build()

        override fun doInBackground(vararg params: Void?): List<Song> {
            val list = db.getDao().getSongsList()
            db.close()
            return list
        }
    }


}