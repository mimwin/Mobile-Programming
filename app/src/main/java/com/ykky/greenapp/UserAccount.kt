package com.ykky.greenapp

data class UserAccount (var emailId : String, var password : String, var RegisterDate : String, var nickname : String,var todo : ArrayList<TodoData>){
    //token : Firebase 고유 토큰 (식별자)
}