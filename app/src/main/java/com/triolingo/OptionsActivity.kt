package com.triolingo

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.triolingo.databinding.ActivityOptionsBinding
import android.provider.Telephony.Mms.Part.TEXT

class OptionsActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityOptionsBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isMinusSP: SharedPreferences = getSharedPreferences("isMinus", MODE_PRIVATE)
        val lectureSP: SharedPreferences = getSharedPreferences("Lecture", MODE_PRIVATE)
        val tagsSP: SharedPreferences = getSharedPreferences("Tags", MODE_PRIVATE)

        showIsMinusSP(isMinusSP)
        showFiltersSP(lectureSP, tagsSP)

        binding.swMinusPoints.setOnClickListener {
            updateIsMinusSP(isMinusSP)
            showIsMinusSP(isMinusSP)
        }

        binding.btnSearchSubmit.setOnClickListener {
            updateFiltersSP(lectureSP, tagsSP)
            showFiltersSP(lectureSP, tagsSP)
        }
    }

    private fun showIsMinusSP(isMinusSP: SharedPreferences)
    {
        if (isMinusSP.getString(TEXT, "") == "true")
        {
            binding.swMinusPoints.isChecked = true
        }
    }
    private fun updateIsMinusSP(isMinusSP: SharedPreferences)
    {
        if (binding.swMinusPoints.isChecked)
        {
            val editor = isMinusSP.edit()
            editor.putString(TEXT, "true")
            editor.apply()
        }
        else
        {
            val editor = isMinusSP.edit()
            editor.putString(TEXT, "false")
            editor.apply()
        }
    }
    private fun showFiltersSP(LectureSP: SharedPreferences, TagsSP: SharedPreferences)
    {
        if (LectureSP.getString(TEXT, "") != "")
        {
            binding.etLecture.setText(LectureSP.getString(TEXT, ""))
        }

        if (TagsSP.getString(TEXT, "") != "")
        {
            binding.etTags.setText(TagsSP.getString(TEXT, "").toString())
        }
    }
    private fun updateFiltersSP(LectureSP: SharedPreferences, TagsSP: SharedPreferences)
    {
        val lecture = binding.etLecture.text.toString().trim()
        val editor2 = LectureSP.edit()
        editor2.putString(TEXT, lecture)
        editor2.apply()

        val tags = binding.etTags.text.toString().trim().filter { !it.isWhitespace() }
        val editor3 = TagsSP.edit()
        editor3.putString(TEXT, tags)
        editor3.apply()
    }
}