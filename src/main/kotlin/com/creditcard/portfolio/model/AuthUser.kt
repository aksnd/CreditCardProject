package com.creditcard.portfolio

import jakarta.persistence.*

@Entity
@Table(name = "auth_users")
data class AuthUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ ID는 자동 증가
    val id: Long = 0,

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true) // ✅ siteUser와 연결되는 ID
    val siteUser: SiteUser,

    @Column(unique = true, nullable = false)
    val siteId: String,

    @Column(nullable = false)
    val password: String
)
