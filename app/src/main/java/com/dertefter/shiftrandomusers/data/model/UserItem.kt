package com.dertefter.shiftrandomusers.data.model

data class UserItem(
    val user: User,
    var isExpanded: Boolean = false
)