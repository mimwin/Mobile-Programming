package com.ykky.greenapp

data class UserAccount (var emailId : String, var password : String, var token : String, var nickname : String){
    //token : Firebase 고유 토큰 (식별자)
}