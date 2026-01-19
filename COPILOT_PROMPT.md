# GitHub Copilot Initial Prompt

Copy this into Copilot Chat in Android Studio (Ctrl+Shift+I):

---

I'm building an Android app in Kotlin with Jetpack Compose that connects to two REST APIs running on a remote NixOS server:

1. Budget Tracker API (https://octopustechnology.net:3000/api)
2. Health Tracker API (https://octopustechnology.net:3001/api)

Both APIs are running in Docker containers managed by Portainer. They use JWT authentication.

I need to create:

1. **Authentication System**
   - Login screen with username/password
   - Register screen
   - Secure token storage using DataStore
   - Token refresh logic (tokens expire after 7 days)
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

API Configuration:
- Budget Base URL: https://octopustechnology.net:3000/api/
- Health Base URL: https://octopustechnology.net:3001/api/
- Authentication: JWT Bearer token in Authorization header
- Token expiry: 7 days
- SSL/TLS: HTTPS enabled

Please help me set up the initial project structure with:
- Data models matching the API documentation (see MOBILE_API_DOCS.md)
- Retrofit API interfaces with proper error handling
- Repository classes with offline support
- ViewModels following MVVM pattern
- Navigation setup with Compose Navigation
- A bottom navigation with Budget and Health tabs
- Proper loading states and error handling

Start with the authentication flow and data models. Use the ANDROID_MODELS_REFERENCE.md file for reference implementations.

---

## Before You Start

1. ✅ Make sure your Docker containers are running in Portainer
2. ✅ Test the APIs with curl to ensure they're accessible
3. ✅ Review MOBILE_API_DOCS.md for API endpoints
4. ✅ Review ANDROID_MODELS_REFERENCE.md for code examples

## Test Your APIs

```bash
# Test Budget API
curl -X POST https://octopustechnology.net:3000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'

# Test Health API
curl -X POST https://octopustechnology.net:3001/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'
```

## ApiConfig.kt

Create this file first: `app/src/main/java/com/octopus/apps/ApiConfig.kt`

```kotlin
package com.octopus.apps

object ApiConfig {
    const val BUDGET_BASE_URL = "https://octopustechnology.net:3000/api/"
    const val HEALTH_BASE_URL = "https://octopustechnology.net:3001/api/"
}
```

## Next Steps After Copilot Generates Code

1. Update dependencies in build.gradle.kts (if needed)
2. Sync Gradle
3. Build and run on emulator or device
4. Test authentication first
5. Then implement Budget features
6. Then implement Health features
7. Add polish and offline support

## Updating Backend Code

When you make changes to your backend:

1. SSH into NixOS server
2. Pull changes: `git pull`
3. Restart containers in Portainer
4. Or use: `docker-compose restart`

## Common Issues

**Can't connect to server:**
- Check firewall allows ports 3000, 3001
- Check containers are running in Portainer
- Test with curl first

**Authentication errors:**
- Check token is being sent in Authorization header
- Check token hasn't expired (7 days)
- Re-login if expired

**Network errors:**
- Add error handling in repositories
- Implement offline mode with Room
- Show user-friendly error messages
