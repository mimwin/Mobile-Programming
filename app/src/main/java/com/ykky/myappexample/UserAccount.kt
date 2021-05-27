package com.ykky.myappexample

data class UserAccount (var emailId : String, var password : String, var token : String){
    //token : Firebase 고유 토큰 (식별자)
}