package com.triolingo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony.Mms.Part.TEXT
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.triolingo.databinding.ActivityPasswordBinding
import com.triolingo.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordActivity : AppCompatActivity()
{

    private lateinit var binding: ActivityPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editTextTextOldPassword.isVisible = true /* it will be visible on init */

        val sp = getSharedPreferences("password", MODE_PRIVATE)

        updateTW(sp.getString(TEXT, ""))

        if (sp.getString(TEXT, "").equals("")) /* No password yet */
        {
            binding.editTextTextOldPassword.isVisible = false
            binding.btnPwdSubmit.setOnClickListener {
                val pwd = binding.editTextTextNewPassword.text.toString()
                val editor = sp.edit()
                if (pwd != "")
                {
                    editor.putString(TEXT, pwd)
                    editor.apply()
                    Toast.makeText(
                        this@PasswordActivity,
                        "Successful password update!",
                        Toast.LENGTH_SHORT
                    ).show()
                    doNetworkCall("", pwd)
                }
            }
            binding.editTextTextOldPassword.isVisible = true
            updateTW(sp.getString(TEXT, ""))
        }
        else /* There is a password already */
        {
            binding.btnPwdSubmit.setOnClickListener {
                val oldPwd = binding.editTextTextOldPassword.text.toString()
                if (oldPwd == sp.getString(TEXT, ""))
                {
                    val newPwd = binding.editTextTextNewPassword.text.toString()
                    val editor = sp.edit()

                    /* Here we allow empty String */

                    editor.putString(TEXT, newPwd)
                    doNetworkCall(oldPwd, newPwd)
                }
                else if (oldPwd != sp.getString(TEXT, ""))
                {
                    Toast.makeText(
                        this@PasswordActivity,
                        "Wrong password!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                updateTW(sp.getString(TEXT, ""))
            }
        }
    }

    private fun doNetworkCall(oldPass: String, newPass: String)
    {
        NetworkManager.addOrModPWD(oldPass, newPass).enqueue(object : Callback<Int>
        {

            override fun onResponse(call: Call<Int>, response: Response<Int>)
            {
                if (response.isSuccessful)
                {
                    Log.d("Result of POST: ", response.toString())
                }
                else
                {
                    Toast.makeText(
                        this@PasswordActivity,
                        "Error: " + response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Int>, throwable: Throwable)
            {
                throwable.printStackTrace()
                Toast.makeText(
                    this@PasswordActivity,
                    "Network request error occurred, check LOG",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateTW(redPwd: String?)
    {
        if (!redPwd.equals(""))
        {
            binding.textView2.text = "Current password: $redPwd"
        }
    }
}