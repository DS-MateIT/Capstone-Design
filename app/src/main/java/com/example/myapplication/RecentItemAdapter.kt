package com.example.myapplication


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecentItemAdapter(var context: Context, private val items: ArrayList<RecentItemData> ) :
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
        val item = items[position]
        Glide.with(holder.itemView)
            .load(items.get(position).Image)
            .into(holder.Image);
        //holder.rate.text = items.get(position).rate
        //holder.keyword.text = items.get(position).keyword
        //holder.hashtag.text = items.get(position).hashtag
    }

    //viewHolder 단위 객체로 view 의 데이터를 설정
    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var Image: ImageView
        //var rate: TextView
        //var keyword: TextView
        //var hashtag: TextView

        init {
            Image = itemView.findViewById(R.id.recentVideo)
            //rate = itemView.findViewById(R.id.rate)
            //keyword = itemView.findViewById(R.id.keyword)
            //hashtag = itemView.findViewById(R.id.hashtag)
        }
    }
}