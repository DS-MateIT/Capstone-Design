package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookmarkFragment: Fragment() {
    private lateinit var adapter: BookmarkItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bookmark_main, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerview = view.findViewById<RecyclerView>(R.id.recyclerview)
        val layoutManager = LinearLayoutManager(activity)
        recyclerview.layoutManager = LinearLayoutManager(activity)

        adapter = BookmarkItemAdapter()
        recyclerview.adapter =  adapter

        adapter.items.add(BookmarkItemData("제목1",""))
        adapter.items.add(BookmarkItemData("제목2",""))
        adapter.items.add(BookmarkItemData("제목3",""))
        adapter.items.add(BookmarkItemData("제목4",""))
        adapter.items.add(BookmarkItemData("제목5",""))
        adapter.items.add(BookmarkItemData("제목6",""))
        adapter.items.add(BookmarkItemData("제목7",""))
    }
}