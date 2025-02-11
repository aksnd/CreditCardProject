package com.creditcard.portfolio

import jakarta.transaction.Transactional
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val authUserRepository: AuthUserRepository,
    private val siteUserRepository: SiteUserRepository
) {

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    fun loginUser(siteId: String, password: String): String {
        val authToken = UsernamePasswordAuthenticationToken(siteId, password)

        // 🔥 인증 수행
        val authentication = authenticationManager.authenticate(authToken)

        // ✅ SecurityContextHolder에 인증 정보 저장
        SecurityContextHolder.getContext().authentication = authentication

        println("로그인 성공! authentication 확인${authentication.name}")
        return "✅ 로그인 성공!"
    }

    fun registerUser(siteId: String, password: String, name: String): String {
        if (authUserRepository.findBySiteId(siteId).isPresent) {
            throw IllegalArgumentException("🚨 이미 존재하는 siteId 입니다.")
        }

        val hashedPassword = passwordEncoder.encode(password)

        val siteUser = siteUserRepository.save(SiteUser(name = name)) // ✅ User 먼저 저장
        println("✅ SiteUser 저장 완료 - ID: ${siteUser.id}, Name: ${siteUser.name}")

        val authUser = AuthUser(siteUser = siteUser, siteId = siteId, password = hashedPassword)

        authUserRepository.save(authUser) // ✅ User의 ID를 포함한 AuthUser 저장

        return "✅ 회원가입 성공!"
    }
}
