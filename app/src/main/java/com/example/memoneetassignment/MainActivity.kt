package com.example.memoneetassignment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.memoneetassignment.adapter.GridAdapter
import com.example.memoneetassignment.databinding.ActivityMainBinding
import com.example.memoneetassignment.network.PhotoApi
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var ls : List<String>
    private val TAG = "joewjo2"
    private val BASE_URL = "https://api.unsplash.com/"
    private val bitmapCache = HashMap<String, Bitmap>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch(Dispatchers.IO) {
            val photos = getAllPhotos()
            val bitmapLS = async {
                downloadBitmap(photos)
            }
            withContext(Dispatchers.Main){
                setupRecyclerView(bitmapLS.await())
            }

        }
    }

    private fun setupRecyclerView(photos: MutableList<Bitmap>) {
        if (photos.isNotEmpty()) {
            binding.recyclerView.apply {
                adapter = GridAdapter(photos)
                layoutManager  = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            }
        } else {
            Log.d(TAG, "No photos available")
        }
    }
    private suspend fun downloadBitmap(ls: List<String>): MutableList<Bitmap> {
        return withContext(Dispatchers.IO) {
            val bitmapLs: MutableList<Bitmap> = mutableListOf()
            for (item in ls) {
                if (bitmapCache.containsKey(item)) {
                    bitmapLs.add(bitmapCache[item]!!)
                } else {
                    val conn = URL(item).openConnection()
                    conn.connect()
                    val inputStream = conn.getInputStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream.close()
                    bitmapLs.add(bitmap)
                    // Store the downloaded Bitmap in the cache
                    bitmapCache[item] = bitmap
                }
            }
            Log.d(TAG, "downloadBitmap: $bitmapLs")
            bitmapLs
        }
    }

    private suspend fun getAllPhotos(): List<String> {
        return withContext(Dispatchers.IO) {
            val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PhotoApi::class.java)



            val response = api.getPhotos(clientId = "iWW2Sdo4jB9I_8hIloe86vcNZqDy299Jh1eYxD5ec9M").execute()
            if (response.isSuccessful) {
                val responseBody = response.body()
                responseBody?.map { it.urls.small } ?: emptyList()
            } else {
                emptyList()
            }
        }
    }
    
}


