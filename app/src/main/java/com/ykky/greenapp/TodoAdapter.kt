package com.ykky.greenapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.ykky.greenapp.databinding.RowBinding


class TodoAdapter(options: FirebaseRecyclerOptions<TodoData>)
    : FirebaseRecyclerAdapter<TodoData, TodoAdapter.ViewHolder>(options){

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
        holder.binding.apply {
            checkBtn.isSelected = model.isChecked
            todo.text = model.todo.toString()

            checkBtn.setOnClickListener {
                if(model.isChecked){
                    model.isChecked = false
                    checkBtn.isSelected = false
                    Toast.makeText(it.context, "취소", Toast.LENGTH_SHORT).show()
                }else{
                    model.isChecked = true
                    checkBtn.isSelected = true
                    Toast.makeText(it.context, "완료", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}