package com.polyhabr.poly_back.entity

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "file")
open class File(
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id")
    open var id: String? = null,

    @field:NotBlank
    @Column(name = "username", nullable = false)
    open var username: String? = null,

    @Column(name = "description")
    open var description: String? = null,

    @field:NotBlank
    @Column(name = "name", nullable = false)
    open var name: String? = null,

    @field:NotBlank
    @Column(name = "type", nullable = false)
    open var type: String? = null,

    @field:JsonIgnore
    @Column(name = "data", nullable = false)
    open var data: ByteArray? = null,

    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter::class)
    @Column(name = "created_at")
    open var createdAt: LocalDateTime? = LocalDateTime.now()
) {

}