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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.lang.Math.round
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class TODOFragment(y:Int, m:Int,d:Int) : Fragment() {
    lateinit var firebaseauth : FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref : DatabaseReference    // 실시간 데이터베이스
    lateinit var adapter: TodoAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var firebaseUser : FirebaseUser
    lateinit var todayrate : TextView

    var backimg=0
    var tyear=y
    var tmonth=m
    var tday=d

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refreshFragment(this, requireFragmentManager())
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
        todayrate=view.findViewById<TextView>(R.id.todayrate)
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

            var count = 0L
            var truecount : Double = 0.0
            var rate : Double = 0.0
            firebaseauth = FirebaseAuth.getInstance()
            databaseref = FirebaseDatabase.getInstance().getReference("myAppExample")
            firebaseUser= firebaseauth.currentUser!!

            TodayRate2("2021-06-09")

            //해당날짜 투두 가져와서 recyclerView에 출력
            val query = databaseref.child("UserAccount")
                    .child(firebaseUser.uid.toString())
                    .child("todo")
                    .orderByChild("date")
                    .`equalTo`("2021-06-09")

            val option = FirebaseRecyclerOptions.Builder<TodoData>()
                .setQuery(query, TodoData::class.java)
                .build()
            adapter = TodoAdapter(option)
            TodayRate2("2021-06-09")
            adapter.itemClickListener = object : TodoAdapter.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    //투두 클릭시 update 화면으로 이동
                }
            }

            recyclerView.adapter = adapter

            TodayRate2("2021-06-09")
            val simpleCallBack = object :ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN
            or ItemTouchHelper.UP, ItemTouchHelper.RIGHT){
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val item = adapter.getItem(position)
                    databaseref.child("UserAccount")
                            .child(firebaseUser.uid.toString())
                            .child("todo")
                            .child(item.todo.toString())
                            .removeValue()
                            .addOnSuccessListener {
                                Log.i("TODO","Remove Success")
                                TodayRate2("2021-06-09")
                            }
                    IncreaseCount(false)
                    adapter.notifyItemRemoved(position)
                }
            }
            val itemTouchHelper = ItemTouchHelper(simpleCallBack)
            itemTouchHelper.attachToRecyclerView(recyclerView)

            adapter.startListening()
            adapter.notifyDataSetChanged()

            addBtn.setOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment, TodoAddFragment())
                    ?.addToBackStack(null)
                    ?.commit()
                (activity as MainActivity).replaceFragment(TodoAddFragment(),"todoadd")
            }

            backimg = (activity as MainActivity).getBackImg()
            todobackimg.setBackgroundResource(backimg)
            todobackimg.alpha = 0.7f

            calendarBtn.setOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment, TodoCalendarFragment())
                    ?.addToBackStack("cal")
                    ?.commit()
                (activity as MainActivity).replaceFragment(TodoCalendarFragment(),"todocal")
            }
        }
        // Fragment 클래스에서 사용 시
        refreshFragment(this, requireFragmentManager())
        return view
    }

    // Fragment 새로고침
    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
        var ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }

    fun TodayRate2(date:String) : Double{

        var count = 0L
        var truecount : Double = 0.0
        var rate : Double = 0.0

        val countlistener : ValueEventListener = object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                count = snapshot.childrenCount
                for(item in snapshot.children){
                    if(item.child("checked").value==true)truecount++
                }

                rate = ((truecount/(count)) * 100)
                val rateInt : Int = round(rate).toInt()

                todayrate.text= "오늘의 달성률 : ${rateInt.toString()}"
                Log.e("LISTENER - INSIDE","${count.toString()} $truecount  $rate")
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }

        val query = databaseref.child("UserAccount")
                .child(firebaseUser.uid.toString())
                .child("todo")
                .orderByChild("date")
                .equalTo(date)

        query.addListenerForSingleValueEvent(countlistener)

        Log.e("LISTENER - OUTSIDE","${count.toString()} $truecount")
        return rate
    }

    fun IncreaseCount(f : Boolean) {
        databaseref.child("UserAccount")
                .child(firebaseUser.uid.toString()).get().addOnSuccessListener { it ->
                    var count = it.child("allCount").value as Long

                    if(f){
                        count++
                        databaseref.child("UserAccount")
                                .child(firebaseUser.uid.toString())
                                .child("allCount")
                                .setValue(count)
                    }
                    else if(!f){
                        count--
                        databaseref.child("UserAccount")
                                .child(firebaseUser.uid.toString())
                                .child("allCount")
                                .setValue(count)
                    }

                    var truecount = it.child("trueCount").value as Long

                    //리더보드 갱신
                    databaseref.child("Leaderboard").child(firebaseUser.uid.toString()).get().addOnSuccessListener { it2->

                        databaseref.child("Leaderboard")
                                .child(firebaseUser.uid.toString()).child("allCount").setValue(count)
                        val rate : Double = kotlin.math.round(truecount / (count.toDouble()) * 100.0)

                        Log.e("INC COUNT - LEADER BOARD","$count  $truecount  $rate")
                        databaseref.child("Leaderboard").child(firebaseUser.uid.toString()).child("rate").setValue(rate)

                    }
                }
    }


}