package com.ykky.greenapp

import android.os.Bundle
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
    lateinit var todoEditText : EditText
    lateinit var memo : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_todo_add, container, false)
        finish = view.findViewById<Button>(R.id.finishBtn)
        todoEditText = view.findViewById(R.id.todoEditText)
        memo = view.findViewById(R.id.memo)

        init()
        return view
    }

    private fun init() {
        firebaseauth = FirebaseAuth.getInstance()
        databaseref = FirebaseDatabase.getInstance().getReference("myAppExample")


        finish.setOnClickListener {

            val todotext = todoEditText.text.toString()
            val memo = memo.text.toString()

            val firebaseUser : FirebaseUser? = firebaseauth.currentUser
            var todoarr : ArrayList<TodoData>

           //기존 투두 가져오기
            databaseref.child("UserAccount")
                    .child(firebaseUser?.uid.toString())
                    .child("todo")
                    .get().addOnSuccessListener {

                        todoarr = it.value as ArrayList<TodoData>

                        //투두 내용 추가
                        todoData = TodoData("2021-05-31",todotext,memo,false)
                        todoarr.add(todoData)

                        //추가한 내용 업데이트
                        databaseref.child("UserAccount")
                                .child(firebaseUser?.uid.toString())
                                .child("todo")
                                .setValue(todoarr)

                    }.addOnFailureListener {
                        Toast.makeText(context,"TODO 정보를 불러오는데 실패하였습니다.",Toast.LENGTH_SHORT).show()
                    }


            Toast.makeText(context,"추가 완료",Toast.LENGTH_SHORT).show()

            //todoFragment로 돌아가기
        }


    }
}