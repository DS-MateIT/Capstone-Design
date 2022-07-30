package com.example.myapplication

import retrofit2.Call
import retrofit2.http.*

interface RetrofitService{

    @GET("/cal")//서버에 GET 요청을 할 주소를 입력
    fun getresult() : Call<ResponseDTO> //MainActivity에서 불러와서 이 함수에 큐를 만들고 대기열에 콜백을 넣어주면 그거갖고 요청하는거임.
}

interface RetrofitPost{
    @POST("/cal")
    //fun sendReq(@Body postdto: PostDTO) : Call<PostDTO>
    fun post(@FieldMap param: HashMap<String, String>): Call<PostDTO>
}

interface NetworkService {
    @GET("youtube/v3/search")
    fun getList(
        @Query("key") key:String,
        @Query("q") search_query : String,
        @Query("type") returnType: String,
        @Query("part") returnData : String
    ): Call<SearchListResponse>
}