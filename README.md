[简体中文](README_cn.md)

# Tore

Tore is a workout journal for strength training and cardio.

The name comes from the Japanese term `筋トレ`, short for `筋肉トレーニング`, meaning strength training. Tore helps you build a steady routine, record each session, and review your training balance over time.

## Features

- Log resistance and cardio sessions
- **Calendar history**: browse completed workouts by date and open each session
- **Exercise progress**: review best sets, estimated 1RM, and total volume over time
- Build plans with training days, rest days, and custom day titles
- **Flexible workout editing**: add multiple exercises, including exercises outside the current plan
- Import and export plans as JSON
- Organize and filter exercises by body part, muscle group, and equipment
- **Weekly summary**: see this week's training and today's planned exercises on Home
- Track consistency with heatmaps, trends, balance, and adherence statistics
- Record body weight and review progress over time
- **Health Connect sync**: optionally sync completed workouts
- **Color palettes**: choose a palette alongside light and dark themes
- Use an inline workout timer with pause and resume
- Delete data through explicit confirmation dialogs
- Back up the complete database or export selected training data
- English and Simplified Chinese localization

## Tech stack

- Kotlin 2.2.x
- Jetpack Compose and Material 3 Expressive
- Hilt for dependency injection
- Room and KSP for local persistence
- WorkManager for scheduled backups
- Navigation Compose with serializable routes

## Build

```bash
# Build a debug APK
./gradlew assembleDebug

# Build and install on a connected device or emulator
./gradlew installDebug

# Run unit tests
./gradlew test

# Run instrumented tests on a connected device or emulator
./gradlew connectedAndroidTest
```

The `scripts/` directory contains non-interactive wrappers for common Windows and Unix build commands.

## Release

GitHub Actions prepares each release by updating the version, changelog, and Fastlane metadata, then opening a `release/<version>` pull request. Merging that pull request into `main` builds and signs the APK and AAB, then creates a GitHub Release draft with both files attached. A maintainer publishes the draft from GitHub Releases. This project does not upload to Google Play.

## License

Tore is free software licensed under the GNU General Public License, version 3 or later.

See [LICENSE](LICENSE) for the full license text.

## Changelog

- [Full changelog](CHANGELOG.md)
- [Unreleased changes](CHANGELOG.md#unreleased)
