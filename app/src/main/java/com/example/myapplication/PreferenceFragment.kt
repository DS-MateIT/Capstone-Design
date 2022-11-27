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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.preference_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class PreferenceFragment: Fragment() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val view = inflater.inflate(R.layout.preference_main, container, false)
        val binding = com.example.myapplication.databinding.PreferenceMainBinding.inflate(
            inflater,
            container,
            false
        )

        // 검색어 get, 연관검색어
        RetrofitClient.retrofitService.srchresult()
            .enqueue(object : Callback<List<srchRelatedDTO>> {
                override fun onResponse(
                    call: Call<List<srchRelatedDTO>>,
                    response: Response<List<srchRelatedDTO>>
                ) {
                    if (response.isSuccessful) {
                        try {

                            val srchresult = response.body()?.get(0)?.srch_keyword //가장 최근검색어 출력해보기
                            //binding.srchtest.text = srchresult.toString()

                                //내가 자주 검색하는 검색어 버튼 생성
                                for (srchkeyword in 0 until 3) { //srchresult!!.length ->  index오류이유? 3으로 변경해봄
                                    var srchkeyword = Button(context).apply {
                                        //width = 30
                                        //height = 20
                                        background = getDrawable(context, R.drawable.hashtag)
                                        text = "#" + response.body()?.get(srchkeyword)?.srch_keyword
                                        val lp = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        lp.setMargins(10, 10, 10, 10)
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
                                for (i in 0 until 3) {
                                    //연관검색어 1
                                    var srchcraw = Button(context).apply {
                                        //width = 30
                                        //height = 20
                                        background = getDrawable(context, R.drawable.hashtag)
                                        text =
                                            "#" + response.body()?.get(i)?.srch_craw1 //craw1,2,3 다 적용하기
                                        val lp = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        lp.setMargins(10, 10, 10, 10)
                                        layoutParams = lp

                                        setOnClickListener {
                                            //클릭하면 클릭한 키워드로 영상 서치하게 ?
                                            //btnaction 함수 생성하기
                                        }
                                    }

                                    //연관검색어 2
                                    var srchcraw2 = Button(context).apply {
                                        //width = 30
                                        //height = 20
                                        background = getDrawable(context, R.drawable.hashtag)
                                        text =
                                            "#" + response.body()?.get(i)?.srch_craw2 //craw1,2,3 다 적용하기
                                        val lp = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        lp.setMargins(10, 10, 10, 10)
                                        layoutParams = lp

                                        setOnClickListener {
                                            //클릭하면 클릭한 키워드로 영상 서치하게 ?
                                            //btnaction 함수 생성하기
                                        }
                                    }

                                    //연관검색어 3
                                    var srchcraw3 = Button(context).apply {
                                        //width = 30
                                        //height = 20
                                        background = getDrawable(context, R.drawable.hashtag)
                                        text =
                                            "#" + response.body()?.get(i)?.srch_craw3 //craw1,2,3 다 적용하기
                                        val lp = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        lp.setMargins(10, 10, 10, 10)
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
                        //Log.v("srch_get", "error = " + String.valueOf(response.code()))

                    }
                }


                override fun onFailure(call: Call<List<srchRelatedDTO>>, t: Throwable) {
                    Log.d("srch_get", "get" + t.toString())
                }

            })

/*
* //S3 파이차트 불러오기
            // Cognito 샘플 코드. CredentialsProvider 객체 생성
            val credentialsProvider = CognitoCachingCredentialsProvider(
                context,
                "us-east-2:98f15302-78cb-4d26-9ae4-c4ef2c378ccd",  // 자격 증명 풀 ID
                Regions.US_EAST_2 // 리전
            )

            // 반드시 호출
            TransferNetworkLossHandler.getInstance(context)

            // TransferUtility 객체 생성
            val transferUtility = TransferUtility.builder()
                .context(context)
                .defaultBucket("mateityoutube") // 디폴트 버킷 이름.
                .s3Client(AmazonS3Client(credentialsProvider, Region.getRegion(Regions.US_EAST_2)))
                .build()

            // 다운로드 실행. object: "wordcloud_test.png". 두 번째 파라메터는 Local경로 File 객체.
            // 대상 객체 ex) "Bucket_Name/SomeFile.mp4"
            val downloadObserver = transferUtility.download("piechart.png", File(context?.cacheDir, "/piechart.png"))

            // 다운로드 과정을 알 수 있도록 Listener를 추가
            downloadObserver.setTransferListener(object : TransferListener {
                override fun onStateChanged(id: Int, state: TransferState) {
                    if (state == TransferState.COMPLETED) {
                        Log.d("AWS", "DOWNLOAD Completed!")
                        val imgpath = "${context?.cacheDir}/piechart.png" // 내부 저장소에 저장되어 있는 이미지 경로
                        val bm = BitmapFactory.decodeFile(imgpath)

                        binding.piechart.setImageBitmap(bm)
                        //내부 저장소에 저장된 이미지를 이미지뷰에 셋
                        //이미지 뷰에 나타내기
                    }
                }

                override fun onProgressChanged(id: Int, current: Long, total: Long) {
                    try {
                        val done = (((current.toDouble() / total) * 100.0).toInt()) //as Int
                        Log.d("AWS", "DOWNLOAD - - ID: $id, percent done = $done")
                    } catch (e: Exception) {
                        Log.d("AWS", "Trouble calculating progress percent", e)
                    }
                }

                override fun onError(id: Int, ex: Exception) {
                    Log.d("AWS", "DOWNLOAD ERROR - - ID: $id - - EX: ${ex.message.toString()}")
                }
            })
        //파이차트 끝*/


        // 파이어 베이스에서 현재 접속한 유저의 정보 가져옴
        var user = auth.currentUser
        var email = user?.email

        val colors = java.util.ArrayList<Int>()
        colors.add(resources.getColor(R.color.color1))
        colors.add(resources.getColor(R.color.color2))
        colors.add(resources.getColor(R.color.color3))
        colors.add(resources.getColor(R.color.color4))
        colors.add(resources.getColor(R.color.color5))
        colors.add(resources.getColor(R.color.color6))
        colors.add(resources.getColor(R.color.color7))
        colors.add(resources.getColor(R.color.color8))
        colors.add(resources.getColor(R.color.color9))
        colors.add(resources.getColor(R.color.color10))

        // 파이차트 테스트 시작
        RetrofitClient.retrofitService.PieChartget(email.toString()).enqueue(object :
            Callback<List<PieChartDTO>> {
            override fun onResponse(call: Call<List<PieChartDTO>>, response: Response<List<PieChartDTO>>) {
                if (response.isSuccessful) {

                        var values:ArrayList<PieEntry> = ArrayList()

                        for (i in 0 until response.body()!!.size) {
                            val word = response.body()?.get(i)?.stt_word
                            val count = response.body()?.get(i)?.stt_word_count.toString()
                            values.add(PieEntry(count!!.toFloat(),word.toString()))
                        }

                        var dataset = PieDataSet(values, "")
                        var data = PieData(dataset)
                        binding.pie.data = data

                        dataset.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
                        dataset.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

                        dataset.setColors(colors)
                        //dataset.setColors(*ColorTemplate.JOYFUL_COLORS)  // 컬러 템플릿 이용 = 알록달록해요!
                        dataset.valueTextSize = 15f
                        dataset.valueTextColor = resources.getColor(R.color.text)
                        dataset.setValueFormatter(PercentFormatter()) // 백분율 소수점 한자리

                        /*
                        * // legend 설정 하나 밖에 안나와서 무쓸모,,,
                        *                     val legend: Legend = binding.pie.getLegend()
                        legend.form = Legend.LegendForm.CIRCLE
                        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                        legend.orientation = Legend.LegendOrientation.HORIZONTAL
                        legend.setDrawInside(false)
                        legend.textColor = resources.getColor(R.color.text)*/

                        binding.pie.setCenterTextColor(R.color.main_theme)
                        binding.pie.description.isEnabled = false
                        binding.pie.setUsePercentValues(true)  // 백분율 소수점 두자리까지
                        binding.pie.centerTextRadiusPercent = 0f
                        binding.pie.isDrawHoleEnabled = true
                        binding.pie.legend.isEnabled = false
                        binding.pie.setEntryLabelColor(resources.getColor(R.color.text))
                        binding.pie.setEntryLabelTextSize(15f)
                        binding.pie.setHoleColor(resources.getColor(R.color.main_theme))  // 가운데 도넛색 배경색으로 지정
                        binding.pie.animateY(2000, Easing.EaseInOutCubic)



                    try {
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    //Log.v("Pie_get", "error = " + String.valueOf(response.code()))
                }
            }

            override fun onFailure(call: Call<List<PieChartDTO>>, t: Throwable) {
                Log.d("Pie_get","get"+t.toString())
            }
        })




        //새로고침
        binding.swipe.setOnRefreshListener {

            swipe.isRefreshing = false
        }





        return binding.root
    }


}





