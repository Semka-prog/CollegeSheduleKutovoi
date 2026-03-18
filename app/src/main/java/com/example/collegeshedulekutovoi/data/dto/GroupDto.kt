package com.example.collegeshedulekutovoi.data.dto

import com.google.gson.annotations.SerializedName

data class GroupDto(
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName(value = "name", alternate = ["groupName"])
    val name: String = ""
)
