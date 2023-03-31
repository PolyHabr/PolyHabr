package com.polyhabr.poly_back.config

import com.polyhabr.poly_back.entity.*
import com.polyhabr.poly_back.entity.auth.Role
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.repository.*
import com.polyhabr.poly_back.repository.auth.RoleRepository
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.utility.Utility
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.sql.Timestamp
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

    private val sizeGenerateId = 5
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
                login = "uservasya",
                password = encoder.encode("userbroke"),
                name = "vasya",
                surname = "pupkin",
                email = "userbroke@notfake.com",
                enabled = true,
                roles = listOf(defaultRole)
            )
        )

        val user1 = userRepository.save(
            User(
                login = "uservanya",
                password = encoder.encode("a12345678"),
                name = "vanya",
                surname = "ivanov",
                email = "uservanya@notfake.com",
                enabled = true,
                roles = listOf(defaultRole)
            )
        )

        val user2 = userRepository.save(
            User(
                login = "usergrisha",
                password = encoder.encode("a12345678"),
                name = "grisha",
                surname = "mescheryakov",
                email = "usergrisha@notfake.com",
                enabled = true,
                roles = listOf(defaultRole)
            )
        )

        val user3 = userRepository.save(
            User(
                login = "userdima",
                password = encoder.encode("a12345678"),
                name = "dima",
                surname = "shabinsky",
                email = "userdima@notfake.com",
                enabled = true,
                roles = listOf(defaultRole)
            )
        )

        val user4 = userRepository.save(
            User(
                login = "userlesha",
                password = encoder.encode("a12345678"),
                name = "lesha",
                surname = "stolyarov",
                email = "userlesha@notfake.com",
                enabled = true,
                roles = listOf(defaultRole)
            )
        )

        val user5 = userRepository.save(
            User(
                login = "usersasha",
                password = encoder.encode("a12345678"),
                name = "sasha",
                surname = "dmitriev",
                email = "usersasha@notfake.com",
                enabled = true,
                roles = listOf(defaultRole)
            )
        )

        val user6 = userRepository.save(
            User(
                login = "usernikita",
                password = encoder.encode("a12345678"),
                name = "nikita",
                surname = "kolesnikov",
                email = "usernikita@notfake.com",
                enabled = true,
                roles = listOf(defaultRole)
            )
        )

        val tag1 = tagTypeRepository.save(TagType("Квантовая запутанность"))
        val tag2 = tagTypeRepository.save(TagType("C++"))
        val tag3 = tagTypeRepository.save(TagType("Python"))
        val tag4 = tagTypeRepository.save(TagType("Интегралы"))
        val tag5 = tagTypeRepository.save(TagType("Дифференциал"))
        val tag6 = tagTypeRepository.save(TagType("Химические опыты"))
        val tag7 = tagTypeRepository.save(TagType("Ницше"))
        val tag8 = tagTypeRepository.save(TagType("Теория вероятностей"))
        val tag9 = tagTypeRepository.save(TagType("Ковалентная связь"))


        val discipline1 = disciplineTypeRepository.save(DisciplineType("Физика"))
        val discipline2 = disciplineTypeRepository.save(DisciplineType("Математика"))
        val discipline3 = disciplineTypeRepository.save(DisciplineType("Программирование"))
        val discipline4 = disciplineTypeRepository.save(DisciplineType("Высшая математика"))
        val discipline5 = disciplineTypeRepository.save(DisciplineType("Философия"))
        val discipline6 = disciplineTypeRepository.save(DisciplineType("Химия"))

        val articleType1 = articleTypeRepository.save(ArticleType("Курсовая работа"))
        val articleType2 = articleTypeRepository.save(ArticleType("Доклад"))
        val articleType3 = articleTypeRepository.save(ArticleType("Лабораторная работа"))
        val articleType4 = articleTypeRepository.save(ArticleType("Отчёт"))
        val articleType5 = articleTypeRepository.save(ArticleType("Самостоятельная работа"))
        val articleType6 = articleTypeRepository.save(ArticleType("Исследовательская работа"))


        val article1 = articleRepository.save(
            Article(
                title = "Необяснимо, но вполне логично! Что скрывает в себе квантовая механика?" + Utility.getRandomString(
                    sizeGenerateId
                ),
                previewText = "Квантовая мехависимым. В этом мирем и взаимозависимым. В этом мия взаимосв",
                likes = 0,
                typeId = articleType1,
                userId = admin,
                date = DateTime.now().millis,
                text = "Квантовая мехависимым. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия"
            )
        )
        articleToTagTypeRepository.save(ArticleToTagType(article1, tag1))
        articleToDisciplineTypeRepository.save(ArticleToDisciplineType(article1, discipline1))

        val article2 = articleRepository.save(
            Article(
                title = "Циклы в C++" + Utility.getRandomString(sizeGenerateId),
                previewText = "Иногда необходимо повторять одно и то же действие несколько раз подряд. Для этого используют циклы. В этом уроке мы научимся программировать циклы на C++, после чего посчитаем сумму всех чисел от 1 до 1000.",
                likes = 0,
                typeId = articleType2,
                userId = user,
                date = DateTime.now().millis,
                text = "Иногда необходимо повторять одно и то же действие несколько раз подряд. Для этого используют циклы. В этом уроке мы научимся программировать циклы на C++, после чего посчитаем сумму всех чисел от 1 до 1000. Если мы знаем точное количество действий (итераций) цикла, то можем использовать цикл for. Синтаксис его выглядит примерно так: for (действие до начала цикла;\n" +
                        "     условие продолжения цикла;\n" +
                        "     действия в конце каждой итерации цикла) {\n" +
                        "         инструкция цикла;\n" +
                        "         инструкция цикла 2;\n" +
                        "         инструкция цикла N;\n" +
                        "}Итерацией цикла называется один проход этого цикла\n" +
                        "\n" +
                        "Существует частный случай этой записи, который мы сегодня и разберем:\n" +
                        "\n" +
                        "for (счетчик = значение; счетчик < значение; шаг цикла) {\n" +
                        "    тело цикла;\n" +
                        "}\n" +
                        "Счетчик цикла — это переменная, в которой хранится количество проходов данного цикла."
            )
        )
        articleToTagTypeRepository.save(ArticleToTagType(article2, tag2))
        articleToDisciplineTypeRepository.save(ArticleToDisciplineType(article2, discipline3))

        val article3 = articleRepository.save(
            Article(
                title = "Циклы в C++" + Utility.getRandomString(sizeGenerateId),
                previewText = "Иногда необходимо повторять одно и то же действие несколько раз подряд. Для этого используют циклы. В этом уроке мы научимся программировать циклы на C++, после чего посчитаем сумму всех чисел от 1 до 1000.",
                likes = 0,
                typeId = articleType2,
                userId = user1,
                date = DateTime.now().millis,
                text = "Иногда необходимо повторять одно и то же действие несколько раз подряд. Для этого используют циклы. В этом уроке мы научимся программировать циклы на C++, после чего посчитаем сумму всех чисел от 1 до 1000. Цикл while. Когда мы не знаем, сколько итераций должен произвести цикл, нам понадобится цикл while или do...while. Синтаксис цикла while в C++ выглядит следующим образом.\n" +
                        "\n" +
                        "while (Условие) {\n" +
                        "    Тело цикла;\n" +
                        "}\n" +
                        "Данный цикл будет выполняться, пока условие, указанное в круглых скобках является истиной. Решим ту же задачу с помощью цикла while. Хотя здесь мы точно знаем, сколько итераций должен выполнить цикл, очень часто бывают ситуации, когда это значение неизвестно.\n" +
                        "\n" +
                        "Ниже приведен исходный код программы, считающей сумму всех целых чисел от 1 до 1000.\n" +
                        "\n" +
                        "#include <iostream>\n" +
                        "using namespace std;\n" +
                        "\n" +
                        "int main()\n" +
                        "{\n" +
                        "    setlocale(0, \"\");\n" +
                        "    int i = 0; // инициализируем счетчик цикла.\n" +
                        "    int sum = 0; // инициализируем счетчик суммы.\n" +
                        "    while (i < 1000)\n" +
                        "    {\n" +
                        "        i++;\n" +
                        "        sum += i;\n" +
                        "    }\n" +
                        "    cout << \"Сумма чисел от 1 до 1000 = \" << sum << endl; \n" +
                        "    return 0;\n" +
                        "} "
            )
        )
        articleToTagTypeRepository.save(ArticleToTagType(article3, tag2))
        articleToDisciplineTypeRepository.save(ArticleToDisciplineType(article3, discipline3))

        val article4 = articleRepository.save(
            Article(
                title = "Изучаем понятие «интеграл»" + Utility.getRandomString(sizeGenerateId),
                previewText = "Интегрирование было известно еще в Древнем Египте. Конечно, не в современном виде, но все же. С тех пор математики написали очень много книг по этой теме. Особенно отличились Ньютон и Лейбниц, но суть вещей не изменилась.",
                likes = 0,
                typeId = articleType4,
                userId = user2,
                date = DateTime.now().millis,
                text = "Интегрирование было известно еще в Древнем Египте. Конечно, не в современном виде, но все же. С тех пор математики написали очень много книг по этой теме. Особенно отличились Ньютон и Лейбниц, но суть вещей не изменилась. Неопределенный интеграл\n" +
                        "Пусть у нас есть какая-то функция f(x).\n" +
                        "\n" +
                        "Неопределенным интегралом функции f(x) называется такая функция F(x), производная которой равна функции f(x).\n" +
                        "\n" +
                        "Первообразная существует для всех непрерывных функций. Также к первообразной часто прибавляют знак константы, так как производные функций, различающихся на константу, совпадают. Процесс нахождения интеграла называется интегрированием."
            )
        )
        articleToTagTypeRepository.save(ArticleToTagType(article3, tag4))
        articleToDisciplineTypeRepository.save(ArticleToDisciplineType(article4, discipline4))

        val article5 = articleRepository.save(
            Article(
                title = "Химия для смертных или Интересные опыты, которые можно повторить самому" + Utility.getRandomString(
                    sizeGenerateId
                ),
                previewText = "Для большинства химия - скучная и сложная наука, но мы же знаем, что это не так )\n" +
                        "\n" +
                        "Лично я заинтересовался химией после посмотра фейерверка, и понеслась душа в рай, как говорится.",
                likes = 0,
                typeId = articleType3,
                userId = user3,
                date = DateTime.now().millis,
                text = "Для большинства химия - скучная и сложная наука, но мы же знаем, что это не так )\n" +
                        "\n" +
                        "Лично я заинтересовался химией после посмотра фейерверка, и понеслась душа в рай, как говорится.\n" +
                        "\n" +
                        "Возможно этот пост вызовет интерес у кого-нибудь ещё ( по крайней мере я на это надеюсь ))\n" +
                        "\n" +
                        "Я лично облазил всю Лигу химиков, здесь были посты с опытами, но они были одиночные, да и самих этих постов не очень много. Так вот, я решил сделать пост ( или даже серию ) с опытами, которые можно повторить не имея дорогих реактивов, или особых навыков в химии.\n" +
                        "\n" +
                        "И, наконец-то, спустя 7 месяцев долгой подготовки видео и сдачи экзаменов моей лени я готов выложить этот пост. *троекратное ура*\n" +
                        "\n" +
                        "1) Это, наверное, самый известный эффектный химический опыт. Дихроматный  вулканчик.\n" +
                        "\n" +
                        "Всё, что надо вам для проведения этого опыта это: негорючая подложка и дихромат аммония, который есть во многих наборах для проведения опытов ( ну или его можно одолжить у учителя химии, он есть в каждой школьной лаборатории )\n" +
                        "\n" +
                        "Порядок проведения опыта:\n" +
                        "\n" +
                        "1) Насыпать горку дихромата аммония на подложку\n" +
                        "\n" +
                        "2) Поджечь\n" +
                        "\n" +
                        "Над вулканчиком не дышим, если не хотим получить дозу дихромата в лёгкие."
            )
        )
        articleToTagTypeRepository.save(ArticleToTagType(article5, tag6))
        articleToDisciplineTypeRepository.save(ArticleToDisciplineType(article5, discipline6))

        val article6 = articleRepository.save(
            Article(
                title = "решения дифференциальных уравнений" + Utility.getRandomString(sizeGenerateId),
                previewText = "Задание\n" +
                        "\n" +
                        "Решить дифференциальное уравнение xy’=y.",
                likes = 0,
                typeId = articleType5,
                userId = user3,
                date = DateTime.now().millis,
                text = "Задание\n" +
                        "\n" +
                        "Решить дифференциальное уравнение xy’=y.\n" +
                        "\n" +
                        "Решение\n" +
                        "\n" +
                        "В первую очередь, необходимо переписать уравнение в другой вид. Пользуясь \n" +
                        "\n" +
                        "\\[y{}'=\\frac{dy}{dx}\\]\n" +
                        "\n" +
                        "переписываем дифференциальное уравнение, получаем\n" +
                        "\n" +
                        "Дальше смотрим, насколько реально разделить переменные, то есть путем обычных манипуляций (перенос слагаемых из части в часть, вынесение за скобки и пр.) получить выражение, где «иксы» с одной стороны, а «игреки» с другой. В данном уравнении разделить переменные вполне реально, и после переноса множителей  по правилу пропорции получаем\n" +
                        "\n" +
                        "Далее интегрируем полученное уравнение:\n" +
                        "\n" +
                        "В данном случае интегралы берём из таблицы:\n" +
                        "\n" +
                        "После того, как взяты интегралы, дифференциальное уравнение считается решённым. Решение дифференциального уравнения в неявном виде называется общим интегралом дифференциального уравнения.\n" +
                        "\n" +
                        "То есть,\n" +
                        "\n" +
                        "– это общий интеграл. Также для удобства и красоты, его можно переписать в другом виде: y=Cx, где С=Const\n" +
                        "\n" +
                        "Ответ\n" +
                        "\n" +
                        "y=Cx, где С=Const."
            )
        )
        articleToTagTypeRepository.save(ArticleToTagType(article6, tag5))
        articleToDisciplineTypeRepository.save(ArticleToDisciplineType(article6, discipline2))

        val article7 = articleRepository.save(
            Article(
                title = "Три этапа философии Ницще" + Utility.getRandomString(sizeGenerateId),
                previewText = "Имя немецкого философа и мыслителя Фридриха Ницше – одно из наиболее узнаваемых в мире. Его учение пронизано нигилизмом и критическим отношением ко всему существующему на земле.",
                likes = 0,
                typeId = articleType6,
                userId = user2,
                date = DateTime.now().millis,
                text = "Имя немецкого философа и мыслителя Фридриха Ницше – одно из наиболее узнаваемых в мире. Его учение пронизано нигилизмом и критическим отношением ко всему существующему на земле. Труды мыслителя являются очень сложными для понимания и принятия. Его убеждения могут встретить и резко отрицательную реакцию, и недоумение, и положительное принятие. Но только не равнодушие. Биография и жизненный путь Фридриха наполнены событиями.\n" +
                        "\n" +
                        "Философские взгляды Ницше очень долго воспринимались как идеи фашизма и нацизма. И до сих пор, его труды могут быть восприняты как основа фашистского мировоззрения. Ницше обвиняют в том, что его философия нигилизма и Сверхчеловека являются основополагающими для действий Гитлера."
            )
        )
        articleToTagTypeRepository.save(ArticleToTagType(article7, tag7))
        articleToDisciplineTypeRepository.save(ArticleToDisciplineType(article7, discipline5))

        val article8 = articleRepository.save(
            Article(
                title = "Сортировка пузырьком" + Utility.getRandomString(sizeGenerateId),
                previewText = "Сортировка пузырьком - это метод сортировки массивов и списков путем последовательного сравнения и обмена соседних элементов, если предшествующий оказывается больше последующего.",
                likes = 0,
                typeId = articleType3,
                userId = user4,
                date = DateTime.now().millis,
                text = "Сортировка пузырьком - это метод сортировки массивов и списков путем последовательного сравнения и обмена соседних элементов, если предшествующий оказывается больше последующего.\n" +
                        "\n" +
                        "В процессе выполнения данного алгоритма элементы с большими значениями оказываются в конце списка, а элементы с меньшими значениями постепенно перемещаются по направлению к началу списка. Образно говоря, тяжелые элементы падают на дно, а легкие медленно всплывают подобно пузырькам воздуха.\n" +
                        "\n" +
                        "В сортировке методом пузырька количество итераций внешнего цикла определяется длинной списка минус единица, так как когда второй элемент становится на свое место, то первый уже однозначно минимальный и находится на своем месте.\n" +
                        "\n" +
                        "Количество итераций внутреннего цикла зависит от номера итерации внешнего цикла, так как конец списка уже отсортирован, и выполнять проход по этим элементам смысла нет.\n" +
                        "\n" +
                        "Пусть имеется список [6, 12, 4, 3, 8].\n" +
                        "\n" +
                        "За первую итерацию внешнего цикла число 12 переместится в конец. Для этого потребуется 4 сравнения во внутреннем цикле:\n" +
                        "\n" +
                        "6 > 12? Нет\n" +
                        "12 > 4? Да. Меняем местами\n" +
                        "12 > 3? Да. Меняем местами\n" +
                        "12 > 8? Да. Меняем местами\n" +
                        "Результат: [6, 4, 3, 8, 12]"
            )
        )
        articleToTagTypeRepository.save(ArticleToTagType(article8, tag3))
        articleToDisciplineTypeRepository.save(ArticleToDisciplineType(article8, discipline3))

        val article9 = articleRepository.save(
            Article(
                title = "Теория вероятностей. Базовые термины и понятия" + Utility.getRandomString(sizeGenerateId),
                previewText = "Под занавес продолжительных летних каникул пришло время потихоньку возвращаться к высшей математике и торжественно открыть пустой вёрдовский файл, чтобы приступить к созданию нового раздела – Теория вероятностей и математическая статистика.",
                likes = 0,
                typeId = articleType2,
                userId = user3,
                date = DateTime.now().millis,
                text = "Под занавес продолжительных летних каникул пришло время потихоньку возвращаться к высшей математике и торжественно открыть пустой вёрдовский файл, чтобы приступить к созданию нового раздела – Теория вероятностей и математическая статистика. Признаюсь, нелегко даются первые строчки, но первый шаг – это пол пути, поэтому я предлагаю всем внимательно проштудировать вводную статью, после чего осваивать тему будет в 2 раза проще! Ничуть не преувеличиваю. …Накануне очередного 1 сентября вспоминается первый класс и букварь…. Буквы складываются в слоги, слоги в слова, слова в короткие предложения – Мама мыла раму. Совладать с тервером и математической статистикой так же просто, как научиться читать! Однако для этого необходимо знать ключевые термины, понятия и обозначения, а также некоторые специфические правила, которым и посвящён данный урок.\n" +
                        "\n" +
                        "Но сначала примите мои поздравления с началом (продолжением, завершением, нужное отметить) учебного года и примите подарок. Лучший подарок – это книга, и для самостоятельной работы я рекомендую следующую литературу:\n" +
                        "\n" +
                        "1) Гмурман В.Е. Теория вероятностей и математическая статистика\n" +
                        "\n" +
                        "Легендарное учебное пособие, выдержавшее более десяти переизданий. Отличается доходчивостью и предельной простой изложения материала, а первые главы так и вовсе доступны, думаю, уже для учащихся 6-7-х классов.\n" +
                        "\n" +
                        "2) Гмурман В.Е. Руководство к решению задач по теории вероятностей и математической статистике\n" +
                        "\n" +
                        "Решебник того же Владимира Ефимовича с подробно разобранными примерами и задачами.\n" +
                        "\n" +
                        "ОБЯЗАТЕЛЬНО закачайте обе книги из Интернета или раздобудьте их бумажные оригиналы! Подойдёт и версия 60-70-х годов, что даже лучше для чайников. Хотя фраза «теория вероятностей для чайников» звучит довольно нелепо, поскольку почти всё ограничивается элементарными арифметическими действиями. Проскакивают, правда, местами производные и интегралы, но это только местами."
            )
        )
        articleToTagTypeRepository.save(ArticleToTagType(article9, tag8))
        articleToDisciplineTypeRepository.save(ArticleToDisciplineType(article9, discipline4))

        val article10 = articleRepository.save(
            Article(
                title = "Ковалентная связь и ее характеристики" + Utility.getRandomString(sizeGenerateId),
                previewText = "Химическая связь — взаимодействие атомов, осуществляемое путём обмена электронами или их перехода от одного атома к другому.",
                likes = 0,
                typeId = articleType1,
                userId = user2,
                date = DateTime.now().millis,
                text = "Химическая связь — взаимодействие атомов, осуществляемое путём обмена электронами или их перехода от одного атома к другому.\n" +
                        "\n" +
                        "Причиной образования химической связи является стремление системы к более устойчивому состоянию с минимально возможным запасом энергии.\n" +
                        "\n" +
                        "Основным условием образования химической связи является понижение полной энергии системы по сравнению с суммарной энергией изолированных атомов. Молекула может образоваться только в том случае, если при взаимодействии атомов их общая энергия уменьшается. Образование химической связи всегда сопровождается выделением энергии, которая называется энергией химической связи.\n" +
                        "\n" +
                        "Образование такой системы может идти несколькими способами и приводит к образованию соединений с различными видами химической связи. При образовании химической связи атомы стремятся приобрести устойчивую электронную оболочку.\n" +
                        "\n" +
                        "Наиболее устойчивым является завершенный внешний энергетический уровень, который имеют атомы благородных газов. Завершенный внешний электронный уровень атомов всех благородных газов, кроме гелия, содержит восемь электронов, атом гелия — два электрона. Г. Льюис в  г. предположил, что при образовании химической связи атомы стремятся изменить свои электронные оболочки до конфигурации устойчивой электронной оболочки ближайшего благородного газа, т. е. до двух, как у гелия, или до восьми электронов, как у остальных благородных газов. Это утверждение называется правилом октета (от лат. окто — восемь). Завершение внешних электронных оболочек становится возможным путём отдачи или присоединения электронов, а также путём образования общих электронных пар. Такое изменение электронных оболочек атомов приводит к образованию устойчивых структур.\n" +
                        "\n" +
                        "Различают четыре основных типа химической связи: ковалентную, ионную, металлическую и водородную.\n" +
                        "\n" +
                        "Важнейшей характеристикой атома при образовании химической связи является его электроотрицательность (ЭО) — способность притягивать электроны."
            )
        )
        articleToTagTypeRepository.save(ArticleToTagType(article10, tag9))
        articleToDisciplineTypeRepository.save(ArticleToDisciplineType(article10, discipline6))

        for (i in 0 until 40) {
            val article11 = articleRepository.save(
                Article(
                    title = "Необяснимо, но вполне логично! Что скрывает в себе квантовая механика? " + Utility.getRandomString(
                        sizeGenerateId
                    ),
                    previewText = "Квантовая мехависимым. В этом мирем и взаимозависимым. В этом мия взаимосв",
                    likes = 0,
                    typeId = articleType1,
                    userId = admin,
                    date = DateTime.now().millis,
                    text = "Квантовая мехависимым. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия взаимосвязаны. В этом мирем и взаимозависимым. В этом мия"
                )
            )
            articleToTagTypeRepository.save(ArticleToTagType(article11, tag1))
            articleToDisciplineTypeRepository.save(ArticleToDisciplineType(article11, discipline1))
        }
    }
}