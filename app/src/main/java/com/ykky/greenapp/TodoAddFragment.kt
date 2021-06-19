package com.ykky.greenapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.round

class TodoAddFragment : Fragment() {

    lateinit var firebaseauth : FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref : DatabaseReference    // 실시간 데이터베이스
    lateinit var todoData : TodoData
    lateinit var finish : Button
    lateinit var cancel : Button
    lateinit var todoEditText : EditText
    lateinit var memo : EditText
    lateinit var firebaseUser : FirebaseUser
    lateinit var date : String
    var count = 0L
    var trueflag = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_todo_add, container, false)
        finish = view.findViewById<Button>(R.id.finishBtn)
        cancel = view.findViewById(R.id.cancelBtn)
        todoEditText = view.findViewById(R.id.todoEditText)
        memo = view.findViewById(R.id.memo)

        date = requireArguments().getString("date").toString()
        (activity as MainActivity).nav.visibility=View.GONE
        init()
        return view
    }

    private fun init() {
        firebaseauth = FirebaseAuth.getInstance()
        databaseref = FirebaseDatabase.getInstance().getReference("myAppExample")
        firebaseUser = firebaseauth.currentUser!!

        //val date = "2021-06-09"

        finish.setOnClickListener {

            val todotext = todoEditText.text.toString()
            val memo = memo.text.toString()

            if(todotext==""){
                Toast.makeText(context, "Todo를 입력하세요", Toast.LENGTH_SHORT).show()
            }else{
                todoData = TodoData(date,todotext,memo,false)

                // 투두 추가
                databaseref.child("UserAccount")
                        .child(firebaseUser.uid.toString())
                        .child("todo")
                        .child(todoData.todo)
                        .setValue(todoData)


                // 개수 증가 (전체)
                IncreaseCount(true)

                Toast.makeText(context, "추가 완료", Toast.LENGTH_SHORT).show()

                var token = date.split('-')

                clearInput()
                //todoFragment로 돌아가기
                (activity as MainActivity).nav.visibility=View.VISIBLE
                (activity as MainActivity).replaceFragment(TODOFragment(token[0].toInt(),token[1].toInt(),token[2].toInt()),"navtodo")
            }
        }

        cancel.setOnClickListener {
            Toast.makeText(context,"추가 취소",Toast.LENGTH_SHORT).show()
            var token = date.split('-')
            clearInput()
            (activity as MainActivity).nav.visibility=View.VISIBLE
            (activity as MainActivity).replaceFragment(TODOFragment(token[0].toInt(),token[1].toInt(),token[2].toInt()),"navtodo")
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

                    if(trueflag){
                        truecount += 1L
                    }

                    //리더보드 갱신
                    databaseref.child("Leaderboard").child(firebaseUser.uid.toString()).get().addOnSuccessListener { it2->

                        databaseref.child("Leaderboard").child(firebaseUser.uid.toString())
                                .child("useraccount").child("allCount").setValue(count)
                        val rate : Double = round(truecount / (count.toDouble()) * 100.0)

                        Log.e("INC COUNT - LEADER BOARD","$count  $truecount  $rate")
                        databaseref.child("Leaderboard").child(firebaseUser.uid.toString()).child("rate").setValue(rate)

                    }
                }
    }

    fun clearInput(){
        view.apply {
            todoEditText.text.clear()
            memo.text.clear()
        }
    }
}