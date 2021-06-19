package com.ykky.greenapp

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ykky.greenapp.databinding.RowBinding
import kotlin.math.round


class TodoAdapter(val options: FirebaseRecyclerOptions<TodoData>)
    : FirebaseRecyclerAdapter<TodoData, TodoAdapter.ViewHolder>(options){
    lateinit var firebaseauth : FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref : DatabaseReference    // 실시간 데이터베이스
    lateinit var firebaseUser : FirebaseUser

    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
        fun onCheckClick(holder:ViewHolder,view:View,position:Int)
    }

    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding: RowBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.checkBtn.setOnClickListener {
                itemClickListener!!.onCheckClick(this,it, adapterPosition)
            }
            binding.todo.setOnClickListener {
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
        firebaseUser = firebaseauth.currentUser!!

        if(model.isChecked) {
            holder.binding.checkBtn.isSelected=true
            holder.binding.row.setBackgroundColor(Color.parseColor("#ffccbc"))
        }
        else{
            holder.binding.checkBtn.isSelected=false
            holder.binding.row.setBackgroundColor(Color.parseColor("#ffffee"))
        }
        holder.binding.todo.text=model.todo.toString()


//        holder.binding.apply {
//            checkBtn.isSelected = model.isChecked
//            todo.text = model.todo.toString()
//
//            checkBtn.setOnClickListener {
//                if(model.isChecked){
//                    model.isChecked = false
//                    checkBtn.isSelected = false
//                    databaseref.child("UserAccount")
//                            .child(firebaseUser.uid.toString())
//                            .child("todo")
//                            .child(model.todo.toString())
//                            .child("checked")
//                            .setValue(false)
//                    IncreaseTrue(false)
//                    Toast.makeText(it.context, "취소", Toast.LENGTH_SHORT).show()
//
//                }else{
//                    model.isChecked = true
//                    checkBtn.isSelected = true
//                    databaseref.child("UserAccount")
//                            .child(firebaseUser.uid.toString())
//                            .child("todo")
//                            .child(model.todo.toString())
//                            .child("checked")
//                            .setValue(true)
//                    IncreaseTrue(true)
//                    Toast.makeText(it.context, "완료", Toast.LENGTH_SHORT).show()
//
//                }
//            }
//        }
    }

    fun IncreaseTrue(f : Boolean){
        databaseref.child("UserAccount")
                .child(firebaseUser.uid.toString()).get().addOnSuccessListener { it ->
                    var count = it.child("trueCount").value as Long

                    if(f){
                        val TodoAddFragment : TodoAddFragment = TodoAddFragment()
                        TodoAddFragment.trueflag = true

                        count++

                        databaseref.child("UserAccount")
                                .child(firebaseUser.uid.toString())
                                .child("trueCount")
                                .setValue(count)
                    }
                    else if(!f){
                        count--
                        databaseref.child("UserAccount")
                                .child(firebaseUser.uid.toString())
                                .child("trueCount")
                                .setValue(count)
                    }

                    val allcount : Long = it.child("allCount").value as Long

                    //리더보드 갱신
                    databaseref.child("Leaderboard").child(firebaseUser.uid.toString()).get().addOnSuccessListener { it2 ->
                        databaseref.child("Leaderboard").child(firebaseUser.uid.toString())
                                .child("useraccount").child("trueCount").setValue(count)

                        val rate: Double = round(count / (allcount.toDouble()) * 100.0)
                        Log.e("INC TRUE - LEADER BOARD", "${count}  ${allcount}  $rate")
                        databaseref.child("Leaderboard").child(firebaseUser.uid.toString()).child("rate").setValue(rate)
                    }
                }
    }
}