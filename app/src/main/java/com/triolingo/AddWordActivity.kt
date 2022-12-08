package com.triolingo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import com.triolingo.dataTypes.WordPair
import com.triolingo.databinding.ActivityAddWordBinding
import com.triolingo.network.NetworkManager
import java.lang.Integer.parseInt

class AddWordActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityAddWordBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sp = getSharedPreferences("password", MODE_PRIVATE)

        val pwd = sp.getString(Telephony.Mms.Part.TEXT, "")

        val surePwd = if (pwd.isNullOrBlank()) "" else pwd

        binding.btnNewWordSubmit.setOnClickListener{
            val newWord = checkedWord()
            if (newWord != null)
            {
                val postResult = NetworkManager.addWordPair(newWord, surePwd)
                Log.d("Result of post: ", postResult.toString())
            }
        }
    }
    private fun checkedWord(): WordPair?
    {
        if (binding.etNative.text.toString().trim() == "")
        {
            binding.etNative.error = "This field is required!"
            return null
        }
        val native = binding.etNative.text.toString()
        if (binding.etForeign.text.toString().trim() == "")
        {
            binding.etForeign.error = "This field is required!"
            return null
        }
        if (binding.etLecture.text.toString().trim() == "")
        {
            binding.etLecture.error = "This field is required!"
            return null
        }
        val foreign = binding.etForeign.text.toString()
        val lecture = parseInt(binding.etLecture.text.toString())
        val tags = binding.etTags.toString().trim()
        return WordPair(native, foreign, lecture, tags)
    }
}