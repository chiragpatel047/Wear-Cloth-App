package com.ecomapp.wear.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecomapp.wear.Models.MainCatModel
import com.ecomapp.wear.R
import com.ecomapp.wear.SubCategory
import com.ecomapp.wear.databinding.MainCategorySingleBinding

class MainCatAdapter(
    myContext: Context,
    myMaimCatList: ArrayList<MainCatModel>,
    val onSubCatClick: () -> String,
) : RecyclerView.Adapter<MainCatAdapter.MainCatViewHolder>() {

    var context = myContext
    var mainCatList = myMaimCatList
    class MainCatViewHolder(mainCatBinding: MainCategorySingleBinding) :
        RecyclerView.ViewHolder(mainCatBinding.getRoot()) {

        var mainCatBinding: MainCategorySingleBinding

        init {
            this.mainCatBinding = mainCatBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCatViewHolder {
        val binding: MainCategorySingleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.main_category_single, parent, false
        )

        return MainCatViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return mainCatList.size
    }

    override fun onBindViewHolder(holder: MainCatViewHolder, position: Int) {
        val singleMainCat = mainCatList.get(position)

        Glide.with(context).load(singleMainCat.mainCatImage).into(holder.mainCatBinding.mainCatItemImage)
        holder.mainCatBinding.mainCatItemName.text = singleMainCat.mainCatName

        holder.itemView.setOnClickListener {

            val intent = Intent(context,SubCategory::class.java)
            intent.putExtra("mainCat",singleMainCat.mainCatName)
            intent.putExtra("parentCat",onSubCatClick.invoke())
            context.startActivity(intent)

        }
    }
}