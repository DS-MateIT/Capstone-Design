package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.String

class BookmarkFragment: Fragment() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val data:MutableList<BookmarkItemData> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = com.example.myapplication.databinding.BookmarkMainBinding.inflate(inflater, container, false)

        val adapter = BookmarkItemAdapter(requireContext())
        adapter.listData = data
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())


        data.clear()
        //adapter.notifyDataSetChanged()


        // 파이어 베이스에서 현재 접속한 유저의 정보 가져옴
        var user = auth.currentUser
        var email = user?.email


        RetrofitClient.retrofitService.BMvideoidget(email.toString()).enqueue(object :
            Callback<List<userBMDTO>> {
            override fun onResponse(call: Call<List<userBMDTO>>, response: Response<List<userBMDTO>>) {
                if (response.isSuccessful) {
                    val adapter = BookmarkItemAdapter(requireContext())
                    adapter.listData = data
                    binding.recyclerview.adapter = adapter
                    binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
                    adapter.notifyDataSetChanged()

                    for (i in 0 until response.body()!!.size) {
                        val BMid = response.body()?.get(i)?.videoid
                        val BMtitle = response.body()?.get(i)?.title
                        adapter.listData.add(BookmarkItemData(BMid,BMtitle.toString()))
                    }

                    try {
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    Log.v("BM_get", "error = " + String.valueOf(response.code()))
                }
            }

            override fun onFailure(call: Call<List<userBMDTO>>, t: Throwable) {
                Log.d("BM_get","get"+t.toString())
            }
        })


        return binding.root
    }
}