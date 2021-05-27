package com.ykky.myappexample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ykky.myappexample.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    lateinit var firebaseauth : FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref : DatabaseReference    // 실시간 데이터베이스
    lateinit var binding : ActivityRegisterBinding
    lateinit  var useraccount : UserAccount

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
                //파이어베이스 인증 시작

                val email = etEmail.text.toString()
                val pw = etPwd.text.toString()

                Log.e("REGISTER","$email   $pw")

                firebaseauth.createUserWithEmailAndPassword(email,pw).addOnCompleteListener {
                    task->
                    if(task.isSuccessful){
                        val firebaseUser : FirebaseUser? = firebaseauth.currentUser
                        useraccount = UserAccount(firebaseUser?.email.toString(),etPwd.text.toString(),firebaseUser?.uid.toString())

                        Log.e("REGISTER","HERERERERERERERERERERE")
                        //setvalue = insert
                        databaseref.child("UserAccount").child(firebaseUser?.uid.toString()).setValue(useraccount)

                        Toast.makeText(this@RegisterActivity,"회원가입 성공",Toast.LENGTH_SHORT).show()

                    }
                    else{
                        Toast.makeText(this@RegisterActivity,"회원가입 실패",Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }
}