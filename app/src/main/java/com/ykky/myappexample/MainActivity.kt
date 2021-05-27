package com.ykky.myappexample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.ykky.myappexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var firebaseauth : FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref : DatabaseReference    // 실시간 데이터베이스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        firebaseauth = FirebaseAuth.getInstance()


        binding.apply {
            btnLogout.setOnClickListener {
                firebaseauth.signOut()
                val intent = Intent(this@MainActivity,LoginActivity::class.java)
                startActivity(intent)
                finish()

                //탈퇴
                //firebaseauth.currentUser?.delete()
            }

        }
    }

}