package com.triolingo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony.Mms.Part.TEXT
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.triolingo.databinding.ActivityPasswordBinding
import com.triolingo.network.NetworkManager

class PasswordActivity : AppCompatActivity()
{

    private lateinit var binding: ActivityPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editTextTextOldPassword.isVisible = true

        val sp = getSharedPreferences("password", MODE_PRIVATE)

        updateTW(sp.getString(TEXT, ""))

        if (sp.getString(TEXT, "").equals("")) /* No password yet */
        {
            binding.editTextTextOldPassword.isVisible = false
            binding.btnPwdSubmit.setOnClickListener {
                val pwd = binding.editTextTextNewPassword.text
                val editor = sp.edit()
                if (!pwd.equals(""))
                {
                    editor.putString(TEXT, pwd.toString())
                    editor.apply()
                    Toast.makeText(
                        this@PasswordActivity,
                        "Successful password update!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val postResult = NetworkManager.addOrModPWD("", pwd.toString())
                    Log.d("Result of post: ", postResult.toString())
                }
            }
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

                    editor.putString(TEXT, newPwd)
                    NetworkManager.addOrModPWD(oldPwd, newPwd)
                    editor.apply()
                    Toast.makeText(
                        this@PasswordActivity,
                        "Successful password update!",
                        Toast.LENGTH_SHORT
                    ).show()

                    /*if (!newPwd.equals(""))
                    {

                    }*/
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

    private fun updateTW(redPwd: String?)
    {
        if (!redPwd.equals(""))
        {
            binding.textView2.text = "Current password: $redPwd"
        }
    }
}