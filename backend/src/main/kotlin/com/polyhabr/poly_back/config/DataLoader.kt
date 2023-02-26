package com.polyhabr.poly_back.config

import com.polyhabr.poly_back.entity.*
import com.polyhabr.poly_back.entity.auth.Role
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.repository.*
import com.polyhabr.poly_back.repository.auth.RoleRepository
import com.polyhabr.poly_back.repository.auth.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDate


@Component
class DataLoader : ApplicationRunner {

    @Autowired
    lateinit var encoder: PasswordEncoder

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var userRepository: UsersRepository

    @Autowired
    lateinit var tagTypeRepository: TagTypeRepository

    @Autowired
    lateinit var disciplineTypeRepository: DisciplineTypeRepository

    @Autowired
    lateinit var articleTypeRepository: ArticleTypeRepository

    @Autowired
    lateinit var articleRepository: ArticleRepository

    @Autowired
    lateinit var articleToTagTypeRepository: ArticleToTagTypeRepository

    @Autowired
    lateinit var articleToDisciplineTypeRepository: ArticleToDisciplineTypeRepository

    override fun run(args: ApplicationArguments) {
        setup()
    }

    private fun setup() {
        val defaultRole = roleRepository.save(Role("ROLE_USER"))
        val adminRole = roleRepository.save(Role("ROLE_ADMIN"))

        val admin = userRepository.save(
            User(
                login = "admin",
                password = encoder.encode("admincool"),
                name = "dmitry",
                surname = "shabinsky",
                email = "admin@notfake.com",
                enabled = true,
                roles = listOf(adminRole)
            )
        )
        val user = userRepository.save(
            User(
                login = "user",
                password = encoder.encode("userbroke"),
                name = "vasya",
                surname = "pupkin",
                email = "userbroke@notfake.com",
                enabled = true,
                roles = listOf(defaultRole)
            )
        )

        val tag1 = tagTypeRepository.save(TagType("Квантовая запутанность"))
        val tag2 = tagTypeRepository.save(TagType("tag2"))
        val tag3 = tagTypeRepository.save(TagType("tag3"))

        val discipline1 = disciplineTypeRepository.save(DisciplineType("Физика"))
        val discipline2 = disciplineTypeRepository.save(DisciplineType("Математика"))
        val discipline3 = disciplineTypeRepository.save(DisciplineType("Программировани"))

        val articleType1 = articleTypeRepository.save(ArticleType("Статья"))
        val articleType2 = articleTypeRepository.save(ArticleType("Конспект"))
        val articleType3 = articleTypeRepository.save(ArticleType("Расчётное задание"))

        val article1 = articleRepository.save(
            Article(
                title = "Необяснимо, но вполне логично! Что скрывает в себе квантовая механика?",
                previewText = "Квантовая мехависимым. В этом мирем и взаимозависимым. В этом мия взаимосв",
                filePdf = "filePdf 1",
                likes = 0,
                typeId = articleType1,
                userId = admin,
                date = LocalDate.now()
            )
        )
        articleToTagTypeRepository.save(ArticleToTagType(article1, tag1))
        articleToDisciplineTypeRepository.save(ArticleToDisciplineType(article1, discipline1))
    }
}