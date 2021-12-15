package com.example.opeqesample.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opeqesample.repository.MainRepository
import com.example.opeqesample.repository.api.response.RandomUsersResponse
import com.example.opeqesample.utils.Resource
import com.example.opeqesample.utils.Utils.checkInternet
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeActivityViewModel @ViewModelInject constructor(val mainRepository: MainRepository) : ViewModel() {

    val userList:MutableLiveData<Resource<RandomUsersResponse>> = MutableLiveData()
    var userListResponse:RandomUsersResponse?=null
    var userListPage = 1


   //coroutine error handler
   private val handler = CoroutineExceptionHandler { _, exception ->

        Log.i("kk", " retrofit exception :$exception")
    }


    fun getUserListt(pageNumber:String,result:String,seed:String="abc") = viewModelScope.launch(Dispatchers.Main) {

        userList.postValue(Resource.Loading())
        val response= withContext(handler){mainRepository.getUserList(pageNumber, result, seed)}
        userList.postValue(handleUserListResponse(response))
    }


    private fun handleUserListResponse(response: retrofit2.Response<RandomUsersResponse>):Resource<RandomUsersResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse->
                userListPage++
                if(userListResponse==null){//first page
                    userListResponse=resultResponse
                }else{// if not null means value is set before
                    val oldArticles=userListResponse?.results//very very very important note . oldArticles defined as VAL and innext line it is set to a  value so how it is possible .it means we named resultResponse.articles as a vlaue and value will be set to resultResponse.articles so this"val newArticles "is just like a label https://stackoverflow.com/questions/51718229/kotlin-val-mutablelist-vs-var-immutablelist-when-to-use-which
                    val newArticles=resultResponse.results
                    oldArticles?.addAll(newArticles!!)// get new loaded list then add it into last list that we had before to expand list

                }
                return Resource.Success(userListResponse?:resultResponse)
            }
        }
        return Resource.Error(response.errorBody().toString())
    }





}