package com.ecomapp.wear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ecomapp.wear.databinding.ActivityHomeScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class HomeScreen : AppCompatActivity() {

    lateinit var binding : ActivityHomeScreenBinding

    @Inject
    lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home_screen)

        supportActionBar?.hide()

        val home = Home()
        val shop = Shop()
        val bag = Bag()
        val fav = Fav()
        val profile = Profile()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_framelayout,home)
        transaction.commit()

        FirebaseMessaging.getInstance().subscribeToTopic(firebaseAuth.uid!!)
        FirebaseMessaging.getInstance().subscribeToTopic("new")

        binding.bottomNavigationView.setOnItemSelectedListener {

            var transaction = supportFragmentManager.beginTransaction()

            when(it.itemId){
                R.id.nav_home ->{
                    transaction.replace(R.id.home_framelayout,home)
                }
                R.id.nav_shop ->{
                    transaction.replace(R.id.home_framelayout,shop)
                }
                R.id.nav_bag ->{
                    transaction.replace(R.id.home_framelayout,bag)
                }
                R.id.nav_fav ->{
                    transaction.replace(R.id.home_framelayout,fav)
                }
                R.id.nav_profile ->{
                    transaction.replace(R.id.home_framelayout,profile)
                }
            }
            transaction.commit()
            return@setOnItemSelectedListener true
        }
    }
}