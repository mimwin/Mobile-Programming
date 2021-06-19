package com.ykky.greenapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ykky.greenapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var firebaseauth : FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref : DatabaseReference    // 실시간 데이터베이스
    lateinit  var useraccount : UserAccount
    lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
        init()
    }

    private fun init() {

        firebaseauth = FirebaseAuth.getInstance()
        databaseref = FirebaseDatabase.getInstance().getReference("myAppExample")

        binding.apply {
            btnRegister.setOnClickListener {

                //회원가입 화면 전환
                val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
                startActivity(intent)
            }

            btnLogin.setOnClickListener {
                if(etEmail.text.isBlank()||etPwd.text.isBlank()){
                    Toast.makeText(this@LoginActivity,"아이디, 비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show()
                }
                else{
                    //로그인 요청
                    firebaseauth.signInWithEmailAndPassword(etEmail.text.toString(),etPwd.text.toString()).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //로그인 성공
                            Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()

                        } else {
                            //로그인 실패
                            Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}