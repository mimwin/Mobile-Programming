package com.ykky.greenapp

data class UserAccount (var emailId : String, var password : String, var nickname : String,var todo : ArrayList<TodoData>, var token : String){
    //token : Firebase 고유 토큰 (식별자)
}