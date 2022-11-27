package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
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
import kotlinx.android.synthetic.main.search_play.*
import retrofit2.*
import java.io.IOException
import java.util.concurrent.Executor


/*
class SearchViewActivity : AppCompatActivity(),
    SearchView.OnQueryTextListener{
    private lateinit var searchItem : RecyclerView  // 최근 시청한 영상
    var list_search = ArrayList<VideoItem>()
    var adapter: SearchItemAdapter? = null
    var db = Firebase.database
    lateinit var inflater : MenuInflater // 메뉴를 가져오기 위해 필요
    // searchview
    private lateinit var mySearchView: SearchView
    // searchview 텍스트
    private lateinit var mySearchViewEditText: EditText

    lateinit var searchbar : MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.toolbar_search)

        searchItem = findViewById(R.id.search_video_rv)

        val reference = Firebase.database.getReference("videos")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list_search.clear()
                // db 데이터를 가져와서 그 중 썸네일 이미지를 recyclerview에 표시
                for (dataSnapshot1 in dataSnapshot.children) {
                    val item: VideoItem? = dataSnapshot1.getValue(VideoItem::class.java)
                    if (item != null) {
                        list_search.add(item)
                    }
                    adapter = SearchItemAdapter(applicationContext, list_search)
                    searchItem.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        //searchview
        searchbar = findViewById(R.id.searchBar)
        setSupportActionBar(searchbar)  // 액티비티에서 어떤 액션바를 사용할지 설정

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 메뉴
        inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)   // 매개변수로 들어온 menu와 연결
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        // searchview 가져오기
        mySearchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView

        mySearchView.apply{
            this.queryHint = "검색어를 입력해주세요"

            this.setOnQueryTextListener(this@SearchViewActivity)

            this.setOnQueryTextFocusChangeListener { view, hasExpanded ->
                when(hasExpanded){
                    true -> {
                        Log.d(ContentValues.TAG, "서치뷰 열림")
                    }
                    false -> {
                        Log.d(ContentValues.TAG, "서치뷰 닫힘")
                    }
                }
            }
            // searchview에서 텍스트를 가져옴
            mySearchViewEditText = this.findViewById(androidx.appcompat.R.id.search_src_text)
        }
        mySearchViewEditText.apply{
            this.filters = arrayOf(InputFilter.LengthFilter(12))   // 입력 텍스트는 최대 12자
            this.setTextColor(Color.parseColor("#F8F8F8"))
            this.setHintTextColor(Color.parseColor("#F8F8F8"))
        }
        return true
    }

    /* 서치뷰 검색어 입력 이벤트 */
    // 검색어가 submit됐을 때
    override fun onQueryTextSubmit(query: String?): Boolean {
        // 쿼리값이 있으면!
        if(!query.isNullOrEmpty()){
            searchbar.title = query

            // api 호출
            // 검색어 저장
        }
        //mySearchView.setQuery("", false)
        //mySearchView.clearFocus()  // 키보드를 내림
        searchbar.collapseActionView()
        return true
    }

    // 검색어 텍스트가 바뀌었을 때
    override fun onQueryTextChange(newText: String?): Boolean {
        val userInputText = newText.let{
            it
        }?:""

        if(userInputText.count() == 12)
            Toast.makeText(this, "검색어는 12자까지만 입력 가능합니다.", Toast.LENGTH_SHORT).show()

        return true
    }
}

*/

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