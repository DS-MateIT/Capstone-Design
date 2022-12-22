package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.SearchFilterBinding
import com.google.firebase.auth.FirebaseAuth
import io.grpc.android.BuildConfig
import kotlinx.android.synthetic.main.search_play.*
import retrofit2.*
import java.io.IOException
import java.util.concurrent.Executor

class SearchViewActivity : AppCompatActivity() {
    lateinit var binding: SearchFilterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = com.example.myapplication.databinding.SearchFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        //검색용
        val intent = intent
        val query = intent.getStringExtra("query")
        Log.d("query","$query") //문자열 확인


        //homeFragment 검색결과 가져오기
        var call: Call<SearchListResponse> = MyApplication.networkService.getList(
            getString(R.string.youtube_key),
            query.toString(),
            "video",
            "snippet",
            "relevance",
            "medium"
        )

        call?.enqueue(object : Callback<SearchListResponse> {
            override fun onResponse(
                call: Call<SearchListResponse>,
                response: Response<SearchListResponse>
            ) {
                if (response.isSuccessful) {
                    binding.recyclerview.layoutManager =
                        LinearLayoutManager(this@SearchViewActivity)
                    binding.recyclerview.adapter =
                        MyAdapter(this@SearchViewActivity, response.body()?.items, query.toString())
                }
            }

            override fun onFailure(call: Call<SearchListResponse>, t: Throwable) {
                Log.d("mobileApp", "error..")
            }
        })


        setSupportActionBar(toolbar);


        //스피너 필터링
        var filter = resources.getStringArray(R.array.filtering)
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, filter)
        binding.spinner.adapter = adapter

        var result =

        binding.spinner.setSelection(0)
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //item선택했을때

                when (position) {
                    0 -> {
                        //0 : 필터링 게시물 - 일치율 받아와서 필터링 구현필요
                        Toast.makeText(
                            this@SearchViewActivity,
                            filter[position],
                            Toast.LENGTH_SHORT
                        ).show()


                    }
                    1 -> {
                        //1 : 전체 게시물 보여주기
                        Toast.makeText(
                            this@SearchViewActivity,
                            filter[position],
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }

        }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

    }

    }
    //searchviewActivity에 있는 서치뷰
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                // SearchViewActivity -> VideoPlayerActivity 검색어 보내기 //상세 페이지용
                val intent = Intent(this@SearchViewActivity, SearchViewActivity::class.java)
                intent.putExtra("srchquery", "$query")


                //query 최근 검색어 post
                RetrofitClient.retrofitService.srchData(query).enqueue(object: Callback<srchDTO> {
                    override fun onResponse(call: Call<srchDTO>, response: Response<srchDTO>) {
                        if (response.isSuccessful) {
                            try {
                                val result = response.body().toString()
                                Log.v("srch", result)

                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        } else {
                            Log.v("srch","error = " + java.lang.String.valueOf(response.code()))

                        }
                    }

                    override fun onFailure(call: Call<srchDTO>, t: Throwable) {
                        Log.d("srch","post실패"+t.toString())
                    }


                })

                
                var call: Call<SearchListResponse> = MyApplication.networkService.getList(
                    getString(R.string.youtube_key),
                    query.toString(),
                    "video",
                    "snippet",
                    "relevance",
                    "medium"
                )

                call?.enqueue(object : Callback<SearchListResponse> {
                    override fun onResponse(
                        call: Call<SearchListResponse>,
                        response: Response<SearchListResponse>
                    ) {
                        if (response.isSuccessful) {
                            binding.recyclerview.layoutManager =
                                LinearLayoutManager(this@SearchViewActivity)
                            binding.recyclerview.adapter =
                                MyAdapter(this@SearchViewActivity, response.body()?.items, query)
                        }
                    }

                    override fun onFailure(call: Call<SearchListResponse>, t: Throwable) {
                        Log.d("mobileApp", "error..")
                    }
                })

                startActivity(intent)
                searchView.onActionViewCollapsed()
                return false //키보드

            }
            override fun onQueryTextChange(p0: String): Boolean {
                return false //키보드
            }
        })


        return true



    }






}

