package com.creditcard.portfolio

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.creditcard.portfolio"])
class PortfolioApplication

fun main(args: Array<String>) {
	runApplication<PortfolioApplication>(*args)
}
