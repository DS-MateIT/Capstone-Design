package com.example.myapplication

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.FragmentHomeBinding
import com.google.android.youtube.player.YouTubeStandalonePlayer
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_recent.*
import kotlinx.android.synthetic.main.item_recent.view.*
import kotlinx.android.synthetic.main.search_play.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.String
import kotlin.Throwable
import kotlin.toString

class HomeFragment: Fragment()  {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val rec_data:MutableList<RecentItemData> = mutableListOf()
    private val fav_data:MutableList<FavoriteItemData> = mutableListOf()


    /*
    //테스트2 - 시연용 firebase 임시 데이터
    private lateinit var rv_recent: RecyclerView  // 최근 시청한 영상
    private lateinit var rv_favor: RecyclerView   // 선호 카테고리 영상

    var list_recent = ArrayList<VideoItem>()
    val menu: Menu? = null

    */


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        val RECadapter = RecentItemAdapter(requireContext())
        RECadapter.rec_listData = rec_data
        RECadapter.rec_listData.clear()
        binding.itemRecent.adapter = RECadapter
        binding.itemRecent.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        RECadapter.notifyDataSetChanged()
        binding.itemRecent.invalidate()


        val FAVadapter = FavoriteItemAdapter(requireContext())
        FAVadapter.fav_listData = fav_data
        FAVadapter.fav_listData.clear()
        binding.itemFavorite.adapter = FAVadapter
        binding.itemFavorite.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        FAVadapter.notifyDataSetChanged()
        binding.itemFavorite.invalidate()





        // 파이어 베이스에서 현재 접속한 유저의 정보 가져옴
        var user = auth.currentUser
        var email = user?.email

        //유저 정보
        RetrofitClient.retrofitService.useridget2(email.toString()).enqueue(object : Callback<List<useridDTO>>{
            override fun onResponse(call: Call<List<useridDTO>>, response: Response<List<useridDTO>>) {
                if (response.isSuccessful) {

                    //val id = response.body()?.get(0)?.user_id // DB에서 userid 가져오기
                    var name = email?.split('@')
                    binding.text1.text = "안녕하세요 " + (name?.get(0)) + "님! "

                    try {
                        //Log.v("userid_get", id.toString())
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    Log.v("userid_get", "error = " + String.valueOf(response.code()))
                }
            }

            override fun onFailure(call: Call<List<useridDTO>>, t: Throwable) {
                Log.d("userid_get","get"+t.toString())
            }
        })







        //최근 시청 영상
        RetrofitClient.retrofitService.Recentvideoid(email.toString()).enqueue(object :
            Callback<List<userRecentDTO>> {
            override fun onResponse(call: Call<List<userRecentDTO>>, response: Response<List<userRecentDTO>>) {
                if (response.isSuccessful) {


                    if (response.body() == null) {

                        binding.XdataVersion.visibility = View.VISIBLE
                        binding.OdataVersion.visibility = View.GONE
                        //Intro 노그로 소개영상 썸네일 띄우기
                        val introvideoId = "jsulDJWwXcE"
                        Glide.with(requireContext()!!)
                            .load("https://img.youtube.com/vi/jsulDJWwXcE/maxresdefault.jpg")
                            .centerCrop()
                            .into(binding.introvideo)

                        binding.introvideo!!.setOnClickListener{
                            val intent = YouTubeStandalonePlayer.createVideoIntent(
                                context as Activity,
                                getString(R.string.youtube_key), //유튜브 api 키
                                introvideoId, //비디오 id
                                0, //몇초후 재생
                                true, //자동실행 할지 말지
                                true //작은 뷰박스에서 재생할지 말지 false하면 풀화면으로 재생
                            )
                            requireContext().startActivity(intent)
                        }




                    }



                    else {
                        val RECadapter = RecentItemAdapter(requireContext())
                        RECadapter.rec_listData = rec_data
                        binding.itemRecent.adapter = RECadapter
                        binding.itemRecent.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                        RECadapter.notifyDataSetChanged()

                        for (i in 0 until response.body()!!.size) {
                            val RecentId = response.body()?.get(i)?.videoid
                            val Recentitle = response.body()?.get(i)?.title
                            RECadapter.rec_listData.add(RecentItemData(RecentId,Recentitle.toString()))
                        }
                    }




                    try {
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    Log.v("recent_get", "error = " + String.valueOf(response.code()))
                }
            }

            override fun onFailure(call: Call<List<userRecentDTO>>, t: Throwable) {
                Log.d("recent_get","get"+t.toString())

            }
        })




        //선호 영상
        RetrofitClient.retrofitService.Favoritevideoid(email.toString()).enqueue(object :
            Callback<List<userFavoriteDTO>> {
            override fun onResponse(call: Call<List<userFavoriteDTO>>, response: Response<List<userFavoriteDTO>>) {
                if (response.isSuccessful) {

                    if (response.body() == null) {

                        binding.XdataVersion.visibility = View.VISIBLE
                        binding.OdataVersion.visibility = View.GONE
                        //Intro 노그로 소개영상 썸네일 띄우기
                        val introvideoId = "jsulDJWwXcE"
                        Glide.with(requireContext()!!)
                            .load("https://img.youtube.com/vi/jsulDJWwXcE/maxresdefault.jpg")
                            .centerCrop()
                            .into(binding.introvideo)

                        binding.introvideo!!.setOnClickListener{
                            val intent = YouTubeStandalonePlayer.createVideoIntent(
                                context as Activity,
                                getString(R.string.youtube_key), //유튜브 api 키
                                introvideoId, //비디오 id
                                0, //몇초후 재생
                                true, //자동실행 할지 말지
                                true //작은 뷰박스에서 재생할지 말지 false하면 풀화면으로 재생
                            )
                            requireContext().startActivity(intent)
                        }


                    }


                    else {

                        val FAVadapter = FavoriteItemAdapter(requireContext())
                        FAVadapter.fav_listData = fav_data
                        binding.itemFavorite.adapter = FAVadapter
                        binding.itemFavorite.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                        FAVadapter.notifyDataSetChanged()


                        for (i in 0 until response.body()!!.size) {
                            val FavorId = response.body()?.get(i)?.videoid
                            val Favortitle = response.body()?.get(i)?.title
                            FAVadapter.fav_listData.add(FavoriteItemData(FavorId,Favortitle))
                        }

                    }




                    try {

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    Log.v("favorite_get", "error = " + String.valueOf(response.code()))
                }
            }

            override fun onFailure(call: Call<List<userFavoriteDTO>>, t: Throwable) {
                Log.d("favorite_get","get"+t.toString())

            }
        })



        /*
        //테스트2 시연용 - firebase 임시데이터
        rv_recent = binding.itemRecent //최근 동영상 어댑터 1
        loadrecyclerViewData()
        rv_favor = binding.itemFavorite //선호 동영상 어댑터 2
        loadrecyclerViewData()
        setHasOptionsMenu(true)
        */




        return binding.root


    }

    /*
    //테스트 2 시연 - firebase 임시 데이터
    private fun loadrecyclerViewData() {
        //val reference = FirebaseDatabase.getInstance().getReference("videos")
        val reference = Firebase.database.getReference("videos")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //list_recent.clear()
                // db 데이터를 가져와서 그 중 썸네일 이미지를 recyclerview에 표시


                for (dataSnapshot1 in dataSnapshot.children) {
                    val item: VideoItem? = dataSnapshot1.getValue(VideoItem::class.java)
                    if (item != null) {
                        list_recent.add(item)
                    }
                    //val adapter1 = RecentItemAdapter(requireContext(), list_recent)
                    //rv_recent.adapter = adapter1


                    //rv_favor.adapter = adapter1
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
*/


}
