package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.setting_main.*

class SubActivityTrigger_add : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_trigger_add)
        setContentView(R.layout.setting_main)

        /*
        // 트리거 워닝 페이지로 이동
        to_trigger.setOnClickListener{
            val intent = Intent(this, SubActivityTrigger::class.java)
            startActivity(intent)
        }

        // 비밀번호 재설정 페이지로 이동
        to_password.setOnClickListener{
            val intent = Intent(this, SubActivityPassword::class.java)
            startActivity(intent)
        }

         */
    }
}


