package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.controller.model.user.request.UserRequest
import com.polyhabr.poly_back.controller.model.user.request.UserUpdateRequest
import com.polyhabr.poly_back.controller.model.user.request.toDto
import com.polyhabr.poly_back.controller.utils.currentLogin
import com.polyhabr.poly_back.dto.UserDto
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.service.UsersService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.io.UnsupportedEncodingException
import javax.mail.MessagingException


@Service
class UsersServiceImpl(
    private val usersRepository: UsersRepository
) : UsersService {

    @Autowired
    lateinit var encoder: PasswordEncoder

    @Autowired
    lateinit var mailSender: JavaMailSender
    override fun getAll(
        offset: Int,
        size: Int,
    ): Page<UserDto> {
        return usersRepository
            .findAll(
                PageRequest.of(
                    offset,
                    size,
                )
            )
            .map { it.toDto() }
    }

    override fun getById(id: Long): UserDto? {
        return usersRepository.findByIdOrNull(id)
            ?.toDto()
    }

    override fun searchByName(prefix: String?, offset: Int, size: Int): Page<UserDto> =
        usersRepository
            .findUsersByName(
                PageRequest.of(
                    offset,
                    size,
                ), prefix ?: ""
            )
            .map { it.toDtoWithoutPasswordAndEmail() }

    override fun create(userRequest: UserRequest): Long? {
        return usersRepository.save(
            userRequest
                .toDto()
                .toEntity()
        ).id
    }

    override fun update(userRequest: UserUpdateRequest): Pair<Boolean, String> {
        usersRepository.findByLogin(currentLogin())?.let { currentUser ->
            currentUser.apply {
                userRequest.email?.let { email = it }
                userRequest.name?.let { name = it }
                userRequest.surname?.let { surname = it }
                userRequest.password?.let { password = encoder.encode(it) }
            }
            return usersRepository.save(currentUser).id?.let { true to "Ok" } ?: (false to "Error while update")
        } ?: return false to "User not found"
    }

    override fun delete(): Pair<Boolean, String> {
        return try {
            usersRepository.findByLogin(currentLogin())?.let { currentUser ->
                currentUser.id?.let { id ->
                    usersRepository.deleteById(id)
                    true to "User deleted"
                } ?: (false to "User id not found")
            } ?: (false to "User not found")
        } catch (e: Exception) {
            false to "Internal server error"
        }
    }

    @Throws(MessagingException::class, UnsupportedEncodingException::class)
    override fun sendVerificationEmail(user: User, siteURL: String) {
        val toAddress: String = user.email
        val fromAddress = "polyhabr@mail.ru"
        val senderName = "PolyHabr"
        val subject = "Please verify your registration"
        var content = ("Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.")
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message)
        helper.setFrom(fromAddress, senderName)
        helper.setTo(toAddress)
        helper.setSubject(subject)
        content = content.replace("[[name]]", user.name)
        val verifyURL = siteURL + "/api/auth/verify?code=" + user.verificationCode
        content = content.replace("[[URL]]", verifyURL)
        helper.setText(content, true)
        mailSender.send(message)
    }

    override fun verify(verificationCode: String): Boolean {
        usersRepository.findByVerificationCode(verificationCode)
            ?.takeIf { !it.enabled }
            ?.let {
                it.verificationCode = null
                it.enabled = true
                usersRepository.save(it)
                return true
            } ?: return false
    }

    private fun User.toDto() = UserDto(
        id = this.id,
        email = this.email,
        login = this.login,
        name = this.name,
        password = this.password,
        surname = this.surname,
    )

    private fun User.toDtoWithoutPasswordAndEmail() = UserDto(
        id = this.id,
        login = this.login,
        name = this.name,
        surname = this.surname,
    )

    private fun UserDto.toEntity() = User(
        email = this.email!!,
        login = this.login!!,
        name = this.name!!,
        password = this.password!!,
        surname = this.surname!!,
    )
}