package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemRecentBinding
import com.google.firebase.auth.FirebaseAuth


private val auth: FirebaseAuth = FirebaseAuth.getInstance()


class RecentItemAdapter(val context: Context) : RecyclerView.Adapter<RecHolder>() {
    var rec_listData = mutableListOf<RecentItemData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecHolder {
        val binding = ItemRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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


            context.startActivity(intent)

            notifyItemChanged(position)
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
        val videoIdString = "https://img.youtube.com/vi/" + videoId + "/maxresdefault.jpg"
        Glide.with(binding.root)
            .load(videoIdString)
            .centerCrop()
            .into(binding.recentThumbnail)
    }

}



/*
//최근 시청동영상 - firebase 임시 데이터
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
            Image = itemView.findViewById(R.id.recent_Thumbnail)
            title = itemView.findViewById(R.id.recent_Title)
        }
    }
}


 */

