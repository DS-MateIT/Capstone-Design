package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.preference_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.String

class PreferenceFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val view = inflater.inflate(R.layout.preference_main, container, false)
        val binding = com.example.myapplication.databinding.PreferenceMainBinding.inflate(inflater, container, false)

        // 검색어 get, 연관검색어
        RetrofitClient.retrofitService.srchresult().enqueue(object : Callback<List<srchRelatedDTO>> {
            override fun onResponse(
                call: Call<List<srchRelatedDTO>> ,
                response: Response<List<srchRelatedDTO>>
            ) {
                if (response.isSuccessful) {
                    try {

                        val srchresult =  response.body()?.get(0)?.srch_keyword //가장 최근검색어 출력해보기
                        //binding.srchtest.text = srchresult.toString()

                        //내가 자주 검색하는 검색어 버튼 생성
                        for (srchkeyword in 0 until srchresult!!.length) {
                            var srchkeyword = Button(context).apply{
                                //width = 30
                                //height = 20
                                background = getDrawable(context, R.drawable.hashtag)
                                text = "#" + response.body()?.get(srchkeyword)?.srch_keyword
                                val lp = LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                                lp.setMargins(10,10,10,10)
                                layoutParams = lp

                                setOnClickListener {
                                    //클릭하면 클릭한 키워드로 영상 서치하게 ?
                                    //btnaction 함수 생성하기
                                }
                            }
                            // 내가 자주 검색하는 검색어 리니어레이아웃에 버튼 추가
                            srchwordLayout.addView(srchkeyword)
                        }

                        //연관 키워드 버튼 생성
                        for (i in 0 until srchresult!!.length) {
                            //연관검색어 1
                            var srchcraw = Button(context).apply{
                                //width = 30
                                //height = 20
                                background = getDrawable(context, R.drawable.hashtag)
                                text = "#" + response.body()?.get(i)?.srch_craw1 //craw1,2,3 다 적용하기
                                val lp = LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                                lp.setMargins(10,10,10,10)
                                layoutParams = lp

                                setOnClickListener {
                                    //클릭하면 클릭한 키워드로 영상 서치하게 ?
                                    //btnaction 함수 생성하기
                                }
                            }

                            //연관검색어 2
                            var srchcraw2 = Button(context).apply{
                                //width = 30
                                //height = 20
                                background = getDrawable(context, R.drawable.hashtag)
                                text = "#" + response.body()?.get(i)?.srch_craw2 //craw1,2,3 다 적용하기
                                val lp = LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                                lp.setMargins(10,10,10,10)
                                layoutParams = lp

                                setOnClickListener {
                                    //클릭하면 클릭한 키워드로 영상 서치하게 ?
                                    //btnaction 함수 생성하기
                                }
                            }

                            //연관검색어 3
                            var srchcraw3 = Button(context).apply{
                                //width = 30
                                //height = 20
                                background = getDrawable(context, R.drawable.hashtag)
                                text = "#" + response.body()?.get(i)?.srch_craw3 //craw1,2,3 다 적용하기
                                val lp = LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                                lp.setMargins(10,10,10,10)
                                layoutParams = lp

                                setOnClickListener {
                                    //클릭하면 클릭한 키워드로 영상 서치하게 ?
                                    //btnaction 함수 생성하기
                                }
                            }
                            // 연관 키워드 리니어레이아웃에 버튼 추가
                            srchcrawLayout1.addView(srchcraw)   // 연관검색어 1
                            srchcrawLayout2.addView(srchcraw2)  // 연관검색어 2
                            srchcrawLayout3.addView(srchcraw3)  // 연관검색어 3
                        }
                        
                        
                        
                        
                        

                        Log.v("srch_get", srchresult.toString())

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    Log.v("srch_get", "error = " + String.valueOf(response.code()))

                }
            }



            override fun onFailure(call: Call<List<srchRelatedDTO>> , t: Throwable) {
                Log.d("srch_get","get"+t.toString())
            }

        })




        return binding.root
    }
}




