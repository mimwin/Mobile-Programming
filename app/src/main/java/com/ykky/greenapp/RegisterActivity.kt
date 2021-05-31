package com.ykky.greenapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ykky.greenapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    lateinit var firebaseauth : FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref : DatabaseReference    // 실시간 데이터베이스
    lateinit var binding : ActivityRegisterBinding
    lateinit  var useraccount : UserAccount
    val todo = ArrayList<TodoData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)
        init()
    }

    private fun init() {
        firebaseauth = FirebaseAuth.getInstance()
        databaseref = FirebaseDatabase.getInstance().getReference("myAppExample")

        binding.apply {

            btnRegister.setOnClickListener {

                val email = etEmail.text.toString()
                val pw = etPwd.text.toString()
                val nickname = nickname.text.toString()
                val repw = etPwdAgain.text.toString()
                todo.add(TodoData("hi","hi","hi",false))

                Log.e("todo","$todo")

                //비밀번호 == 비밀번호확인 인 경우
                if(pw == repw){

                    //파이어베이스 인증 시작
                    firebaseauth.createUserWithEmailAndPassword(email,pw).addOnCompleteListener {
                        task->
                        if(task.isSuccessful){
                            val firebaseUser : FirebaseUser? = firebaseauth.currentUser
                            // email, pw, token, nickname
                            useraccount = UserAccount(firebaseUser?.email.toString(),pw,firebaseUser?.uid.toString(),nickname,todo)

                            //setvalue = insert
                            databaseref.child("UserAccount").child(firebaseUser?.uid.toString()).setValue(useraccount)

                            Toast.makeText(this@RegisterActivity,"회원가입 성공",Toast.LENGTH_SHORT).show()

                            finish()

                        }
                        else{
                            Log.e("ERROR", task.exception?.message.toString())
                            Toast.makeText(this@RegisterActivity,task.exception?.message.toString(),Toast.LENGTH_SHORT).show()
                            todo.clear()
                        }
                    }
                }
                else{
                    // 비밀번호 != 비밀번호확인 인 경우
                    Toast.makeText(this@RegisterActivity,"비밀번호가 같지 않습니다.",Toast.LENGTH_SHORT).show()

                    //데이터베이스에서 중복 확인하여 중복되면 중복 toast 띄우기 ( 회원가입 실패 이유 )

                }


            }

        }
    }
}