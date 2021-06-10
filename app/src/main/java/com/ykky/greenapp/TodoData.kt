package com.ykky.greenapp

import java.io.Serializable

data class TodoData(var date : String, var todo:String, var memo:String, var isChecked : Boolean) : Serializable {
    constructor():this("2021-01-01", "noinfo","noinfo",false)
}