package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.SearchFilterBinding
import com.example.myapplication.databinding.SearchPlayBinding
import com.example.myapplication.databinding.VideoPlayerBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.item_play.*
import kotlinx.android.synthetic.main.search_play.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


// 비디오 플레이어 - 목록에서 영상 클릭시 보여지는 화면
class VideoPlayerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    lateinit var binding: SearchPlayBinding

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {
        if (!p2) {
            val playKey = intent.getStringExtra("playKey")
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

        videoPlayerView.initialize(getString(R.string.youtube_key), this@VideoPlayerActivity)!!

        val title = intent.getStringExtra("title").toString()
        videoTitleTextView.setText(title);


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



    private fun getresult() {
        val gson : Gson = GsonBuilder()
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
                        if(response.isSuccessful.not()) {
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
*/
    }


}








