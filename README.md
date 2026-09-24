# NutriTrack Pro

An Android app that turns a patient's dietary questionnaire and Healthy Eating Index (HEIFA) scores into personal insights and AI coaching, with a separate password-protected dashboard for clinicians. Built solo in Kotlin with Jetpack Compose for FIT2081 Mobile Application Development at Monash University Malaysia (Semester 1, 2025). The unit result was a High Distinction.

| | |
|---|---|
| **Author** | Ian Leong Zheng Yan |
| **Unit** | FIT2081 Mobile Application Development, Monash University Malaysia |
| **Period** | March to May 2025 (Assignments 1 and 3) |
| **Stack** | Kotlin 2.0, Jetpack Compose, Material 3, Room, Navigation Compose, Retrofit, Gemini API |

## What it does

The app ships with an anonymised CSV of patients and their HEIFA scores (fruit, vegetables, grains, dairy, water, fats, sodium, sugar, alcohol and so on). On first launch the CSV is loaded into a local Room database. From there:

1. **Register and log in.** A patient claims their record with their user ID and phone number, sets a name and password, and logs in. Passwords are stored as SHA-256 hashes, never in plain text. Sessions persist across restarts.
2. **Questionnaire.** Food preferences, eating persona, and meal, sleep and wake times, saved per patient.
3. **Home and Insights.** The patient's total HEIFA score and a per-category breakdown with progress bars, plus a share button that sends the score through any messaging app.
4. **NutriCoach.** Looks up any fruit's nutrition from the FruityVice REST API, and asks Google Gemini for a short motivational message tailored to the patient's fruit and vegetable scores. Every generated tip is stored and can be reviewed later.
5. **Settings and account recovery.** Change name or password, or recover them by confirming the phone number, with live password-strength feedback.
6. **Clinician dashboard.** A clinician logs in with a separate hashed key, sees average HEIFA scores split by sex and food group across all patients, and can ask Gemini for three data-driven observations returned as structured JSON.

## Architecture

Single-activity Compose app following MVVM with a repository layer over Room.

```
app/src/main/java/com/fit2081/ian_34423680/nutritrackpro_app/
├── MainActivity.kt / NutriTrackPro.kt   entry point; Application class seeds the DB on first run
├── navigation/    NavGraph.kt, ScreenRoute.kt         ten routes, one NavHost
├── screen/        Welcome, Login, Register, Questionnaire, Home, Insight,
│                  NutriCoach, Settings, ResetDetails, ClinicianDashboard
├── viewmodel/     one ViewModel per screen, state exposed as Compose State
├── factory/       ViewModel factories injecting repositories
├── repository/    Patient, Questionnaire, Insight, NutriCoachTips, ClinicianKey
├── dao/           Room DAOs
├── entity/        Patient, Questionnaire, NutriCoachTips, ClinicianKey, Fruit
├── database/      NutritrackDatabase.kt (Room, 4 tables)
└── utils/         CsvLoader, ClinicianKeyLoader, SessionManager,
                   SecurityUtils (SHA-256), FruitApi (Retrofit), PersonaData
```

| Concern | Choice |
|---|---|
| UI | Jetpack Compose, Material 3, Navigation Compose, Coil for images |
| State | ViewModel + Compose State, coroutines on Dispatchers.IO |
| Persistence | Room 2.6 with four entities, SharedPreferences for session and first-run flags |
| Networking | Retrofit 2 + Gson against https://fruityvice.com |
| AI | Google Generative AI SDK, model gemini-1.5-flash, prompts built from the patient's own scores |
| Security | SHA-256 password hashing, clinician key stored hashed in its own table, API key kept out of source |

## Setup

Requires Android Studio (AGP 8.10, Kotlin 2.0.21) and an emulator or device on API 35.

1. Clone the repo and open it in Android Studio.
2. Get a free Gemini API key from [Google AI Studio](https://aistudio.google.com/).
3. Copy `local.properties.example` to `local.properties` and set `GEMINI_API_KEY` (and `sdk.dir` if Android Studio has not written it). `local.properties` is git-ignored.
4. Build and run.

Log in with any user ID and phone number from `app/src/main/assets/nutritrackpro_app.csv`, then register a name and password. The clinician key is set on first run in `utils/ClinicianKeyLoader.kt`.

## Documents

- `docs/HD_Feature_List.pdf`: the five features submitted for the above-and-beyond marks (account recovery, self-service settings, password hashing, animated NutriCoach, clinician login with its own table).
- `docs/Screen_Mockup.pdf`: the screen design from Assignment 1.

Assignment 1 was a six-screen prototype of the same app without a database; this repository contains the final Assignment 3 version.

## Notes

- The Gemini prompts are written in a deliberately playful anime-nutritionist voice. That was a design choice for the assignment's target users, and it is a one-line change in `NutriCoachViewModel.kt` and `ClinicianDashboardViewModel.kt`.
- The patient CSV is the anonymised dataset supplied by the unit.
