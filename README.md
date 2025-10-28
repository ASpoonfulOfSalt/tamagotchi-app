# HabitGotchi 🐾  
A wellness-based Tamagotchi habit tracker built in Kotlin with Jetpack Compose.

---

## Overview

**HabitGotchi** is a gamified productivity app that motivates users to maintain healthy habits through the care of a virtual pet.  
Completing real-world tasks keeps your pet happy, healthy, and hydrated — while earning coins that can be used to buy in-game items.

All data is stored locally using **Room** and **DataStore**, ensuring full offline functionality.  
HabitGotchi is built using the **MVVM architecture**, following clean separation of concerns for maintainability and scalability.

---

## ✨ Current MVP Features

* **Virtual Pet System**  
  Feed, play with, and hydrate your Tamagotchi. Its stats (hunger, happiness, hydration) persist and naturally decay over time.

* **Dynamic Task System**  
  Daily and weekly tasks are loaded from a JSON file and stored in a local Room database. Completing them rewards currency.

* **Reward & Shop System**  
  Earn coins through tasks and purchase cosmetic or interactive items from the in-game store.

* **Local Data Persistence**  
  Uses Room (for structured data) and DataStore (for user preferences like reset times).

* **Simple Navigation**  
  Five main screens accessible via bottom navigation and horizontal pager:
  - Store 🛒  
  - Inventory 🎒  
  - Home 🏠 (Pet view)  
  - Tasks ✅  
  - Settings ⚙️  

* **Settings & Preferences**  
  Includes app settings such as adjustable **music and sound effect volume sliders**.

---

## 🧠 Technologies Used

* **Kotlin**
* **Jetpack Compose**
* **Room (Local Database)**
* **DataStore (Preferences)**
* **MVVM Architecture**
* **Android ViewModel & StateFlow**
* **Coroutine-based Asynchronous Operations**

---

## 🧩 Project Structure

The app follows **MVVM** architecture for clean separation of UI, logic, and data layers.

```

// com.cse.tamagotchi
│
├── model/                 # Entities (Task, Tamagotchi, StoreItem)
├── data/                  # Room database & DAOs
├── repository/            # Repositories for Tamagotchi, Tasks, Store, Preferences
├── viewmodel/             # ViewModels + Factories (TaskViewModelFactory, etc.)
├── ui/                    # Jetpack Compose screens (Home, Task, Store, etc.)
├── ui/navigation/         # AppNavRoot with pager + navigation
└── assets/                # tasks.json (Task definitions)

```

---

## 🔧 Setup & Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/ASpoonfulOfSalt/tamagotchi-app.git
   ```

2. **Open in Android Studio**
3. Install Android SDK **34+**
4. Build & run on an emulator or physical Android device.
5. JSON seeding: ensure `tasks.json` exists in `src/main/assets`.

---

## 👨‍💻 Development Workflow

**Branching Guidelines**

| Branch Type       | Purpose                         |
| ----------------- | ------------------------------- |
| `main`            | Always runnable, stable version |
| `feature/<name>`  | Add new features                |
| `bugfix/<name>`   | Fix specific issues             |
| `refactor/<name>` | Code cleanup, restructuring     |
| `chore/<name>` | Small misc changes unworthy of the other branches | 
| `testing/<name>` | A secondary branch used for PRing unfinished changes that shouldn't be added to main | 

**Pull Requests**

* PRs must describe changes clearly and include screenshots if UI-related.
* Only merge **tested and compiling** code into `main`.

**Commits**

* Use descriptive messages:

  * `feat: add volume slider to settings screen`
  * `fix: crash when resetting daily tasks`
 
---

## 📦 MVP Deliverables

- ✅ Persistent Tamagotchi system (decay, actions)
- ✅ Daily and weekly habit tracking via JSON seeding
- ✅ Local Room + DataStore integration
- ✅ Store and inventory system
- ✅ Compose UI and MVVM architecture
- 🟡 Upcoming: Volume slider, polish settings page
- ⚪ Future: Step tracking, analytics, achievements

---

## 👥 Team Contributions

**Daniel Nelson – Lead Software Engineer**  
Developed the core application framework and app loop, implemented major MVVM components, fixed critical bugs, and ensured adherence to Android best practices and functionality standards.

**John Hudson – Project Manager & Developer**  
Oversaw project milestones and coordinated development efforts. Contributed significantly to application logic and UI, including the Store and Inventory systems, while ensuring feature integration across the app.

**Karol Espiritu – UI/UX Developer & Documentation Lead**  
Designed and implemented numerous UI components and quality-of-life improvements. Authored most of the project’s documentation and created various placeholder art assets.

**Angel Hernandez – Assistant Project Manager & Art Designer**  
Contributed to project coordination, documentation writing and editing, and task management. Produced a large portion of the app’s current artwork and assisted in visual design consistency.

---

## 📜 License

MIT License © 2025 HabitGotchi Development Team
