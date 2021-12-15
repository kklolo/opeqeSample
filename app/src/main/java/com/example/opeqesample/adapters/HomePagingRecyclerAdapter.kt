package com.example.opeqesample.adapters

import android.annotation.SuppressLint

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.opeqesample.R
import com.example.opeqesample.databinding.ItemHomeRecyclerBinding
import com.example.opeqesample.repository.api.response.RandomUsersResponse
import com.example.opeqesample.utils.GlideApp


class HomePagingRecyclerAdapter:RecyclerView.Adapter<MyViewHolder> (){
    val differ=AsyncListDiffer(this,MyViewHolder.differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding: ItemHomeRecyclerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_home_recycler, parent, false
        )

        return MyViewHolder(
          binding
        )

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val homeItem=differ.currentList[position]
        holder.binding.root.setOnClickListener {


            itemHomeClickListener?.let {
                it(homeItem)
            }


        }

        holder.binding.itemHomeTxtName.text ="${homeItem.name?.first} ${homeItem.name?.last}"
        holder.binding.itemHomeTxtEmail.text ="${homeItem.email}"

        holder.setImage(homeItem.picture?.medium!!)
    }

    private var itemHomeClickListener: ((RandomUsersResponse.Result?) -> Unit)? = null
    fun itemHomeOnItemClickListener(listener: (RandomUsersResponse.Result?) -> Unit) {
        itemHomeClickListener = listener
    }
}

    class MyViewHolder(val binding: ItemHomeRecyclerBinding):RecyclerView.ViewHolder(binding.root){

        companion object{
            val differCallback=object : DiffUtil.ItemCallback<RandomUsersResponse.Result>(){
                override fun areItemsTheSame(oldItem: RandomUsersResponse.Result, newItem: RandomUsersResponse.Result): Boolean {
                    return oldItem.login?.uuid==newItem.login?.uuid
                }

                override fun areContentsTheSame(oldItem: RandomUsersResponse.Result, newItem: RandomUsersResponse.Result): Boolean {
                    return oldItem==newItem
                }

            }
        }

        fun setImage(imageUrl: String) {
            GlideApp.with(binding.root)
                .load(imageUrl)
                .centerCrop()
                .into(binding.itemHomeImgAvatar)
        }

    }





