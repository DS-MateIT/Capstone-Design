package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.Intent
import kotlinx.android.synthetic.main.setting_main.*

class SettingFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.setting_main, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 프로필 수정 페이지로 이동
        editprofile.setOnClickListener{
            val intent = Intent(activity, SubActivityTrigger::class.java)
            startActivity(intent)
        }

        // 비밀번호 재설정 페이지로 이동
        to_password.setOnClickListener{
            val intent = Intent(activity, SubActivityPassword::class.java)
            startActivity(intent)
        }
    }
}