package com.yourcompany.propertyroi.data.models

// Data models for calculator inputs and results

data class LoanEmiInput(
    val principal: Double,
    val interestRate: Double,
    val tenureYears: Int
)

data class LoanEmiResult(
    val monthlyEmi: Double,
    val totalInterest: Double,
    val totalPayment: Double,
    val amortizationSchedule: List<AmortizationRow>
)

data class AmortizationRow(
    val month: Int,
    val emi: Double,
    val principal: Double,
    val interest: Double,
    val balance: Double
)

data class RentalYieldInput(
    val propertyPrice: Double,
    val monthlyRent: Double,
    val annualMaintenance: Double = 0.0,
    val propertyTax: Double = 0.0
)

data class RentalYieldResult(
    val grossYield: Double,
    val netYield: Double,
    val annualRent: Double,
    val annualExpenses: Double
)

data class RoiProjectionInput(
    val propertyValue: Double,
    val monthlyRent: Double,
    val appreciationRate: Double,
    val rentEscalationRate: Double,
    val vacancyRate: Double,
    val maintenanceCostRate: Double,
    val stampDutyRegistration: Double,
    val projectionYears: Int = 10
)

data class RoiProjectionResult(
    val irr: Double,
    val timeline: List<YearProjection>
)

data class YearProjection(
    val year: Int,
    val propertyValue: Double,
    val annualRent: Double,
    val expenses: Double,
    val netCashFlow: Double,
    val cumulativeReturn: Double
)
