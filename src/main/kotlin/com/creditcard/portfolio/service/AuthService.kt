package com.creditcard.portfolio

import jakarta.transaction.Transactional
import org.springframework.security.authentication.*
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
        return try {
            val authToken = UsernamePasswordAuthenticationToken(siteId, password)

            // 🔥 인증 수행 (실패 시 예외 발생)
            val authentication = authenticationManager.authenticate(authToken)

            // ✅ SecurityContextHolder에 인증 정보 저장
            SecurityContextHolder.getContext().authentication = authentication

            println("로그인 성공! authentication 확인: ${authentication.name}")
            "✅ 로그인 성공! 유저: ${authentication.name}"

        } catch (e: BadCredentialsException) {
            throw BadCredentialsException("❌ 로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.")
        } catch (e: DisabledException) {
            throw DisabledException("❌ 로그인 실패: 계정이 비활성화되었습니다.")
        } catch (e: LockedException) {
            throw LockedException("❌ 로그인 실패: 계정이 잠겼습니다.")
        } catch (e: Exception) {
            throw RuntimeException("❌ 로그인 실패: 서버 오류 발생 (${e.message})")
        }
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
