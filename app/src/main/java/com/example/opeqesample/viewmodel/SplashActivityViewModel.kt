package com.example.opeqesample.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opeqesample.repository.MainRepository
import com.example.opeqesample.utils.Resource
import com.example.opeqesample.utils.Utils.checkInternet
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SplashActivityViewModel @ViewModelInject constructor(val mainRepository: MainRepository) : ViewModel() {


    var checkInternetResponse: MutableLiveData<Resource<String>> = MutableLiveData()



   //coroutine error handler
    val handler = CoroutineExceptionHandler { _, exception ->

        Log.i("kk", " retrofit exception :$exception")
    }

    fun checkNetwork() = viewModelScope.launch(Dispatchers.Main) {
        checkInternetResponse.postValue(Resource.Loading())

        if (checkInternet()) {


            checkInternetResponse.postValue(Resource.Success("internet_ok"))



        } else {
            Log.i("kk", "internet false")
            checkInternetResponse.postValue(Resource.Error("no_internet"))
        }
    }

}