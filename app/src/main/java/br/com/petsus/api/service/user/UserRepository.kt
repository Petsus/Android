package br.com.petsus.api.service.user

import android.net.Uri
import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.ChangePassword
import br.com.petsus.api.model.auth.ResetPassword
import br.com.petsus.api.model.base.EmptyResponse
import br.com.petsus.api.model.user.CreateUser
import br.com.petsus.api.model.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun resetPassword(body: ResetPassword): Flow<EmptyResponse>
    fun changePassword(body: ChangePassword): Flow<EmptyResponse>
    fun createUser(body: CreateUser): Flow<AuthToken>
    fun getUser(): Flow<User>
    fun update(body: User): Flow<EmptyResponse>
    fun name(): Flow<String>
    fun saveImage(uri: Uri): Flow<Boolean>
    fun currentImage(): Flow<String?>
}