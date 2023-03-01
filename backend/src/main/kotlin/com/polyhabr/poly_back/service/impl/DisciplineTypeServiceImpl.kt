package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.controller.model.disciplineType.request.DisciplineTypeRequest
import com.polyhabr.poly_back.controller.model.disciplineType.request.UpdateMyDisciplineRequest
import com.polyhabr.poly_back.controller.model.disciplineType.request.toDto
import com.polyhabr.poly_back.controller.utils.currentLogin
import com.polyhabr.poly_back.dto.DisciplineTypeDto
import com.polyhabr.poly_back.entity.DisciplineType
import com.polyhabr.poly_back.entity.MyDiscipline
import com.polyhabr.poly_back.repository.DisciplineTypeRepository
import com.polyhabr.poly_back.repository.MyDisciplineRepository
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.service.DisciplineTypeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class DisciplineTypeServiceImpl(
    private val disciplineTypeRepository: DisciplineTypeRepository,
    private val usersRepository: UsersRepository,
    private val myDisciplineRepository: MyDisciplineRepository
) : DisciplineTypeService {

    @Autowired
    lateinit var transactionTemplate: TransactionTemplate
    override fun getAll(offset: Int, size: Int): Page<DisciplineTypeDto> {
        return disciplineTypeRepository
            .findAll(
                PageRequest.of(
                    offset,
                    size,
                )
            )
            .map { it.toDto() }
    }

    override fun getById(id: Long): DisciplineTypeDto {
        return disciplineTypeRepository.findByIdOrNull(id)
            ?.toDto()
            ?: throw RuntimeException("Discipline type not found")
    }

    override fun searchByName(prefix: String?, offset: Int, size: Int): Page<DisciplineTypeDto> =
        disciplineTypeRepository
            .findDisciplineTypesByName(
                PageRequest.of(
                    offset,
                    size,
                ), prefix ?: ""
            )
            .map { it.toDto() }

    override fun create(disciplineTypeRequest: DisciplineTypeRequest): Long? {
        return disciplineTypeRepository.save(
            disciplineTypeRequest
                .toDto()
                .toEntity()
        ).id
    }

    override fun update(id: Long, disciplineTypeRequest: DisciplineTypeRequest): Pair<Boolean, String> {
        disciplineTypeRepository.findByIdOrNull(id)?.let { currentDisciplineType ->
            currentDisciplineType.apply {
                disciplineTypeRequest.name?.let { name = it }
            }
            return disciplineTypeRepository.save(currentDisciplineType).id?.let { true to "Ok" }
                ?: (false to "Error while update")
        } ?: return false to "Discipline type not found"
    }

    override fun delete(id: Long): Pair<Boolean, String> {
        return try {
            disciplineTypeRepository.findByIdOrNull(id)?.let { currentDisciplineType ->
                currentDisciplineType.id?.let { id ->
                    disciplineTypeRepository.deleteById(id)
                    true to "Discipline type deleted"
                } ?: (false to "Discipline type id not found")
            } ?: (false to "Discipline type not found")
        } catch (e: Exception) {
            false to "Internal server error"
        }
    }

    override fun getMy(): List<DisciplineTypeDto> {
        return usersRepository.findByLogin(currentLogin())?.let { currentUser ->
            val myDiscipline = myDisciplineRepository.findByUserId(id = currentUser.id!!)
            val disciplines = mutableListOf<DisciplineTypeDto>()
            myDiscipline?.forEach {
                disciplineTypeRepository.findByName(it.disciplineId!!.name!!)?.let { myD ->
                    disciplines.add(myD.toDto())
                }
            }
            return disciplines
        } ?: throw Exception("You should be login")
    }

    override fun updateMy(updateResponse: UpdateMyDisciplineRequest): Boolean {
        transactionTemplate.execute {
            usersRepository.findByLogin(currentLogin())?.let { currentUser ->
                myDisciplineRepository.findByUserId(currentUser.id!!)?.forEach {
                    myDisciplineRepository.delete(it)
                }
                updateResponse.namesDiscipline?.forEach {
                    disciplineTypeRepository.findByName(it)?.let { findedDiscipline ->
                        myDisciplineRepository.save(
                            MyDiscipline(
                                userId = currentUser,
                                disciplineId = findedDiscipline
                            )
                        )
                    }
                }
            } ?: throw Exception("You should be login")
        } ?: return false
        return true
    }

    private fun DisciplineType.toDto(): DisciplineTypeDto =
        DisciplineTypeDto(
            id = this.id!!,
            name = this.name!!,
        )

    private fun DisciplineTypeDto.toEntity() = DisciplineType(
        name = this.name,
    )
}