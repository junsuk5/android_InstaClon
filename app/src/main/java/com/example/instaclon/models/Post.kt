package com.example.instaclon.models

import java.io.Serializable

data class Post(
    var email: String = "",
    var imageUrl: String = ""
) : Serializable