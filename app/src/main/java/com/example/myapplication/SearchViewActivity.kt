package com.example.myapplication

import android.app.SearchManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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