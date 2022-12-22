package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.databinding.ItemRecentBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_recent.view.*
import kotlin.coroutines.coroutineContext


private val auth: FirebaseAuth = FirebaseAuth.getInstance()


class RecentItemAdapter(val context: Context) : RecyclerView.Adapter<RecHolder>() {
    var rec_listData = mutableListOf<RecentItemData>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecHolder {
        val binding = ItemRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.likebutton.setOnClickListener {
            Toast.makeText(context, "영상을 추천합니다!", Toast.LENGTH_SHORT).show()
        }

        return RecHolder(binding)
    }

    override fun onBindViewHolder(holder: RecHolder, position: Int) {
        val Recentlist = rec_listData[position]
        holder.setData(Recentlist)


        //id 값 VideoActivity로 전달
        holder.itemView.setOnClickListener {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra("id",Recentlist.id)
            intent.putExtra("title", Recentlist.title)
            intent.putExtra("publishedAt", "2019-11-16")
            intent.putExtra("srchquery","검색결과가 아닙니다.")


            context.startActivity(intent)

            //notifyItemChanged(position)
        }

        var user = auth.currentUser
        var email = user?.email

        }
        override fun getItemCount(): Int {
            return rec_listData.size

        }
}


class RecHolder(val binding: ItemRecentBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setData(rec_listData: RecentItemData) {
        binding.recentTitle.text = rec_listData.title
        val videoId = rec_listData.id

        // id값으로 썸네일 불러오기
        val videoIdString = "https://img.youtube.com/vi/" + videoId + "/sddefault.jpg"
        Glide.with(binding.root)
            .load(videoIdString)
            .centerCrop()
            .into(binding.recentThumbnail)
    }


}



