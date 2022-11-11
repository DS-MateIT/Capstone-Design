package com.example.myapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.example.myapplication.databinding.PreferenceMainBinding
import kotlinx.android.synthetic.main.preference_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.lang.String

class PreferenceFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val view = inflater.inflate(R.layout.preference_main, container, false)
        val binding = PreferenceMainBinding.inflate(
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
                        Log.v("srch_get", "error = " + String.valueOf(response.code()))

                    }
                }


                override fun onFailure(call: Call<List<srchRelatedDTO>>, t: Throwable) {
                    Log.d("srch_get", "get" + t.toString())
                }

            })


            //S3 파이차트 불러오기
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
        //파이차트 끝





        return binding.root
    }

}





