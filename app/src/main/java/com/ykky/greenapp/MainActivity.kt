package com.ykky.greenapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ykky.greenapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var nav:BottomNavigationView

    var backbitmap : String = ""
    var isCompleted=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun getBackImg():String{
        return backbitmap
    }

    fun setBackImg(s:String){
        backbitmap=s
    }


    fun setComplete(t:Boolean){
        isCompleted=t
    }
    fun getComplete():Boolean{
        return isCompleted
    }

    private fun init() {

        replaceFragment(GreenFragment(),"navgreen")
        nav=findViewById<BottomNavigationView>(R.id.navigationbar)
        nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navgreen ->{
                    replaceFragment(GreenFragment(),"navgreen")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navtodo->{
                    //TodoAddFragment() --> TODOFragment()로 바꿔주면 됩니다!
                    replaceFragment(TODOFragment(0,0,0),"navtodo")
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

    fun changeBack(){
        val greentag=supportFragmentManager.findFragmentByTag("navgreen")
        if(greentag!=null&&greentag.isVisible){ //배경을 바꿔 새싹화면으로 넘어왔다면
            //네비게이션 바도 새싹으로 바꿔준다
            nav.getMenu().findItem(R.id.navgreen).isChecked=true
        }
    }
}