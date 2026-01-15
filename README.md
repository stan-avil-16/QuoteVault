# QuoteVault 🏛️

**🎨 Design Reference:** [Stitch Project Dashboard](https://drive.google.com/drive/folders/1RVF6veqMh0gIws39AnqdM8VjQoGUI72I?usp=sharing) | [App UI & Functionality Screenshots](https://drive.google.com/drive/folders/1_Yt8LW1nt51NJCA97eIktdw5coJQxlud?usp=sharing)

**QuoteVault** is a premium, full-featured mobile application designed for quote discovery, curation, and personalization. Built with a "Design-First" philosophy and powered by AI, the app offers a sophisticated editorial experience for inspiration seekers.

---

## 🚀 AI Coding Approach & Workflow
This project serves as a comprehensive demonstration of **AI-tool proficiency**, showcasing how effectively leveraging AI can accelerate high-quality software development.

### **1. AI-Driven Design**
Instead of standard stock UI components, the visual identity was established using **Stitch (by Google)** and **Figma Make**.
- **Process:** Prompted for a "Sophisticated dark-mode first aesthetic with Electric Violet accents and premium serif typography."
- **Implementation:** Design tokens (colors, typography scales) were extracted from AI generations and implemented as a custom **Jetpack Compose Design System**.

### **2. Rapid Infrastructure & Seeding**
- **Architecture Scaffolding:** Used **Claude Code** to architect the project using **Clean Architecture** principles, ensuring a clear separation between data, domain, and UI layers.
- **Supabase Integration:** AI assisted in generating the SQL schema and RLS (Row Level Security) policies for secure user data management.
- **Content Curation:** Leveraged AI to curate and format **100+ high-quality quotes** across categories (Motivation, Success, Wisdom, Love, Humor) for initial database seeding.

### **3. Complex Module Implementation**
- **Dynamic Image Generation:** AI logic was used to build the `QuoteShareDialog`, which uses Android's Canvas API to generate beautiful, shareable cards from quote data.
- **Background Tasks:** Implemented **WorkManager** logic via AI prompts to handle deterministic "Daily Quote" logic and local push notifications.

---

## 🛠️ Tech Stack & AI Tools
### **Development Stack**
- **Framework:** Kotlin + Jetpack Compose (100% Declarative UI)
- **Backend:** Supabase (Auth, Postgrest DB, Realtime)
- **Local Storage:** Jetpack DataStore (Preferences)
- **Background Work:** WorkManager (Scheduling & Notifications)
- **Widget:** Jetpack Compose Glance
- **Image Loading:** Coil

### **AI Tools Used**
- **Claude Code / Cursor:** Primary development assistants for logic, debugging, and refactoring.
- **Stitch:** For high-fidelity UI/UX design generation.
- **ChatGPT:** For data seeding and complex SQL query generation.

---

## ✨ Feature Requirements Checklist

### **1. Authentication & User Accounts**
- [x] Sign up with Email/Password.
- [x] Secure Login/Logout flow.
- [x] Professional Profile screen with 12 selectable avatar presets.
- [x] Secure Password Update flow (requires current password verification).
- [x] **Session Persistence:** Users stay logged in across app restarts.

### **2. Quote Browsing & Discovery**
- [x] High-performance home feed with editorial "Quote of the Day" hero.
- [x] Browse by 5+ categories (Motivation, Love, Success, Wisdom, Humor).
- [x] **Real-time Search:** Filter quotes by keyword or author instantly.
- [x] Pull-to-refresh data synchronization.
- [x] Graceful loading and empty states.

### **3. Favorites & Collections**
- [x] One-tap "Favorite" functionality with cloud sync.
- [x] Dedicated "Vault" screen for managing curated content.
- [x] Create and manage custom collections (e.g., "Morning Motivation").
- [x] Add/Remove quotes from collections with instant UI updates.

### **4. Daily Quote & Notifications**
- [x] "Quote of the Day" changes daily based on a date-seeded algorithm.
- [x] Local push notifications for the daily quote.
- [x] **Custom Schedule:** Set preferred notification time in settings.
- [x] Android 13+ permission handling.

### **5. Sharing & Export**
- [x] Share quote as plain text via system sheet.
- [x] **Generate Quote Cards:** Dynamically render quotes onto styled image backgrounds.
- [x] Save generated cards directly to device storage.

### **6. Personalization & Settings**
- [x] Dark Mode / Light mode support.
- [x] **Dynamic Scaling:** Real-time font size adjustment that affects the entire app typography.
- [x] Settings persist locally and sync to the user's Supabase profile.

### **7. Widget**
- [x] Home screen widget built with Glance.
- [x] Displays the current "Quote of the Day" clearly.
- [x] Tapping the widget launches the app directly.

---

## 📦 Setup Instructions

### **1. Supabase Backend**
1. Create a project at [supabase.com](https://supabase.com).
2. Enable **Email Auth**.
3. Create a `quotes` table and seed it with the provided dataset.
4. Set up `collections` and `favorites` tables linked to user IDs.

### **2. App Config**
1. Update `SupabaseConfig` in `data/SupabaseClient.kt` with your credentials.
2. Sync Project with Gradle files.
3. Deploy to an Android device or emulator.

---

Developed with ❤️ and **AI** by Stan Avil Dsouza
