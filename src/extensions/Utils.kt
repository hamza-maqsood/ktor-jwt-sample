package com.grayhatdevelopers.jwtsample.extensions

import com.grayhatdevelopers.jwtsample.User
import io.ktor.application.*
import io.ktor.auth.*

val ApplicationCall.user get() = authentication.principal<User>()

val testUser = User("1", "Test User")