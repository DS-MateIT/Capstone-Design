package com.example.myapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.*
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.example.myapplication.databinding.SearchPlayBinding
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
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
/*
    lateinit var videos: VideoItem
    lateinit var player: SimpleExoPlayer
    lateinit var factory: DataSource.Factory
    lateinit var mediaFactory: ProgressiveMediaSource.Factory

    var db = Firebase.database
    var storageRef = Firebase.storage.reference

*/



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //setContentView(R.layout.search_play)

        binding = SearchPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val intent2 = intent
        srchtext.text = intent2.getStringExtra("query") //검색어 띄우기

        downloadWithTransferUtility() //S3 이미지 불러오는 함수 호출

        videoPlayerView.initialize(getString(R.string.youtube_key), this@VideoPlayerActivity)!!

        val title = intent.getStringExtra("title").toString()
        videoTitleTextView.setText(title);

        //sendReq()
        getresult()

        //북마크 클릭시 videoId 서버 전송
        val videoId = intent.getStringExtra("id").toString()
        binding.btnBook.setOnClickListener({

            binding.btnBook.setImageResource(R.drawable.bookmark)
            RetrofitClient.retrofitService.bmvideoData(videoId)
                .enqueue(object : Callback<videoIdDTO> {
                    override fun onResponse(
                        call: Call<videoIdDTO>,
                        response: Response<videoIdDTO>
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

                    override fun onFailure(call: Call<videoIdDTO>, t: Throwable) {
                        Log.d("videoid_bookmark", "post" + t.toString())
                    }

                })
        })

        /*
        factory = DefaultDataSourceFactory(applicationContext, "Ex90ExoPlayer") // 매개 두번째는 임의로 그냥 적음
        mediaFactory= ProgressiveMediaSource.Factory(factory)
        // recyclerview에서 아이템 클릭시 전송된 데이터를 가져옴

         */
        /*
        var videos = intent.getSerializableExtra("videos") as SearchResultSnippet


        var videoTitle = findViewById<TextView>(R.id.videoTitleTextView)
        videoTitle.text = videos.title
        */

        /*
        // 비디오 가져오기
        videoPlayerView = findViewById(R.id.videoPlayerView)
        val mediaItem = MediaItem.fromUri(videos.sources!!)
        val mediaSource = mediaFactory.createMediaSource(mediaItem)
        //val mediaSource = mediaFactory.createMediaSource(Uri.parse(videos.sources))
        //위에서 만든 비디오 데이터 소스를 플레이어에게 로딩하도록....
        player = SimpleExoPlayer.Builder(applicationContext).build()
        player.playWhenReady = true   // 준비가 되면 바로 재생
        player.setMediaSource(mediaSource)
        player.prepare()

        videoPlayerView.player = player




        // 워드클라우드
        var wordCloud = findViewById<ImageView>(R.id.wordCloud)
        //var url = db.getReference("videos").child("7").child("wordcloud")
        if(videoTitle.text == "설현 최근 모습, 헨리 그사건 이후 근황.................요즘 화제된 이슈 TOP24"){
            var imagesRef = storageRef.child("images/word_cloud_SEOLHYUN.png").downloadUrl.addOnSuccessListener {
                Glide.with(this).load(it).override(320,150).into(wordCloud)
            }
        }

        if(videoTitle.text == "본격 공개! 설현의 뷰티 노하우 ‘심쿵 꿀팁’ @본격연예 한밤 13회 20170228"){
            var imageRef = storageRef.child("images/word_cloud_SEOLHYUN2_HANBAM.png").downloadUrl.addOnSuccessListener{
                Glide.with(this).load(it).override(320,150).into(wordCloud)
            }
        }
        getresult()


    }
    override fun onStop() {
        videoPlayerView.player = null
        player.release()   // 릴리스해주지 않으면 부하가 와서 영상을 많이 재생할 수 없음
        super.onStop()
    }

*/
    }
    //S3 워드클라우드 불러오기
    
    private fun downloadWithTransferUtility() {
        // Cognito 샘플 코드. CredentialsProvider 객체 생성
        val credentialsProvider = CognitoCachingCredentialsProvider(
            applicationContext,
            "us-east-2:98f15302-78cb-4d26-9ae4-c4ef2c378ccd",  // 자격 증명 풀 ID
            Regions.US_EAST_2 // 리전
        )

        // 반드시 호출해야 한다.
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
        val downloadObserver = transferUtility.download("${videoId}/${videoId}.png", File(cacheDir , "/${videoId}/${videoId}.png"))

        // 다운로드 과정을 알 수 있도록 Listener를 추가할 수 있다.
        downloadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    Log.d("AWS", "DOWNLOAD Completed!")
                    val imgpath = "$cacheDir/${videoId}/${videoId}.png" // 내부 저장소에 저장되어 있는 이미지 경로
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
    /*
    * private fun sendReq() {
        var input = HashMap<String, String>()
        input["title"] = "TEST입니다"
        RetrofitBuilder.api.post(input).enqueue(object: Callback<PostDTO> {
            override fun onResponse(call: Call<PostDTO>, response: Response<PostDTO>) {
                if(response.isSuccessful) {
                    Log.d("test", "연결성공")
                    var a: PostDTO = response.body()!!
                }
            }

            override fun onFailure(call: Call<PostDTO>, t: Throwable) {
                Log.d("test", "연결실패")
            }

        })
    }

    object RetrofitBuilder {
        var api: RetrofitPost

        init {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            api = retrofit.create(RetrofitPost::class.java)
        }
    }*/


}








