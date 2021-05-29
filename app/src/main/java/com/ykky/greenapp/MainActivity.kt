package com.ykky.greenapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.ykky.greenapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var firebaseauth : FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref : DatabaseReference    // 실시간 데이터베이스
    lateinit var nav:BottomNavigationView
    var backimgname="image1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        firebaseauth = FirebaseAuth.getInstance()
        replaceFragment(GreenFragment(),"navgreen")
        nav=findViewById<BottomNavigationView>(R.id.navigationbar)
        nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navgreen ->{
                    replaceFragment(GreenFragment(),"navgreen")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navtodo->{
                    replaceFragment(TODOFragment(),"navtodo")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navrank ->{
                    replaceFragment(RankFragment(),"navrank")
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navsetting ->{
                    replaceFragment(SettingFragment(),"navsetting")
                    return@setOnNavigationItemSelectedListener true
                }
                else ->{
                    replaceFragment(GreenFragment(),"navgreen")
                    return@setOnNavigationItemSelectedListener true
                }
            }
        }
    }

    fun replaceFragment(fragment: Fragment, tag:String){
        if(supportFragmentManager.findFragmentByTag(tag)!=null){ //똑같은 화면 반복 출력일 경우
            return
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment,tag)
        fragmentTransaction.commit()
    }

}