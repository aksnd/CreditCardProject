package com.creditcard.portfolio

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AuthUserRepository : JpaRepository<AuthUser, Long> {
    fun findBySiteId(siteId: String): Optional<AuthUser>
}
