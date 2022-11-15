package com.example.myapplication
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.setting_trigger_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.String

class SubActivityTrigger : AppCompatActivity(){

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient
    var GOOGLE_LOGIN_CODE = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.setting_trigger_main)

        // 뒤로가기 버튼
        btn_goback.setOnClickListener {
            finish()
        }

        // 추가하기 버튼
       // btn_add.setOnClickListener {
      //      val intent = Intent(this, SubActivityTrigger_add::class.java)
      //      startActivity(intent)
      //  }
    }
}