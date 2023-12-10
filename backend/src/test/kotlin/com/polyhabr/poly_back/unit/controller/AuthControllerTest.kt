//package com.polyhabr.poly_back.unit.controller
//
//import com.polyhabr.poly_back.controller.auth.AuthController
//import com.polyhabr.poly_back.controller.auth.LoginUser
//import com.polyhabr.poly_back.controller.auth.NewUser
//import com.polyhabr.poly_back.controller.auth.PasswordChange
//import com.polyhabr.poly_back.entity.*
//import com.polyhabr.poly_back.entity.auth.Role
//import com.polyhabr.poly_back.entity.auth.User
//import com.polyhabr.poly_back.entity.auth.toDtoWithoutPasswordAndEmail
//import com.polyhabr.poly_back.exception.UserAlreadyExistException
//import com.polyhabr.poly_back.jwt.JwtProvider
//import com.polyhabr.poly_back.repository.auth.RoleRepository
//import com.polyhabr.poly_back.repository.auth.UsersRepository
//import com.polyhabr.poly_back.service.UsersService
//import com.polyhabr.poly_back.utility.Utility
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.mockito.InjectMocks
//import org.mockito.Mock
//import org.mockito.Mockito.`when`
//import org.mockito.junit.jupiter.MockitoExtension
//import org.mockito.kotlin.any
//import org.mockito.kotlin.doNothing
//import org.mockito.kotlin.eq
//import org.mockito.kotlin.given
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.mail.javamail.JavaMailSender
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.crypto.password.PasswordEncoder
//
//@ExtendWith(MockitoExtension::class)
//class AuthControllerTest {
//    private companion object {
//        val defaultRole = Role("ROLE_ADMIN")
//
//        val defaultUser1 = User(
//            login = "admin",
//            password = "admin",
//            name = "dmitry",
//            surname = "shabinsky",
//            email = "admin@notfake.com",
//            enabled = true,
//            roles = listOf(defaultRole),
//        ).apply {
//            id = 1L
//        }
//
//        val defaultTagType = TagType(
//            name = "tag",
//        ).apply {
//            id = 1L
//        }
//
//        val defaultDisciplineType = DisciplineType(
//            name = "discipline",
//        ).apply {
//            id = 1L
//        }
//
//        val defaultArticleType = ArticleType(
//            name = "type",
//        ).apply {
//            id = 1L
//        }
//
//        val myDate = 123123123123L
//
//        val defaultArticle = Article(
//            title = "title",
//            text = "content",
//            previewText = "preview",
//            date = myDate,
//            likes = 0,
//            isFav = false,
//            view = 0,
//            typeId = defaultArticleType,
//            userId = defaultUser1,
//        ).apply {
//            id = 1L
//        }
//
//        val articles = listOf(
//            defaultArticle,
//        )
//        val disciplineTypes = listOf(
//            defaultDisciplineType,
//        ).map { it.name.toString() }
//
//        val tagTypes = listOf(
//            defaultTagType,
//        ).map { it.name.toString() }
//
//        val articleToDisciplineTypes = listOf(
//            ArticleToDisciplineType(
//                article = defaultArticle,
//                disciplineType = defaultDisciplineType
//            ),
//        )
//
//        val articleToTagTypes = listOf(
//            ArticleToTagType(
//                article = defaultArticle,
//                tagType = defaultTagType
//            ),
//        )
//    }
//
//    @Mock
//    private lateinit var usersService: UsersService
//
//    @Mock
//    private lateinit var authenticationManager: AuthenticationManager
//
//    @Mock
//    private lateinit var usersRepository: UsersRepository
//
//    @Mock
//    private lateinit var roleRepository: RoleRepository
//
//    @Mock
//    private lateinit var passwordEncoder: PasswordEncoder
//
//    @Mock
//    private lateinit var jwtProvider: JwtProvider
//
//    @Mock
//    private lateinit var javaMailSender: JavaMailSender
//
//    @InjectMocks
//    private lateinit var authController: AuthController
//
//    @Test
//    fun `test send verification email`() {
//        val user = defaultUser1
//        val userDto = user.toDtoWithoutPasswordAndEmail()
//
//        val newUser = NewUser(
//            "newUser", "firstName", "lastName", "dgrjryd@gmail.com", "somepassword"
//        )
//        val newUser2 = NewUser()
//        `when`(passwordEncoder.encode("somepassword")).thenReturn("somepassword")
//        `when`(usersRepository.save(any())).thenReturn(defaultUser1)
//        doNothing().`when`(usersService).sendVerificationEmail(any(), eq("http://localhost:4200"))
//
//        val resultString = "somemessage"
//        val expectedResponse = ResponseEntity(null, HttpStatus.OK).statusCode
//
//        val actualResponse = authController.registerUser(newUser).statusCode
//
//        Assertions.assertEquals(expectedResponse, actualResponse)
//    }
//
//    @Test
//    fun `test verify user`() {
//        given(usersService.verify("someCode")).willReturn(true)
//
//        val expectedResponse = ResponseEntity(null, HttpStatus.OK)
//        val actualResponse = authController.verifyUser("someCode")
//
//        Assertions.assertEquals(expectedResponse, actualResponse)
//    }
//
//    @Test
//    fun `test reset password`() {
//        doNothing().`when`(usersService).sendResetPasswordEmail("someMail@mail.com")
//
//        val expectedResponse = ResponseEntity(null, HttpStatus.OK)
//        val actualResponse = authController.resetPassword("someMail@mail.com")
//
//        Assertions.assertEquals(expectedResponse, actualResponse)
//    }
//
//    @Test
//    fun `test verify no change password`() {
//        val expectedResponse = ResponseEntity(null, HttpStatus.BAD_REQUEST)
//        val actualResponse = authController.verifyChangePasswordWithToken("adminnnn")
//
//        Assertions.assertEquals(expectedResponse, actualResponse)
//    }
//
//    @Test
//    fun `test check free login`() {
//        val expectedResponse = ResponseEntity(null, HttpStatus.OK)
//        val actualResponse = authController.checkFreeLogin("someMail@mail.com")
//
//        Assertions.assertEquals(expectedResponse, actualResponse)
//    }
//
//    @Test
//    fun `test savePassword success`() {
//        val passwordChange = PasswordChange("token", "newPassword")
//        given(usersService.changeUserPassword(any())).willReturn(true)
//
//        val expected = ResponseEntity(null, HttpStatus.OK)
//        val actual = authController.savePassword(passwordChange)
//
//        Assertions.assertEquals(expected, actual)
//
//    }
//
//    @Test
//    fun `test savePassword faild`() {
//        val passwordChange = PasswordChange("token", "newPassword")
//        given(usersService.changeUserPassword(any())).willReturn(false)
//
//        val expected = ResponseEntity(null, HttpStatus.BAD_REQUEST)
//        val actual = authController.savePassword(passwordChange)
//
//        Assertions.assertEquals(expected, actual)
//
//    }
//
//    @Test
//    fun `test check free email`() {
//        val expectedResponse = ResponseEntity(null, HttpStatus.OK)
//        val actualResponse = authController.checkFreeEmail("someMail@mail.com")
//
//        Assertions.assertEquals(expectedResponse, actualResponse)
//    }
//
//    @Test
//    fun `test manual login`() {
//        val expected = defaultUser1
//        given(usersRepository.findByLogin(any())).willReturn(expected)
//
//        val expectedResponse = ResponseEntity(defaultUser1.login, HttpStatus.OK).body
//
//        val loginUser = LoginUser("admin", "admin")
//        val actualResponse = authController.manualLogin(loginUser).username
//
//        Assertions.assertEquals(expectedResponse, actualResponse)
//    }
//
//    @Test
//    fun `test authenticate user`() {
//        val expected = defaultUser1
//        given(usersRepository.findByLogin(any())).willReturn(expected)
//
//        val expectedResponse = ResponseEntity(true, HttpStatus.OK).statusCode
//
//        val loginUser = LoginUser("admin", "admin")
//        val loginUser2 = LoginUser()
//        val actualResponse = authController.authenticateUser(loginUser).statusCode
//
//        Assertions.assertEquals(expectedResponse, actualResponse)
//    }
//
//
//    @Test
//    fun `test verify change password`() {
//        val expectedResponse = ResponseEntity(null, HttpStatus.BAD_REQUEST)
//        val actualResponse = authController.verifyChangePasswordWithToken("aaaaaa")
//
//        Assertions.assertEquals(expectedResponse, actualResponse)
//    }
//
//    @Test
//    fun `test exception`() {
//        val userAlreadyExistException = UserAlreadyExistException()
//        val userAlreadyExistException2 = UserAlreadyExistException("message", cause = Throwable())
//        val userAlreadyExistException3 = UserAlreadyExistException(cause = Throwable())
//        val userAlreadyExistException4 = UserAlreadyExistException("message")
//    }
//
//    @Test
//    fun `test utility`() {
//        val utility = Utility
//        val randomString = utility.getRandomString(10)
//    }
//}