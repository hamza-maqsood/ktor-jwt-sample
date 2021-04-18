package com.grayhatdevelopers.jwtsample.userservice

import com.grayhatdevelopers.jwtsample.User
import com.grayhatdevelopers.jwtsample.extensions.testUser
import io.ktor.auth.*

class UserServiceImpl : UserService {

    override fun findUserById(id: String): User = users.getValue(id)

    override fun findUserByCredentials(credential: UserPasswordCredential): User = testUser

    private val users = listOf(testUser).associateBy(User::id)

}