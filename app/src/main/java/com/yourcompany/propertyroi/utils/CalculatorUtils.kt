package com.yourcompany.propertyroi.utils

import com.yourcompany.propertyroi.data.models.*
import kotlin.math.pow

object CalculatorUtils {
    
    /**
     * Calculate Home Loan EMI and amortization schedule
     */
    fun calculateLoanEmi(input: LoanEmiInput): LoanEmiResult {
        val principal = input.principal
        val monthlyRate = input.interestRate / 12 / 100
        val totalMonths = input.tenureYears * 12
        
        // EMI Formula: P * r * (1+r)^n / ((1+r)^n - 1)
        val emi = if (monthlyRate > 0) {
            principal * monthlyRate * (1 + monthlyRate).pow(totalMonths) /
                    ((1 + monthlyRate).pow(totalMonths) - 1)
        } else {
            principal / totalMonths
        }
        
        val totalPayment = emi * totalMonths
        val totalInterest = totalPayment - principal
        
        // Generate amortization schedule
        val schedule = mutableListOf<AmortizationRow>()
        var balance = principal
        
        for (month in 1..totalMonths) {
            val interestPayment = balance * monthlyRate
            val principalPayment = emi - interestPayment
            balance -= principalPayment
            
            schedule.add(
                AmortizationRow(
                    month = month,
                    emi = emi,
                    principal = principalPayment,
                    interest = interestPayment,
                    balance = maxOf(balance, 0.0)
                )
            )
        }
        
        return LoanEmiResult(
            monthlyEmi = emi,
            totalInterest = totalInterest,
            totalPayment = totalPayment,
            amortizationSchedule = schedule
        )
    }
    
    /**
     * Calculate Rental Yield
     */
    fun calculateRentalYield(input: RentalYieldInput): RentalYieldResult {
        val annualRent = input.monthlyRent * 12
        val annualExpenses = input.annualMaintenance + input.propertyTax
        
        val grossYield = (annualRent / input.propertyPrice) * 100
        val netYield = ((annualRent - annualExpenses) / input.propertyPrice) * 100
        
        return RentalYieldResult(
            grossYield = grossYield,
            netYield = netYield,
            annualRent = annualRent,
            annualExpenses = annualExpenses
        )
    }
    
    /**
     * Calculate ROI Projection with IRR
     */
    fun calculateRoiProjection(input: RoiProjectionInput): RoiProjectionResult {
        val timeline = mutableListOf<YearProjection>()
        var currentPropertyValue = input.propertyValue
        var currentMonthlyRent = input.monthlyRent
        var cumulativeReturn = -input.stampDutyRegistration
        
        for (year in 1..input.projectionYears) {
            // Calculate property appreciation
            currentPropertyValue *= (1 + input.appreciationRate / 100)
            
            // Calculate rent escalation
            if (year > 1) {
                currentMonthlyRent *= (1 + input.rentEscalationRate / 100)
            }
            
            val annualRent = currentMonthlyRent * 12
            
            // Calculate expenses
            val vacancyLoss = annualRent * (input.vacancyRate / 100)
            val maintenanceCost = currentPropertyValue * (input.maintenanceCostRate / 100)
            val totalExpenses = vacancyLoss + maintenanceCost
            
            // Net cash flow
            val netCashFlow = annualRent - totalExpenses
            cumulativeReturn += netCashFlow
            
            timeline.add(
                YearProjection(
                    year = year,
                    propertyValue = currentPropertyValue,
                    annualRent = annualRent,
                    expenses = totalExpenses,
                    netCashFlow = netCashFlow,
                    cumulativeReturn = cumulativeReturn
                )
            )
        }
        
        // Calculate IRR (simplified approximation)
        val irr = calculateIRR(input, timeline)
        
        return RoiProjectionResult(
            irr = irr,
            timeline = timeline
        )
    }
    
    /**
     * Calculate Internal Rate of Return (IRR) - Simplified
     */
    private fun calculateIRR(input: RoiProjectionInput, timeline: List<YearProjection>): Double {
        // Cash flows: Initial investment (negative), then annual net cash flows, plus final property value
        val cashFlows = mutableListOf<Double>()
        cashFlows.add(-(input.propertyValue + input.stampDutyRegistration))
        
        timeline.forEachIndexed { index, projection ->
            val cashFlow = if (index == timeline.lastIndex) {
                // Last year: net cash flow + property value
                projection.netCashFlow + projection.propertyValue
            } else {
                projection.netCashFlow
            }
            cashFlows.add(cashFlow)
        }
        
        // Newton-Raphson method for IRR calculation
        var rate = 0.1 // Initial guess 10%
        val maxIterations = 100
        val tolerance = 0.0001
        
        for (i in 0 until maxIterations) {
            var npv = 0.0
            var npvDerivative = 0.0
            
            cashFlows.forEachIndexed { year, cashFlow ->
                npv += cashFlow / (1 + rate).pow(year)
                if (year > 0) {
                    npvDerivative -= year * cashFlow / (1 + rate).pow(year + 1)
                }
            }
            
            val newRate = rate - npv / npvDerivative
            
            if (kotlin.math.abs(newRate - rate) < tolerance) {
                return newRate * 100 // Convert to percentage
            }
            
            rate = newRate
        }
        
        return rate * 100
    }
}
