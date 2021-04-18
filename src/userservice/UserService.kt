package com.grayhatdevelopers.jwtsample.userservice

import com.grayhatdevelopers.jwtsample.User
import io.ktor.auth.*


interface UserService {

    fun findUserById(id: String): User

    fun findUserByCredentials(credential: UserPasswordCredential): User

}
