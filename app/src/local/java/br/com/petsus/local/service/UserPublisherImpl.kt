package br.com.petsus.local.service

import br.com.petsus.api.model.user.User
import br.com.petsus.service.publisher.UserPublisher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPublisherImpl @Inject constructor() : UserPublisher {
    private val stateFlow: MutableStateFlow<User> = MutableStateFlow(PLACEHOLDER)
    override val current: Flow<User>
        get() = stateFlow

    override suspend fun update(newUser: User) =
        stateFlow.emit(newUser)

    companion object {
        private val PLACEHOLDER = User(
            id = 1,
            name = "User name test",
            email = "email@mail.com.br",
            enable = true,
            createdAt = "2023-01-01 00:00:00",
            updatedAt = "2023-01-01 00:00:01",
            phone = "15987654321",
            emailVerified = "2023-01-01 00:00:02",
            phoneVerified = "2023-01-01 00:00:03"
        )
    }
}