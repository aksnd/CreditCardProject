package com.creditcard.portfolio.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class ViewController {

    @GetMapping("/")
    fun start(): String {
        return "index"
    }

    @GetMapping("/index")
    fun index(): String {
        return "index"
    }

    @GetMapping("/login")
    fun login():String{
        return "login"
    }

    @GetMapping("/register")
    fun register():String{
        return "register"
    }

    @GetMapping("/main")
    fun main():String{
        return "main"
    }
}