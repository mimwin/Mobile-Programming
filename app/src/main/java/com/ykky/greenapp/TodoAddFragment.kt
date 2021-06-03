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

            val firebaseUser : FirebaseUser? = firebaseauth.currentUser

            val todotext = todoEditText.text.toString()
            val memo = memo.text.toString()
            var todoarr : ArrayList<TodoData>

            todoData = TodoData("2021-05-31",todotext,memo,false)

            databaseref.child("UserAccount")
                    .child(firebaseUser?.uid.toString())
                    .child("todo")
                    .child(todoData.date)
                    .child(todoData.todo)
                    .setValue(todoData)


            Toast.makeText(context,"추가 완료",Toast.LENGTH_SHORT).show()

            //todoFragment로 돌아가기
        }


    }
}