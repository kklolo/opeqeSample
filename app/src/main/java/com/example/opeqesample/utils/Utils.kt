package com.example.opeqesample.utils


import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.opeqesample.R
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.coroutines.*
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.coroutines.CoroutineContext


object Utils {
    const val QUERY_PAGE_SIZE=20
    const val TOTAL_RESULT=10000

    fun successToast(message:String){

        TastyToast.makeText(App.appContext, message, Toast.LENGTH_LONG, TastyToast.SUCCESS)
    }

    fun errorToast(message:String){
        TastyToast.makeText(App.appContext, message, Toast.LENGTH_LONG, TastyToast.ERROR)
    }

    fun warningToast(message:String){
        TastyToast.makeText(App.appContext, message, Toast.LENGTH_LONG, TastyToast.WARNING)
    }

    fun infoToast(message:String){
        TastyToast.makeText(App.appContext, message, Toast.LENGTH_LONG, TastyToast.INFO)
    }

    fun defaultToast(message:String){
        TastyToast.makeText(App.appContext, message, Toast.LENGTH_LONG, TastyToast.DEFAULT)
    }

    fun confusingToast(message:String){
        TastyToast.makeText(App.appContext, message, Toast.LENGTH_LONG, TastyToast.CONFUSING)
    }


    //alert dialog for loading
    fun setProgressDialog(): AlertDialog {
        val llPadding = 30
        val ll = LinearLayout(App.curentActivity)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam
        val progressBar = ProgressBar(App.curentActivity)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(App.curentActivity)
        tvText.text = App.appContext.getString(R.string.loading)
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = llParam
        ll.addView(progressBar)
        ll.addView(tvText)
        val builder: AlertDialog.Builder = AlertDialog.Builder(App.curentActivity!!)
        builder.setCancelable(false)
        builder.setView(ll)


        return builder.create()
    }
    //alert dialog for loading
    fun applyProgressDialogWindow(dialog: AlertDialog){
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
    }





    private var parentJob = Job()
    private val coRoutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    suspend fun checkInternet()=
            withContext(Dispatchers.IO) {
                try {
                    val sock = Socket()
                    val socketAddress = InetSocketAddress("8.8.8.8", 53)
                    sock.connect(socketAddress, 2000) // This will block no more than timeoutMs
                    sock.close()
                    return@withContext true

                } catch (e: Exception) {
                    //cancel("error")
                    return@withContext false
                }

            }



    fun setImage(imageUrl: String,context: Context,imageView: ImageView) {
        GlideApp.with(context)
            .load(imageUrl)
            .centerCrop()
            .into(imageView)
    }


}