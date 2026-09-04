# MySpotify

MySpotify is an Android application that uses the Spotify Web API to browse artists, albums, and songs.

The project was developed using Kotlin, Jetpack Compose, MVVM, Clean Architecture, Hilt, Coroutines, Flow, Retrofit, OkHttp, and Coil.

## Repository

GitHub repository:

https://github.com/nacho914/MySpotify

---

# Features

* Browse artists from Spotify.
* Browse albums for a selected artist.
* Browse songs for a selected album.
* Spotify OAuth 2.0 authentication using Authorization Code with PKCE.
* Automatic access token refresh.
* Pagination using Spotify's `limit` and `offset` parameters.
* Image loading with Coil.
* Loading, empty, and error states.
* Navigation between artists, albums, and songs.
* Unit tests for ViewModels.
* Compose UI tests for the main screens.

---

# Requirements

To run the application, you need:

* A computer running macOS, Windows, or Linux.
* Android Studio.
* Android SDK.
* A compatible JDK.
* An Android emulator or physical Android device.
* A Spotify account.
* A Spotify Developer application.
* Internet access.

No previous Android development experience is required. The steps below describe how to set up the environment from scratch.

---

# 1. Install Android Studio

Download and install Android Studio from the official Android developer website.

During the installation, allow Android Studio to install the recommended components, including:

* Android SDK
* Android SDK Platform
* Android SDK Build-Tools
* Android Emulator
* Android SDK Platform-Tools

After installation, open Android Studio and allow the initial setup to complete.

---

# 2. Configure the JDK

Android Studio includes a JDK that can be used by Gradle.

Open Android Studio and go to:

**Settings/Preferences → Build, Execution, Deployment → Build Tools → Gradle**

Under **Gradle JDK**, select the JDK bundled with Android Studio if it is available.

Using the JDK configured by Android Studio is recommended instead of manually configuring a separate Java installation.

---

# 3. Install an Android SDK

In Android Studio, open:

**Settings/Preferences → Languages & Frameworks → Android SDK**

Make sure an Android SDK platform is installed.

Also verify that the following components are installed:

* Android SDK Platform
* Android SDK Build-Tools
* Android SDK Platform-Tools
* Android Emulator

Click **Apply** if Android Studio needs to install any missing components.

---

# 4. Clone the Repository

Open a terminal and run:

```bash
git clone https://github.com/nacho914/MySpotify.git
```

Then enter the project directory:

```bash
cd MySpotify
```

The project can also be downloaded directly from GitHub as a ZIP file.

---

# 5. Open the Project in Android Studio

1. Open Android Studio.
2. Select **Open**.
3. Select the `MySpotify` directory.
4. Wait for Android Studio to import the project.
5. Wait for Gradle synchronization to finish.
6. If Android Studio asks to install missing SDK components, allow it to do so.

The first Gradle synchronization may take several minutes because Gradle needs to download the project's dependencies.

The project uses Gradle Version Catalogs for dependency management.

---

# 6. Create a Spotify Developer Application

MySpotify uses the Spotify Web API, so a Spotify Developer application is required.

Sign in to the Spotify Developer Dashboard with your Spotify account and create a new application.

After creating the application, Spotify provides a **Client ID**.

The Client ID is required by MySpotify to start the authentication process.

---

# 7. Configure the Spotify Redirect URI

The application uses the following redirect URI:

```text
myspotify://callback
```

Add this exact Redirect URI to the Spotify Developer application.

The value must match exactly.

```text
myspotify://callback
```

Do not change:

* `myspotify`
* `://`
* `callback`

If the Redirect URI configured in Spotify does not match the application configuration, the authentication callback will not work correctly.

---

# 8. Configure Spotify Credentials

The Spotify Client ID must be provided through the application's local configuration.

Do not commit private credentials, access tokens, refresh tokens, or other secrets to Git.

The repository should never contain:

* Access tokens.
* Refresh tokens.
* Client secrets.
* Personal Spotify credentials.

If credentials are accidentally committed to a public repository, they should be revoked and replaced immediately.

---

# 9. Configure an Android Emulator

If you do not have an Android phone available, Android Studio can run the application using an emulator.

Open:

**Android Studio → Device Manager**

Then:

1. Select **Create Device**.
2. Select an Android phone model.
3. Select an available Android system image.
4. Download the system image if necessary.
5. Complete the device creation.
6. Start the emulator.

Wait until Android has completely booted before running the application.

---

# 10. Using a Physical Android Device

A physical Android device can also be used.

On the Android device:

1. Open **Settings**.
2. Enable **Developer Options**.
3. Enable **USB Debugging**.
4. Connect the device to the computer using USB.
5. Accept the debugging authorization prompt on the device.

The device should then appear in Android Studio's device selector.

---

# 11. Run the Application

Once the project is synchronized and an Android device or emulator is running:

1. Select the device from Android Studio's device selector.
2. Select the `app` run configuration.
3. Press **Run**.

Android Studio will:

1. Compile the project.
2. Install the application.
3. Start MySpotify on the selected device.

---

# Authentication

MySpotify uses Spotify OAuth 2.0 with Authorization Code and PKCE.

The authentication flow is:

```text
┌──────────────┐
│  MySpotify   │
└──────┬───────┘
       │
       │ Authorization request
       ▼
┌──────────────┐
│   Spotify    │
└──────┬───────┘
       │
       │ Authorization code
       ▼
┌──────────────┐
│  MySpotify   │
│   Callback   │
└──────┬───────┘
       │
       │ Code + PKCE verifier
       ▼
┌──────────────┐
│   Spotify    │
└──────┬───────┘
       │
       │ Access token
       │ Refresh token
       ▼
┌──────────────┐
│  MySpotify   │
└──────────────┘
```

The application uses the access token to authenticate Spotify Web API requests.

When an access token expires, the application uses the refresh token to obtain a new access token without requiring the user to authenticate again.

---

# Application Flow

The application consists of three main browsing screens:

```text
Artists
   │
   │ artistId
   ▼
Albums
   │
   │ albumId
   ▼
Songs
```

## Artists

The first screen searches Spotify for artists.

Endpoint:

```text
GET /v1/search
```

The request specifies:

```text
type=artist
```

Selecting an artist navigates to the artist's albums.

## Albums

The album screen retrieves albums for the selected artist.

Endpoint:

```text
GET /v1/artists/{artistId}/albums
```

Selecting an album navigates to the album's songs.

## Songs

The song screen retrieves tracks belonging to the selected album.

Endpoint:

```text
GET /v1/albums/{albumId}/tracks
```

---

# Pagination

Artists, albums, and songs use Spotify's pagination parameters:

* `limit`
* `offset`

The application loads a page of results and requests additional data as the user approaches the end of the current list.

The Compose `LazyColumn` observes the scroll position and triggers another request when the user gets close to the end of the currently loaded results.

---

# Architecture

The project follows **Clean Architecture with MVVM**.

```text
┌─────────────────────────────┐
│       Jetpack Compose       │
│             UI              │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│          ViewModel          │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│          Use Case           │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│         Repository          │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│       Spotify API           │
│    Retrofit / OkHttp        │
└─────────────────────────────┘
```

## Presentation Layer

The presentation layer contains:

* Jetpack Compose screens.
* UI state models.
* ViewModels.
* Navigation.

ViewModels expose UI state through `StateFlow`.

Asynchronous operations are handled using Kotlin Coroutines and Flow.

## Domain Layer

The domain layer contains:

* Domain models.
* Repository interfaces.
* Use cases.

The domain layer does not depend directly on Retrofit, OkHttp, or Spotify-specific networking implementations.

## Data Layer

The data layer contains:

* Repository implementations.
* Retrofit API services.
* Spotify API response models.
* Authentication components.
* Token management.

---

# Dependency Injection

Hilt is used for dependency injection.

It provides dependencies such as:

* Retrofit services.
* Repositories.
* Use cases.
* Authentication components.
* ViewModels.

This reduces coupling between components and makes the application easier to test.

---

# Project Structure

```text
com.vic.android.myspotify
│
├── data
│   ├── model
│   ├── network
│   └── repository
│
├── domain
│   ├── model
│   ├── repository
│   └── usecase
│
├── di
│
├── navigation
│
└── ui
    ├── artistscreen
    ├── albumscreen
    └── songscreen
```

---

# Technology Stack

* Kotlin
* Jetpack Compose
* Material 3
* MVVM
* Clean Architecture
* Kotlin Coroutines
* Flow
* StateFlow
* Hilt
* Retrofit
* OkHttp
* Gson
* Coil
* Navigation Compose
* JUnit
* MockK
* Compose UI Testing

---

# Testing

The project contains both unit tests and Compose UI tests.

## Unit Tests

The ViewModels are tested independently from Android UI components and the Spotify API.

The tests cover:

* Successful data loading.
* Loading additional pages.
* Appending subsequent pages.
* Preventing multiple simultaneous loading requests.
* Error handling.

`MockK` is used to mock dependencies.

`kotlinx-coroutines-test` is used to control coroutine execution during tests.

Run the unit tests with:

```bash
./gradlew test
```

---

# UI Tests

The main Compose screens contain UI tests.

## Artists

The tests verify:

* Artists are displayed.
* Clicking an artist invokes the expected callback.

## Albums

The tests verify:

* Albums are displayed.
* Clicking an album invokes the expected callback.

## Songs

The tests verify:

* Songs are displayed.
* Song durations are displayed using the expected format.

To run the Compose UI tests, start an Android emulator or connect a physical Android device and run:

```bash
./gradlew connectedDebugAndroidTest
```

The UI tests can also be executed directly from Android Studio.

---

# Running the Complete Test Suite

Run unit tests:

```bash
./gradlew test
```

Run Compose UI tests:

```bash
./gradlew connectedDebugAndroidTest
```

Before submitting the project, run both test suites and verify that the application builds and tests successfully.

---

# Troubleshooting

## Gradle Synchronization Fails

If Gradle synchronization fails:

1. Verify that Android Studio is correctly installed.
2. Verify the Gradle JDK configuration.
3. Verify that the required Android SDK components are installed.
4. Verify your internet connection.
5. Wait for Gradle to finish downloading all dependencies.

You can also try:

```bash
./gradlew clean
```

Then synchronize the project again from Android Studio.

## Spotify Authentication Fails

Verify that:

1. A Spotify Developer application has been created.
2. The Client ID is correctly configured.
3. The Redirect URI is exactly:

```text
myspotify://callback
```

4. The Spotify account being used can access the application.
5. The credentials are valid.
6. The application has an internet connection.

## The Application Cannot Connect to Spotify

Check:

* Internet connectivity.
* Spotify API availability.
* Authentication status.
* Android Studio Logcat for additional error information.

---

# Security

Never commit sensitive authentication information to the repository.

Do not commit:

* Spotify access tokens.
* Spotify refresh tokens.
* Client secrets.
* Passwords.
* Other private credentials.

If a credential is accidentally committed to Git, revoke it and replace it before making the repository public.

---

# Design Decisions

## Clean Architecture

Clean Architecture was used to separate presentation, business logic, and data access responsibilities.

This allows the domain layer to remain independent from Android UI and networking implementation details.

## MVVM

ViewModels handle UI-related state and business flow while keeping composables focused on rendering the UI.

## StateFlow

`StateFlow` provides a single observable source of UI state for each screen.

This makes state changes explicit and allows Compose to react to state updates.

## Repository Pattern

Repositories abstract data access from the domain layer.

Use cases interact with repository interfaces rather than directly depending on Retrofit or Spotify API implementations.

## Dependency Injection

Hilt provides dependencies throughout the application and reduces direct coupling between components.

It also makes ViewModels easier to test because their dependencies can be replaced with mocks.

## Coroutines and Flow

Kotlin Coroutines are used for asynchronous operations.

Flow is used to represent asynchronous data streams between the data, domain, and presentation layers.

## Jetpack Compose

Jetpack Compose provides a declarative UI approach and allows the main screens to be tested independently from networking and navigation.

---

# Conclusion

MySpotify demonstrates an Android application built around modern Android development practices, including:

* Kotlin.
* Jetpack Compose.
* MVVM.
* Clean Architecture.
* Dependency Injection.
* Coroutines and Flow.
* REST API integration.
* Spotify OAuth 2.0 with PKCE.
* Pagination.
* Unit testing.
* Compose UI testing.
* Separation of concerns.
* Maintainable and testable code.
