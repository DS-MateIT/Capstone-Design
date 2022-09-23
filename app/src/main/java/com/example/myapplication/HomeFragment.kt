package com.example.myapplication

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeStandalonePlayer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.toolbar_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.String
import kotlin.Throwable
import kotlin.toString

class HomeFragment: Fragment()  {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = com.example.myapplication.databinding.FragmentHomeBinding.inflate(inflater, container, false)

        // 파이어 베이스에서 현재 접속한 유저의 정보 가져옴
        var user = auth.currentUser
        var email = user?.email


        RetrofitClient.retrofitService.useridget2(email.toString()).enqueue(object : Callback<List<useridDTO>>{
            override fun onResponse(call: Call<List<useridDTO>>, response: Response<List<useridDTO>>) {
                if (response.isSuccessful) {

                    val id =  response.body()?.get(0)?.user_id // DB에서 userid 가져오기
                    binding.text1.text = "안녕하세요 "+id+"님! " + email

                    try {
                        Log.v("userid_get", id.toString())
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



        //Intro 노그로 소개영상 썸네일 띄우기
        val introvideoId = "n3xMgsKzZfE"
        Glide.with(requireContext()!!)
            .load("https://img.youtube.com/vi/n3xMgsKzZfE/maxresdefault.jpg")
            .centerCrop()
            .into(binding.introvideo)

        binding.introvideo!!.setOnClickListener{
            val intent = YouTubeStandalonePlayer.createVideoIntent(
                context as Activity,
                "AIzaSyBwDt0NvNliavwfyYm2kSJCNt10Rc0-bxk", //유튜브 api 키
                introvideoId, //비디오 id
                0, //몇초후 재생
                true, //자동실행 할지 말지
                true //작은 뷰박스에서 재생할지 말지 false하면 풀화면으로 재생
            )
            requireContext().startActivity(intent)
        }


        return binding.root


    }
    /*
    *    val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private lateinit var rv_recent: RecyclerView  // 최근 시청한 영상
    private lateinit var rv_favor: RecyclerView   // 선호 카테고리 영상

    //var list_recent = ArrayList<VideoItem>()
    //var adapter: VideoAdapter? = null
    //var adapter: RecentItemAdapter? = null
    val menu: Menu? = null

    //private lateinit var RecentItemAdapter : Adapter
    //private lateinit var favoriteItemAdapter: Adapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //최근 동영상 어댑터 1
        rv_recent = view.findViewById(R.id.item_recent)

        //val list_recent = ArrayList<recentItemData>() //임시 데이터
/*
        list_recent.add(recentItemData(null, "70%","한소희","어그로"))
        list_recent.add(recentItemData(null, "70%","한소희","어그로"))
        list_recent.add(recentItemData(null, "70%","한소희","어그로"))
        list_recent.add(recentItemData(null, "70%","한소희","어그로"))
        list_recent.add(recentItemData(null, "70%","한소희","어그로"))
*/
        //val adapter1 = RecentItemAdapter(list_recent)
        loadrecyclerViewData()
        //rv.adapter = adapter

        //선호 동영상 어댑터 2
        rv_favor = view.findViewById(R.id.item_favorite)

        loadrecyclerViewData()

        setHasOptionsMenu(true)
        return view
    }

    private fun loadrecyclerViewData() {
        //val reference = FirebaseDatabase.getInstance().getReference("videos")
        val reference = Firebase.database.getReference("videos")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //list_recent.clear()
                // db 데이터를 가져와서 그 중 썸네일 이미지를 recyclerview에 표시
                /*
                for (dataSnapshot1 in dataSnapshot.children) {
                    val item: VideoItem? = dataSnapshot1.getValue(VideoItem::class.java)
                    if (item != null) {
                        list_recent.add(item)
                    }
                    adapter = RecentItemAdapter(requireContext(), list_recent)
                    rv_recent.adapter = adapter

                    rv_favor.adapter = adapter
                }
                */
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }*/
}

