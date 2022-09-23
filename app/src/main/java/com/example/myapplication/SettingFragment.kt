package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.Intent
import android.util.Log
import androidx.viewbinding.ViewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.youtube.player.internal.e
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.setting_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.String


class SettingFragment: Fragment() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient
    var GOOGLE_LOGIN_CODE = 9001

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View?{
        val binding = com.example.myapplication.databinding.SettingMainBinding.inflate(
            inflater,
            container,
            false
        )

        // 파이어 베이스에서 현재 접속한 유저의 정보 가져옴
        var user = auth.currentUser
        var email = user?.email

        RetrofitClient.retrofitService.useridget2(email.toString())
            .enqueue(object : Callback<List<useridDTO>> {
                override fun onResponse(
                    call: Call<List<useridDTO>>,
                    response: Response<List<useridDTO>>
                ) {
                    if (response.isSuccessful) {

                        val id = response.body()?.get(0)?.user_id // DB에서 userid 가져오기
                        var name = email?.split('@')
                        binding.userId1.text = (name?.get(0)) + "님! "

                        try {
                            Log.v("userid_get", id.toString())
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } else {
                        Log.v("userid_get", "error = " + String.valueOf(response.code()))
                    }
                }

                override fun onFailure(call: Call<List<useridDTO>>, t: Throwable) {
                    Log.d("userid_get", "get" + t.toString())
                }
            })
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 프로필 수정 페이지로 이동
        editprofile.setOnClickListener{
            val intent = Intent(activity, SubActivityTrigger::class.java)
            startActivity(intent)
        }

        //AboutUs 페이지로 이동
        about_us.setOnClickListener{
            val intent = Intent(activity, SubActivityAboutUs::class.java)
            startActivity(intent)
        }

        // 비밀번호 재설정 페이지로 이동
        to_password.setOnClickListener{
            val intent = Intent(activity, SubActivityPassword::class.java)
            startActivity(intent)
        }

        logoutbutton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}