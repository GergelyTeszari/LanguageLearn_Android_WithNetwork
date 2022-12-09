package com.triolingo

import android.os.Bundle
import android.provider.Telephony.Mms.Part.TEXT
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.gson.Gson
import com.triolingo.dataTypes.WordPair
import com.triolingo.databinding.ActivityWordLearnBinding
import com.triolingo.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WordLearnActivity : AppCompatActivity()
{
    private lateinit var binding : ActivityWordLearnBinding
    private var isMinusPoints: String = ""
    private var points = 0
    private var localList = mutableListOf<WordPair>()
    private var currentWordPair = WordPair("", "", 0, null)
    private var currentIndex : Int = 0
    private lateinit var buttonsList : MutableList<Button>
    private lateinit var textViewsList : MutableList<TextView>
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityWordLearnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isMinusPoints = getSharedPreferences("isMinus", MODE_PRIVATE).getString(TEXT, "")!!
        buttonsList = mutableListOf(binding.btnN1, binding.btnN2, binding.btnN3, binding.btnN4, binding.btnN5)
        textViewsList = mutableListOf(binding.twF1, binding.twF2, binding.twF3, binding.twF4, binding.twF5)

        if (isMinusPoints == "false") binding.twPoints.isVisible = false

        getWords { refreshView() }

        binding.btnN1.setOnClickListener { buttonHandler(1) }
        binding.btnN2.setOnClickListener { buttonHandler(2) }
        binding.btnN3.setOnClickListener { buttonHandler(3) }
        binding.btnN4.setOnClickListener { buttonHandler(4) }
        binding.btnN5.setOnClickListener { buttonHandler(5) }
        binding.btnGetNewSet.setOnClickListener {
            getWords { refreshView() }
        }
    }

    private fun getWords (callback: () -> Unit)
    {
        localList.clear()
        /* TODO var lecture= getSharedPreferences("Lecture", MODE_PRIVATE).getString(TEXT, "")?.toInt()
        var tags = getSharedPreferences("Tags", MODE_PRIVATE).getString(TEXT, "")
        NetworkManager.getWordPairs(lecture, tags).enqueue(object : Callback<List<WordPair?>?> */

        NetworkManager.getWordPairs(null, null).enqueue(object : Callback<List<WordPair?>?>
        {

            override fun onResponse(call: Call<List<WordPair?>?>, response: Response<List<WordPair?>?>)
            {
                if (response.isSuccessful)
                {
                    for (wp in response.body()!!)
                    {
                        val newWordPair = wp?.let {
                            WordPair(
                                local = wp.local,
                                native = wp.native,
                                lecture = wp.lecture,
                                tags = wp.tags,
                            )
                        }
                        if (newWordPair != null)
                        {
                            localList.add(newWordPair)
                        }
                        Log.d("WORD", Gson().toJson(wp))
                    }
                    Log.d("List:", localList.toString())
                    Log.d("List len: ", localList.size.toString())
                    callback()
                }
                else
                {
                    Toast.makeText(
                        this@WordLearnActivity,
                        "Error: " + response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<WordPair?>?>, throwable: Throwable)
            {
                throwable.printStackTrace()
                Toast.makeText(
                    this@WordLearnActivity,
                    "Network request error occurred, check LOG",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setEnvironment()
    {
        if (localList.isEmpty() || localList.size != 5)
        {
            /* initializing the array for a burnt-in dataset */
            localList.clear()
            localList = mutableListOf(
                WordPair("Autó", "Car", 0, null),
                WordPair("Villamos", "Tram", 0, null),
                WordPair("Busz", "Bus", 0, null),
                WordPair("HÉV", "Suburban railway", 0, null),
                WordPair("Metró", "Subway", 0, null),
                WordPair("Bicikli", "Bike", 0, null),
                WordPair("Roller", "Scooter", 0, null),
                WordPair("Görkorcsolya", "Roller skates", 0, null),
                WordPair("Troli", "Trolley", 0, null),
                WordPair("Helikopter", "Helicopter", 0, null),
                WordPair("Robogó", "Scooter", 0, null),
                WordPair("Gördeszka", "Skateboard", 0, null),
                WordPair("Hajó", "Boat", 0, null),
                WordPair("Ló", "Horse", 0, null),
                WordPair("Lábbusz", "Foot walk", 0, null),
                WordPair("Repülőgép", "Aeroplane", 0, null),
                WordPair("Taxi", "Cab", 0, null),
                WordPair("Fogaskerekű", "Cog-rail", 0, null),
                WordPair("Libegő", "Chairlift", 0, null),
                WordPair("Sikló", "Hill Funicular", 0, null),
                WordPair("Étterem", "Restaurant", 0, null),
                WordPair("Kávézó", "Cafe", 0, null),
                WordPair("Pékség", "Bakery", 0, null),
                WordPair("Cukrászda", "confectionery", 0, null),
                WordPair("Kocsma", "Pub", 0, null),
                WordPair("Bevásáró központ", "Shopping center", 0, null),
                WordPair("Színház", "Theatre", 0, null),
                WordPair("Mozi", "Cinema", 0, null),
                WordPair("Múzeum", "Museum", 0, null),
                WordPair("Kiállítás", "Exhibition", 0, null),
                WordPair("Park", "Park", 0, null),
                WordPair("Vidámpark", "Amusement park", 0, null),
                WordPair("Piros", "Red", 0, null),
                WordPair("Zöld", "Green", 0, null),
                WordPair("Kék", "Blue", 0, null),
                WordPair("Lila", "Purple", 0, null),
                WordPair("Narancs sárga", "Orange", 0, null),
                WordPair("Sárga", "Yellow", 0, null),
                WordPair("Ciánkék", "Cyan", 0, null),
                WordPair("Barna", "Brown", 0, null),
                WordPair("Fehér", "White", 0, null),
                WordPair("Fekete", "Black", 0, null),
                WordPair("Szürke", "Gray", 0, null),
                WordPair("Rózsaszín", "Pink", 0, null),
                WordPair("Átlátszó", "Transparent", 0, null),
                WordPair("Magyar", "English", 0, null),
            ).shuffled().toMutableList().subList(0, 5)
        }

        for (iter in localList)
        {
            buttonsList[localList.indexOf(iter)].text = iter.local
            buttonsList[localList.indexOf(iter)].isClickable = true
            buttonsList[localList.indexOf(iter)].isVisible = true

            textViewsList[localList.indexOf(iter)].text = iter.native
            textViewsList[localList.indexOf(iter)].isVisible = false
        }

        currentIndex = 0
        localList = localList.shuffled().toMutableList() /* Not to ask questions in order */
        currentWordPair = localList[currentIndex]
    }

    private fun refreshPointTW()
    {
        binding.twPoints.text = "Points: $points"
    }
    private fun refreshCurrentWordTW()
    {
        binding.twCurrentWord.text = currentWordPair.native
    }

    private fun buttonHandler(num: Int)
    {
        if (buttonsList[num-1].text == currentWordPair.local)
        {
            textViewsList[num-1].isVisible = true
            if (isMinusPoints == "true")
            {
                buttonsList[num-1].isVisible = false
            }
            buttonsList[num-1].isClickable = false
            points++
            currentIndex++
            currentWordPair = if (currentIndex > 4)
            {
                WordPair("", "", 0, null)
            }
            else
            {
                localList[currentIndex]
            }
        }
        else
        {
            if (points > 0 && isMinusPoints == "true" ) points--
        }
        refreshPointTW()
        refreshCurrentWordTW()
    }

    private fun feedbackOnSuccess()
    {
        if (localList.isNotEmpty() && localList.size == 5)
        {
            Toast.makeText(
                this@WordLearnActivity,
                "Continuing with list got from network...",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (localList.isEmpty() || localList.size != 5)
        {
            Toast.makeText(
                this@WordLearnActivity,
                "List from network is not full!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun refreshView(){
        feedbackOnSuccess()
        setEnvironment()
        refreshPointTW()
        refreshCurrentWordTW()
    }
}