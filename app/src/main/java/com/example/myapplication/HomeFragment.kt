package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentHomeBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment: Fragment() {
    val binding by lazy { FragmentHomeBinding.inflate(layoutInflater)}
    private lateinit var rv_recent : RecyclerView  // 최근 시청한 영상
    private lateinit var rv_favor : RecyclerView   // 선호 카테고리 영상
    var list_recent = ArrayList<VideoItem>()
    //var adapter: VideoAdapter? = null
    var adapter: RecentItemAdapter? = null
    val menu: Menu?= null

    //private lateinit var RecentItemAdapter : Adapter
    //private lateinit var favoriteItemAdapter: Adapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home,container,false)

        val toolbar : Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)

        //최근 동영상 어댑터 1
        rv_recent= view.findViewById(R.id.item_recent)

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
                list_recent.clear()
                // db 데이터를 가져와서 그 중 썸네일 이미지를 recyclerview에 표시
                for (dataSnapshot1 in dataSnapshot.children) {
                    val item: VideoItem? = dataSnapshot1.getValue(VideoItem::class.java)
                    if (item != null) {
                        list_recent.add(item)
                    }
                    adapter = RecentItemAdapter(requireContext(), list_recent)
                    rv_recent.adapter = adapter

                    rv_favor.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String): Boolean {
                //val text = view?.findViewById(R.id.text) as TextView
                //text.text = "$p0"
                //searchView.setOnSearchClickListener {
                    val intent = Intent(context, SearchViewActivity::class.java)
                    startActivity(intent)
                //}
                return true
            }

            override fun onQueryTextChange(p0: String): Boolean {
                val text = view?.findViewById(R.id.text) as TextView
                text.text = "$p0"
                return true
            }
        })

        //return true
    }
}

