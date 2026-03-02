package oc.moneylog.server.service

import oc.moneylog.server.domain.BudgetSettings
import oc.moneylog.server.domain.Transaction
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters

object AllowanceCalculator {
    fun cycleRange(now: LocalDateTime, paydayDay: Int): Pair<LocalDateTime, LocalDateTime> {
        val today = now.toLocalDate()
        val currentMonthPayday = today.withDayOfMonth(paydayDay.coerceAtMost(today.lengthOfMonth()))
        val startDate = if (today >= currentMonthPayday) currentMonthPayday else currentMonthPayday.minusMonths(1)
        val nextMonth = startDate.plusMonths(1)
        val endDate = nextMonth.withDayOfMonth(paydayDay.coerceAtMost(nextMonth.lengthOfMonth()))
        return startDate.atStartOfDay() to endDate.atStartOfDay()
    }

    fun weeklySpent(transactions: List<Transaction>, now: LocalDateTime): Long {
        val monday = now.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay()
        return transactions
            .asSequence()
            .filter { !it.excluded }
            .filter { it.occurredAt >= monday && it.occurredAt <= now }
            .sumOf { it.amount }
    }

    fun weeklyLimit(settings: BudgetSettings, transactions: List<Transaction>, now: LocalDateTime): Long {
        val (cycleStart, cycleEnd) = cycleRange(now, settings.paydayDay)
        val used = transactions
            .asSequence()
            .filter { !it.excluded }
            .filter { it.occurredAt >= cycleStart && it.occurredAt < cycleEnd }
            .sumOf { it.amount }

        val remainingBudget = (settings.monthlyBudget - used).coerceAtLeast(0)
        val remainingDays = (cycleEnd.toLocalDate().toEpochDay() - now.toLocalDate().toEpochDay()).coerceAtLeast(1)
        val remainingWeeks = (remainingDays / 7.0).coerceAtLeast(1.0)
        return (remainingBudget / remainingWeeks).toLong()
    }

    fun dailyRange(month: String): Pair<LocalDate, LocalDate> {
        val parts = month.split("-")
        val y = parts[0].toInt()
        val m = parts[1].toInt()
        val start = LocalDate.of(y, m, 1)
        return start to start.withDayOfMonth(start.lengthOfMonth())
    }
}
