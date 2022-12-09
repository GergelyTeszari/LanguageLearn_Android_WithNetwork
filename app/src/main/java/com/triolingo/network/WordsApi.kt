package com.triolingo.network

import com.triolingo.dataTypes.WordPair
import retrofit2.Call
import retrofit2.http.*

interface WordsApi
{
    @GET("api/WordPair")
    fun getWordPairByName(@Query("Lecture") Lecture: Int? = null, @Query("Tags") Tags: String? = null): Call<List<WordPair?>?>

    @POST("api/WordPair")
    fun addWordPair(@Body wordPair: WordPair, @Query("pwd") pwd: String): Call<Int>

    @POST("api/WordPair/pwd")
    fun createOrUpdatePWD(@Query("oldPWD") old: String, @Query("newPWD") new: String): Call<Int>
}