# Fitman Native v2.0 — Liquid Glass Edition

Ultra-premium, futuristic iOS 45-inspired Liquid Glass & Obsidian Black Glassmorphism Android application for discovering games on the official Steam catalogue.

---

## Visual Architecture & Design Language

- **Obsidian Dark Palette**: Strict `#050608` deep obsidian canvas with `#090C12` secondary backdrops.
- **Multi-Layer Liquid Glass**: Translucent graphite surfaces (`Color(0x990A0E17)`), specular top glints, and hairline border refraction (`Color(0x1FFFFFFF)`).
- **Subtle Electric Lime Accent**: `#B7FF6A` highlights for badges, selection indicators, price tags, and primary CTAs.
- **Continuous Curved Squircles**: 18.dp to 32.dp continuous squircle radii matching modern Apple OS interface standards.
- **Tactile Micro-Interactions**: Spring-based physics scaling on button and card press gestures.

---

## Features

1. **Game Discovery Dashboard (`DiscoverScreen`)**:
   - Cinematic Hero Spotlight with multi-stop obsidian gradient overlay.
   - Segmented categories: *Discover*, *Top Sellers*, *New Releases*, and *Specials*.
   - Floating liquid glass price tags and pagination controls.
2. **Futuristic Glass Search (`SearchScreen`)**:
   - Instant live search directly across Steam's public store catalogue.
   - Search term history caching with one-tap search and clear history.
   - Curated trending suggestions chips.
   - Dedicated liquid glass loading, empty, and retry error states.
3. **Cinematic Game Details (`GameDetailScreen`)**:
   - Full-bleed backdrop game artwork with smooth gradient readability mask.
   - Floating action header with Back navigation and Favorite bookmark toggle.
   - Structured glass cards for Developer, Publisher, Release Date, Store Price, and Genre tags.
   - Functional **"OPEN OFFICIAL STEAM STORE"** button launching the verified Steam page.
4. **Offline Library & Bookmarks (`LibraryScreen`)**:
   - Local storage of favorite titles using persistent JSON repository.
   - Real-time bookmark count badge and quick remove action.
   - Direct link to Explore when empty.
5. **System & Visual Engine Settings (`SettingsScreen`)**:
   - Specular Edge Lighting toggle.
   - Liquid Glass Blur engine toggle.
   - Steam Store currency and regional catalogue picker (US, UK, EU, JP).
   - One-tap disk artwork cache clearing with memory purge.

---

## Architecture

```
com.fitman.nativeapp/
├── MainActivity.kt               # Edge-to-edge coordinator, back handling & intent launcher
├── data/
│   ├── model/                   # Game, GameDetail models
│   ├── remote/                  # SteamApi (search, featured, app details endpoints)
│   └── repository/              # FavoritesRepository, SettingsRepository
├── ui/
│   ├── components/              # 10 Reusable Liquid Glass components
│   │   ├── LiquidGlassCard.kt
│   │   ├── LiquidGlassButton.kt
│   │   ├── LiquidGlassSearchBar.kt
│   │   ├── LiquidGlassNavigationBar.kt
│   │   ├── LiquidGlassTab.kt
│   │   ├── LiquidGlassChip.kt
│   │   ├── LiquidGlassIconButton.kt
│   │   ├── LiquidGlassDialog.kt
│   │   ├── LiquidGlassSheet.kt
│   │   └── LiquidGlassLoadingIndicator.kt
│   ├── navigation/              # NavDestination
│   ├── screens/
│   │   ├── discover/            # DiscoverScreen, HeroSection
│   │   ├── search/              # SearchScreen
│   │   ├── details/             # GameDetailScreen
│   │   ├── library/             # LibraryScreen
│   │   └── settings/            # SettingsScreen
│   └── theme/                   # FitmanColors, FitmanTypography, FitmanTheme
```

---

## Build & APK Generation

- Built with **Kotlin 2.0.21**, **Android Gradle Plugin 8.7.3**, and **Jetpack Compose 2024.12.01 BOM**.
- Minimum SDK: 23 (Android 6.0+) | Target SDK: 35 (Android 15+).
- GitHub Actions CI/CD automatically compiles, packages, and releases `Fitman-v2.0-LiquidGlass.apk` on every push to `main`.
