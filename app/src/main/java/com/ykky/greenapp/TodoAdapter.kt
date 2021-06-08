package com.ykky.greenapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ykky.greenapp.databinding.RowBinding


class TodoAdapter(options: FirebaseRecyclerOptions<TodoData>)
    : FirebaseRecyclerAdapter<TodoData, TodoAdapter.ViewHolder>(options){
    lateinit var firebaseauth : FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref : DatabaseReference    // 실시간 데이터베이스

    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding: RowBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                itemClickListener!!.onItemClick(it, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: TodoData) {
        firebaseauth = FirebaseAuth.getInstance()
        databaseref = FirebaseDatabase.getInstance().getReference("myAppExample")
        val firebaseUser : FirebaseUser? = firebaseauth.currentUser

        holder.binding.apply {
            checkBtn.isSelected = model.isChecked
            todo.text = model.todo.toString()

            checkBtn.setOnClickListener {
                if(model.isChecked){
                    model.isChecked = false
                    checkBtn.isSelected = false
                    databaseref.child("UserAccount")
                            .child(firebaseUser?.uid.toString())
                            .child("todo")
                            .child(model.todo.toString())
                            .child("checked")
                            .setValue(false)
                    Toast.makeText(it.context, "취소", Toast.LENGTH_SHORT).show()
                }else{
                    model.isChecked = true
                    checkBtn.isSelected = true
                    databaseref.child("UserAccount")
                            .child(firebaseUser?.uid.toString())
                            .child("todo")
                            .child(model.todo.toString())
                            .child("checked")
                            .setValue(true)
                    Toast.makeText(it.context, "완료", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}