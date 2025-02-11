package com.creditcard.portfolio.service

import com.creditcard.portfolio.AuthUserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthUserService(private val authUserRepository: AuthUserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val authUser = authUserRepository.findBySiteId(username)
            .orElseThrow { UsernameNotFoundException("❌ 사용자 없음: $username") }

        return User.builder()
            .username(authUser.siteId)
            .password(authUser.password)
            .roles("USER") // 🔥 기본 ROLE_USER 부여
            .build()
    }
}

