package com.easy.itunesapp.Modal

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://itunes.apple.com/"

interface SongsNetworkService {
    @GET("search")
    fun getSongsData(@Query("term") name: String): Call<Result>
}

object SongsServices {
    val songsInstances: SongsNetworkService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        songsInstances = retrofit.create(SongsNetworkService::class.java)
    }
}

