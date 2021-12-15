package com.example.opeqesample.repository.api


import com.example.opeqesample.repository.api.response.RandomUsersResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.*


interface MainRetrofitApi {




    @GET(".")// use . when no path is included
    suspend fun getUsersList(@Query("page")  pageNumber:String ,@Query("results")  result:String,@Query("seed")  seed:String,@Query("nat")  nat:String="us"): Response<RandomUsersResponse>


}