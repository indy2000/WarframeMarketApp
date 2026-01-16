package com.fukajima.warframemarket

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fukajima.warframemarket.fragments.Fragment_Contracts
import com.fukajima.warframemarket.fragments.Fragment_Login
import com.fukajima.warframemarket.fragments.Fragment_Market
import com.fukajima.warframemarket.fragments.Fragment_Profile
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    lateinit var navigation: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        var imageButtonLogin: ImageButton = findViewById(R.id.login_button_tb)

        navigation = findViewById(R.id.main_bottom_nav)
        navigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_menu_market -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_linear, Fragment_Market(), "FRAG_MARKET")
                        .addToBackStack(null)
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.nav_menu_contracts -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_linear, Fragment_Contracts(), "FRAG_CONTRACTS")
                        .addToBackStack(null)
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.nav_menu_profile -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_linear, Fragment_Profile(), "FRAG_PROFILE")
                        .addToBackStack(null)
                        .commit()
                    return@setOnItemSelectedListener true
                }
                else -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_linear, Fragment_Market(), "FRAG_MARKET")
                        .addToBackStack(null)
                        .commit()
                    return@setOnItemSelectedListener true
                }
            }
        }
        navigation.selectedItemId = R.id.nav_menu_market

        imageButtonLogin.setOnClickListener{
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_linear, Fragment_Login(), "FRAG_LOGIN")
                .addToBackStack(null)
                .commit()
            //Toast.makeText(this@HomeActivity,"Not Implemented", Toast.LENGTH_SHORT).show()
        }

    }
}

