package com.ykky.greenapp
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ykky.greenapp.databinding.FragmentTODOBinding

class TODOFragment(y:Int, m:Int,d:Int) : Fragment() {

    var binding:FragmentTODOBinding?=null
    var backimg=0
    var tyear=y
    var tmonth=m
    var tday=d
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentTODOBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backimg=(activity as MainActivity).getBackImg()
        binding!!.apply{
            todobackimg.setBackgroundResource(backimg)
            todobackimg.alpha=0.7f
            year.text="${tyear}년"
            date.text="${tmonth}월 ${tday}일"

            calendarBtn.setOnClickListener {
                (activity as MainActivity).replaceFragment(TodoCalendarFragment(),"cal")
            }
        }
    }
}