# Property ROI & Home Loan Planner (India)

A comprehensive Android application for property investment analysis and home loan planning designed specifically for the Indian market.

## Features

### Calculators
- **Home Loan EMI Calculator**: Calculate monthly EMI, total interest, and view detailed amortization schedule
- **Rental Yield Calculator**: Compute gross and net rental yields considering maintenance and taxes
- **ROI Projection**: Advanced property investment analysis with IRR calculation, considering appreciation, rent escalation, vacancy, and maintenance costs

### Pro Features (₹349 one-time)
- Export detailed PDF reports with charts and tables
- Save unlimited scenarios for comparison
- Advanced projections with comprehensive analytics
- Side-by-side scenario comparison

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room Database for local data persistence
- **Monetization**: Google Play Billing Library v6+ for in-app purchases
- **PDF Generation**: iText 7 for report generation
- **Charts**: MPAndroidChart for data visualization
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

## Project Structure

```
com.yourcompany.propertyroi/
├── billing/              # Billing integration and purchase management
├── data/
│   ├── database/        # Room database, DAOs
│   ├── models/          # Data models and entities
│   └── repository/      # Repository pattern implementation
├── ui/
│   ├── navigation/      # Navigation setup
│   ├── screens/         # Compose UI screens
│   ├── theme/           # Material 3 theming
│   └── viewmodels/      # ViewModels for each screen
└── utils/               # Calculator logic and PDF generation utilities
```

## Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17 or later
- Android SDK with API 34

### Building the Project

1. Clone the repository:
```bash
git clone https://github.com/zaheerabbas7892034214-ai/Property-ROI-Home-Loan-Planner-India-.git
```

2. Open the project in Android Studio

3. Sync Gradle files

4. Run the app on an emulator or physical device

### Configuration

The app uses Google Play Billing for in-app purchases. The product configuration is:
- **Product ID**: `roi_pro_unlock`
- **Price**: ₹349
- **Type**: One-time in-app purchase

Note: Billing functionality requires proper Play Console setup and app signing for testing.

## Key Components

### Calculators

#### EMI Calculator
Uses the standard EMI formula: `EMI = P × r × (1+r)^n / ((1+r)^n - 1)`
- P = Principal loan amount
- r = Monthly interest rate
- n = Loan tenure in months

#### ROI Projection
Calculates Internal Rate of Return (IRR) using Newton-Raphson method, considering:
- Property appreciation
- Rent escalation
- Vacancy rates
- Maintenance costs
- Initial stamp duty and registration charges

### Database Schema

**Scenarios Table**
- Stores user-created calculation scenarios
- JSON format for flexible input storage

**Entitlements Table**
- Tracks Pro purchase status
- Manages purchase tokens for verification

## Development Notes

- All monetary values use Indian Rupee (₹) formatting
- Calculations are performed with double precision
- PDF reports include cover page, summary tables, and projection timelines
- Offline-first architecture with Room database
- Reactive UI using Kotlin Flow and StateFlow

## License

Copyright © 2024. All rights reserved.

## Support

For issues and feature requests, please use the GitHub issue tracker.
