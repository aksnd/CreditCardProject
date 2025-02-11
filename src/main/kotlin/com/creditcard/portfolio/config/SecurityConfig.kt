package com.creditcard.portfolio

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(private val authUserDetailsService: AuthUserDetailsService) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityContext { securityContext -> securityContext.requireExplicitSave(false) } // ✅ SecurityContext 자동 저장
            .csrf { it.disable() }  // ✅ CSRF 보호 비활성화 (POST 요청 가능하도록 설정)
            .authorizeHttpRequests { authorizeHttpRequests ->
                authorizeHttpRequests
                    .requestMatchers("/auth/register", "/auth/login","/auth/check-auth", "/", "/index","/login","/register").permitAll()  // ✅ 회원가입 & 로그인 허용
                    .requestMatchers("/public/**", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()  // ✅ 정적 리소스 허용
                    .anyRequest().authenticated()  // 🔥 그 외 요청은 인증 필요
            }
            .formLogin { formLogin ->
                formLogin
                    .loginPage("/")
            }
            .logout { logout ->
                logout
                    .logoutRequestMatcher(AntPathRequestMatcher("/auth/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")  // ✅ 쿠키 삭제 (중요)
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // ✅ 세션 항상 유지
            }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(authUserDetailsService) // ✅ 유저 정보 가져오기
        provider.setPasswordEncoder(passwordEncoder()) // 🔐 비밀번호 암호화 적용
        return provider
    }

    @Bean
    fun authenticationManagerBean(): AuthenticationManager {
        return ProviderManager(listOf(daoAuthenticationProvider()))
    }



}

