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

class TodoAddFragment : Fragment() {

    lateinit var firebaseauth : FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref : DatabaseReference    // 실시간 데이터베이스
    lateinit var todoData : TodoData
    lateinit var finish : Button
    lateinit var cancel : Button
    lateinit var todoEditText : EditText
    lateinit var memo : EditText
    lateinit var firebaseUser : FirebaseUser
    var count = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_todo_add, container, false)
        finish = view.findViewById<Button>(R.id.finishBtn)
        cancel = view.findViewById(R.id.cancelBtn)
        todoEditText = view.findViewById(R.id.todoEditText)
        memo = view.findViewById(R.id.memo)

        init()
        return view
    }

    private fun init() {
        firebaseauth = FirebaseAuth.getInstance()
        databaseref = FirebaseDatabase.getInstance().getReference("myAppExample")
        firebaseUser = firebaseauth.currentUser!!

        val date = "2021-05-31"

        //투두 개수 가져오기
        count = getCount(date)

        finish.setOnClickListener {

            val todotext = todoEditText.text.toString()
            val memo = memo.text.toString()

            todoData = TodoData("2021-06-07",todotext,memo,false)

            // 투두 추가
            databaseref.child("UserAccount")
                    .child(firebaseUser.uid.toString())
                    .child("todo")
                    .child((count+1L).toString())
                    .setValue(todoData)

            // 개수 증가 (전체)
            IncreaseCount()

            // 전체 개수 가져오기
            count = getCount(date)

            // 체크 true 했을 경우
            checkTrue(date)
            IncreaseTrue()

            //오늘의 달성률 계산
            TodayRate(date)

            //리더보드 갱신
            Leaderboard()

            Toast.makeText(context, "추가 완료", Toast.LENGTH_SHORT).show()

            clearInput()
            //todoFragment로 돌아가기
//            activity?.supportFragmentManager?.beginTransaction()
//                    ?.replace(R.id.fragment, TODOFragment(0,0,0))
//                    ?.addToBackStack(null)
//                    ?.commit()
            (activity as MainActivity).replaceFragment(TODOFragment(0,0,0),"navtodo")
        }

        cancel.setOnClickListener {
            Toast.makeText(context,"추가 취소",Toast.LENGTH_SHORT).show()
            clearInput()
//            activity?.supportFragmentManager?.beginTransaction()
//                    ?.replace(R.id.fragment, TODOFragment(0,0,0))
//                    ?.addToBackStack(null)
//                    ?.commit()
            (activity as MainActivity).replaceFragment(TODOFragment(0,0,0),"navtodo")
        }
    }

    fun Leaderboard() {
        var allcount : Double = 0.0
        var alltrue : Double = 0.0
        databaseref.child("UserAccount").child(firebaseUser.uid).get().addOnSuccessListener {
                    val emailId=it.child("emailId").value.toString()
                    val password=it.child("password").value.toString()
                    val registerDate = it.child("registerDate").value.toString()
                    val nickname=it.child("nickname").value.toString()
                    allcount = (it.child("allCount").value as Long).toDouble()
                    alltrue = (it.child("trueCount").value as Long).toDouble()

                    var useraccount = UserAccount(emailId,password,registerDate,firebaseUser.uid,nickname,ArrayList<TodoData>(),allcount,alltrue)

                    val rate : Double = (alltrue / allcount)* 100

            Log.e("LEADERBOARD-INSIDE","$useraccount  $allcount $alltrue $rate")

                    val userleader = LeaderboardData(rate,useraccount)
                    val leadercount = getLeaderCount()

                    databaseref.child("Leaderboard")
                            .child((leadercount+1L).toString())
                            .setValue(userleader)

                    databaseref.child("Leaderboard")
                            .orderByChild("rate")
                }
    }

    fun IncreaseCount(){
        databaseref.child("UserAccount")
                .child(firebaseUser.uid.toString()).get().addOnSuccessListener {
            val count = it.child("allCount").value as Long
            databaseref.child("UserAccount")
                    .child(firebaseUser.uid.toString())
                    .child("allCount")
                    .setValue(count+1L)
        }

    }


    fun IncreaseTrue(){
        databaseref.child("UserAccount")
                .child(firebaseUser.uid.toString()).get().addOnSuccessListener {
                    val count = it.child("trueCount").value as Long

                    Log.e("INCREASETRUE",count.toString())

                    databaseref.child("UserAccount")
                            .child(firebaseUser.uid.toString())
                            .child("trueCount")
                            .setValue(count + 1L)

                }
    }


    fun getLeaderCount() : Long{
        var count = 0L
        databaseref.child("Leaderboard").get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val doc = it.result!!
                        count = doc.childrenCount
                    }
                }
        return count
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

    fun TodayRate(date:String) {

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
                }
    }


    fun checkTrue(date : String){

        val count = getCount(date)

        databaseref.child("UserAccount")
                .child(firebaseUser.uid.toString())
                .child("todo")
                .child((count).toString())
                .child("checked")
                .setValue("true")
    }

    fun clearInput(){
        view.apply {
            todoEditText.text.clear()
            memo.text.clear()
        }
    }
}