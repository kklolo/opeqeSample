package com.example.opeqesample.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.opeqesample.databinding.ActivitySplashBinding
import com.example.opeqesample.utils.Resource
import com.example.opeqesample.viewmodel.SplashActivityViewModel
import com.hanks.htextview.HTextViewType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashActivityViewModel by viewModels()

//Comments by Keivan Basiri 2021/12
//Some SAMPLE comments are included


    var sentences = arrayOf(
        "Opeqe Sample App", "By KEIVAN BASIRI"
    )


    lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.checkNetwork()


        //animated text
        binding.activitySplashTxtDetails.apply {

            setTextColor(Color.BLACK)
            typeface = null
            setAnimateType(HTextViewType.LINE)
            animateText(sentences[0])
        }


        Handler().postDelayed(Runnable {

            binding.activitySplashTxtDetails.animateText(sentences[0])


        }, 1000)


        //handle no internet state
        binding.txtRetry.setOnClickListener {

            hideNointernet()
            viewModel.checkNetwork()

        }



        viewModel.checkInternetResponse.observe(this, Observer { checkInternetResponse ->

            when (checkInternetResponse) {
                is Resource.Success -> {

                    //delay for 3 seconds, then go to login activity
                    Handler().postDelayed(Runnable {

                        gotoLoginActivity()


                    }, 1500)

                }

                is Resource.Error -> {

                    if (checkInternetResponse.message.equals("no_internet")) {//no internet
                        showNointernet()

                    } else {


                    }

                    Toast.makeText(this, checkInternetResponse.message, Toast.LENGTH_LONG).show()


                }

                is Resource.Loading -> {


                }

            }

        })


    }


    private fun showNointernet() {
        binding.activitySplashTxtDetails.visibility = View.INVISIBLE
        binding.activitySplashNoInternetOrErrorLayout.visibility = View.VISIBLE
    }


    private fun hideNointernet() {
        if (binding.activitySplashTxtErrorDetails.visibility == View.GONE) {
            binding.activitySplashTxtErrorDetails.visibility = View.VISIBLE
        }
        binding.activitySplashTxtDetails.visibility = View.VISIBLE
        binding.activitySplashTxtDetails.animate()

        binding.activitySplashNoInternetOrErrorLayout.visibility = View.GONE
    }


    //set delay using coroutine
    private fun gotoLoginActivity() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500L)
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            cancel("finish coroutine")
            finish()
        }
    }


}
