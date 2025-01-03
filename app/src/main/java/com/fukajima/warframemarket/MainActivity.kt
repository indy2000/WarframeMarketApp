package com.fukajima.warframemarket

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fukajima.warframemarket.fragments.Fragment_Market
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        navigation = findViewById(R.id.main_bottom_nav)
        navigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_menu_market -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_linear, Fragment_Market(), "FRAG_MARKET")
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.nav_menu_contracts -> {
                    Toast.makeText(this@MainActivity, "Not Implemented", Toast.LENGTH_SHORT).show()
                    return@setOnItemSelectedListener true
                }
                R.id.nav_menu_profile -> {
                    Toast.makeText(this@MainActivity, "Not Implemented", Toast.LENGTH_SHORT).show()
                    return@setOnItemSelectedListener true
                }
                else -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_linear, Fragment_Market(), "FRAG_MARKET")
                        .commit()
                    return@setOnItemSelectedListener true
                }
            }
        }
        navigation.selectedItemId = R.id.nav_menu_market
    }
}

