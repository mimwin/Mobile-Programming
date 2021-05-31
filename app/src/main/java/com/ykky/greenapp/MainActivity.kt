package com.ykky.greenapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ykky.greenapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var nav:BottomNavigationView
    var backimgname="image1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        replaceFragment(GreenFragment(),"navgreen")
        nav=findViewById<BottomNavigationView>(R.id.navigationbar)
        nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navgreen ->{
                    replaceFragment(GreenFragment(),"navgreen")
                    nav.itemIconTintList=ContextCompat.getColorStateList(this,R.color.btn1)
                    nav.itemTextColor=ContextCompat.getColorStateList(this,R.color.btn1)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navtodo->{
                    //TodoAddFragment() --> TodoFragment()로 바꿔주면 됩니다!
                    replaceFragment(TodoAddFragment(),"navtodo")
                    nav.itemIconTintList=ContextCompat.getColorStateList(this,R.color.btn2)
                    nav.itemTextColor=ContextCompat.getColorStateList(this,R.color.btn2)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navrank ->{
                    replaceFragment(RankFragment(),"navrank")
                    nav.itemIconTintList=ContextCompat.getColorStateList(this,R.color.btn3)
                    nav.itemTextColor=ContextCompat.getColorStateList(this,R.color.btn3)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navsetting ->{
                    replaceFragment(SettingFragment(),"navsetting")
                    nav.itemIconTintList=ContextCompat.getColorStateList(this,R.color.btn4)
                    nav.itemTextColor=ContextCompat.getColorStateList(this,R.color.btn4)
                    return@setOnNavigationItemSelectedListener true
                }
                else ->{
                    replaceFragment(GreenFragment(),"navgreen")
                    nav.itemIconTintList=ContextCompat.getColorStateList(this,R.color.btn1)
                    nav.itemTextColor=ContextCompat.getColorStateList(this,R.color.btn1)
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