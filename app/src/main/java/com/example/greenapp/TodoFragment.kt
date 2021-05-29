package com.example.greenapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.util.*

class TodoFragment : Fragment() {

    fun timeGenerator() :String{
        val instance = Calendar.getInstance()
        var year = instance.get(Calendar.YEAR).toString()
        var month = instance.get(Calendar.MONTH).toString()
        var date = instance.get(Calendar.DATE).toString()

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }


}