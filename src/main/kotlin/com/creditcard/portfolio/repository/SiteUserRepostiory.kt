package com.creditcard.portfolio

import org.springframework.data.jpa.repository.JpaRepository

interface SiteUserRepository : JpaRepository<SiteUser, Long> {
}