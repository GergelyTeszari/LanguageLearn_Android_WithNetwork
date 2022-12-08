package com.triolingo.network

import android.provider.Telephony
import androidx.appcompat.app.AppCompatActivity
import com.triolingo.dataTypes.WordPair
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager
{
    private val retrofit: Retrofit
    private val WordPairApi: WordsApi
    private const val SERVICE_URL = "http://10.0.2.2:8080/"

    init
    {
        retrofit = Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        WordPairApi = retrofit.create(WordsApi::class.java)
    }

    fun getWordPairs(Lecture: Int?, Tags: String?): Call<List<WordPair?>?>
    {
        return WordPairApi.getWordPairByName(Lecture, Tags)
    }

    fun addWordPair(wordPair: WordPair, pwd: String): Response
    {
        return WordPairApi.addWordPair(wordPair, pwd)
    }

    fun addOrModPWD(oldPWD: String, newPWD: String): Response
    {
        return WordPairApi.createOrUpdatePWD(oldPWD, newPWD)
    }
}