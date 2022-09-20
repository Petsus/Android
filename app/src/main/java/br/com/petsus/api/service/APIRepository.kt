package br.com.petsus.api.service

interface APIRepository {
    fun auth(): AuthRepository
    fun user(): UserRepository
    fun clinic(): ClinicRepository
}