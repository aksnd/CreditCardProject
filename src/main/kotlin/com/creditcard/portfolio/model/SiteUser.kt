package com.creditcard.portfolio

import jakarta.persistence.*

@Entity
@Table(name = "site_users")
data class SiteUser(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var name: String,

    var debt: Double = 0.0,   // 현재 빚
    var income: Double = 0.0, // 이번 달 소득
    var expense: Double = 0.0, // 이번 달 소비
    var interestRate: Double = 0.17, //이자율 17%기본값, 카드값마다 상이

    @OneToOne(mappedBy = "siteUser", cascade = [CascadeType.ALL], orphanRemoval = true)
    var authUser: AuthUser? = null
)
