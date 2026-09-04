# MySpotify

MySpotify is an Android application built with Kotlin and Jetpack Compose that consumes the Spotify Web API to display artists, albums, and songs.

The application implements Spotify OAuth 2.0 Authorization Code with PKCE, token persistence and refresh, pagination, MVVM, Clean Architecture, dependency injection, and automated tests.

## Features

* Spotify authentication using OAuth 2.0 Authorization Code with PKCE
* Automatic access token refresh
* Search and display Spotify artists
* Browse albums for a selected artist
* Browse songs for a selected album
* Infinite scrolling / pagination
* Album and artist images loaded from Spotify
* Song duration formatting
* Loading, empty, and error states
* Jetpack Compose UI
* MVVM architecture
* Clean Architecture
* Dependency Injection with Hilt
* Coroutines and Kotlin Flow
* Retrofit for networking
* OkHttp for HTTP communication
* Coil for image loading
* Unit tests for ViewModels
* UI tests for Compose screens

## Application Flow

```text
Spotify Authentication
        |
        v
   Artist List
        |
        | Select artist
        v
    Album List
        |
        | Select album
        v
    Song List
```

Each list supports pagination and loads additional content as the user approaches the end of the list.

## Screens

### Artists

The first screen displays artists retrieved from the Spotify Web API.

Each artist contains:

* Artist name
* Artist image

Selecting an artist navigates to the artist's albums.

### Albums

The album screen displays albums belonging to the selected artist.

Each album contains:

* Album name
* Album image

Selecting an album navigates to its songs.

### Songs

The song screen displays the tracks contained in the selected album.

Each song contains:

* Song name
* Duration

Song duration is displayed in the `minutes:seconds` format.

---

# Requirements

Before running the application, install the following:

* Android Studio
* JDK 17
* Android SDK 37
* A physical Android device or Android Emulator
* A Spotify account
* A Spotify Developer application
* Internet connection

The project currently uses:

* Android Gradle Plugin: `9.4.0`
* Gradle: `9.6`
* Kotlin: `2.2.10`
* Java source/target compatibility: `11`
* Compile SDK: `37`
* Target SDK: `37`
* Minimum SDK: `24`

AGP 9.4 requires JDK 17 to run Gradle and supports API level 37. The project itself uses Java 11 source/target compatibility, which is different from the JDK used to execute Gradle.

---

# 1. Install Android Studio

Download and install Android Studio from the official Android developer website.

During installation, allow Android Studio to install the recommended Android SDK components.

After opening Android Studio, make sure Android SDK 37 is installed.

You can check this from:

```text
Android Studio
    > Settings
    > Languages & Frameworks
    > Android SDK
```

On macOS:

```text
Android Studio
    > Settings
    > Languages & Frameworks
    > Android SDK
```

Make sure the following are installed:

```text
Android SDK Platform 37
Android SDK Build-Tools
Android SDK Platform-Tools
Android Emulator
```

---

# 2. Configure the JDK

This project uses Android Gradle Plugin 9.4.0.

Gradle must run using JDK 17.

In Android Studio, open:

```text
Settings
    > Build, Execution, Deployment
    > Build Tools
    > Gradle
```

Set:

```text
Gradle JDK: JDK 17
```

Android Studio's Gradle JDK is the JDK used to run the Gradle build. AGP 9.4 requires JDK 17.

Do not confuse this with the project's Java source compatibility.

The project uses:

```kotlin
sourceCompatibility = JavaVersion.VERSION_11
targetCompatibility = JavaVersion.VERSION_11
```

This means the application source is compiled with Java 11 compatibility, while Gradle itself runs using JDK 17.

---

# 3. Clone the Repository

Clone the project from GitHub:

[MySpotify GitHub Repository](https://github.com/nacho914/MySpotify?utm_source=chatgpt.com)

Using Git:

```bash
git clone https://github.com/nacho914/MySpotify.git
```

Then enter the project directory:

```bash
cd MySpotify
```

Open the project using Android Studio.

Android Studio should automatically detect the Gradle project and start a Gradle sync.

Wait for the synchronization to finish before running the application.

---

# 4. Create a Spotify Developer Application

MySpotify uses the Spotify Web API, so a Spotify Developer application is required.

Open the Spotify Developer Dashboard:

[Spotify for Developers](https://developer.spotify.com/?utm_source=chatgpt.com)

Sign in using your Spotify account.

Create a new application from the Developer Dashboard.

Spotify applications provide the credentials required for Web API authorization.

---

# 5. Configure the Redirect URI

The application uses a custom Android URI scheme for the OAuth callback.

The redirect URI used by this project is:

```text
myspotify://callback
```

This value must be configured exactly in the Spotify Developer Dashboard.

In your Spotify application settings, add:

```text
myspotify://callback
```

The URI must match the value used by the Android application.

The AndroidManifest registers the same scheme and host:

```xml
<data
    android:scheme="myspotify"
    android:host="callback" />
```

If the redirect URI does not match, Spotify authentication will not return correctly to the Android application.

---

# 6. Configure the Spotify Client ID

The application requires a Spotify Client ID.

The Client ID is configured in:

```text
app/src/main/res/values/strings.xml
```

Find:

```xml
<string name="spotify_client_id">YOUR_SPOTIFY_CLIENT_ID</string>
```

Replace `YOUR_SPOTIFY_CLIENT_ID` with the Client ID from your Spotify Developer application.

For example:

```xml
<string name="spotify_client_id">your_client_id_here</string>
```

The Client ID is an application identifier. It is not equivalent to a Client Secret or an access/refresh token.

Do not place a Spotify Client Secret, access token, or refresh token in the source code.

---

# 7. Spotify Development Mode

Spotify applications may run in Development Mode.

Development Mode has restrictions on who can use the application and on API usage. Spotify's current Development Mode requirements include a Spotify Premium requirement for the developer and restrictions on authorized users.

If authentication fails even though the Client ID and redirect URI are correct, check the Spotify Developer Dashboard and make sure the Spotify account being used for testing is authorized for the application.

For a different developer evaluating the project, there are two options:

1. Use their own Spotify Developer application and replace the Client ID in `strings.xml`.
2. Use an account that is authorized by the application owner, when permitted by Spotify's Development Mode configuration.

Spotify's Web API documentation should be consulted for the latest Development Mode requirements because these restrictions can change.

---

# 8. Run the Application

After configuring the Spotify Client ID:

1. Open the project in Android Studio.
2. Wait for Gradle synchronization to complete.
3. Connect an Android device or create an Android Emulator.
4. Select the `app` run configuration.
5. Press **Run**.

The application should start with the Spotify authentication flow.

If authentication is successful, the application will display the artist list.

---

# Authentication Flow

The application uses OAuth 2.0 Authorization Code with PKCE.

The high-level flow is:

```text
Android Application
        |
        | Authorization request
        v
Spotify Authorization
        |
        | Redirect
        v
myspotify://callback
        |
        v
Android Application
        |
        | Authorization code
        v
Spotify Token Endpoint
        |
        | Access token + Refresh token
        v
Spotify Web API
```

The application does not use the deprecated Implicit Grant flow.

The access token is used to authenticate Spotify Web API requests.

When the access token expires, the application uses the refresh token to obtain a new access token.

---

# API Endpoints

The application currently uses the following Spotify Web API endpoints.

## Search Artists

```text
GET /v1/search
```

The application searches for artists and uses the returned artist data to populate the first screen.

The request includes:

```text
type=artist
limit=10
offset=<current offset>
```

## Get Artist Albums

```text
GET /v1/artists/{artistId}/albums
```

The selected artist ID is used to retrieve the artist's albums.

The request includes:

```text
limit=10
offset=<current offset>
```

## Get Album Tracks

```text
GET /v1/albums/{albumId}/tracks
```

The selected album ID is used to retrieve its tracks.

The request includes:

```text
limit=10
offset=<current offset>
```

---

# Pagination

The application implements offset-based pagination.

The page size is currently:

```text
10 items
```

The initial request starts at:

```text
offset = 0
```

The next request uses the offset after the previous page.

For example:

```text
Request 1:
offset = 0
limit = 10

Request 2:
offset = 10
limit = 10

Request 3:
offset = 20
limit = 10
```

The UI monitors the user's scroll position.

When the user approaches the end of the current list, another page is requested.

The same approach is used for:

* Artists
* Albums
* Songs

Pagination prevents the application from loading the entire result set at once.

Spotify also applies API rate limits and Development Mode quota restrictions, so clients should avoid unnecessary API requests.

---

# Architecture

The application follows Clean Architecture with MVVM.

The main layers are:

```text
Presentation
     |
     v
Domain
     |
     v
Data
     |
     v
Remote API
```

## Presentation Layer

Responsible for:

* Compose UI
* UI state
* ViewModels
* Navigation
* User interactions

Examples:

```text
ui/artistscreen
ui/albumscreen
ui/songscreen
ui/auth
```

## Domain Layer

Contains application business models and use cases.

Examples:

```text
domain/model/Artist.kt
domain/model/Album.kt
domain/model/Song.kt
```

Use cases include:

```text
GetArtistsUseCase
GetAlbumsByArtistUseCase
GetSongsByAlbumUseCase
```

The domain layer does not depend directly on Retrofit or Android UI components.

## Data Layer

Responsible for retrieving and mapping remote data.

The repositories expose domain models to the domain layer.

Examples:

```text
ArtistRepository
ArtistRepositoryImpl

AlbumRepository
AlbumRepositoryImpl

SongRepository
SongRepositoryImpl
```

The repositories communicate with:

```text
SpotifyApiService
```

---

# MVVM

The application uses Model-View-ViewModel.

The general flow is:

```text
Compose UI
    |
    | User action
    v
ViewModel
    |
    v
Use Case
    |
    v
Repository
    |
    v
Spotify API
    |
    v
Repository
    |
    v
ViewModel
    |
    | StateFlow
    v
Compose UI
```

The ViewModel exposes immutable `StateFlow` objects to the UI.

For example:

```kotlin
val uiState: StateFlow<ArtistListUiState>
```

The UI observes the state using lifecycle-aware collection.

---

# UI State

Each screen has a dedicated UI state.

For example:

```kotlin
data class ArtistListUiState(
    val artists: List<Artist> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = true
)
```

The UI can therefore represent:

```text
Loading
Success
Empty
Error
```

without directly managing network operations.

---

# Dependency Injection

Hilt is used for dependency injection.

The main dependencies include:

```text
SpotifyApiService
ArtistRepository
AlbumRepository
SongRepository
GetArtistsUseCase
GetAlbumsByArtistUseCase
GetSongsByAlbumUseCase
```

The dependency graph is configured through Hilt modules.

This keeps object creation outside of the ViewModels and makes dependencies easier to replace during testing.

---

# Networking

The application uses:

* Retrofit
* OkHttp
* Gson
* Kotlin Coroutines

Retrofit defines the Spotify API interface:

```kotlin
interface SpotifyApiService
```

OkHttp handles HTTP communication.

An authentication interceptor adds the current access token to Spotify API requests.

The authentication layer is responsible for:

* Authorization
* Access token storage
* Refresh token handling
* Token refresh
* Authentication state

---

# Image Loading

Spotify artist and album images are loaded using Coil.

The application uses:

```text
coil-compose
coil-network-okhttp
```

Images are displayed directly from the URLs returned by Spotify.

---

# Navigation

Navigation is implemented using Jetpack Navigation Compose.

The application contains three main destinations:

```text
artists
albums/{artistId}
songs/{albumId}
```

The selected artist ID is passed to the album screen.

The selected album ID is passed to the song screen.

Example:

```text
artists
   |
   | artistId
   v
albums/{artistId}
   |
   | albumId
   v
songs/{albumId}
```

---

# Project Structure

The project follows a package structure based on application responsibility.

```text
app/
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── vic/
    │   │           └── android/
    │   │               └── myspotify/
    │   │                   │
    │   │                   ├── data/
    │   │                   │   ├── model/
    │   │                   │   ├── remote/
    │   │                   │   └── repository/
    │   │                   │
    │   │                   ├── di/
    │   │                   │
    │   │                   ├── domain/
    │   │                   │   ├── model/
    │   │                   │   ├── repository/
    │   │                   │   └── usecase/
    │   │                   │
    │   │                   ├── navigation/
    │   │                   │
    │   │                   └── ui/
    │   │                       ├── auth/
    │   │                       ├── artistscreen/
    │   │                       ├── albumscreen/
    │   │                       └── songscreen/
    │   │
    │   └── res/
    │       └── values/
    │           └── strings.xml
    │
    ├── test/
    │   └── ...
    │
    └── androidTest/
        └── ...
```

---

# Testing

The project includes both unit tests and UI tests.

## Unit Tests

ViewModels are tested using:

* JUnit
* Kotlin Coroutines Test
* MockK

The tests cover:

* Successful requests
* Pagination
* Loading protection
* Error handling

The tested ViewModels include:

```text
ArtistListViewModel
AlbumListViewModel
SongListViewModel
```

## UI Tests

Jetpack Compose UI tests verify:

### Artist screen

* Artists are displayed
* Clicking an artist invokes the expected callback

### Album screen

* Albums are displayed
* Clicking an album invokes the expected callback

### Song screen

* Songs are displayed
* Song duration is formatted correctly

The UI tests use:

```text
Compose Test Rule
onNodeWithText
performClick
assertIsDisplayed
```

---

# Running Tests

To run unit tests from Android Studio:

```text
Right click app
    > Run 'Tests in app'
```

Or use Gradle from the terminal:

```bash
./gradlew test
```

To run instrumentation/UI tests on a connected device or emulator:

```bash
./gradlew connectedAndroidTest
```

---

# Troubleshooting

## Gradle says that Java 17 is required

Make sure Android Studio is using JDK 17 for Gradle.

Go to:

```text
Settings
    > Build, Execution, Deployment
    > Build Tools
    > Gradle
```

Set:

```text
Gradle JDK = JDK 17
```

AGP 9.4 requires JDK 17.

---

## Spotify authentication does not return to the application

Verify that the redirect URI configured in the Spotify Developer Dashboard is exactly:

```text
myspotify://callback
```

Also make sure the Android application is using the same URI.

---

## Spotify returns an authorization error

Check:

1. The Client ID is correct.
2. The Spotify Developer application is active.
3. The redirect URI is configured correctly.
4. The Spotify account being used for testing is authorized for the application if Development Mode requires it.
5. The Spotify account satisfies the current Spotify Development Mode requirements.

Spotify's Development Mode rules have changed during 2026, so consult the current Spotify Developer documentation if the behavior differs from this README.

---

## Artists, albums, or songs are not loading

Check:

* Internet connection
* Spotify authentication state
* Logcat for HTTP errors
* Spotify Developer Dashboard configuration
* Access token expiration/refresh behavior

A `401` response generally indicates an authentication problem.

A `429` response indicates that a Spotify API rate or quota limit has been reached.

---

# Security Considerations

The project intentionally does not use a Spotify Client Secret inside the Android application.

A mobile application cannot safely keep a confidential client secret because the application package can be inspected.

The application therefore uses Authorization Code with PKCE.

Sensitive credentials such as:

```text
Client Secret
Access Token
Refresh Token
```

must not be committed to Git.

The Spotify Client ID is configured as an application resource because it identifies the Spotify application but is not a secret credential.

---

# Design Decisions

## Why Jetpack Compose?

Jetpack Compose provides a declarative approach to Android UI development.

It also makes UI state easier to connect to `StateFlow` exposed by ViewModels.

## Why MVVM?

MVVM keeps UI rendering separate from business and data operations.

The ViewModel owns screen state and coordinates use cases.

## Why Clean Architecture?

Clean Architecture keeps the domain layer independent from Android UI and networking implementations.

This makes the application easier to:

* Test
* Maintain
* Extend
* Refactor

## Why Kotlin Flow?

The application uses Flow to represent asynchronous data streams and expose state from repositories and ViewModels.

This integrates naturally with Kotlin Coroutines and lifecycle-aware Compose state collection.

## Why Hilt?

Hilt provides compile-time dependency injection and integrates directly with Android components such as ViewModels.

## Why Retrofit?

Retrofit provides a clear interface-based abstraction for the Spotify REST API and works naturally with Kotlin suspend functions.

## Why pagination?

Pagination avoids loading large result sets into memory at once and demonstrates the use of Spotify's `offset` and `limit` parameters.

---

# Technical Stack

| Technology         | Purpose                      |
| ------------------ | ---------------------------- |
| Kotlin             | Primary programming language |
| Jetpack Compose    | UI                           |
| Material 3         | UI components                |
| MVVM               | Presentation architecture    |
| Clean Architecture | Application architecture     |
| Hilt               | Dependency Injection         |
| Coroutines         | Asynchronous programming     |
| Flow               | Reactive data streams        |
| Retrofit           | REST API client              |
| OkHttp             | HTTP client                  |
| Gson               | JSON serialization           |
| Coil               | Image loading                |
| Navigation Compose | Navigation                   |
| JUnit              | Unit testing                 |
| MockK              | Mocking                      |
| Compose UI Test    | UI testing                   |

---

# Requirements Summary

| Requirement           | Version                |
| --------------------- | ---------------------- |
| Minimum Android SDK   | 24                     |
| Compile SDK           | 37                     |
| Target SDK            | 37                     |
| Android Gradle Plugin | 9.4.0                  |
| Gradle                | 9.6                    |
| Gradle JDK            | 17                     |
| Java source/target    | 11                     |
| Kotlin                | 2.2.10                 |
| Jetpack Compose       | Compose BOM 2026.02.01 |
| Hilt                  | 2.59.2                 |
| Retrofit              | 2.11.0                 |
| OkHttp                | 4.12.0                 |
| Coil                  | 3.4.0                  |

---

# Complete Setup Checklist

If you are setting up the project for the first time, follow this checklist:

```text
[ ] Install Android Studio
[ ] Install Android SDK 37
[ ] Configure Gradle to use JDK 17
[ ] Clone the MySpotify repository
[ ] Open the project in Android Studio
[ ] Wait for Gradle synchronization
[ ] Create or configure a Spotify Developer application
[ ] Configure myspotify://callback as the redirect URI
[ ] Copy the Spotify Client ID
[ ] Open app/src/main/res/values/strings.xml
[ ] Replace YOUR_SPOTIFY_CLIENT_ID
[ ] Verify Spotify Development Mode access
[ ] Start an Android Emulator or connect a physical device
[ ] Run the application
[ ] Authenticate with Spotify
[ ] Browse artists
[ ] Select an artist
[ ] Browse albums
[ ] Select an album
[ ] Browse songs
```

---

# Application Flow Summary

Once configured, the complete application flow is:

```text
                 +-------------------+
                 | Spotify OAuth PKCE|
                 +---------+---------+
                           |
                           v
                 +-------------------+
                 |   Artist Screen   |
                 |                   |
                 | Artist 1          |
                 | Artist 2          |
                 | Artist 3          |
                 | ...               |
                 +---------+---------+
                           |
                     Select artist
                           |
                           v
                 +-------------------+
                 |    Album Screen   |
                 |                   |
                 | Album 1           |
                 | Album 2           |
                 | Album 3           |
                 | ...               |
                 +---------+---------+
                           |
                     Select album
                           |
                           v
                 +-------------------+
                 |    Song Screen    |
                 |                   |
                 | Song 1     3:42   |
                 | Song 2     4:15   |
                 | Song 3     2:58   |
                 | ...               |
                 +-------------------+
```

---

# Repository

Source code:

[MySpotify on GitHub](https://github.com/nacho914/MySpotify?utm_source=chatgpt.com)

---

# Spotify Documentation

For additional information about the Spotify Web API, OAuth, applications, and Development Mode:

[Spotify Web API Documentation](https://developer.spotify.com/documentation/web-api?utm_source=chatgpt.com)

[Spotify Web API Getting Started](https://developer.spotify.com/documentation/web-api/tutorials/getting-started?utm_source=chatgpt.com)

[Spotify Apps Documentation](https://developer.spotify.com/documentation/web-api/concepts/apps?utm_source=chatgpt.com)

---

# Conclusion

MySpotify demonstrates a complete Android application architecture for consuming the Spotify Web API.

The project combines:

```text
Kotlin
+
Jetpack Compose
+
MVVM
+
Clean Architecture
+
Hilt
+
Coroutines / Flow
+
Retrofit / OkHttp
+
Spotify OAuth PKCE
+
Pagination
+
Unit Tests
+
UI Tests
```

The application is designed to keep presentation, business logic, and data access separated while providing a straightforward user experience for browsing Spotify artists, albums, and songs.
