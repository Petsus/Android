package br.com.petsus.api.service

import br.com.petsus.api.manager.RequestAPI

class APIRepositoryImpl : APIRepository {
    override fun auth(): AuthRepository = RequestAPI.api.create(AuthRepository::class.java)
    override fun user(): UserRepository = RequestAPI.api.create(UserRepository::class.java)
    override fun clinic(): ClinicRepository = RequestAPI.api.create(ClinicRepository::class.java)
}