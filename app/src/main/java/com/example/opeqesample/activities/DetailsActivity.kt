package com.example.opeqesample.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.opeqesample.databinding.ActivityDetailsBinding
import com.example.opeqesample.repository.api.response.RandomUsersResponse
import com.example.opeqesample.utils.App
import com.example.opeqesample.utils.Resource
import com.example.opeqesample.utils.StatusBarUtil
import com.example.opeqesample.utils.Utils
import com.example.opeqesample.viewmodel.DetailActivityViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    //Comments by Keivan Basiri 2021/12
    //Some SAMPLE comments are included

    lateinit var binding: ActivityDetailsBinding

    private val viewModel: DetailActivityViewModel by viewModels()

    private lateinit var loadingAlertDialog: AlertDialog


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        App.curentActivity=this

        loadingAlertDialog = Utils.setProgressDialog()

        binding.activityDetailsLayoutItems.visibility=View.INVISIBLE

        binding.waveHeader.velocity=1.5f//setup header view


        StatusBarUtil.immersive(this)//make status bar transparent




        val resultItem = intent?.getSerializableExtra("postList") as? RandomUsersResponse.Result




        Utils.setImage(resultItem?.picture?.medium!!,this,binding.activityDetailsImgAvatar)

        binding.activityDetailsUserInfo.text="${resultItem.name?.first} ${resultItem.name?.last}"
        binding.activityDetailsEmail.text=resultItem.email






        Handler(Looper.getMainLooper()).postDelayed({
            binding.activityDetailsLayoutItems.visibility=View.VISIBLE

            YoYo.with(Techniques.SlideInLeft)
                .duration(500)
                .repeat(0)
                .playOn(binding.activityDetailsEmailLayout)

            YoYo.with(Techniques.SlideInRight)
                .duration(500)
                .repeat(0)
                .playOn(binding.activityDetailsLayoutContact)

            YoYo.with(Techniques.SlideInLeft)
                .duration(500)
                .repeat(0)
                .playOn(binding.activityDetailsLayoutExit)

            YoYo.with(Techniques.SlideInDown)
                .onStart {  binding.activityDetailsImgAvatar.visibility=View.VISIBLE}
                .duration(1500)
                .repeat(0)
                .playOn(binding.activityDetailsImgAvatar)

        }, 50)



        binding.activityDetailsLayoutExit.setOnClickListener {

            finishAffinity()
        }


        binding.activityDetailsLayoutContact.setOnClickListener {


            openGoogleWebsite()


        }

        binding.activityDetailsImgBack.setOnClickListener {
            onBackPressed()
        }



    }

private fun openGoogleWebsite(){
    val url = "http://www.google.com"
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(url)
    startActivity(i)
}
}