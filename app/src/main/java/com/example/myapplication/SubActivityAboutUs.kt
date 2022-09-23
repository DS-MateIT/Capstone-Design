package com.example.myapplication
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle

import kotlinx.android.synthetic.main.setting_aboutus.*

class SubActivityAboutUs : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_aboutus)

        // 뒤로가기 버튼
        btn_goback.setOnClickListener{
            finish()
        }

    }
}