package com.example.opeqesample.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.opeqesample.adapters.HomePagingRecyclerAdapter
import com.example.opeqesample.databinding.ActivityHomeBinding
import com.example.opeqesample.repository.api.response.RandomUsersResponse
import com.example.opeqesample.utils.Resource
import com.example.opeqesample.utils.Utils
import com.example.opeqesample.utils.Utils.QUERY_PAGE_SIZE
import com.example.opeqesample.utils.Utils.TOTAL_RESULT
import com.example.opeqesample.viewmodel.HomeActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.inflationx.viewpump.ViewPumpContextWrapper

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

// by Keivan Basiri 2021/12

//Some SAMPLE comments are included


    private val viewModel: HomeActivityViewModel by viewModels()


    lateinit var binding: ActivityHomeBinding

    lateinit var recyclerAdapter: HomePagingRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeRecycler.layoutManager = LinearLayoutManager(this)

        setupRecycler()

        recyclerAdapter.itemHomeOnItemClickListener {

            goToDetailsActivity(it!!)

        }



        viewModel.getUserListt(viewModel.userListPage.toString(), QUERY_PAGE_SIZE.toString())


        //setup refresh layout
        binding.activityHomeRefreshLayout.setOnRefreshListener {
            // reset all values to get fresh one
            viewModel.userListPage = 1
            viewModel.userList.value = null
            viewModel.userListResponse = null
            viewModel.getUserListt(
                viewModel.userListPage.toString(),
                QUERY_PAGE_SIZE.toString()
            )


            binding.activityHomeRefreshLayout.isRefreshing = false
        }


        viewModel.userList.observe(this, Observer { getUserListResponse ->

            when (getUserListResponse) {
                is Resource.Success -> {
                    hideProgressBar()
                    if (getUserListResponse.data?.results != null) {


                        getUserListResponse.data?.let { newsResponse ->
                            recyclerAdapter.differ.submitList(newsResponse.results?.toList())
                            // val totalPages=newsResponse.info?.results!!/ QUERY_PAGE_SIZE +2 //api should send total results but this api does not handle it
                            val totalPages = TOTAL_RESULT / QUERY_PAGE_SIZE + 2
                            isLastPage = viewModel.userListPage == totalPages
                            if (isLastPage) {
                                binding.homeRecycler.setPadding(0, 0, 0, 0)
                            }
                        }


                        //   setupRecycler(getUserListResponse.data!!)


                    } else {
                        Utils.errorToast("something went wrong")

                    }


                }

                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(this, getUserListResponse.message, Toast.LENGTH_LONG).show()


                }

                is Resource.Loading -> {
                    showProgresBar()
                }

            }

        })

    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }


    private fun setupRecycler() {


        recyclerAdapter = HomePagingRecyclerAdapter()



        binding.homeRecycler.adapter = recyclerAdapter
        binding.homeRecycler.addOnScrollListener(this@HomeActivity.scrollListener)

        /* binding.homeRecycler.addItemDecoration(
             DividerItemDecoration(
                 binding.homeRecycler.context,
                 DividerItemDecoration.VERTICAL
             )
         )*/

    }

    private fun goToDetailsActivity(itemData: RandomUsersResponse.Result) {

        val intent = Intent(this, DetailsActivity::class.java)


        val bundle = Bundle()

        bundle.putSerializable("postList", itemData)

        intent.putExtras(bundle)
        //  intent.putExtra("itemId",itemData.login?.uuid)
        // val itemBundle = Bundle()
        //itemBundle.putSerializable("item",itemData)
        //intent.putExtras(itemBundle)

        startActivity(intent)

    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgresBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    //CUSTOM lightweight paging . Paging 3 library could be used too.
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)//we are scrolling
            {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0 //first item is n ot visible
            val isTotalMoreThanVissible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVissible && isScrolling
            if (shouldPaginate) {
                viewModel.getUserListt(
                    viewModel.userListPage.toString(),
                    QUERY_PAGE_SIZE.toString()

                )
                isScrolling = false
            }
        }
    }


}