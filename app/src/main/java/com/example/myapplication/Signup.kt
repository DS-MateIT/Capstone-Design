package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.SignupPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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


        val signUpButton = binding.signUpButton
        //계정생성버튼
        binding.signUpButton.setOnClickListener {
            createAccount(signUpId.text.toString(),signUpPassword.text.toString())
        }



    }

    private fun createAccount(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "계정 생성 완료.",
                            Toast.LENGTH_SHORT
                        ).show()
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

