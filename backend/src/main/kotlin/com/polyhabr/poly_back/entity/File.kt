package com.polyhabr.poly_back.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@JsonIgnoreProperties("hibernateLazyInitializer")
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

    @Column(name = "created_at")
    open var createdAt: Long? = DateTime.now().millis
)
