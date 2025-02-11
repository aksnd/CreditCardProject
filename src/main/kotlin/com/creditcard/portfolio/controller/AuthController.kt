package com.creditcard.portfolio

import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val authUserRepository: AuthUserRepository,
    private val authenticationManager: AuthenticationManager // ✅ Spring Security AuthenticationManager 주입
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<String> {
        return ResponseEntity.ok(authService.registerUser(request.siteId, request.password, request.name))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<String> {
        return ResponseEntity.ok(authService.loginUser(request.siteId, request.password))
    }

    @GetMapping("/check-auth")
    fun checkAuth(): ResponseEntity<Any> {
        val authentication = SecurityContextHolder.getContext().authentication
        return if (authentication != null && authentication.isAuthenticated) {
            ResponseEntity.ok("✅ 로그인 상태 유지 중: ${authentication.name}")
        } else {
            ResponseEntity.status(401).body("🚨 로그인되지 않음")
        }
    }

    @GetMapping("/me")
    fun getCurrentUser(): ResponseEntity<Any> {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication == null || authentication.name == "anonymousUser") {
            return ResponseEntity.status(401).body("🚨 인증되지 않은 사용자입니다.")
        }

        val authUser = authUserRepository.findBySiteId(authentication.name).orElse(null)
        if (authUser != null) {
            return ResponseEntity.ok(authUser.siteUser) // ✅ SiteUser 정보 반환
        }

        return ResponseEntity.status(404).body("🚨 사용자 정보를 찾을 수 없습니다.")
    }

}

data class RegisterRequest(val siteId: String, val password: String, val name: String)
data class LoginRequest(val siteId: String, val password: String)
