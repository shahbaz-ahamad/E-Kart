package com.example.e_kart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.e_kart.databinding.ActivityShoppingBinding
import com.example.e_kart.util.Resource1
import com.example.e_kart.viewmodel.CartViewodel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    val binding by lazy{
        ActivityShoppingBinding.inflate(layoutInflater)
    }
    val viewmodel by viewModels<CartViewodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        // Find the NavHostFragment and NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        // Associate the NavController with the BottomNavigationView
        val bottomNavigationView = binding.bottomnavigation
        bottomNavigationView.setupWithNavController(navController)


        lifecycleScope.launchWhenStarted {
            viewmodel.cartProducts.collectLatest {
                when(it){

                     is Resource1.Success ->{
                            val count = it.data?.size?: 0
                            val bottomNavigationView =findViewById<BottomNavigationView>(R.id.bottomnavigation)
                            bottomNavigationView.getOrCreateBadge(R.id.cartFragment).apply {
                                number=count
                                backgroundColor=resources.getColor(R.color.g_blue)
                            }


                    }else -> Unit
                }
            }
        }
    }

    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController = navHostFragment.navController

        // Get the current destination of the NavController
        val currentDestinationId = navController.currentDestination?.id

        if (currentDestinationId == R.id.homeFragment) {
            // If the current fragment is the home fragment, display a confirmation dialog to exit the app
            AlertDialog.Builder(this)
                .setMessage("Do you want to exit the app?")
                .setPositiveButton("Exit") { _, _ ->
                    finishAffinity() // Exit the app
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss() // Dismiss the dialog
                }
                .show()
        } else {
            // If the current fragment is not the home fragment, allow the default back behavior
            super.onBackPressed()
        }
    }
}