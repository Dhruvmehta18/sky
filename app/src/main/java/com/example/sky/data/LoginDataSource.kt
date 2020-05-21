package com.example.sky.data

import com.example.sky.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun signUp(fullName: String, email: String, password: String): Result<LoggedInUser> {
        return try {
            //TODO: handle signUpUser authentication
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), fullName, email)
            Result.Success(fakeUser)
        } catch (e: Throwable) {
            Result.Error(IOException("Error Register", e))
        }
    }

    fun login(email: String, password: String): Result<LoggedInUser> {
        return try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe", email)
            Result.Success(fakeUser)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

