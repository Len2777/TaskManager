package com.example.jetpacktraning.domain.model

import java.util.UUID

data class Tasks(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val hours: Int,
    val minutes: Int)