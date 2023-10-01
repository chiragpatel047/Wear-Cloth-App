package com.ecomapp.wear

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecomapp.wear.Adapters.BagAdapter
import com.ecomapp.wear.Factories.BagVMF
import com.ecomapp.wear.Models.BagModel
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.BagViewModel
import com.ecomapp.wear.databinding.FragmentBagBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Bag.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint()
class Bag : Fragment() {

    lateinit var bagAdapter: BagAdapter

    public lateinit var binding : FragmentBagBinding

    lateinit var bagViewModel: BagViewModel

    @Inject
    lateinit var bagVMF: BagVMF

    companion object{
        lateinit var bagList : ArrayList<BagModel>
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bagViewModel.cart_liveData.observe(viewLifecycleOwner,{
            when(it){
                is Response.Sucess -> {
                    bagList.clear()
                    bagList.addAll(it.data!!)
                    binding.loadingAnim.visibility = View.GONE
                    bagAdapter.notifyDataSetChanged()
                    it.data.clear()
                    isListIsEmpty()
                }
                is Response.Error -> {
                    binding.loadingAnim.visibility = View.GONE
                }
                is Response.Loading -> {
                    binding.nodataAnim.visibility = View.GONE
                    binding.emptyBagText.visibility = View.GONE
                    binding.loadingAnim.visibility = View.VISIBLE
                }

                else -> {}
            }
        })

        bagViewModel.remove_liveData.observe(viewLifecycleOwner,{
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_bag, container, false)
        binding.bagRecyclerview.layoutManager = LinearLayoutManager(activity)

        bagList = ArrayList()
        bagAdapter = BagAdapter(requireActivity(),bagList,::removeFromCart)

        binding.bagRecyclerview.adapter = bagAdapter
        bagViewModel = ViewModelProvider(requireActivity(),bagVMF).get(BagViewModel::class.java)

        bagViewModel.loadCartProducts()

        binding.checkout.setOnClickListener {
            val intent = Intent(activity,Address::class.java)
            intent.putExtra("from", "multiple")
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bagViewModel.clearLiveData()
    }

    fun removeFromCart(productId : String){
        bagViewModel.removeFromCart(productId)
    }
    fun isListIsEmpty(){

        if(bagAdapter.itemCount!=0){
            binding.nodataAnim.visibility = View.GONE
            binding.emptyBagText.visibility = View.GONE
            binding.checkoutLayout.visibility = View.VISIBLE
        }else{
            binding.nodataAnim.visibility = View.VISIBLE
            binding.emptyBagText.visibility = View.VISIBLE
            binding.checkoutLayout.visibility = View.GONE
        }
    }
}