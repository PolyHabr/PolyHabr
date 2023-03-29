package com.polyhabr.poly_back.integration

//@Testcontainers
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class TagRepositoryTest @Autowired constructor(
//    val entityManager: TestEntityManager,
//    val tagTypeRepository: TagTypeRepository
//) {
//
//    companion object {
//        @Container
//        val container = PostgreSQLContainer<Nothing>("postgres:12").apply {
//            withDatabaseName("postgres")
//            withUsername("postgres")
//            withPassword("postgres")
//        }
//
//        @JvmStatic
//        @DynamicPropertySource
//        fun properties(registry: DynamicPropertyRegistry) {
//            registry.add("spring.datasource.url", container::getJdbcUrl);
//            registry.add("spring.datasource.password", container::getPassword);
//            registry.add("spring.datasource.username", container::getUsername);
//        }
//    }
//
//    @Test
//    fun test() {
//        val tagType = tagTypeRepository.save(TagType(name = "test"))
//        val tagType2 = tagTypeRepository.findById(tagType.id!!).get()
//        assert(tagType2.name == tagType.name)
//    }
//}