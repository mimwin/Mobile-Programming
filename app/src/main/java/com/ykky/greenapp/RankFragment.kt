package com.ykky.greenapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.util.*


class RankFragment : Fragment() {

    lateinit var firebaseauth: FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref: DatabaseReference    // 실시간 데이터베이스
    lateinit var firebaseUser: FirebaseUser

    lateinit var first: TextView
    lateinit var second: TextView
    lateinit var third: TextView
    lateinit var fourth: TextView
    lateinit var fifth: TextView
    lateinit var firstpercent: TextView
    lateinit var secondpercent: TextView
    lateinit var thirdpercent: TextView
    lateinit var fourthpercent: TextView
    lateinit var fifthpercent: TextView
    lateinit var mypercent: TextView
    lateinit var mytotaltodo: TextView
    lateinit var mycompletetodo: TextView
    lateinit var myusedday: TextView
    lateinit var myrelative: TextView
    lateinit var yourrank: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_rank, container, false)

        first = view.findViewById(R.id.first)
        second = view.findViewById(R.id.second)
        third = view.findViewById(R.id.third)
        fourth = view.findViewById(R.id.fourth)
        fifth = view.findViewById(R.id.fifth)
        firstpercent = view.findViewById(R.id.firstpercent)
        secondpercent = view.findViewById(R.id.secondpercent)
        thirdpercent = view.findViewById(R.id.thirdpercent)
        fourthpercent = view.findViewById(R.id.fourthpercent)
        fifthpercent = view.findViewById(R.id.fifthpercent)
        mypercent = view.findViewById(R.id.mypercent)
        mytotaltodo = view.findViewById(R.id.mytotaltodo)
        mycompletetodo = view.findViewById(R.id.mycompletedtodo)
        myusedday = view.findViewById(R.id.myusedday)
        myrelative = view.findViewById(R.id.myrelative)
        yourrank = view.findViewById(R.id.yourrank)
        val firstimage = view.findViewById<ImageView>(R.id.firstimg)
        val secondimage = view.findViewById<ImageView>(R.id.secondimg)
        val thirdimage = view.findViewById<ImageView>(R.id.thirdimg)
        val fourthimage = view.findViewById<ImageView>(R.id.fourthimg)
        val fifthimage = view.findViewById<ImageView>(R.id.fifthimg)
        firstimage.setColorFilter(Color.parseColor("#FF0000"))
        secondimage.setColorFilter(Color.parseColor("#FF7F00"))
        thirdimage.setColorFilter(Color.parseColor("#FFD400"))
        fourthimage.setColorFilter(Color.parseColor("#008000"))
        fifthimage.setColorFilter(Color.parseColor("#000080"))

        init()
        return view
    }

    private fun init() {
        firebaseauth = FirebaseAuth.getInstance()
        databaseref = FirebaseDatabase.getInstance().getReference("myAppExample")
        firebaseUser = firebaseauth.currentUser!!

        val getTime = getdate2()
        val timearr = getTime.split(",")
        var mon = timearr[1]
        var date = timearr[2]
        var hour = timearr[3]
        var min = timearr[4]
        var sec = timearr[5]
        if(date=="01"&&hour=="00"&&min=="00"&&sec=="00"){
            //달이 바뀌면 leaderboard rate, allcount truecount 초기화
            databaseref.child("Leaderboard").get().addOnSuccessListener {
                    val count=it.childrenCount
                    for(item in it.children){
                        val uid=item.key!!
                        databaseref.child("Leaderboard").child(uid).child("rate").setValue(0)
                        databaseref.child("Leaderboard").child(uid).child("useraccount")
                            .child("allCount").setValue(0)
                        databaseref.child("Leaderboard").child(uid).child("useraccount")
                            .child("trueCount").setValue(0)
                    }

            }
        }

            val countlistener: ValueEventListener = object : ValueEventListener {
                var c = 0
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children) {
                        c++
                        if (c == 5) {
                            first.text =
                                item.child("useraccount").child("nickname").value.toString()
                            firstpercent.text = item.child("rate").value.toString()
                        } else if (c == 4) {
                            second.text =
                                item.child("useraccount").child("nickname").value.toString()
                            secondpercent.text = item.child("rate").value.toString()
                        } else if (c == 3) {
                            third.text =
                                item.child("useraccount").child("nickname").value.toString()
                            thirdpercent.text = item.child("rate").value.toString()
                        } else if (c == 2) {
                            fourth.text =
                                item.child("useraccount").child("nickname").value.toString()
                            fourthpercent.text = item.child("rate").value.toString()
                        } else if (c == 1) {
                            fifth.text =
                                item.child("useraccount").child("nickname").value.toString()
                            fifthpercent.text = item.child("rate").value.toString()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }

            val mylistener: ValueEventListener = object : ValueEventListener {
                var c = 0

                override fun onDataChange(snapshot: DataSnapshot) {
                    var count = 0L
                    databaseref.child("Leaderboard").get().addOnSuccessListener {
                        count = it.childrenCount
                        for (item in snapshot.children) {
                            c++
                            if (item.child("useraccount")
                                    .child("token").value.toString() == firebaseUser.uid.toString()
                            ) {
                                val point = round((c - 1) / (count.toDouble()) * 100)
                                yourrank.text = "${count - c + 1}등"
                                myrelative.text = "당신은 ${point}%의 유저보다 더 열심히 살고 있습니다!"
                                Log.e("MyLISTENER - LEADER BOARD", "$count  $point $c")
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }

            val query = databaseref.child("Leaderboard")
                .orderByChild("rate").limitToLast(5)
            query.addListenerForSingleValueEvent(countlistener)

            val query2 = databaseref.child("Leaderboard").orderByChild("rate")
            query2.addListenerForSingleValueEvent(mylistener)


            databaseref.child("UserAccount")
                .child(firebaseUser.uid.toString()).get().addOnSuccessListener {
                    mypercent.text = "${it.child("nickname").value.toString()}" + "님의 달성률 확인"
                    mytotaltodo.text = it.child("allCount").value.toString()
                    mycompletetodo.text = it.child("trueCount").value.toString()

                    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREAN)
                    val registerdate = dateFormat.parse(it.child("registerDate").value.toString())
                    val today = dateFormat.parse(getdate())

                    myusedday.text =
                        "${((today.time - registerdate.time) / (24 * 60 * 60 * 1000)) + 1}"

                }
        }


    private fun getdate(): String {
        val now: Long = System.currentTimeMillis()
        val mdate: Date = Date(now)
        val simpelDate: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        val getTime: String = simpelDate.format(mdate)
        return getTime
    }

    private fun getdate2(): String {
        val now: Long = System.currentTimeMillis()
        val mdate: Date = Date(now)
        val simpelDate: SimpleDateFormat = SimpleDateFormat("yyyy,MM,dd,HH,mm,ss", Locale.KOREA)
        val getTime: String = simpelDate.format(mdate)
        return getTime
    }

}