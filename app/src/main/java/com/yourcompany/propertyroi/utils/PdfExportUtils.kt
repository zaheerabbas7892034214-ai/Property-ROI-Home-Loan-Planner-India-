package com.yourcompany.propertyroi.utils

import android.content.Context
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.yourcompany.propertyroi.data.models.*
import java.io.File
import java.io.OutputStream
import java.text.NumberFormat
import java.util.*

object PdfExportUtils {
    
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    private val percentFormat = NumberFormat.getPercentInstance().apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    
    /**
     * Generate PDF report for a scenario
     */
    fun generatePdfReport(
        outputStream: OutputStream,
        scenarioName: String,
        loanEmiResult: LoanEmiResult?,
        rentalYieldResult: RentalYieldResult?,
        roiProjectionResult: RoiProjectionResult?
    ) {
        val writer = PdfWriter(outputStream)
        val pdfDoc = PdfDocument(writer)
        val document = Document(pdfDoc)
        
        // Cover Page
        document.add(
            Paragraph("Property ROI & Home Loan Report")
                .setFontSize(24f)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20f)
        )
        
        document.add(
            Paragraph("Scenario: $scenarioName")
                .setFontSize(16f)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(40f)
        )
        
        document.add(
            Paragraph("Generated: ${Date()}")
                .setFontSize(10f)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(40f)
        )
        
        // Loan EMI Section
        loanEmiResult?.let { result ->
            document.add(
                Paragraph("Home Loan EMI Calculator")
                    .setFontSize(18f)
                    .setBold()
                    .setMarginTop(20f)
            )
            
            val emiTable = Table(UnitValue.createPercentArray(floatArrayOf(50f, 50f)))
                .useAllAvailableWidth()
            
            emiTable.addCell("Monthly EMI")
            emiTable.addCell(currencyFormat.format(result.monthlyEmi))
            emiTable.addCell("Total Interest")
            emiTable.addCell(currencyFormat.format(result.totalInterest))
            emiTable.addCell("Total Payment")
            emiTable.addCell(currencyFormat.format(result.totalPayment))
            
            document.add(emiTable)
            
            // Amortization schedule (first 12 months and last 12 months)
            document.add(
                Paragraph("Amortization Schedule (Sample)")
                    .setFontSize(14f)
                    .setBold()
                    .setMarginTop(20f)
            )
            
            val scheduleTable = Table(UnitValue.createPercentArray(floatArrayOf(15f, 20f, 25f, 20f, 20f)))
                .useAllAvailableWidth()
            
            scheduleTable.addHeaderCell("Month")
            scheduleTable.addHeaderCell("EMI")
            scheduleTable.addHeaderCell("Principal")
            scheduleTable.addHeaderCell("Interest")
            scheduleTable.addHeaderCell("Balance")
            
            // First 12 months
            result.amortizationSchedule.take(12).forEach { row ->
                scheduleTable.addCell(row.month.toString())
                scheduleTable.addCell(currencyFormat.format(row.emi))
                scheduleTable.addCell(currencyFormat.format(row.principal))
                scheduleTable.addCell(currencyFormat.format(row.interest))
                scheduleTable.addCell(currencyFormat.format(row.balance))
            }
            
            document.add(scheduleTable)
        }
        
        // Rental Yield Section
        rentalYieldResult?.let { result ->
            document.add(
                Paragraph("Rental Yield Calculator")
                    .setFontSize(18f)
                    .setBold()
                    .setMarginTop(30f)
            )
            
            val yieldTable = Table(UnitValue.createPercentArray(floatArrayOf(50f, 50f)))
                .useAllAvailableWidth()
            
            yieldTable.addCell("Annual Rent")
            yieldTable.addCell(currencyFormat.format(result.annualRent))
            yieldTable.addCell("Annual Expenses")
            yieldTable.addCell(currencyFormat.format(result.annualExpenses))
            yieldTable.addCell("Gross Yield")
            yieldTable.addCell("${String.format("%.2f", result.grossYield)}%")
            yieldTable.addCell("Net Yield")
            yieldTable.addCell("${String.format("%.2f", result.netYield)}%")
            
            document.add(yieldTable)
        }
        
        // ROI Projection Section
        roiProjectionResult?.let { result ->
            document.add(
                Paragraph("ROI Projection")
                    .setFontSize(18f)
                    .setBold()
                    .setMarginTop(30f)
            )
            
            document.add(
                Paragraph("Internal Rate of Return (IRR): ${String.format("%.2f", result.irr)}%")
                    .setFontSize(14f)
                    .setMarginBottom(10f)
            )
            
            val projectionTable = Table(UnitValue.createPercentArray(floatArrayOf(10f, 22f, 22f, 22f, 24f)))
                .useAllAvailableWidth()
            
            projectionTable.addHeaderCell("Year")
            projectionTable.addHeaderCell("Property Value")
            projectionTable.addHeaderCell("Annual Rent")
            projectionTable.addHeaderCell("Net Cash Flow")
            projectionTable.addHeaderCell("Cumulative Return")
            
            result.timeline.forEach { projection ->
                projectionTable.addCell(projection.year.toString())
                projectionTable.addCell(currencyFormat.format(projection.propertyValue))
                projectionTable.addCell(currencyFormat.format(projection.annualRent))
                projectionTable.addCell(currencyFormat.format(projection.netCashFlow))
                projectionTable.addCell(currencyFormat.format(projection.cumulativeReturn))
            }
            
            document.add(projectionTable)
        }
        
        document.close()
    }
}
