# Android App Setup Guide - Octopus Apps

## Step 1: Create GitHub Repository

1. Go to https://github.com/new
2. Repository name: `octopus-mobile-android` (or your preference)
3. Description: "Android app for Octopus Budget and Health tracking"
4. Choose: Private or Public
5. Initialize with README: Yes
6. Add .gitignore: Choose "Android"
7. License: MIT (optional)
8. Click "Create repository"

## Step 2: Clone Repository

```bash
cd ~/Documents
git clone https://github.com/YOUR_USERNAME/octopus-mobile-android.git
cd octopus-mobile-android
```

## Step 3: Add to VS Code Workspace

In VS Code:
1. File → Add Folder to Workspace
2. Select the `octopus-mobile-android` folder
3. Save workspace

## Step 4: Create Android Project in Android Studio

### Option A: Using Android Studio GUI

1. Open Android Studio
2. New Project → Empty Activity (Compose)
3. Settings:
   - Name: `Octopus Apps`
   - Package name: `com.octopus.apps`
   - Save location: `/home/psychopathy/Documents/octopus-mobile-android`
   - Language: Kotlin
   - Minimum SDK: API 26 (Android 8.0) - covers 95%+ devices
   - Build configuration language: Kotlin DSL (build.gradle.kts)
4. Click Finish

### Option B: Using Command Line (if you prefer)

```bash
# This creates the project structure
# Android Studio is still recommended for initial setup
```

## Step 5: Install GitHub Copilot for Android Studio

### GitHub Copilot (Recommended - Official AI Assistant)

1. Open Android Studio
2. File → Settings (or Preferences on Mac)
3. Plugins → Marketplace
4. Search for "GitHub Copilot"
5. Click Install
6. Restart Android Studio
7. Sign in with your GitHub account (requires Copilot subscription)

**Features:**
- Code completion as you type
- Generate entire functions from comments
- Suggest tests
- Explain code
- Fix bugs
- Generate documentation

**Subscription:**
- Free for verified students (with GitHub Student Pack)
- Free for open source maintainers
- $10/month for individuals
- $19/user/month for businesses

### Alternative: Tabnine (If you don't have Copilot)

1. Plugins → Marketplace → Search "Tabnine"
2. Install and restart
3. Has a free tier with basic completion

## Step 6: Project Structure

Your Android project will have this structure:

```
octopus-mobile-android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/octopus/apps/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── data/
│   │   │   │   │   ├── api/        # API interfaces
│   │   │   │   │   ├── model/      # Data models
│   │   │   │   │   ├── repository/ # Data repositories
│   │   │   │   ├── ui/
│   │   │   │   │   ├── budget/     # Budget screens
│   │   │   │   │   ├── health/     # Health screens
│   │   │   │   │   ├── auth/       # Login/Register
│   │   │   │   │   ├── common/     # Shared components
│   │   │   │   ├── navigation/     # Navigation setup
│   │   │   │   └── di/             # Dependency injection
│   │   │   ├── res/                # Resources
│   │   │   └── AndroidManifest.xml
│   │   └── test/                   # Unit tests
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
└── settings.gradle.kts
```

## Step 7: Update Dependencies

Edit `app/build.gradle.kts` and add these dependencies:

```kotlin
dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.01.00")
    implementation(composeBom)
    
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    
    // Compose UI
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")
    
    // Networking - Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    
    // Dependency Injection - Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    
    // Local Database - Room (for offline support)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // DataStore (for secure token storage)
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    
    // Charts (for health/budget visualizations)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
```

Also add at the top of the file:
```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}
```

And in project-level `build.gradle.kts`:
```kotlin
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
}
```

Add to `settings.gradle.kts`:
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // For MPAndroidChart
    }
}
```

## Step 8: Add Internet Permission

Edit `app/src/main/AndroidManifest.xml` and add before `<application>`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## Step 9: Create Configuration File

Create `app/src/main/java/com/octopus/apps/BuildConfig.kt`:

```kotlin
package com.octopus.apps

object ApiConfig {
    // Change these to your server URLs
    const val BUDGET_BASE_URL = "http://10.0.2.2:3000/api/" // Android emulator localhost
    const val HEALTH_BASE_URL = "http://10.0.2.2:3001/api/"
    
    // For real device testing, use your computer's IP:
    // const val BUDGET_BASE_URL = "http://192.168.1.XXX:3000/api/"
    // const val HEALTH_BASE_URL = "http://192.168.1.XXX:3001/api/"
    
    // For production:
    // const val BUDGET_BASE_URL = "https://budget.octopusapps.com/api/"
    // const val HEALTH_BASE_URL = "https://health.octopusapps.com/api/"
}
```

## Step 10: Using GitHub Copilot in Android Studio

Once installed, here's how to use GitHub Copilot effectively:

### 1. Code Completion
Just start typing and Copilot will suggest completions:
```kotlin
// Type this comment:
// Function to login user with username and password

// Copilot will suggest the function implementation
// Press Tab to accept the suggestion
```

### 2. Generate from Comments
```kotlin
// Create a data class for Budget Subscription with id, name, amount, and frequency
// Copilot will generate:
data class Subscription(
    val id: Int,
    val name: String,
    val amount: Double,
    val frequency: String
)
```

### 3. Chat with Copilot
- Open Copilot Chat: Right-click → Copilot → Open Chat
- Or use the shortcut: Ctrl+Shift+I (Windows/Linux) or Cmd+Shift+I (Mac)

### 4. Explain Code
- Select code → Right-click → Copilot → Explain This

### 5. Fix Issues
- When you see an error, Copilot may suggest fixes
- Or ask in chat: "How do I fix this error?"

## Step 11: Initial Prompt for GitHub Copilot

Once you have the project set up, use this prompt in Copilot Chat:

```
I'm building an Android app in Kotlin with Jetpack Compose that connects to two REST APIs:

1. Budget Tracker API (http://localhost:3000/api)
2. Health Tracker API (http://localhost:3001/api)

Both APIs use JWT authentication. I need to create:

1. **Authentication System**
   - Login screen with username/password
   - Register screen
   - Secure token storage using DataStore
   - Token refresh logic
   - Logout functionality

2. **Budget Features**
   - View/add/edit/delete subscriptions
   - View/add/edit/delete accounts
   - View/add/edit/delete income sources
   - View/add/edit/delete debts
   - Dashboard showing budget summary

3. **Health Features**
   - View/add/edit/delete weight entries with chart
   - View/add/edit/delete exercises
   - View/add/edit/delete meals with calorie tracking
   - View/add/edit/delete goals with progress
   - Dashboard showing health summary

4. **Architecture**
   - MVVM pattern
   - Repository pattern for data
   - Retrofit for API calls
   - Room for offline caching
   - Hilt for dependency injection
   - Jetpack Compose for UI
   - Material 3 design

Please help me set up the initial project structure with:
- Data models matching the API documentation
- Retrofit API interfaces
- Repository classes
- ViewModels
- Navigation setup
- A bottom navigation with Budget and Health tabs

Start with the authentication flow and data models.
```

## Step 12: Recommended Development Order

1. **Data Layer** (Day 1-2)
   - Create data models (Subscription, Account, Weight, Exercise, etc.)
   - Set up Retrofit API interfaces
   - Create Repository classes
   - Set up Hilt dependency injection

2. **Authentication** (Day 2-3)
   - Login screen
   - Register screen
   - Token storage with DataStore
   - Auth interceptor for Retrofit

3. **Budget Features** (Day 3-5)
   - Budget home screen with tabs
   - Subscriptions list and forms
   - Accounts list and forms
   - Income list and forms
   - Debts list and forms
   - Budget summary dashboard

4. **Health Features** (Day 5-7)
   - Health home screen with tabs
   - Weight tracking with chart
   - Exercise tracking
   - Meal tracking
   - Goals tracking
   - Health summary dashboard

5. **Polish** (Day 7-8)
   - Error handling
   - Loading states
   - Empty states
   - Offline support
   - Pull to refresh
   - Dark mode
   - Animations

## Step 13: Testing the APIs

Before building the app, make sure your APIs are running:

```bash
# Terminal 1 - Budget App
cd ~/Documents/octopus-budget
npm install  # if not already done
npm start

# Terminal 2 - Health App
cd ~/Documents/octopus-health
npm install  # if not already done
npm start
```

Test with curl or Postman:
```bash
# Test budget login
curl -X POST http://localhost:3000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'

# Test health login
curl -X POST http://localhost:3001/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'
```

## Step 14: Play Store Preparation (Future)

When ready for Play Store:

1. **App Icon**: Design 512x512 PNG icon
2. **Screenshots**: Prepare screenshots for different device sizes
3. **Privacy Policy**: Create privacy policy (required)
4. **Content Rating**: Fill out questionnaire
5. **App Signing**: Set up Google Play App Signing
6. **Testing**: Internal/closed testing first
7. **Developer Account**: $25 one-time fee

## Resources

- **Android Developers**: https://developer.android.com
- **Jetpack Compose**: https://developer.android.com/jetpack/compose
- **Material 3 Design**: https://m3.material.io
- **Retrofit**: https://square.github.io/retrofit
- **Hilt**: https://dagger.dev/hilt

## Notes

- Use Android Emulator for development (faster than physical device)
- Enable "USB Debugging" on physical devices for testing
- Use Android Studio's built-in profiler to monitor performance
- Test on different screen sizes (phone, tablet)
- Test different Android versions (min API 26 to latest)

## Next Steps

After completing the setup:
1. Create the project in Android Studio
2. Install GitHub Copilot
3. Copy the initial prompt to Copilot Chat
4. Let Copilot guide you through implementation
5. Test each feature as you build it
6. Commit regularly to GitHub

Good luck! 🚀
