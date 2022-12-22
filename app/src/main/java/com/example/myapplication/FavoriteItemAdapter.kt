package com.example.myapplication


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemFavoriteBinding
import com.google.firebase.auth.FirebaseAuth


private val auth: FirebaseAuth = FirebaseAuth.getInstance()


class FavoriteItemAdapter(val context: Context) : RecyclerView.Adapter<FavHolder>() {
    var fav_listData = mutableListOf<FavoriteItemData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavHolder(binding)
    }

    override fun onBindViewHolder(holder: FavHolder, position: Int) {
        val Favlist = fav_listData[position]
        holder.setData(Favlist)

        //id 값 VideoActivity로 전달
        holder.itemView.setOnClickListener {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra("id",Favlist.id)
            intent.putExtra("title", Favlist.title)
            intent.putExtra("publishedAt", "2019-11-16")
            intent.putExtra("srchquery","검색결과가 아닙니다.")



            context.startActivity(intent)

            notifyItemChanged(position)
        }

        var user = auth.currentUser
        var email = user?.email

    }
    override fun getItemCount(): Int {
        return fav_listData.size

    }
}



class FavHolder(val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setData(fav_listData: FavoriteItemData) {
        //binding.textview1.text = Bookmark.title
        val videoId = fav_listData.id

        // id값으로 썸네일 불러오기
        val videoIdString = "https://img.youtube.com/vi/" + videoId + "/sddefault.jpg"
        Glide.with(binding.root)
            .load(videoIdString)
            .centerCrop()
            .into(binding.favThumbnail)
    }

}



