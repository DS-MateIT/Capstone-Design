package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myapplication.databinding.ItemSearchBinding
import com.example.myapplication.databinding.VideoPlayerBinding
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.google.android.youtube.player.YouTubeStandalonePlayer
import com.google.android.youtube.player.YouTubeStandalonePlayer.createVideoIntent

/*
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

 */

class MyViewHolder(val binding: ItemSearchBinding): RecyclerView.ViewHolder(binding.root)
class MyAdapter(val context: Context, val datas: ArrayList<SearchResult>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MyViewHolder(ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyViewHolder).binding
        val model = datas!![position].snippet!!

        //binding.dateTextView.text = model.publishedAt
        binding.searchVideoTitle.text = model.title
        //binding.contentsTextView.text = model.description

        if(model.thumbnails != null && model.thumbnails.medium != null){
            Glide.with(binding.root)
                .load(model.thumbnails.medium.url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .dontAnimate()
                .into(binding.searchVideoThumbnails)
        }




        //방법1: VideoPlayerActivity로 이동하여 재생
        holder.itemView.setOnClickListener {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra("title", datas!![position].snippet!!.title.toString())
            intent.putExtra("id", datas!![position].id!!.videoId.toString())

        /*
        //방법2: 스탠드얼론플레이어로 재생하는 방법 - test
        holder.itemView.setOnClickListener{
            val intent = YouTubeStandalonePlayer.createVideoIntent(
                context as Activity,
                "AIzaSyBwDt0NvNliavwfyYm2kSJCNt10Rc0-bxk", //유튜브 api 키
                datas!![position].id!!.videoId.toString(), //비디오 id
                0, //몇초후 재생
                true, //자동실행 할지 말지
                true //작은 뷰박스에서 재생할지 말지 false하면 풀화면으로 재생
            )

         */
            context.startActivity(intent)
        }

    }
}