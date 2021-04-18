package com.grayhatdevelopers.jwtsample

import com.grayhatdevelopers.jwtsample.extensions.user
import com.grayhatdevelopers.jwtsample.userservice.UserService
import com.grayhatdevelopers.jwtsample.userservice.UserServiceImpl
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.features.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.gson.*
import io.ktor.request.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {

    val userService: UserService = UserServiceImpl()
    install(Authentication) {
        /**
         * Setup the JWT authentication to be used in [Routing].
         * If the token is valid, the corresponding [User] is fetched from the database.
         * The [User] can then be accessed in each [ApplicationCall].
         */
        jwt {
            verifier(JwtConfig.verifier)
            realm = "sample-jwt"
            validate {
                it.payload.getClaim("id")?.asString()?.let(userService::findUserById)
            }
        }
    }
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    routing {

        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized, cause)
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden, cause)
            }
        }

        /**
         * A public login [Route] used to obtain JWTs
         */
        post("login") {
            val credentials = call.receive<UserPasswordCredential>()
            val user = userService.findUserByCredentials(credentials)
            val token = JwtConfig.makeToken(user)
            call.respondText(token)
        }

        /**
         * All [Route]s in the authentication block are secured.
         */
        authenticate {
            route("secret") {

                get {
                    val user = call.user!!
                    call.respond(user.displayName)
                }

                put {
                    TODO("All your secret routes can follow here")
                }

            }
        }

        /**
         * Routes with optional authentication
         */
        authenticate(optional = true) {
            get("optional") {
                val user = call.user
                val response = if (user != null) "authenticated!" else "optional"
                call.respond(response)
            }
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

