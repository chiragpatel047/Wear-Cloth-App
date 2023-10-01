package com.ecomapp.wear

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecomapp.wear.Adapters.FavSimpleAdapter
import com.ecomapp.wear.Factories.FavVMF
import com.ecomapp.wear.Factories.HomeScreenVMF
import com.ecomapp.wear.Models.ProuctModel
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.FavViewModel
import com.ecomapp.wear.Viewmodels.HomeScreenViewModel
import com.ecomapp.wear.databinding.FragmentFavBinding
import com.ecomapp.wear.databinding.SortBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class Fav : Fragment() {

    lateinit var binding : FragmentFavBinding

    lateinit var sort_bottomSheetBinding : SortBottomsheetBinding
    lateinit var sortBottomSheet : BottomSheetDialog

    lateinit var itemList : ArrayList<ProuctModel>
    //lateinit var favGridAdapter: FavGridAdapter

    lateinit var favSimpleAdapter: FavSimpleAdapter

    var isGridView : Boolean = true

    lateinit var favViewModel: FavViewModel

    @Inject
    lateinit var favVMF: FavVMF

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_fav, container, false)

        favViewModel = ViewModelProvider(requireActivity(),favVMF).get(FavViewModel::class.java)

        favViewModel.loadFavProducts()
        itemList = ArrayList()

       // favGridAdapter = FavGridAdapter(requireActivity(),itemList,::removeFromFav)
        favSimpleAdapter = FavSimpleAdapter(requireActivity(),itemList,::removeFromFav)

        binding.favProductsRecylcerview.layoutManager = LinearLayoutManager(activity)
        binding.favProductsRecylcerview.adapter = favSimpleAdapter

        binding.nodataAnim.visibility = View.GONE
        binding.emptyBagText.visibility = View.GONE
        binding.loadingAnim.visibility = View.VISIBLE

        //isListIsEmpty()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        favViewModel.favProducts_liveData.observe(viewLifecycleOwner,{
            when(it){
                is Response.Sucess ->{
                    itemList.clear()
                    itemList.addAll(it.data!!)
                    it.data.clear()
                    binding.loadingAnim.visibility = View.GONE
                    favSimpleAdapter.notifyDataSetChanged()

                    isListIsEmpty()

                }
                is Response.Error ->{
                    binding.loadingAnim.visibility = View.GONE

                }
                is Response.Loading ->{
                    binding.nodataAnim.visibility = View.GONE
                    binding.emptyBagText.visibility = View.GONE
                    binding.loadingAnim.visibility = View.VISIBLE
                }

                else -> {}
            }
        })

        favViewModel.remove_from_fav_liveData.observe(viewLifecycleOwner,{
            when(it){
                is Response.Sucess -> {
                    isListIsEmpty()
                }
                is Response.Error -> {

                }
                is Response.Loading -> {

                }
                else -> {}
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        favViewModel.clearLiveData()
    }

    fun removeFromFav(productId : String){
        favViewModel.removeFromFav(productId)
    }

    fun addToFav(productId : String){
        favViewModel.addToFavourites(productId)
    }
    fun isListIsEmpty(){

        if(favSimpleAdapter.itemCount!=0){
            binding.nodataAnim.visibility = View.GONE
            binding.emptyBagText.visibility = View.GONE
        }else{
            binding.nodataAnim.visibility = View.VISIBLE
            binding.emptyBagText.visibility = View.VISIBLE
        }
    }

}