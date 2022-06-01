package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

// 비디오 플레이어 - 목록에서 영상 클릭시 보여지는 화면
class VideoPlayerActivity() : AppCompatActivity() {
    lateinit var videoPlayerView: PlayerView
    lateinit var videos: VideoItem
    lateinit var player: SimpleExoPlayer
    lateinit var factory: DataSource.Factory
    lateinit var mediaFactory: ProgressiveMediaSource.Factory

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_player)
        factory = DefaultDataSourceFactory(applicationContext, "Ex90ExoPlayer") // 매개 두번째는 임의로 그냥 적음
        mediaFactory= ProgressiveMediaSource.Factory(factory)
        // recyclerview에서 아이템 클릭시 전송된 데이터를 가져옴
        videos = intent.getSerializableExtra("videos") as VideoItem
        var videoTitle = findViewById<TextView>(R.id.videoTitleTextView)
        videoTitle.text = videos.title

        // 비디오 가져오기
        videoPlayerView = findViewById<PlayerView>(R.id.video_playerView)
        val mediaSource = mediaFactory.createMediaSource(Uri.parse(videos.sources))
        //위에서 만든 비디오 데이터 소스를 플레이어에게 로딩하도록....
        player = SimpleExoPlayer.Builder(applicationContext).build()
        player.playWhenReady = true   // 준비가 되면 바로 재생
        player.prepare(mediaSource)

        videoPlayerView.player = player
    }
    override fun onStop() {
        videoPlayerView.player = null
        player.release()   // 릴리스해주지 않으면 영상을 많이 재생할 수 없음
        super.onStop()
    }
}
