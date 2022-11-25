package com.example.myapplication

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient { //singleton - 서버 호출이 필요할 때마다 객체를 생성하는 것은 비효율적
    private var instance: Retrofit? = null
    private const val CONNECT_TIMEOUT_SEC = 20000L

    fun getInstance() : Retrofit {
        if(instance == null){

            // 로깅인터셉터 세팅
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            // OKHttpClient에 로깅인터셉터 등록
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
                .build()

            var gson = GsonBuilder().setLenient().create()

            instance = Retrofit.Builder()
                //.baseUrl("http://10.0.2.2:5000/") //에뮬레이터
                //.baseUrl("http://172.20.15.41:5000")  // 서버 돌릴때마다 주소 맞는지 확인해야함
                //.baseUrl("http://172.31.58.130:5000")
                .baseUrl("http://192.168.174.173:5000/") //서우
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Retrofit 객체에 OkHttpClient 적용
                .build()
        }
        return instance!!
    }

    val retrofit: Retrofit = getInstance()
    val retrofitService : RetrofitService =  retrofit.create(RetrofitService::class.java)

    }


