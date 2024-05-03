package com.example.memoneetassignment.network

import com.example.memoneetassignment.model.ResponseModelItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoApi {
    @GET("photos")
    fun getPhotos(@Query("client_id") clientId: String): Call<List<ResponseModelItem>>


}