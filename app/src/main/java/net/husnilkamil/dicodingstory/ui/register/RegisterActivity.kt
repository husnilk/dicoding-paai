package net.husnilkamil.dicodingstory.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import net.husnilkamil.dicodingstory.R
import net.husnilkamil.dicodingstory.data.network.ApiConfig
import net.husnilkamil.dicodingstory.data.response.RegisterResponse
import net.husnilkamil.dicodingstory.databinding.ActivityRegisterBinding
import net.husnilkamil.dicodingstory.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    var binding: ActivityRegisterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.buttonRegister.setOnClickListener {
            val username = binding!!.edRegisterName.text.toString()
            val password = binding!!.edRegisterPassword.text.toString()
            val email = binding!!.edRegisterEmail.text.toString()
            binding!!.progressRegistrasi.visibility = View.VISIBLE
            registerUser(username, email, password)
        }

        binding!!.textLogin.setOnClickListener {
            val loginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }

        binding!!.progressRegistrasi.visibility = View.GONE
    }

    private fun registerUser(username: String, email: String, password: String) {

        val service = ApiConfig.getApiService()

        val response = service.registerUser(username, email, password)
        response.enqueue(object : Callback<RegisterResponse> {

            override fun onResponse(call: Call<RegisterResponse?>, response: Response<RegisterResponse>)
            {
                val registrationResponse = response.body()

                if (registrationResponse != null && !registrationResponse.error!!) {
                    val loginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(loginIntent)
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registrasi Berhasil. Silahkan login",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registrasi Gagal. " + registrationResponse!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding!!.progressRegistrasi.visibility = View.GONE
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable)
            {
                Toast.makeText(
                    this@RegisterActivity,
                    t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                binding!!.progressRegistrasi.visibility = View.GONE
            }
        })

    }
}