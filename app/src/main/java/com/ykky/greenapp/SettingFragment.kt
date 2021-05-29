package com.ykky.greenapp
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.ykky.greenapp.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    val items= arrayOf("image1","image2","image3","image4","image5","image6")
    lateinit var re1:LinearLayout
    lateinit var re2:GridLayout
    lateinit var re3:LinearLayout
    var binding:FragmentSettingBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSettingBinding.inflate(layoutInflater,container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.apply{
            //imagview clicklistener
            backimg1.setOnClickListener {
                (activity as MainActivity).backimgname=items[0]
                (activity as MainActivity).replaceFragment(GreenFragment(),"navgreen")
            }
            backimg2.setOnClickListener {
                (activity as MainActivity).backimgname=items[1]
                (activity as MainActivity).replaceFragment(GreenFragment(),"navgreen")
            }
            backimg3.setOnClickListener {
                (activity as MainActivity).backimgname=items[2]
                (activity as MainActivity).replaceFragment(GreenFragment(),"navgreen")
            }
            backimg4.setOnClickListener {
                (activity as MainActivity).backimgname=items[3]
                (activity as MainActivity).replaceFragment(GreenFragment(),"navgreen")
            }
            backimg5.setOnClickListener {
                (activity as MainActivity).backimgname=items[4]
                (activity as MainActivity).replaceFragment(GreenFragment(),"navgreen")
            }
            backimg6.setOnClickListener {
                (activity as MainActivity).backimgname=items[5]
                (activity as MainActivity).replaceFragment(GreenFragment(),"navgreen")
            }

            mypagedown.setOnClickListener {
                re1=view.findViewById<LinearLayout>(R.id.maypagedata)
                if(re1.visibility==View.VISIBLE){
                    re1.visibility=View.GONE
                }
                else if(re1.visibility==View.GONE){
                    re1.visibility=View.VISIBLE
                }
            }
            changebackgrounddown.setOnClickListener {
                if(re2.visibility==View.VISIBLE){
                    re2.visibility=View.GONE
                }
                else if(re2.visibility==View.GONE){
                    re2.visibility=View.VISIBLE
                }
            }
            devinfodown.setOnClickListener {
                re3=view.findViewById<LinearLayout>(R.id.devinfodata)
                if(re3.visibility==View.VISIBLE){
                    re3.visibility=View.GONE
                }
                else if(re3.visibility==View.GONE){
                    re3.visibility=View.VISIBLE
                }
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }

}