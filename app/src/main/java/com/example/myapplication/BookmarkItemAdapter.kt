package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemBookmarkBinding
import com.google.android.youtube.player.YouTubeStandalonePlayer
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_bookmark.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.ArrayList

private val auth: FirebaseAuth = FirebaseAuth.getInstance()
class BookmarkItemAdapter(val context: Context) : RecyclerView.Adapter<Holder>(){
    var listData = mutableListOf<BookmarkItemData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val Bookmark = listData[position]
        holder.setData(Bookmark)

        // id값, title VideoPlayerActivity로 전달
        holder.itemView.setOnClickListener {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra("title", Bookmark.title)
            intent.putExtra("id", Bookmark.id)

            context.startActivity(intent)
        }

        var user = auth.currentUser
        var email = user?.email

        holder.itemView.BMtabbutton.setOnClickListener {
            RetrofitClient.retrofitService.bmvideoData(email.toString(), Bookmark.id.toString(),
                Bookmark.title.toString()
            )
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
            Toast.makeText(context, "북마크 삭제", Toast.LENGTH_LONG).show()
            //notifyItemRemoved(position)
            notifyItemChanged(position)
            //notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }
}

class Holder(val binding: ItemBookmarkBinding) : RecyclerView.ViewHolder(binding.root){
    fun setData(Bookmark: BookmarkItemData){
        binding.textview1.text = Bookmark.title
        val videoId = Bookmark.id

        // id값으로 썸네일 불러오기
        val videoIdString = "https://img.youtube.com/vi/" + videoId + "/maxresdefault.jpg"
        Glide.with(binding.root)
            .load(videoIdString)
            .centerCrop()
            .into(binding.bmvideo)
    }
}