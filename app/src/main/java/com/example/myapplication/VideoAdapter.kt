package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class VideoAdapter(var context: Context, var videoItems: ArrayList<VideoItem>) :
    RecyclerView.Adapter<VideoAdapter.VH>() {
    lateinit var listener: AdapterView.OnItemClickListener
    var factory: DataSource.Factory
    var mediaFactory: ProgressiveMediaSource.Factory
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_recent, parent, false)

        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val vh = holder as VH
        val videoItem = videoItems[position]
        //vh.tvTitle.setText(videoItem.title)
        //vh.tvDesc.setText(videoItem.description)

        //플레이어가 실행할 비디오데이터 소스 객체 생성( CD or LP ) 미디어 팩토리로부터..
        val mediaSource = mediaFactory.createMediaSource(Uri.parse(videoItem.sources))
        //위에서 만든 비디오 데이터 소스를 플레이어에게 로딩하도록....
        vh.player.prepare(mediaSource)

        // videoItem을 바인딩
        vh.bind(videoItem)
    }

    override fun getItemCount(): Int {
        return videoItems.size
    }

    //inner class..
    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //var title: TextView  // 영상의 제목
        //var desc: TextView   // 영상의 설명
        var pv: PlayerView
        var player: SimpleExoPlayer

        fun bind(item: VideoItem) {
            //title.text = item.title
            itemView.setOnClickListener{
                Intent(context, VideoPlayerActivity::class.java).apply{
                    putExtra("videos", item)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
        init {
            //title = itemView.findViewById(R.id.videoTitleTextView)
            //desc = itemView.findViewById(R.id.tv_desc)
            pv = itemView.findViewById(R.id.recentVideo)
            player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector(context))
            pv.player = player
            pv.useController = false  // 플레이어의 컨트롤러를 숨김(썸네일이미지처럼 보이도록)
        }

    }

    init {
        factory = DefaultDataSourceFactory(context, "Ex90ExoPlayer") // 매개 두번째는 임의로 그냥 적음
        mediaFactory = ProgressiveMediaSource.Factory(factory)
    }
}