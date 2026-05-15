# Mahila-Shakti Unnati

## Problem Statement
Self-Help Groups (SHGs) often struggle with manual, paper-based record-keeping for their micro-finance activities. This leads to inaccuracies, difficulties in tracking loans, savings, and member details, especially in environments with limited internet connectivity and varying levels of digital literacy. The need is for an offline-first, intuitive, and accessible digital solution tailored for SHGs to manage their financial operations efficiently.

## Features
* **Member Directory Management:** Easily manage the SHG member list with strict 10-digit phone number validation to ensure accuracy.
* **Savings & Loan Tracking:** Track weekly savings (₹150 standard) and active loans with automated interest calculations and business logic validations.
* **Smart Filtering:** Quickly identify members with pending weekly payments or active loans using intuitive chip-based filters.
* **Offline Data Storage:** Securely store all data locally using Room Database, allowing full app functionality without an internet connection.
* **Payment Validation Logic:** Automatically updates payment status based on exact contributions, marking members as "paid" or "pending".
* **Data Export & Reporting:** Generate comprehensive financial reports detailing member contact information and key financial metrics.
* **Accessible UI:** A simple, clean user interface designed with Jetpack Compose specifically for users with varying levels of tech proficiency.

## Tech Stack
* **Language:** Kotlin
* **UI Toolkit:** Jetpack Compose
* **Architecture:** MVVM (Model-View-ViewModel)
* **Local Database:** Room Database

## Installation Steps
1. Ensure you have the latest version of [Android Studio](https://developer.android.com/studio) installed.
2. Clone this repository or download the source code zip file.
3. Open Android Studio and select **Open an existing Project**.
4. Navigate to the project directory (`c:\Users\Shreesha\AndroidStudioProjects\updatd_mahila_shakthi`) and open it.
5. Wait for Android Studio to sync the project with Gradle files.

## Run Command
You can run the app directly from Android Studio using the Run button (Shift + F10), or via the terminal using the following Gradle wrapper command:
```bash
./gradlew assembleDebug
```
To install it on a connected device/emulator:
```bash
./gradlew installDebug
```


## Folder Structure
```
app/src/main/java/com/example/updatd_mahila_shakthi/
│
├── data/               # Room Database entities, DAOs, and Repositories
├── navigation/         # Jetpack Compose navigation graph and routes
├── ui/                 # UI components and feature-specific screens
│   ├── components/     # Reusable composables (e.g., CommonComponents.kt)
│   └── screens/        # Main screens (Home, Members, Loans, etc.)
├── utils/              # Helper functions and utilities (ShareUtils, Validators)
├── viewmodel/          # ViewModels managing UI state and logic
│
├── MainActivity.kt     # Entry point of the application
└── MahilaShaktiApp.kt  # Compose Application level setup
```

## Future Improvements
* **Cloud Syncing:** Introduce an optional cloud backup feature to sync data when an internet connection is available, preventing data loss if the device is damaged.
* **Multi-Language Support:** Expand accessibility by adding regional language options (e.g., Hindi, Kannada) for better localized user adoption.
* **SMS Integration:** Send automated SMS reminders to members for upcoming loan installments or pending weekly contributions.
* **Advanced Analytics:** Provide visual charts and graphs for the group's overall financial health over time.
