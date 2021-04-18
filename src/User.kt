package com.grayhatdevelopers.jwtsample

import io.ktor.auth.*

data class User(
    val id: String,
    val displayName: String
) : Principal