package com.ykky.greenapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ykky.greenapp.databinding.FragmentTodoCalendarBinding


class TodoCalendarFragment : Fragment() {

    private var binding:FragmentTodoCalendarBinding?=null
    private var tyear=0
    private var tmonth=0
    private var tdayOfMonth=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentTodoCalendarBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).nav.visibility=View.GONE
        binding!!.apply{
            calendarView.setBackgroundColor(Color.parseColor("#ffffee"))
            calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
                tyear=year
                tmonth=month+1 //1월이 0
                tdayOfMonth=dayOfMonth
                (activity as MainActivity).nav.visibility=View.VISIBLE
                Log.i("calendar",tyear.toString()+tmonth.toString()+tdayOfMonth.toString())
                (activity as MainActivity).replaceFragment(TODOFragment(tyear,tmonth,tdayOfMonth),"navtodo")
            }
        }
    }

}