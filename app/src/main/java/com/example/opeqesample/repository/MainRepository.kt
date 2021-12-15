package com.example.opeqesample.repository

import com.example.opeqesample.repository.api.MainRetrofitApi
import com.example.opeqesample.repository.api.response.RandomUsersResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException
import java.lang.IndexOutOfBoundsException
import javax.inject.Inject

class MainRepository @Inject constructor(private val mainRetrofitApi: MainRetrofitApi) {


    //**coroutine and di are used here**


    //get user list normal call
    suspend fun getUserList(pageNumber:String,result:String,seed:String) = try {
        mainRetrofitApi.getUsersList(pageNumber, result, seed)

    } catch (e: IOException) {
        throw e
    }


    //using best practices  -> get user list with as Flow
    suspend fun getUserListFlow(pageNumber:String,result:String,seed:String):Flow<Response<RandomUsersResponse>> {
        return flow {
            emit(mainRetrofitApi.getUsersList(pageNumber,result, seed))
        }
    }


}



