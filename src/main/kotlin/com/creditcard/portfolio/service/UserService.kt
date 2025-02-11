package com.creditcard.portfolio

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

fun roundLoanInterest(value: Double): Double {
    return BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toDouble()
}

@Service
class UserService(private val siteUserRepository: SiteUserRepository) {


    fun getUserById(id: Long): SiteUser {
        return siteUserRepository.findById(id).orElseThrow { IllegalArgumentException("User not found") }
    }

    @Transactional
    fun updateDebt(id: Long, newDebt: Double): SiteUser {
        val siteUser = getUserById(id)
        siteUser.debt = newDebt
        return siteUserRepository.save(siteUser)
    }


    @Transactional
    fun updateInterestRate(id: Long, newInterest: Double): SiteUser {
        val siteUser = getUserById(id)
        siteUser.interestRate = newInterest
        return siteUserRepository.save(siteUser)
    }



    @Transactional
    fun updateIncomeExpense(id: Long, income: Double, expense: Double): SiteUser {
        val siteUser = getUserById(id)
        siteUser.income = income
        siteUser.expense = expense
        return siteUserRepository.save(siteUser)
    }

    @Transactional
    fun applyMonthlyUpdate(id: Long): SiteUser {

        val siteUser = getUserById(id)
        val netIncome = siteUser.income - siteUser.expense  // 순수입 계산
        val interest = (siteUser.debt /12) * siteUser.interestRate
        print(interest)
        //이자율 계산법 : 전월 잔액 * (30 or 31) / 365 * 이자율(0.17등)

        siteUser.debt = roundLoanInterest(siteUser.debt+ interest-netIncome)
        siteUser.income = 0.0      // 이번 달 소득 초기화
        siteUser.expense = 0.0     // 이번 달 소비 초기화
        return siteUserRepository.save(siteUser)
    }

    fun calculateDebtRepaymentMonth(
        id:Long,
        monthlyIncome: Double,
        monthlyExpense: Double,
    ): Int{
        val user = getUserById(id)
        val monthlyNetIncome = monthlyIncome-monthlyExpense
        val monthlyInterestRate = user.interestRate / 12 // 월 이자율
        val interest = user.debt * monthlyInterestRate
        if (monthlyNetIncome<=interest)
            return -1 //빚이 계속 늘어나거나 유지 뭔짓을 해도 못갚음.

        var months = 0
        var remainingDebt = user.debt
        while (remainingDebt > 0) {

            remainingDebt = roundLoanInterest(remainingDebt-(monthlyIncome-monthlyExpense)+remainingDebt*monthlyInterestRate)
            months++

            if(months>10000) //대충 800년이면 못갚는다 봐야지...
                return -1
            println("📅 ${months}개월 후 남은 빚: ${String.format("%.2f", remainingDebt)}원")
        }

        return months

    }

}