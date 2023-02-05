package com.polyhabr.poly_back.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
open class SecurityConfig {
    @Bean
    @Throws(Exception::class)
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
            .authorizeRequests().antMatchers("/**").permitAll() // config to permit all requests
        return http.build()
    }

    @Bean
    open fun authenticationManager(): AuthenticationManager { // to delete default username and password that is printed in the log every time, you can provide here any auth manager (InMemoryAuthenticationManager, etc) as you need
        return AuthenticationManager { authentication -> throw UnsupportedOperationException() }
    }
}
