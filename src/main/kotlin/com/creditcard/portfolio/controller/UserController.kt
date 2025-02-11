package com.creditcard.portfolio

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {


    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): SiteUser {
        return userService.getUserById(id)
    }

    // ✅ 빚 수정 (PUT)
    @PutMapping("/{id}/debt")
    fun updateDebt(@PathVariable id: Long, @RequestParam newDebt: Double): SiteUser {
        return userService.updateDebt(id, newDebt)
    }

    @PutMapping("/{id}/interest")
    fun updateInterest(@PathVariable id: Long, @RequestParam newInterest: Double): SiteUser {
        return userService.updateInterestRate(id, newInterest)
    }

    // ✅ 이번 달 소득 & 소비 입력 (PUT)
    @PutMapping("/{id}/income-expense")
    fun updateIncomeExpense(
        @PathVariable id: Long,
        @RequestParam income: Double,
        @RequestParam expense: Double
    ): SiteUser {
        return userService.updateIncomeExpense(id, income, expense)
    }

    // ✅ "1달 보내기" 버튼 기능 (POST)
    @PostMapping("/{id}/apply-month")
    fun applyMonthlyUpdate(@PathVariable id: Long): SiteUser {
        return userService.applyMonthlyUpdate(id)
    }

    @GetMapping("{id}/simulation")
    fun calculateDebtPayment(
        @PathVariable id:Long,
        @RequestParam monthlyIncome: Double,
        @RequestParam monthlyExpense: Double
    ): Int{
        return userService.calculateDebtRepaymentMonth(id,monthlyIncome,monthlyExpense)
    }
}
