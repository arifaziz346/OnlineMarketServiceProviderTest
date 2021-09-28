package com.example.onlinemarketserviceprovider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.onlinemarketserviceprovider.Auth.UpdateProfile
import com.example.onlinemarketserviceprovider.Auth.login
import com.example.onlinemarketserviceprovider.Dashbord.SaleDashboard
import com.example.onlinemarketserviceprovider.MyOrder.MyOrder
import com.example.onlinemarketserviceprovider.Products.MyProduct
import com.example.onlinemarketserviceprovider.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //Sale Dashboard
        binding.cvSaleDashboard.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this,SaleDashboard::class.java)
                startActivity(intent)
            })

        binding.cvMyProduct.setOnClickListener(
            View.OnClickListener {
            val intent = Intent(this,MyProduct::class.java)
                startActivity(intent)
            })
        binding.cvOrder.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this,MyOrder::class.java)
                startActivity(intent)

            })
        binding.cvUpdateProfile.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this,UpdateProfile::class.java)
                startActivity(intent)
                finish()
            })

        binding.cvLogOut.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this,login::class.java)
                startActivity(intent)
                finish()
            })

//    var  adapter = myViewPagerAdapter(supportFragmentManager)
//         adapter.addFragment(fragmentDashboard(),"Dashboard")
//         adapter.addFragment(FragmentMyOrder(),"MyOrder")
//         adapter.addFragment(FragmentProduct(),"Product")
//
//        var tabLayout = findViewById<TabLayout>(R.id.tabLayout)
//        var viewPager = findViewById<ViewPager>(R.id.viewPager)
//
//        viewPager.adapter = adapter
//        tabLayout.setupWithViewPager(viewPager)

    }

}