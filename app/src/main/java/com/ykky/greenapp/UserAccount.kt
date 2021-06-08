package com.ykky.greenapp

data class UserAccount (var emailId : String, var password : String, var RegisterDate : String, var token : String , var nickname : String,var todo : ArrayList<TodoData>, var AllCount : Double, var TrueCount : Double){
    //token : Firebase 고유 토큰 (식별자)
}