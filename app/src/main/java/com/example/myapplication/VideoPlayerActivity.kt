package com.example.myapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.*
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.example.myapplication.databinding.SearchPlayBinding
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.internal.t
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.search_play.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException


// 비디오 플레이어 - 목록에서 영상 클릭시 보여지는 화면
class VideoPlayerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var binding: SearchPlayBinding

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {
        if (!p2) {
            val playKey = intent.getStringExtra("id")
            p1!!.cueVideo(playKey);


        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        Log.d("player","실패")
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //setContentView(R.layout.search_play)

        binding = SearchPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //인텐트 테스트
        val srchtext = intent.getStringExtra("srchquery").toString()
        videoSrchtextTextView.setText(srchtext)

        //srchtext.text = "아가씨 리뷰" //임시 텍스트

        
        downloadWithTransferUtility() //S3 이미지 불러오는 함수 호출

        videoPlayerView.initialize(getString(R.string.youtube_key), this@VideoPlayerActivity)!!

        val title = intent.getStringExtra("title").toString()
        videoTitleTextView.setText(title)

        val publishedAt = intent.getStringExtra("publishedAt").toString()
        views.setText(publishedAt)

        //sendReq()
        getresult()

        // 파이어 베이스에서 현재 접속한 유저의 정보 가져옴
        var user = auth.currentUser
        var email = user?.email
        var i = false //북마크

        val videoId = intent.getStringExtra("id").toString()



        // 검색어 get, 연관검색어
        // datas!![position].id!!.videoId.toString()
        RetrofitClient.retrofitService.rateResult("$srchtext", videoId).enqueue(object : Callback<List<SrchRateDTO>> {
            override fun onResponse(
                call: Call<List<SrchRateDTO>> ,
                response: Response<List<SrchRateDTO>>
            )
            {
                if (response.isSuccessful) {
                    Log.d("rate", "!!!!!!!!!!!!!!Rate!!!!!!!!!!")
                    //for(i in 0..4){
                    if (response.body()?.size != 0) {
                        val result = response.body()?.get(0)?.rate

                        Log.d("rate", result.toString())

                        if (result != null) {
                            binding.rate3.text = "일치율 " + result.toString() + "%"
                            binding.ratekeyword.text = result.toString() + "%"
                        }
                        //if (result != null) {
                        if (result != null) {
                            if (result > 35) {
                                binding.rate3.background =
                                    ContextCompat.getDrawable(
                                        this@VideoPlayerActivity,
                                        R.drawable.border_greenround
                                    )
                                binding.connection.text = "관련있는 영상"
                                binding.connection.setTextColor(
                                    ContextCompat.getColor(
                                        applicationContext!!,
                                        R.color.green
                                    )
                                )
                            } else {
                                binding.rate3.background =
                                    ContextCompat.getDrawable(
                                        this@VideoPlayerActivity,
                                        R.drawable.border_redround
                                    )
                                binding.connection.text = "관련없는 영상"
                                binding.connection.setTextColor(
                                    ContextCompat.getColor(
                                        applicationContext!!,
                                        R.color.red
                                    )
                                )
                            }

                        }
                        //}

                    }
                }
                else{
                    Log.v("rate", "retrofit 실패!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                }

            }
            override fun onFailure(call: Call<List<SrchRateDTO>>, t: Throwable) {
                Log.d("retrofit", "에러입니다. => ${t.message.toString()}")
            }


        })





        binding.btnBook.setOnClickListener {
            //북마크 클릭시 videoId 서버 전송
            if (i == false){
                binding.btnBook.setImageResource(R.drawable.bookmark_fill)
                RetrofitClient.retrofitService.bmvideoData(email.toString(),videoId,title)
                    .enqueue(object : Callback<bookmarkDTO> {
                        override fun onResponse(
                            call: Call<bookmarkDTO>,
                            response: Response<bookmarkDTO>
                        ) {
                            if (response.isSuccessful) {
                                try {
                                    val result = response.body().toString()
                                    Log.v("videoid_bookmark", result)
                                    Log.v("videoid_bookmark", title)

                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                            } else {
                                Log.v(
                                    "videoid_bookmark",
                                    "error = " + java.lang.String.valueOf(response.code())
                                )
                            }
                        }
                        override fun onFailure(call: Call<bookmarkDTO>, t: Throwable) {
                            Log.d("videoid_bookmark", "post" + t.toString())
                        }
                    })
                i = true
                Toast.makeText(this, "북마크 저장", Toast.LENGTH_LONG).show()
            }else {
                binding.btnBook.setImageResource(R.drawable.bookmark_empty)
                RetrofitClient.retrofitService.bmvideoData(email.toString(),videoId,title)
                    .enqueue(object : Callback<bookmarkDTO> {
                        override fun onResponse(
                            call: Call<bookmarkDTO>,
                            response: Response<bookmarkDTO>
                        ) {
                            if (response.isSuccessful) {
                                try {
                                    val result = response.body().toString()
                                    Log.v("videoid_bookmark", result)

                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                            } else {
                                Log.v(
                                    "videoid_bookmark",
                                    "error = " + java.lang.String.valueOf(response.code())
                                )
                            }
                        }
                        override fun onFailure(call: Call<bookmarkDTO>, t: Throwable) {
                            Log.d("videoid_bookmark", "post" + t.toString())
                        }
                    })
                i = false
                Toast.makeText(this, "북마크 삭제", Toast.LENGTH_LONG).show()
            }
        }

    }
    
    //S3 워드클라우드 불러오기
    private fun downloadWithTransferUtility() {
        // Cognito 샘플 코드. CredentialsProvider 객체 생성
        val credentialsProvider = CognitoCachingCredentialsProvider(
            applicationContext,
            "us-east-2:98f15302-78cb-4d26-9ae4-c4ef2c378ccd",  // 자격 증명 풀 ID
            Regions.US_EAST_2 // 리전
        )

        // 반드시 호출
        TransferNetworkLossHandler.getInstance(applicationContext)

        // TransferUtility 객체 생성
        val transferUtility = TransferUtility.builder()
            .context(applicationContext)
            .defaultBucket("mateityoutube") // 디폴트 버킷 이름.
            .s3Client(AmazonS3Client(credentialsProvider, Region.getRegion(Regions.US_EAST_2)))
            .build()

        // 다운로드 실행. object: "wordcloud_test.png". 두 번째 파라메터는 Local경로 File 객체.
        // 대상 객체 ex) "Bucket_Name/SomeFile.mp4"
        val videoId = intent.getStringExtra("id") // videoid 인텐트로 받아옴
        val downloadObserver = transferUtility.download("youtube_datas/${videoId}/${videoId}.png", File(cacheDir , "/youtube_datas/${videoId}/${videoId}.png"))

        // 다운로드 과정을 알 수 있도록 Listener를 추가
        downloadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    Log.d("AWS", "DOWNLOAD Completed!")
                    val imgpath = "$cacheDir/youtube_datas/${videoId}/${videoId}.png" // 내부 저장소에 저장되어 있는 이미지 경로
                    val bm = BitmapFactory.decodeFile(imgpath)

                    binding.wordCloudImageview.setImageBitmap(bm)
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
    }

   
    //워드클라우드 끝




    private fun getresult() {
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000")  // 127.0.0.1:5000 이걸로는 안드로이드가 허용을 안해줌
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

        retrofit.create(RetrofitService::class.java).also {
            it.getresult()
                .enqueue(object : Callback<ResponseDTO> {
                    override fun onResponse(
                        call: Call<ResponseDTO>,
                        response: Response<ResponseDTO>
                    ) {
                        if (response.isSuccessful.not()) {
                            Log.d("MainActivity", "response fail")
                            return
                        }

                        val rate = findViewById<TextView>(R.id.rate3)
                        val result = response.body()?.result
                        rate.text = "일치율: " + result.toString() + "%"

                    }

                    override fun onFailure(call: Call<ResponseDTO>, t: Throwable) {
                        Log.d("retrofit", "에러입니다. => ${t.message.toString()}")
                    }
                })
        }
    }


}








