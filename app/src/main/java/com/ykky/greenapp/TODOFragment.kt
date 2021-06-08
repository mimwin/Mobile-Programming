package com.ykky.greenapp
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class TODOFragment(y:Int, m:Int,d:Int) : Fragment() {
    lateinit var firebaseauth : FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref : DatabaseReference    // 실시간 데이터베이스
    lateinit var adapter: TodoAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var firebaseUser : FirebaseUser

    var backimg=0
    var tyear=y
    var tmonth=m
    var tday=d

    var count = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_t_o_d_o, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val addBtn = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val calendarBtn = view.findViewById<ImageButton>(R.id.calendarBtn)
        val todobackimg=view.findViewById<ImageView>(R.id.todobackimg)
        val todayrate=view.findViewById<TextView>(R.id.todayrate)
        var year = view.findViewById<TextView>(R.id.year)
        var date = view.findViewById<TextView>(R.id.date)



        var now = LocalDate.now()
        var yearnow = now.format(DateTimeFormatter.ofPattern("yyyy년"))
        var datenow = now.format(DateTimeFormatter.ofPattern("MM월 dd일"))

        if(tyear==0){
            year.text = yearnow.toString()
            date.text = datenow.toString()
        }
        else{
            year.text = "${tyear}년"
            date.text = "${tmonth}월 ${tday}일"
        }

        view.apply {

            firebaseauth = FirebaseAuth.getInstance()
            databaseref = FirebaseDatabase.getInstance().getReference("myAppExample")
            firebaseUser= firebaseauth.currentUser!!


            //해당날짜 todo 가져와서 recyclerView에 출력
            val query = databaseref.child("UserAccount")
                .child(firebaseUser?.uid.toString())
                .child("todo")
                .orderByChild("date")
                .equalTo("2021-06-07")

            val option = FirebaseRecyclerOptions.Builder<TodoData>()
                .setQuery(query, TodoData::class.java)
                .build()
            adapter = TodoAdapter(option)
            adapter.itemClickListener = object : TodoAdapter.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    //todo 클릭시 update 화면으로 이동
                }
            }

            recyclerView.adapter = adapter

            adapter.startListening()
            adapter.notifyDataSetChanged()

            addBtn.setOnClickListener {
//                activity?.supportFragmentManager?.beginTransaction()
//                    ?.replace(R.id.fragment, TodoAddFragment())
//                    ?.addToBackStack(null)
//                    ?.commit()
                (activity as MainActivity).replaceFragment(TodoAddFragment(),"todoadd")
            }

           // val rate=TodayRate("2021-06-08")
            //todayrate.text="오늘의 달성률 : $rate"

            backimg = (activity as MainActivity).getBackImg()
            todobackimg.setBackgroundResource(backimg)
            todobackimg.alpha = 0.7f

            calendarBtn.setOnClickListener {
//                activity?.supportFragmentManager?.beginTransaction()
//                    ?.replace(R.id.fragment, TodoCalendarFragment())
//                    ?.addToBackStack("cal")
//                    ?.commit()
                (activity as MainActivity).replaceFragment(TodoCalendarFragment(),"todocal")
            }
        }
        return view
    }

    // 해당 날짜에 해당하는 투두 개수 가져오기
    fun getCount(date : String ): Long {
        databaseref.child("UserAccount")
            .child(firebaseUser.uid.toString())
            .child("todo").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val doc = it.result!!
                    count = doc.childrenCount -1
                }
            }
        return count
    }

    fun TodayRate(date:String):Double {

        val cnt = getCount(date)
        var truecount : Double = 0.0
        var rate : Double = 0.0

        databaseref.child("UserAccount")
            .child(firebaseUser.uid.toString())
            .child("todo").get().addOnSuccessListener {

                for(i in 1..count){
                    if(it.child(i.toString()).child("checked").value == "true"){
                        truecount++
                    }
                }
                rate = ((truecount/(cnt)) * 100)
                Log.e("TODAY RATE - INSIDE","$cnt  $truecount  $rate")
                if(rate==100.0) {
                    (activity as MainActivity).setComplete(true)
                }
                else{
                    (activity as MainActivity).setComplete(false)
                }
            }
        return rate

    }

}