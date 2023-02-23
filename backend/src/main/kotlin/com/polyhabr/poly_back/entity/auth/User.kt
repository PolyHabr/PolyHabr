package com.polyhabr.poly_back.entity.auth

import com.polyhabr.poly_back.entity.BaseEntity
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "users")
open class User(
    @Size(max = 255)
    @Column(name = "login")
    open var login: String,

    @Size(max = 255)
    @Column(name = "password")
    open var password: String,

    @Size(max = 255)
    @Column(name = "name")
    open var name: String,

    @Size(max = 255)
    @Column(name = "surname")
    open var surname: String,

    @Size(max = 255)
    @Column(name = "email")
    open var email: String,

    @Column(name = "enabled")
    var enabled: Boolean = false,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    var roles: Collection<Role>? = null
) : BaseEntity<Long>()
