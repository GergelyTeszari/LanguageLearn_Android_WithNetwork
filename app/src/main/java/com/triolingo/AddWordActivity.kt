package com.triolingo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import com.triolingo.dataTypes.WordPair
import com.triolingo.databinding.ActivityAddWordBinding
import com.triolingo.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Integer.parseInt

class AddWordActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityAddWordBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pwdSP = getSharedPreferences("password", MODE_PRIVATE).getString(Telephony.Mms.Part.TEXT, "")

        val surePwd = if (pwdSP.isNullOrBlank()) { "" } else { pwdSP }

        binding.btnNewWordSubmit.setOnClickListener{
            val newWord = confirmPWD()
            if (newWord != null)
            {
                NetworkManager.addWordPair(newWord, surePwd).enqueue(object : Callback<Int>
                {

                    override fun onResponse(call: Call<Int>, response: Response<Int>)
                    {
                        if (response.isSuccessful)
                        {
                            Log.d("Result of POST: ", response.toString())
                            Toast.makeText(
                                this@AddWordActivity,
                                "Success!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else
                        {
                            Toast.makeText(
                                this@AddWordActivity,
                                "Error: " + response.message(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Int>, throwable: Throwable)
                    {
                        throwable.printStackTrace()
                        Toast.makeText(
                            this@AddWordActivity,
                            "Network request error occurred, check LOG",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }
    private fun confirmPWD(): WordPair?
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
        val foreign = binding.etForeign.text.toString()

        if (binding.etLecture.text.toString().trim() == "")
        {
            binding.etLecture.error = "This field is required!"
            return null
        }
        val lecture = parseInt(binding.etLecture.text.toString())

        val tags = binding.etTags.text.toString().trim().filter { !it.isWhitespace() }
        return WordPair(native, foreign, lecture, tags)
    }
}