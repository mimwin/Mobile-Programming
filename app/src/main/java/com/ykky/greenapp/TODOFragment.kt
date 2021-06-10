package com.ykky.greenapp
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

    lateinit var year:TextView
    lateinit var date:TextView
    lateinit var today:String
    var backimg=0
    var tyear=y
    var tmonth=m
    var tday=d

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refreshFragment(this, parentFragmentManager)
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
        year = view.findViewById<TextView>(R.id.year)
        date = view.findViewById<TextView>(R.id.date)

        var now = LocalDate.now()
        var yearnow = now.format(DateTimeFormatter.ofPattern("yyyy년"))
        var datenow = now.format(DateTimeFormatter.ofPattern("MM월 dd일"))
        var nyear = now.format(DateTimeFormatter.ofPattern("yyyy"))
        var nmonth = now.format(DateTimeFormatter.ofPattern("MM"))
        var ndate = now.format(DateTimeFormatter.ofPattern("dd"))


        if(tyear==0){
            year.text = yearnow.toString()
            date.text = datenow.toString()
            tyear = nyear.toInt()
            tmonth = nmonth.toInt()
            tday = ndate.toInt()
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

            //TodayRate2("2021-06-09")

            //해당날짜 투두 가져와서 recyclerView에 출력
            val query = databaseref.child("UserAccount")
                    .child(firebaseUser.uid.toString())
                    .child("todo")
                    .orderByChild("date")
                    .equalTo("$tyear-$tmonth-$tday")

            val option = FirebaseRecyclerOptions.Builder<TodoData>()
                    .setQuery(query, TodoData::class.java)
                    .build()
            adapter = TodoAdapter(option)
            //TodayRate2("2021-06-09")
            adapter.itemClickListener = object : TodoAdapter.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val bundle = Bundle()
                    val Updatefragment = TodoUpdateFragment()
                    val gitem = adapter.getItem(position)
                    bundle.putSerializable("data",gitem)

                    Updatefragment.arguments = bundle
                    //todo 클릭시 update 화면으로 이동
                    /*activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.fragment, Updatefragment)
                            ?.addToBackStack(null)
                            ?.commit()*/
                    (activity as MainActivity).replaceFragment(Updatefragment,"todoadd")
                }

                override fun onCheckClick(holder:TodoAdapter.ViewHolder, view: View, position: Int) {
                    Log.i("todotodo",holder.binding.todo.toString() )
                    if(view.isSelected){
                        view.isSelected = false
                        databaseref.child("UserAccount")
                            .child(firebaseUser.uid.toString())
                            .child("todo")
                            .child(holder.binding.todo.text.toString())
                            .child("checked")
                            .setValue(false)
                        IncreaseTrue(false)
                        Toast.makeText(requireContext(), "취소", Toast.LENGTH_SHORT).show()

                    }else{
                        view.isSelected = true
                        databaseref.child("UserAccount")
                            .child(firebaseUser.uid.toString())
                            .child("todo")
                            .child(holder.binding.todo.text.toString())
                            .child("checked")
                            .setValue(true)
                        IncreaseTrue(true)
                        Toast.makeText(requireContext(), "완료", Toast.LENGTH_SHORT).show()
                    }
                    val getdate=year.text.substring(0,5)+"-"+date.text.substring(0,3)+"-"+date.text.substring(4,6)
                    TodayRate2("2021-06-09")
                }
            }

            recyclerView.adapter = adapter

            TodayRate2("$tyear-$tmonth-$tday")
            val simpleCallBack = object :ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN
            or ItemTouchHelper.UP, ItemTouchHelper.LEFT){
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
                                TodayRate2("$tyear-$tmonth-$tday")
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

                val bundle = Bundle()
                val Addfragment = TodoAddFragment()
                val ndate = "$tyear-$tmonth-$tday"
                bundle.putString("date", ndate)

                Addfragment.arguments = bundle
                (activity as MainActivity).replaceFragment(Addfragment,"todoadd")
            }

            backimg = (activity as MainActivity).getBackImg()
            todobackimg.setBackgroundResource(backimg)
            todobackimg.alpha = 0.7f

            calendarBtn.setOnClickListener {
                (activity as MainActivity).replaceFragment(TodoCalendarFragment(),"todocal")
            }
        }
        // Fragment 클래스에서 사용 시
        refreshFragment(this, parentFragmentManager)
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
                if(rate==100.0&&date==today){
                    (activity as MainActivity).setComplete(true)
                }
                if(rate!=100.0&&date==today){
                    (activity as MainActivity).setComplete(false)
                }
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

    fun IncreaseTrue(f : Boolean){
        databaseref.child("UserAccount")
            .child(firebaseUser.uid.toString()).get().addOnSuccessListener { it ->
                var count = it.child("trueCount").value as Long

                if(f){
                    val TodoAddFragment : TodoAddFragment = TodoAddFragment()
                    TodoAddFragment.trueflag = true

                    count++

                    databaseref.child("UserAccount")
                        .child(firebaseUser.uid.toString())
                        .child("trueCount")
                        .setValue(count)
                }
                else if(!f){
                    count--
                    databaseref.child("UserAccount")
                        .child(firebaseUser.uid.toString())
                        .child("trueCount")
                        .setValue(count)
                }

                val allcount : Long = it.child("allCount").value as Long

                //리더보드 갱신
                databaseref.child("Leaderboard").child(firebaseUser.uid.toString()).get().addOnSuccessListener { it2 ->
                    databaseref.child("Leaderboard").child(firebaseUser.uid.toString())
                        .child("useraccount").child("trueCount").setValue(count)

                    val rate: Double = kotlin.math.round(count / (allcount.toDouble()) * 100.0)
                    Log.e("INC TRUE - LEADER BOARD", "${count}  ${allcount}  $rate")
                    databaseref.child("Leaderboard").child(firebaseUser.uid.toString()).child("rate").setValue(rate)
                }
            }
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