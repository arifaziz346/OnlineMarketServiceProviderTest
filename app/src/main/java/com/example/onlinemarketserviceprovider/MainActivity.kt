package com.example.onlinemarketserviceprovider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewpager.widget.ViewPager
import com.example.onlinemarketserviceprovider.Adapter.myViewPagerAdapter
import com.example.onlinemarketserviceprovider.Dashbord.fragmentDashboard
import com.example.onlinemarketserviceprovider.MyOrder.FragmentMyOrder
import com.example.onlinemarketserviceprovider.Products.FragmentProduct
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    var  adapter = myViewPagerAdapter(supportFragmentManager)
         adapter.addFragment(fragmentDashboard(),"Dashboard")
         adapter.addFragment(FragmentMyOrder(),"MyOrder")
         adapter.addFragment(FragmentProduct(),"Product")

        var tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        var viewPager = findViewById<ViewPager>(R.id.viewPager)

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

    }

}