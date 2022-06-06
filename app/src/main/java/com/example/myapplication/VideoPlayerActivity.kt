package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// 비디오 플레이어 - 목록에서 영상 클릭시 보여지는 화면
class VideoPlayerActivity() : AppCompatActivity() {
    lateinit var videoPlayerView: PlayerView
    lateinit var videos: VideoItem
    lateinit var player: SimpleExoPlayer
    lateinit var factory: DataSource.Factory
    lateinit var mediaFactory: ProgressiveMediaSource.Factory

    var db = Firebase.database
    var storageRef = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_play)
        factory = DefaultDataSourceFactory(applicationContext, "Ex90ExoPlayer") // 매개 두번째는 임의로 그냥 적음
        mediaFactory= ProgressiveMediaSource.Factory(factory)
        // recyclerview에서 아이템 클릭시 전송된 데이터를 가져옴
        videos = intent.getSerializableExtra("videos") as VideoItem
        var videoTitle = findViewById<TextView>(R.id.videoTitleTextView)
        videoTitle.text = videos.title

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

    }
    override fun onStop() {
        videoPlayerView.player = null
        player.release()   // 릴리스해주지 않으면 부하가 와서 영상을 많이 재생할 수 없음
        super.onStop()
    }
}