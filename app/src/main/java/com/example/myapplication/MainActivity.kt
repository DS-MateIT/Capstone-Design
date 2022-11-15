package com.example.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

open class MainActivity : AppCompatActivity() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val toolbar : Toolbar = findViewById(R.id.toolbar);

        val homeFragment = HomeFragment()
        val preferenceFragment = PreferenceFragment()
        val bookmarkFragment = BookmarkFragment()
        val settingFragment = SettingFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        replaceFragment(homeFragment)



        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.tab1 -> replaceFragment(homeFragment)
                R.id.tab2 -> replaceFragment(preferenceFragment)
                R.id.tab3 -> replaceFragment(bookmarkFragment)
                R.id.tab4 -> replaceFragment(settingFragment)
            }
            true
        }

        setSupportActionBar(toolbar);

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        //검색 버튼 클릭
        //val searchView = menu.findItem(R.id.menu_search)

        /*searchView.setOnMenuItemClickListener {
            val intent = Intent(context, SearchViewActivity::class.java)
            startActivity(intent)
            return@setOnMenuItemClickListener true
       */
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // MainActivity -> SearchViewActivity 검색어 보내기 //검색용
                val intent = Intent(this@MainActivity, SearchViewActivity::class.java)
                intent.putExtra("query", "$query")

                // MainActivity -> VideoPlayerActivity 검색어 보내기 //상세 페이지용
                //val srchIntent = Intent(this@MainActivity,MyAdapter::class.java)
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


                startActivity(intent)
                return true

            }
            override fun onQueryTextChange(p0: String): Boolean {
                return true
            }
        })


        return true
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer, fragment)
                commit()
            }

    }
}