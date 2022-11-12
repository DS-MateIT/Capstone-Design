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
        val videoIdString = "https://img.youtube.com/vi/" + videoId + "/maxresdefault.jpg"
        Glide.with(binding.root)
            .load(videoIdString)
            .centerCrop()
            .into(binding.favThumbnail)
    }

}





/*
//테스트 2 시연 - firebase 임시 데이터
class FavoriteItemAdapter(private val items: ArrayList<favoriteItemData> ) : RecyclerView.Adapter<FavoriteItemAdapter.ViewHolder>() {


    //보여줄 아이템 개수가 몇개인지 알려줌
    override fun getItemCount(): Int = items.size

    //생성된 view에 보여줄 데이터를 설정
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
    }

    //보여줄 아이템 개수만큼 view를 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite,parent,false)
        return FavoriteItemAdapter.ViewHolder(view)
    }

    //viewHolder 단위 객체로 view 의 데이터를 설정
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

 */