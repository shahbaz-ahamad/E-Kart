package com.example.e_kart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        // Get the current destination of the NavController
        val currentDestinationId = navController.currentDestination?.id

        if (currentDestinationId == R.id.accountOptionFragment) {
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