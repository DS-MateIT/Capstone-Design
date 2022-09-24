package com.example.myapplication


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.databinding.ItemRecentBinding
import com.example.myapplication.databinding.ItemSearchBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/*
class RecentItemAdapter(var context: Context, private val items: ArrayList<VideoItem> ) :
    RecyclerView.Adapter<RecentItemAdapter.VH>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_recent, parent, false)
        return VH(itemView)
    }

    //보여줄 아이템 개수가 몇개인지 알려줌
    override fun getItemCount(): Int = items.size

    //생성된 view에 보여줄 데이터를 설정
    override fun onBindViewHolder(holder: VH, position: Int) {
        val vh = holder
        val item = items[position]
        // Glide로 썸네일 이미지 가져오기
        Glide.with(holder.itemView)
            .load(items.get(position).thumb)   // db의 thumb에서 가져옴
            .into(holder.Image);
        holder.title.text = items.get(position).title
        //holder.rate.text = items.get(position).rate
        //holder.keyword.text = items.get(position).keyword
        //holder.hashtag.text = items.get(position).hashtag

        vh.bind(item)
    }

    //viewHolder 단위 객체로 view 의 데이터를 설정
    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var Image: ImageView
        var title: TextView
        //var rate: TextView
        //var keyword: TextView
        //var hashtag: TextView
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
            Image = itemView.findViewById(R.id.recentVideo)
            title = itemView.findViewById(R.id.videoTitle)
        }
    }
}

*/
class recentViewHolder(val binding: ItemRecentBinding): RecyclerView.ViewHolder(binding.root)
class recentAdapter(val context: Context, val datas: ArrayList<SearchResult>?, val recentVideoId: SearchResultId) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        recentViewHolder(ItemRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as recentViewHolder).binding
        val model = datas!![position].snippet!!

        //val recentvideoId = datas!![position].id!!.videoId


        //binding.recentTitle.text = model.title

        //최근 시청 영상 call
        /*
        var call: Call<SearchListResponse> = MyApplication.videoIDlist.getvideoList(
            getString(R.string.youtube_key),
            videoId,
            "snippet"
        )

         */

        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        // 파이어 베이스에서 현재 접속한 유저의 정보 가져옴
        var user = auth.currentUser
        var email = user?.email


        //최근 시청한 동영상 - 함수만들기?
        RetrofitClient.retrofitService.videoIdget(email.toString()).enqueue(object :
            Callback<List<videoIdDTO>> {
            override fun onResponse(call: Call<List<videoIdDTO>>, response: Response<List<videoIdDTO>>) {
                if (response.isSuccessful) {
                    try {
                        val recentvideoID = response.body().toString()
                        Log.v("videoid_watch", recentvideoID) //video_id 값 받는거 확인


                        /*
                        //videoid 값 전동하기 fragment -> adapter
                        HomeFragment().apply {
                            arguments = Bundle().apply {
                                putString("recentVideoId","${recentvideoID}")
                            }
                        }

                        //최근시청영상
                        item_recent = binding.itemRecent
                        item_recent.layoutManager = LinearLayoutManager(requireContext())
                        //item_recent.adapter = recentAdapter(requireContext(),recentvideoArray, recentvideoID)


                         */

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    Log.v("videoid_watch","error = " + java.lang.String.valueOf(response.code()))

                }
            }

            override fun onFailure(call: Call<List<videoIdDTO>>, t: Throwable) {
                Log.d("videoid_watch","post"+t.toString())
            }

        })



        if (model.thumbnails != null && model.thumbnails.medium != null) {
            Glide.with(binding.root)
                .load(model.thumbnails.medium.url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .dontAnimate()
                .into(binding.recentThumbnail)
        }


        //VideoPlayerActivity로 이동하여 재생
        holder.itemView.setOnClickListener {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra("title", datas!![position].snippet!!.title.toString())
            intent.putExtra("id", datas!![position].id!!.videoId.toString())

            context.startActivity(intent)
        }

    }



}
