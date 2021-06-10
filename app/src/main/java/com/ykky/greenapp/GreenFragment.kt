package com.ykky.greenapp
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.ykky.greenapp.databinding.FragmentGreenBinding
import java.text.SimpleDateFormat
import java.util.*

class GreenFragment() : Fragment() {
    var binding:FragmentGreenBinding?=null
    val flowerArray= mutableListOf<Int>(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    val flowers=arrayOf(R.id.flower1,R.id.flower2,R.id.flower3,R.id.flower4,R.id.flower5,
        R.id.flower6,R.id.flower7,R.id.flower8,R.id.flower9,R.id.flower10,
        R.id.flower11,R.id.flower12,R.id.flower13,R.id.flower14,R.id.flower15,
        R.id.flower16,R.id.flower17,R.id.flower18,R.id.flower19,R.id.flower20,
        R.id.flower21,R.id.flower22,R.id.flower23,R.id.flower24,R.id.flower25,
        R.id.flower26,R.id.flower27,R.id.flower28,R.id.flower29,R.id.flower30)
    var isComplete=false
    var flowersum=0

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
        val getTime=getdate()
        val timearr=getTime.split(",")
        var mon = timearr[1]
        var date=timearr[2]
        var hour=timearr[3]
        var min=timearr[4]
        var sec=timearr[5]

        if(date=="01"&&hour=="00"&&min=="00"&&sec=="00"){
            //달이 바뀌면 꽃 초기화
            for(i in 0..30){
                flowerArray[i]=0
                flowersum=0
            }
        }

        //오늘 달성률 100%라면
        if(date!="31"){
            Log.i("green date",date)
            isComplete=(activity as MainActivity).getComplete()
            if(isComplete){
                flowerArray[date.toInt()]=1
                flowersum++
            }
            else{
                flowerArray[date.toInt()]=0
                flowersum--
            }
        }

        //2월이나 31일이 있는 달인 경우
        if(mon=="02"&&date=="28"&&flowersum==28){
            flowerArray[29]=1
            flowerArray[30]=1
        }


        for(i in 0 until flowerArray.size){
            Log.i("flower",flowerArray[i].toString())
            val f= requireActivity().findViewById<ImageView>(flowers[i])
            if(flowerArray[i]==1){
                f.visibility=View.VISIBLE
            }
            else{
                f.visibility=View.INVISIBLE
            }
        }
    }

    private fun getdate() : String{
        val now : Long = System.currentTimeMillis()
        val mdate : Date = Date(now)
        val simpelDate : SimpleDateFormat = SimpleDateFormat("yyyy,MM,dd,HH,mm,ss", Locale.KOREA)
        val getTime : String = simpelDate.format(mdate)
        return getTime
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}