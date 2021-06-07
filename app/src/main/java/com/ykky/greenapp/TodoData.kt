package com.ykky.greenapp

data class TodoData(var date : String, var todo:String, var memo:String, var isChecked : Boolean) {
    constructor():this("2021-01-01", "noinfo","noinfo",false)
}