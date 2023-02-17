package br.com.petsus.local.util

import kotlinx.coroutines.delay

const val delayDefaultSimulation: Long = 2000
suspend fun delayDefault(alter: Long? = null) = delay(alter ?: delayDefaultSimulation)