package br.com.petsus.service.publisher

import br.com.petsus.api.model.user.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface UserPublisher {
    val current: Flow<User>
    suspend fun update(newUser: User)
}