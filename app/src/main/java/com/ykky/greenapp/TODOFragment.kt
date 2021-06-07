package com.ykky.greenapp
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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


class TODOFragment : Fragment() {
    lateinit var firebaseauth : FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref : DatabaseReference    // 실시간 데이터베이스
    lateinit var adapter: TodoAdapter
    lateinit var layoutManager: LinearLayoutManager

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
        var year = view.findViewById<TextView>(R.id.year)
        var date = view.findViewById<TextView>(R.id.date)

        var now = LocalDate.now()
        var yearnow = now.format(DateTimeFormatter.ofPattern("yyyy년"))
        var datenow = now.format(DateTimeFormatter.ofPattern("MM월 dd일"))

        year.text = yearnow.toString()
        date.text = datenow.toString()

        view.apply {

            firebaseauth = FirebaseAuth.getInstance()
            databaseref = FirebaseDatabase.getInstance().getReference("myAppExample")
            val firebaseUser : FirebaseUser? = firebaseauth.currentUser


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
            adapter.itemClickListener = object :TodoAdapter.OnItemClickListener{
                override fun onItemClick(view: View, position: Int) {
                    //todo 클릭시 update 화면으로 이동
                }
            }

            recyclerView.adapter = adapter

            adapter.startListening()
            adapter.notifyDataSetChanged()

            addBtn.setOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment, TodoAddFragment())
                    ?.addToBackStack(null)
                    ?.commit()
            }

            calendarBtn.setOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment, TodoCalendarFragment())
                    ?.addToBackStack(null)
                    ?.commit()
            }
        }

        return view
    }



}