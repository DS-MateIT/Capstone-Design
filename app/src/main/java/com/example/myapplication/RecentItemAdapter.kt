package com.example.myapplication


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

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