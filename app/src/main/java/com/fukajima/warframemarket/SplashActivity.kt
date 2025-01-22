package com.fukajima.warframemarket

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val mp = MediaPlayer.create(this, R.raw.this_is_what_you_are_asset_v2)
        mp.start()
        /*Handler(Looper.getMainLooper()).postDelayed({

         }, 6000)*/
        mp.setOnCompletionListener {
            it.release()
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            finish()
        }
    }
}