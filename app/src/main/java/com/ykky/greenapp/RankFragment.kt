package com.ykky.greenapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RankFragment : Fragment() {

    lateinit var firebaseauth : FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref : DatabaseReference    // 실시간 데이터베이스
    lateinit var todoData : TodoData
    lateinit var firebaseUser : FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rank, container, false)

        init()
        return view
    }

    private fun init(){
        firebaseauth = FirebaseAuth.getInstance()
        databaseref = FirebaseDatabase.getInstance().getReference("myAppExample")!!
        firebaseUser = firebaseauth.currentUser!!

        //addleaderboard()

        val text = view?.findViewById<TextView>(R.id.ranktext)

        var rank = databaseref.child("Leaderboard").orderByChild("rate").limitToLast(5)

        Log.e("RANK",rank.toString())
        //val cnt = getLeaderCount()

    }

    fun addleaderboard() {
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

}