package com.example.instaclon.models

import java.io.Serializable

data class Post(
    var email: String? = "",
    var nickName: String? = "",
    var profileUrl: String? = "",
    var imageUrl: String = "",
    var description: String = ""
) : Serializable {
    var uid: String = ""
}