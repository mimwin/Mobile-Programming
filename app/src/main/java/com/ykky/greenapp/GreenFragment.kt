package com.ykky.greenapp
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.ykky.greenapp.databinding.FragmentGreenBinding

class GreenFragment() : Fragment() {
    var binding:FragmentGreenBinding?=null
    val flowerArray=arrayOf(0,0,0,1,0,0,1,1,1,1,0,1,0,0,1,1,0,0,1,1,1,1,0,0,0,0,1,0,1,1)
    val flowers=arrayOf(R.id.flower1,R.id.flower2,R.id.flower3,R.id.flower4,R.id.flower5,
        R.id.flower6,R.id.flower7,R.id.flower8,R.id.flower9,R.id.flower10,
        R.id.flower11,R.id.flower12,R.id.flower13,R.id.flower14,R.id.flower15,
        R.id.flower16,R.id.flower17,R.id.flower18,R.id.flower19,R.id.flower20,
        R.id.flower21,R.id.flower22,R.id.flower23,R.id.flower24,R.id.flower25,
        R.id.flower26,R.id.flower27,R.id.flower28,R.id.flower29,R.id.flower30)

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
        for(i in 0 until flowerArray.size){
            val f= requireActivity().findViewById<ImageView>(flowers[i])
            if(flowerArray[i]==1){
                f.visibility=View.VISIBLE
            }
            else{
                f.visibility=View.INVISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}