package br.com.petsus.local.api.service.clinic

import br.com.petsus.api.model.address.AppAddress
import br.com.petsus.api.model.address.City
import br.com.petsus.api.model.address.State
import br.com.petsus.api.model.animal.Specie
import br.com.petsus.api.model.clinic.Clinic
import br.com.petsus.api.model.clinic.ClinicAddress
import br.com.petsus.api.model.clinic.Exam
import br.com.petsus.api.service.clinic.ClinicRepository
import br.com.petsus.local.util.delayDefault
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class ClinicRepositoryImpl : ClinicRepository {

    private var countExams = Random.nextInt(3, 50)
    private var countSpecies = Random.nextInt(3, 50)
    private fun generateExam(): Exam {
        val id = Random.nextLong(Long.MAX_VALUE)
        return Exam(
            id = id,
            name = "Exam $id"
        )
    }

    private fun generateSpecies(): Specie {
        val id = Random.nextLong(Long.MAX_VALUE)
        return Specie(
            id = id,
            name = "Exam $id"
        )
    }

    override fun find(id: Long): Flow<Clinic> {
        return flow {
            delayDefault()
            emit(
                Clinic(
                    id = id,
                    name = "Parque Zoológico Municipal \"Quinzinho de Barros\"",
                    site = "https://google.com.br/",
                    phone = "15987654321",
                    appAddress = AppAddress(
                        id = 1,
                        address = "R. Teodoro Kaisel",
                        number = 883,
                        complement = null,
                        neighborhood = "Vila Hortência",
                        lat = -23.5053214,
                        lng = -47.4373164,
                        City(
                            id = 1,
                            name = "Sorocaba",
                            ibgeId = 1,
                            state = State(
                                id = 1,
                                name = "São Paulo",
                                initials = "SP",
                                ibgeId = 1
                            )
                        ),
                        postalCode = "18020-268",
                        nickname = null,
                    ),
                    image = "https://lh3.googleusercontent.com/p/AF1QipOYkKLyoGz2aosGJJa6Af1YeHwQY-se-27DAJCc=s680-w680-h510",
                    exams = generateSequence { if (--countExams > 0) generateExam() else null }.toList(),
                    species = generateSequence { if (--countSpecies > 0) generateSpecies() else null }.toList()
                )
            )
        }
    }

    override fun list(lat: Double, lng: Double, distance: Double): Flow<List<ClinicAddress>> {
        return flow {
            emit(
                listOf(
                    ClinicAddress(
                        id = 1,
                        name = "Parque Zoológico Municipal \"Quinzinho de Barros\"",
                        lat = -23.5053214,
                        lng = -47.4373164,
                        distance = 50.0
                    )
                )
            )
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class ClinicRepositoryModule {
    @Provides
    fun clinic(): ClinicRepository = ClinicRepositoryImpl()
}