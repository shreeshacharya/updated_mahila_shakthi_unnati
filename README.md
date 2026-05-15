# Mahila-Shakti Unnati

Mahila-Shakti Unnati is an Android application designed for Self-Help Groups (SHGs) to manage micro-finance records efficiently. Built with modern Android development practices, it provides a robust, offline-first solution for tracking member details, savings, and loans. The user interface is accessible and optimized for low-literacy users.

## Key Features

*   **Member Directory Management:** Maintain a comprehensive list of SHG members with strict 10-digit phone number validation.
*   **Savings & Loan Tracking:** Track weekly savings contributions and manage active loans with automated interest calculations and business validations.
*   **Offline Data Storage:** Built with a Room Database to ensure all data is safely stored locally and accessible without an internet connection.
*   **Payment Validation Logic:** Automatically categorize members' weekly payment status. Members are marked as "paid" if they contribute exactly 150 weekly; otherwise, they are categorized as "pending".
*   **Smart Filtering:** Utilize intuitive chip-based filtering on the member list to quickly identify individuals with pending weekly payments or active loans.
*   **Data Export & Reporting:** Generate detailed financial reports that include member-specific contact details and clear financial metrics.
*   **Accessible UI:** A simple, Jetpack Compose-based user interface tailored for ease of use.

## Tech Stack

*   **Language:** Kotlin
*   **UI Toolkit:** Jetpack Compose
*   **Architecture:** MVVM (Model-View-ViewModel)
*   **Local Database:** Room Database

## Getting Started

### Prerequisites

*   Android Studio (latest version recommended)
*   Android SDK

### Installation

1.  Clone the repository or download the source code.
2.  Open the project in Android Studio `c:\Users\Shreesha\AndroidStudioProjects\updatd_mahila_shakthi`.
3.  Sync the project with Gradle files.
4.  Build and run the application on an emulator or a connected physical Android device.

## Project Architecture

The project follows the MVVM architecture to ensure a clean separation of concerns:
*   **UI/View:** Jetpack Compose screens for presenting data and handling user interactions.
*   **ViewModel:** (`AuthViewModel`, `LoanViewModel`, `ReportViewModel`, etc.) to manage UI-related data in a lifecycle-conscious way.
*   **Model/Data:** Room Database entities (`Savings`, etc.) and Data Access Objects (DAOs) for robust local data persistence.
