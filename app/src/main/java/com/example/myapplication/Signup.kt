package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.SignupPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class Signup : AppCompatActivity(){
    private lateinit var binding : SignupPageBinding
    private var auth : FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_signup)

        binding = SignupPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val signUpId = binding.signUpId
        val signUpPassword = binding.signUpPassword
        val signUpBirth = binding.signUpBirth


        val signUpButton = binding.signUpButton
        //계정생성버튼
        binding.signUpButton.setOnClickListener {
            createAccount(signUpId.text.toString(),signUpPassword.text.toString(), signUpBirth.text.toString().toInt())
        }



    }

    private fun createAccount(email: String, password: String, birth : Int ) {
        if (email.isNotEmpty() && password.isNotEmpty() && birth != null) {
            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "계정 생성 완료.",
                            Toast.LENGTH_SHORT
                        ).show()

                        //유저정보 보내기 테스트
                        RetrofitClient.retrofitService.userData(email, password, birth).enqueue(object :
                            Callback<UserDTO> {
                            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                                if (response.isSuccessful) {
                                    try {
                                        val result = response.body().toString()
                                        Log.v("user", result)

                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                    }
                                } else {
                                    Log.v("user","error = " + java.lang.String.valueOf(response.code()))

                                }
                            }

                            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                                Log.d("user","post"+t.toString())
                            }

                        })

                        finish() // 가입창 종료
                    } else {
                        Toast.makeText(
                            this, "계정 생성 실패",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}

