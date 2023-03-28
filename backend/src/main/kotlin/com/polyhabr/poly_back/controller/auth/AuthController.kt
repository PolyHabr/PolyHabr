package com.polyhabr.poly_back.controller.auth

import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.jwt.JwtProvider
import com.polyhabr.poly_back.repository.auth.RoleRepository
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.service.UsersService
import net.bytebuddy.utility.RandomString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.stream.Collectors
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size


@CrossOrigin(origins = ["*"], maxAge = 14400)
@RestController
@Validated
@RequestMapping("/api/auth")
class AuthController {

    var authenticationManager: AuthenticationManager

    var userRepository: UsersRepository

    var usersService: UsersService

    var roleRepository: RoleRepository

    var encoder: PasswordEncoder

    var jwtProvider: JwtProvider

    var mailSender: JavaMailSender

    constructor(
        authenticationManager: AuthenticationManager,
        userRepository: UsersRepository,
        usersService: UsersService,
        roleRepository: RoleRepository,
        encoder: PasswordEncoder,
        jwtProvider: JwtProvider,
        mailSender: JavaMailSender
    ) {
        this.authenticationManager = authenticationManager
        this.userRepository = userRepository
        this.usersService = usersService
        this.roleRepository = roleRepository
        this.encoder = encoder
        this.jwtProvider = jwtProvider
        this.mailSender = mailSender
    }

    @PostMapping("/signin")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginUser): ResponseEntity<*> {
        userRepository.findByLogin(loginRequest.username!!)?.let { user ->
            if (!user.enabled) {
                throw Exception("Confirm your email")
            }
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
            )
            SecurityContextHolder.getContext().authentication = authentication

            val jwt: String = jwtProvider.generateJwtToken(user.login)
            val authorities: List<GrantedAuthority> =
                user.roles!!.stream()
                    .map { role -> SimpleGrantedAuthority(role.name) }
                    .collect(Collectors.toList<GrantedAuthority>())

            val isFirst = user.isFirst
            userRepository.save(
                user.apply {
                    this.isFirst = false
                }
            )

            val response = JwtResponse(jwt, user.login, authorities, isFirst)
            return ResponseEntity.ok(response)
        } ?: return ResponseEntity(
            ResponseMessage("User not found!"),
            HttpStatus.BAD_REQUEST
        )
    }

    fun manualLogin(loginRequest: LoginUser): JwtResponse {
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

            val isFirst = user.isFirst
            userRepository.save(
                user.apply {
                    this.isFirst = false
                }
            )

            return JwtResponse(jwt, user.login, authorities, isFirst)
        } ?: throw Exception("User not found")
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
                enabled = false,
                verificationCode = RandomString.make(64)
            )
            user.roles = listOf(roleRepository.findByName("ROLE_USER"))

            val savedUser = userRepository.save(user)

            usersService.sendVerificationEmail(savedUser)

            return ResponseEntity(ResponseMessage("User registered successfully!"), HttpStatus.OK)
        }
    }

    @GetMapping("/verify")
    fun verifyUser(@Param("code") code: String): ResponseEntity<String> {
        return if (usersService.verify(code)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/forgotPassword")
    fun resetPassword(
        @RequestParam("email") email: String,
    ): ResponseEntity<Unit> {
        usersService.sendResetPasswordEmail(email)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/changePassword")
    fun verifyChangePassword(@Param("token") token: String): ResponseEntity<String> {
        return if (usersService.validatePasswordResetToken(token)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/checkFreeLogin")
    fun checkFreeLogin(
        @NotBlank
        @NotNull
        @Size(min = 3, max = 20)
        @Pattern(regexp = "^[A-Za-z0-9]+\$")
        @Param("login") login: String
    ): ResponseEntity<Unit> {
        return if (!usernameExists(login)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/checkFreeEmail")
    fun checkFreeEmail(
        @NotBlank
        @NotNull
        @Size(min = 3, max = 50)
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)\$")
        @Param("email") email: String
    ): ResponseEntity<Unit> {
        return if (!emailExists(email)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/savePassword")
    fun verifyChangePassword(@Valid @RequestBody passwordChange: PasswordChange): ResponseEntity<String> {
        return if (usersService.changeUserPassword(passwordChange)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    private fun emailExists(email: String): Boolean {
        return userRepository.findByLogin(email) != null
    }

    private fun usernameExists(username: String): Boolean {
        return userRepository.findByLogin(username) != null
    }
}