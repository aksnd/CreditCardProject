package com.creditcard.portfolio

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AuthUserDetails(private val authUser: AuthUser) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf() // 🔥 기본적으로 권한 없이 처리 (추후 추가 가능)
    }

    override fun getPassword(): String {
        return authUser.password
    }

    override fun getUsername(): String {
        return authUser.siteId
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
