package com.creditcard.portfolio

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthUserDetailsService(private val authUserRepository: AuthUserRepository) : UserDetailsService {

    override fun loadUserByUsername(siteId: String): UserDetails {
        val authUser = authUserRepository.findBySiteId(siteId)
            .orElseThrow { UsernameNotFoundException("🚨 해당 siteId를 찾을 수 없습니다.") }
        return AuthUserDetails(authUser)
    }
}