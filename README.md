# Artwork Price Estimator (Android)

An Android app (Jetpack Compose) that analyzes an uploaded artwork image with OpenAI Vision and estimates:

- artwork type (`traditional` or `digital`)
- artist talent level (1-10)
- estimated artist experience (years)
- estimated fair price in USD
- short reasoning

## Features

- Upload artwork image from device
- Enter your own OpenAI API key
- Secure API key storage with `EncryptedSharedPreferences`
- AI-based visual analysis and pricing estimate
- Clean architecture (`ui`, `data`, `domain`)

## Project Structure

- `app/src/main/java/com/mikusz3/artworkpriceestimator/ui`
  - Compose UI, screen state, ViewModel
- `app/src/main/java/com/mikusz3/artworkpriceestimator/domain`
  - Models, repository contract, use-cases
- `app/src/main/java/com/mikusz3/artworkpriceestimator/data`
  - Encrypted local key storage, OpenAI remote source, parser, repository implementation

## Requirements

- Android Studio (latest stable recommended)
- Android SDK installed
- JDK configured (`JAVA_HOME` or Android Studio Gradle JDK)
- OpenAI API key

## Setup

1. Open the project in Android Studio.
2. Let Gradle sync finish.
3. Run the app on an emulator or device.
4. In the app:
   - paste your OpenAI API key
   - upload an artwork image
   - tap **Analyze and Estimate Price**

## Build and Test

From project root:

```bash
./gradlew test
./gradlew connectedAndroidTest
```

## Notes

- Image analysis and pricing are AI estimates and can vary.
- Pricing is a heuristic output, not financial advice or a guaranteed market value.
- API usage incurs costs based on your OpenAI account and selected model.
