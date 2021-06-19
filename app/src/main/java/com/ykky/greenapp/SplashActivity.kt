package com.ykky.greenapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    val TIME_OUT : Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash2)

        var actionBar : ActionBar? = supportActionBar
        actionBar?.hide()

        Handler().postDelayed(Runnable {
            kotlin.run {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, TIME_OUT)
    }
}