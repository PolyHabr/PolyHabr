package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.controller.model.disciplineType.request.DisciplineTypeRequest
import com.polyhabr.poly_back.controller.model.disciplineType.request.toDto
import com.polyhabr.poly_back.dto.DisciplineTypeDto
import com.polyhabr.poly_back.entity.DisciplineType
import com.polyhabr.poly_back.repository.DisciplineTypeRepository
import com.polyhabr.poly_back.service.DisciplineTypeService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class DisciplineTypeServiceImpl(
    private val disciplineTypeRepository: DisciplineTypeRepository
) : DisciplineTypeService {
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
            return disciplineTypeRepository.save(currentDisciplineType).id?.let { true to "Ok" } ?: (false to "Error while update")
        }?: return false to "Discipline type not found"

//        val existingDisciplineType = disciplineTypeRepository.findByIdOrNull(id)
//            ?: throw RuntimeException("Discipline type not found")
//        existingDisciplineType.name = disciplineTypeRequest.name ?: throw RuntimeException("name not found")
//
//        return disciplineTypeRepository.save(existingDisciplineType).id?.let { true } ?: false
    }

    override fun delete(id: Long): Pair<Boolean, String> {
        return try {
            disciplineTypeRepository.findByIdOrNull(id)?.let { currentDisciplineType ->
                currentDisciplineType.id?.let { id ->
                    disciplineTypeRepository.deleteById(id)
                    true to "Discipline type deleted"
                }?: (false to "Discipline type id not found")
            }?: (false to "Discipline type not found")
        }
        catch (e:Exception){
            false to "Internal server error"
        }


//        val existingDisciplineType = disciplineTypeRepository.findByIdOrNull(id)
//            ?: throw RuntimeException("Discipline type not found")
//        val existedId = existingDisciplineType.id ?: throw RuntimeException("id not found")
//        disciplineTypeRepository.deleteById(existedId)
    }

    private fun DisciplineType.toDto(): DisciplineTypeDto =
        DisciplineTypeDto(
            id = this.id,
            name = this.name,
        )

    private fun DisciplineTypeDto.toEntity() = DisciplineType(
        name = this.name!!,
    )
}