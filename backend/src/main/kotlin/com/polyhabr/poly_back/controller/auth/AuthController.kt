package com.polyhabr.poly_back.controller.auth

import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.jwt.JwtProvider
import com.polyhabr.poly_back.repository.auth.RoleRepository
import com.polyhabr.poly_back.repository.auth.UsersRepository
import javax.validation.Valid
import java.util.*
import java.util.stream.Collectors

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@Validated
@RequestMapping("/api/auth")
class AuthController {

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var userRepository: UsersRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var encoder: PasswordEncoder

    @Autowired
    lateinit var jwtProvider: JwtProvider


    @PostMapping("/signin")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginUser): ResponseEntity<*> {

        userRepository.findByLogin(loginRequest.username!!)?.let { user ->
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
            )
            SecurityContextHolder.getContext().authentication = authentication

            val jwt: String = jwtProvider.generateJwtToken(user.login)
            val authorities: List<GrantedAuthority> =
                user.roles!!.stream()
                    .map { role -> SimpleGrantedAuthority(role.name) }
                    .collect(Collectors.toList<GrantedAuthority>())

            return ResponseEntity.ok(JwtResponse(jwt, user.login, authorities))
        } ?: return ResponseEntity(
            ResponseMessage("User not found!"),
            HttpStatus.BAD_REQUEST
        )
    }

    @PostMapping("/signup")
    fun registerUser(@Valid @RequestBody newUser: NewUser): ResponseEntity<*> {

        userRepository.findByLogin(newUser.username!!)?.let {
            return ResponseEntity(
                ResponseMessage("User already exists!"),
                HttpStatus.BAD_REQUEST
            )
        } ?: run {
            if (usernameExists(newUser.username!!)) {
                return ResponseEntity(
                    ResponseMessage("Username is already taken!"),
                    HttpStatus.BAD_REQUEST
                )
            } else if (emailExists(newUser.email!!)) {
                return ResponseEntity(
                    ResponseMessage("Email is already in use!"),
                    HttpStatus.BAD_REQUEST
                )
            }

            // Creating user's account
            val user = User(
                login = newUser.username!!,
                name = newUser.firstName!!,
                surname = newUser.lastName!!,
                email = newUser.email!!,
                password = encoder.encode(newUser.password),
                enabled = true
            )
            user.roles = listOf(roleRepository.findByName("ROLE_USER"))

            userRepository.save(user)

            return ResponseEntity(ResponseMessage("User registered successfully!"), HttpStatus.OK)
        }
    }

    private fun emailExists(email: String): Boolean {
        return userRepository.findByLogin(email) != null
    }

    private fun usernameExists(username: String): Boolean {
        return userRepository.findByLogin(username) != null
    }
}