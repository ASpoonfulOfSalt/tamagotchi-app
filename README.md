# HabitGotchi 🐾
A simple, task-centered Android Tamagotchi app written in Kotlin.

---

**Tamagotchi Wellness Companion**

HabitGotchi is a mobile app that combines productivity, wellness, and gamification. Users interact with a virtual pet whose health, mood, and growth reflect the completion of real-world habits and tasks such as studying, exercising, or eating well. Completing tasks rewards users with coins, which can be spent on improving the pet’s environment and quality of life. The app supports automatic achievements via device sensors (e.g., step count) and provides analytics like weekly streak summaries.

---

## Features

* **Virtual Pet**: Care for a tamagotchi-like companion with dynamic mood and growth.
* **Task System**: Users can create tasks, track progress, and complete habits.
* **Rewards & Currency**: Coins earned through tasks can be spent on items for the pet.
* **Sensor Integration**: Automatically track steps and other metrics for achievements.
* **Analytics**: Weekly summaries, streaks, and productivity reports.
* **Customizable UI**: User-friendly interface built with Jetpack Compose.

---

## Skills and Technologies

* Android app development with **Kotlin** and **Jetpack Compose**
* **MVVM architecture**
* Database management (Room / Datastore)
* Git workflow and collaboration best practices
* UI/UX design and gamification
* Team collaboration and project management

---

## Project Structure

This project uses **MVVM architecture**:

```
/app
 ├─ /src/main/java/com/habitgotchi
 │   ├─ /model         # Data classes, database entities, domain logic
 │   ├─ /viewmodel     # ViewModels exposing state to UI
 │   ├─ /ui            # Composables and UI screens
 │   └─ /repository    # Interfaces between ViewModel and Model/Database/API
 ├─ /res               # App resources (images, layouts, strings)
```

---

## Git Workflow Guidelines

To keep the repository maintainable and professional, follow these rules:

### Branching

| Branch Type       | Purpose                                    |
| ----------------- | ------------------------------------------ |
| `main`            | Always deployable, stable version          |
| `feature/<name>`  | New features, small increments             |
| `bugfix/<name>`   | Fix bugs without destabilizing main        |
| `refactor/<name>` | Structural changes, code cleanup, renaming |

### Pull Requests

* Always create a PR from a **feature, bugfix, or refactor branch** to `main`.
* Include a clear description of changes, why they were made, and screenshots if applicable.
* PRs must pass review and local tests before merging.

### Commits

* Write **meaningful commit messages**, e.g.:

  * `feat: add step counter integration`
  * `fix: resolve crash when completing task`
  * `refactor: move Task data class to model package`
* Keep commits small and focused on a single logical change.

### MVVM Discipline

* **Model**: Only contains data structures, database entities, and business logic.
* **ViewModel**: Holds UI state and exposes `StateFlow`/`LiveData` for Composables. Should **not** reference Views.
* **View/UI**: Composables only consume ViewModel state and trigger actions via functions in the ViewModel.

### Testing & Stability

* The `main` branch must **always compile and run** on the emulator/device.
* Do not merge incomplete or broken features directly into `main`. 
* If the branch does not run, save your work, but do not merge.

---

## Setup

1. Clone the repository:

```bash
git clone https://github.com/ASpoonfulOfSalt/tamagotchi-app.git
```

2. Open in **Android Studio**.
3. Ensure the proper **Android SDK** is installed.
4. Run the app on an emulator or physical device.

---

## Contributing

1. Fork the repository and create a branch according to the type (`feature/`, `bugfix/`, `refactor/`).
2. Make your changes and **test locally**.
3. Commit your changes with a meaningful message.
4. Push your branch and create a pull request to `main`.
5. Wait for review and approval before merging.

---

## License

MIT License © 2025 HabitGotchi Team

---

