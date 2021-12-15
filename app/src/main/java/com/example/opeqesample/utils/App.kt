package com.example.opeqesample.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.example.opeqesample.R
import dagger.hilt.android.HiltAndroidApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump


@HiltAndroidApp
class App:Application() {


    companion object{

        var shouldHandleBackInFragment=false


        var curentActivity: Activity? = null

     lateinit var appContext:Context


        lateinit var sharedPreferences:SharedPreferences


      lateinit var sharedPreferencesEditor: SharedPreferences.Editor

       var userDefaultAddress=String()




    }



    override fun onCreate() {
        super.onCreate()

       sharedPreferences = getSharedPreferences("MY_SHARED_PREFERENCES", Context.MODE_PRIVATE)
        sharedPreferencesEditor = sharedPreferences.edit()



        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)//to fix vector drawble resourse not found on pre lolipop

        appContext =applicationContext //set context to acceess from companion

        val context = applicationContext



        //font initialize
        ViewPump.init(ViewPump.builder()
            .addInterceptor(
                CalligraphyInterceptor(
                CalligraphyConfig.Builder()
                    .setDefaultFontPath("nunito.ttf")
                    .setFontAttrId(R.attr.fontPath)
                    .build())
            )
            .build())
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this) // to fix black screen and crash on api 19 caused by firebase provider
    }


}