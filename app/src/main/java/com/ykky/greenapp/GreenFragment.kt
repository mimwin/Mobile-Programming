package com.ykky.greenapp
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ykky.greenapp.databinding.FragmentGreenBinding

class GreenFragment() : Fragment() {
    var binding:FragmentGreenBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentGreenBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }

    private fun init() {
        (activity as MainActivity).changeBack() //배경화면 변경해서 새싹화면으로 돌아온 경우

        val backimgindex=(activity as MainActivity).getBackImg()
        binding!!.apply {
            backimg.setBackgroundResource(backimgindex)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}