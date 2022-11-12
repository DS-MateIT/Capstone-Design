package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.myapplication.databinding.ItemSearchBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


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
class MyAdapter(val context: Context, val datas: ArrayList<SearchResult>?, val word: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        val model = datas!![position].snippet!!

        //binding.dateTextView.text = model.publishedAt
        binding.searchVideoTitle.text = model.title
        //binding.contentsTextView.text = model.description

        if (model.thumbnails != null && model.thumbnails.medium != null) {
            Glide.with(binding.root)
                .load(model.thumbnails.medium.url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .dontAnimate()
                .into(binding.searchVideoThumbnails)
        }



        // mlkit 썸네일 문구 인식
            val mlkittext = Glide.with(binding.root)
                .asBitmap()
                .load(model.thumbnails.medium?.url)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                        //mlkit bitmap으로 변환 resource 받은거 확인
                        Log.d("mlkit","resource onResource Ready")

                        val image = InputImage.fromBitmap(resource,0)

                        val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
                        val result : Task<Text> = recognizer.process(image)
                            .addOnSuccessListener { visionText ->
                                val mlkit_Text : String = visionText.text
                                //binding.views.text = mlkit_Text


                                //일치율 임의 테스트
                                /*val range = (15..60)  // 1 <= n <= 15
                                val test = range.random()

                                val ratestring = "일치율 "
                                val pstring = "%"


                                binding.rate2.text = ratestring+test.toString()+pstring

                                if (test > 50 )
                                    binding.rate2.background =
                                        ContextCompat.getDrawable(context, R.drawable.border_greenround)
                                else
                                    binding.rate2.background =
                                        ContextCompat.getDrawable(context, R.drawable.border_redround)
*/
                                Log.d("mlkit",visionText.text)

                                //레트로핏 전송
                                val mlkit_data = visionText.text

                                RetrofitClient.retrofitService.postData(mlkit_data).enqueue(object : Callback<mlkitDTO> {
                                    override fun onResponse(
                                        call: Call<mlkitDTO>,
                                        response: Response<mlkitDTO>
                                    ) {
                                        //통신 성공
                                        if (response.isSuccessful) {
                                            try {
                                                val result = response.body().toString()
                                                Log.v("post", result) //mlkit 값 서버에 전송

                                            } catch (e: IOException) {
                                                e.printStackTrace()
                                            }
                                        } else {
                                            Log.v("post","error = " + java.lang.String.valueOf(response.code()))

                                        }


                                    }


                                    override fun onFailure(call: Call<mlkitDTO>, t: Throwable) {
                                        Log.d("post","post"+t.toString())
                                    }

                                })

                            }
                            .addOnFailureListener { e ->
                            }


                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        Log.d("mlkit","onLoadCleared")
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                    }
                })

        // 검색어 get, 연관검색어
        // datas!![position].id!!.videoId.toString()
        RetrofitClient.retrofitService.rateResult(word, datas!![position].id!!.videoId.toString()).enqueue(object : Callback<List<SrchRateDTO>> {
            override fun onResponse(
                call: Call<List<SrchRateDTO>> ,
                response: Response<List<SrchRateDTO>>
            )
            {
                if (response.isSuccessful) {
                    Log.d("rate","!!!!!!!!!!!!!!Rate!!!!!!!!!!")
                    //for(i in 0..4){
                    if(response.body()?.size != 0) {
                        val result = response.body()?.get(0)?.rate

                        Log.d("rate",result.toString())

                        if (result != null) {
                            binding.rate2.text = "일치율 " + result.toString() + "%"
                        }




                        //if (result != null) {
                        if (result != null) {
                            if (result > 50 )
                                binding.rate2.background =
                                    ContextCompat.getDrawable(context, R.drawable.border_greenround)

                            else
                                binding.rate2.background =
                                    ContextCompat.getDrawable(context, R.drawable.border_redround)



                        }
                        //}

                    }


                    }


                    /*val result0 = response.body()?.get(i)?.result0
                    val result1 = response.body()?.get(i)?.result1
                    val result2 = response.body()?.get(i)?.result2
                    val result3 = response.body()?.get(i)?.result3
                    val result4 = response.body()?.get(i)?.result4

                    val resultList: List<Float> =
                        listOf(result0, result1, result2, result3, result4) as List<Float>
*/


                else{
                    Log.v("rate", "retrofit 실패!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                }

            }
            override fun onFailure(call: Call<List<SrchRateDTO>>, t: Throwable) {
                Log.d("retrofit", "에러입니다. => ${t.message.toString()}")
            }


        })


        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        // 파이어 베이스에서 현재 접속한 유저의 정보 가져옴
        var user = auth.currentUser
        var email = user?.email


        //VideoPlayerActivity로 이동하여 재생
        holder.itemView.setOnClickListener {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra("title", datas!![position].snippet!!.title.toString())
            intent.putExtra("id", datas!![position].id!!.videoId.toString())

            val title = datas!![position].snippet!!.title.toString()
            val videoId = datas!![position].id!!.videoId.toString()

            //videoID값 전송 - 최근 시청한 영상에 쓰일것임 아마도
            RetrofitClient.retrofitService.videoData(email!!,videoId,title).enqueue(object : Callback<videoIdDTO>{
                override fun onResponse(call: Call<videoIdDTO>, response: Response<videoIdDTO>) {
                    if (response.isSuccessful) {
                        try {
                            val result = response.body().toString()
                            Log.v("videoid_watch", result)
                            Log.v("videoid_watch", title)

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } else {
                        Log.v("videoid_watch","error = " + java.lang.String.valueOf(response.code()))

                    }
                }

                override fun onFailure(call: Call<videoIdDTO>, t: Throwable) {
                    Log.d("videoid_watch","post"+t.toString())
                }

            })



            context.startActivity(intent)
        }

    }

}




