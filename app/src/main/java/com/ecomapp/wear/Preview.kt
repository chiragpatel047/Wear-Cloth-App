package com.ecomapp.wear

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ecomapp.wear.Factories.PreviewVMF
import com.ecomapp.wear.Models.BagModel
import com.ecomapp.wear.Models.NotificationModel
import com.ecomapp.wear.Models.OrderModel
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.PreviewViewModel
import com.ecomapp.wear.databinding.ActivityPreviewBinding
import com.ecomapp.wear.databinding.LoadingDialogBinding
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint()
class Preview : AppCompatActivity(), PaymentResultListener {

    lateinit var binding: ActivityPreviewBinding

    lateinit var previewViewModel: PreviewViewModel

    @Inject
    lateinit var previewVMF: PreviewVMF

    lateinit var name: String
    lateinit var address: String
    lateinit var city: String
    lateinit var state: String
    lateinit var zipCode: String
    lateinit var phoneNO: String

    lateinit var loadingDialog: AlertDialog

    var payingPrice: Int = 0
    var codPrice: Int = 0

    companion object {
        var buyingItem: ArrayList<BagModel> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview)
        supportActionBar?.hide()

        binding.imgBack.setOnClickListener {
            finish()
        }

        previewViewModel = ViewModelProvider(this, previewVMF).get(PreviewViewModel::class.java)

        name = intent.getStringExtra("name")!!
        address = intent.getStringExtra("address")!!
        city = intent.getStringExtra("city")!!
        state = intent.getStringExtra("state")!!
        zipCode = intent.getStringExtra("zipCode")!!
        phoneNO = intent.getStringExtra("phoneNO")!!

        binding.fullName.text = name
        binding.middleAddress.text = address
        binding.topAddress.text = city + ", " + state + " " + zipCode

        buyingItem = Address.buyingItem

        var price: Int = 0

        for (single in buyingItem) {
            price += single.productPrice.toString().toInt()
        }

        binding.orderPrice.text = price.toString() + "₹"
        binding.deliveryCharge.text = codPrice.toString() + "₹"
        binding.totalPrice.text = (price + codPrice).toString() + "₹"

        payingPrice = price + codPrice

        binding.codBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    if (buyingItem.size.equals(1)) {
                        codPrice = 50
                    } else {
                        codPrice = 100
                    }
                } else {
                    codPrice = 0
                }
                binding.orderPrice.text = price.toString() + "₹"
                binding.deliveryCharge.text = codPrice.toString() + "₹"
                binding.totalPrice.text = (price + codPrice).toString() + "₹"

                payingPrice = price + codPrice
            }
        })

        binding.orderPrice.text = price.toString() + "₹"
        binding.deliveryCharge.text = codPrice.toString() + "₹"
        binding.totalPrice.text = (price + codPrice).toString() + "₹"

        payingPrice = price + codPrice

        val dialogBinding: LoadingDialogBinding =
            DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.loading_dialog, null, false)

        loadingDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root).create()

        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        loadingDialog.setCancelable(false)

        previewViewModel.order_liveData.observe(this, {
            when (it) {
                is Response.Sucess -> {
                    loadingDialog.dismiss()
                    val intent = Intent(this, OrderDone::class.java)
                    startActivity(intent)
                    finishAffinity()
                }

                is Response.Error -> {

                }

                is Response.Loading -> {

                }

                else -> {}
            }
        })

        binding.submit.setOnClickListener {

            if(binding.codBox.isChecked){
                submitOrder()
            }else{
                val co: Checkout = Checkout()

                val jsonObject: JSONObject = JSONObject()

                try {
                    jsonObject.put("name", "Wear")
                    jsonObject.put("description", "Payment for product")
                    jsonObject.put("currency", "INR")
                    jsonObject.put("amount", payingPrice * 100)

                    co.open(this, jsonObject)

                } catch (e: Exception) {

                }
            }
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        submitOrder()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_LONG).show()
    }

    fun submitOrder(){

        loadingDialog.show()

        val orderList: ArrayList<OrderModel> = ArrayList()
        val notificationList: ArrayList<NotificationModel> = ArrayList()

        val uniqueStr = System.currentTimeMillis().toString()
        var i: Int = 1

        val cal = Calendar.getInstance()
        val s = SimpleDateFormat("dd MMM")
        cal.add(Calendar.DAY_OF_YEAR, 10)
        val deliveryDate = "Delivery within " + s.format(Date(cal.timeInMillis))

        val simpleDate = SimpleDateFormat("dd MMM yyyy")
        val dateInStr = simpleDate.format(Date())

        for (single in buyingItem) {
            orderList.add(
                OrderModel(
                    uniqueStr + i.toString(),
                    single.productId,
                    "",
                    single.productTitle,
                    single.productMainImage,
                    single.productPrice,
                    single.productSelectedSize,
                    name,
                    address,
                    city,
                    state,
                    zipCode,
                    phoneNO,
                    deliveryDate
                )
            )
            notificationList.add(
                NotificationModel(
                    uniqueStr + i.toString(),
                    "Submit",
                    single.productId,
                    single.productMainImage
                )
            )
            i++
        }
        previewViewModel.submitOrder(orderList, notificationList)
    }
}