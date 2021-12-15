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


class DetailActivityViewModel @ViewModelInject constructor(val mainRepository: MainRepository) : ViewModel() {


    var getSingleUserResponse: MutableLiveData<Resource<RandomUsersResponse.Result>> = MutableLiveData()



   //coroutine error handler
    val handler = CoroutineExceptionHandler { _, exception ->

        Log.i("kk", " retrofit exception :$exception")
    }



}