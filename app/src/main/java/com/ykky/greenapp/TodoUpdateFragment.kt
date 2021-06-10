package com.ykky.greenapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class TodoUpdateFragment : Fragment() {
    lateinit var firebaseauth : FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref : DatabaseReference    // 실시간 데이터베이스
    lateinit var todoText : EditText
    lateinit var memoText : EditText
    lateinit var finBtn : Button
    lateinit var todo2 : String
    lateinit var memo2 : String
    lateinit var todo1 : TodoData
    lateinit var todoData : TodoData
    lateinit var date1 : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todo1 = requireArguments().getSerializable("data") as TodoData
        todo2 = todo1.todo.toString()
        memo2 = todo1.memo.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_todo_update, container, false)
        finBtn = view.findViewById(R.id.finishBtn)
        todoText = view.findViewById(R.id.todoEditText)
        memoText = view.findViewById(R.id.memo)

        init()
        return view
    }

    private fun init() {
        firebaseauth = FirebaseAuth.getInstance()
        databaseref = FirebaseDatabase.getInstance().getReference("myAppExample")
        val firebaseUser : FirebaseUser? = firebaseauth.currentUser

        todoText.setText(todo2)
        memoText.setText(memo2)

        view.apply {
            finBtn.setOnClickListener {

                databaseref.child("UserAccount")
                        .child(firebaseUser?.uid.toString())
                        .child("todo")
                        .child(todo1.todo)
                        .removeValue()

                todoData = TodoData(todo1.date,todoText.text.toString(),memoText.text.toString(),false)

                databaseref.child("UserAccount")
                        .child(firebaseUser?.uid.toString())
                        .child("todo")
                        .child(todoData.todo)
                        .setValue(todoData)

                date1 = todo1.date
                var token1 = date1.split('-')

                clearInput()
                (activity as MainActivity).replaceFragment(TODOFragment(token1[0].toInt(),token1[1].toInt(),token1[2].toInt()),"nav")
            }
        }
    }

    fun clearInput(){
        view.apply {
            todoText.text.clear()
            memoText.text.clear()
        }
    }

}