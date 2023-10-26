package com.example.e_kart.util

import android.view.View
import androidx.fragment.app.Fragment
import com.example.e_kart.R
import com.example.e_kart.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigation(){
    val bottomNavigationView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(com.example.e_kart.R.id.bottomnavigation)
    bottomNavigationView.visibility= View.GONE
}

fun Fragment.showBottomNavigation(){
    val bottomNavigationView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(com.example.e_kart.R.id.bottomnavigation)
    bottomNavigationView.visibility= View.VISIBLE
}