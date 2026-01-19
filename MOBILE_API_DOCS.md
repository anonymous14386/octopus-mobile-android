# Octopus Apps - Mobile API Documentation

## Overview
This document describes the REST API endpoints for both the Budget Tracker and Health Tracker applications that can be used by the Android mobile app.

## Base URLs
- **Budget API**: `http://your-server:3000/api` (or your production URL)
- **Health API**: `http://your-server:3001/api` (or your production URL)

## Authentication
All protected endpoints require JWT authentication. Include the token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

---

## Budget Tracker API

### Authentication

#### Register
- **POST** `/api/auth/register`
- **Body**: 
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Response**: 
  ```json
  {
    "success": true,
    "token": "jwt-token",
    "username": "string"
  }
  ```

#### Login
- **POST** `/api/auth/login`
- **Body**: 
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Response**: 
  ```json
  {
    "success": true,
    "token": "jwt-token",
    "username": "string"
  }
  ```

### Subscriptions

#### Get All Subscriptions
- **GET** `/api/budget/subscriptions`
- **Auth**: Required
- **Response**: 
  ```json
  {
    "success": true,
    "data": [
      {
        "id": 1,
        "name": "Netflix",
        "amount": 15.99,
        "frequency": "monthly"
      }
    ]
  }
  ```

#### Create Subscription
- **POST** `/api/budget/subscriptions`
- **Auth**: Required
- **Body**: 
  ```json
  {
    "name": "string",
    "amount": number,
    "frequency": "daily|weekly|monthly|yearly"
  }
  ```

#### Update Subscription
- **PUT** `/api/budget/subscriptions/:id`
- **Auth**: Required
- **Body**: Same as create (all fields optional)

#### Delete Subscription
- **DELETE** `/api/budget/subscriptions/:id`
- **Auth**: Required

### Accounts

#### Get All Accounts
- **GET** `/api/budget/accounts`
- **Auth**: Required
- **Response**: 
  ```json
  {
    "success": true,
    "data": [
      {
        "id": 1,
        "name": "Checking",
        "balance": 1500.00
      }
    ]
  }
  ```

#### Create Account
- **POST** `/api/budget/accounts`
- **Auth**: Required
- **Body**: 
  ```json
  {
    "name": "string",
    "balance": number
  }
  ```

#### Update Account
- **PUT** `/api/budget/accounts/:id`
- **Auth**: Required
- **Body**: Same as create (all fields optional)

#### Delete Account
- **DELETE** `/api/budget/accounts/:id`
- **Auth**: Required

### Income

#### Get All Income
- **GET** `/api/budget/income`
- **Auth**: Required

#### Create Income
- **POST** `/api/budget/income`
- **Auth**: Required
- **Body**: 
  ```json
  {
    "amount": number,
    "frequency": "weekly|biweekly|monthly"
  }
  ```

#### Update Income
- **PUT** `/api/budget/income/:id`
- **Auth**: Required

#### Delete Income
- **DELETE** `/api/budget/income/:id`
- **Auth**: Required

### Debts

#### Get All Debts
- **GET** `/api/budget/debts`
- **Auth**: Required

#### Create Debt
- **POST** `/api/budget/debts`
- **Auth**: Required
- **Body**: 
  ```json
  {
    "name": "string",
    "balance": number
  }
  ```

#### Update Debt
- **PUT** `/api/budget/debts/:id`
- **Auth**: Required

#### Delete Debt
- **DELETE** `/api/budget/debts/:id`
- **Auth**: Required

### Summary

#### Get Budget Summary
- **GET** `/api/budget/summary`
- **Auth**: Required
- **Response**: Returns all subscriptions, accounts, income, and debts in one call
  ```json
  {
    "success": true,
    "data": {
      "subscriptions": [...],
      "accounts": [...],
      "income": [...],
      "debts": [...]
    }
  }
  ```

---

## Health Tracker API

### Authentication
Same as Budget API but on the Health API base URL.

### Weight Tracking

#### Get All Weight Entries
- **GET** `/api/health/weight`
- **Auth**: Required
- **Response**: 
  ```json
  {
    "success": true,
    "data": [
      {
        "id": 1,
        "date": "2026-01-19",
        "weight": 180.5,
        "notes": "Morning weight"
      }
    ]
  }
  ```

#### Create Weight Entry
- **POST** `/api/health/weight`
- **Auth**: Required
- **Body**: 
  ```json
  {
    "date": "YYYY-MM-DD",
    "weight": number,
    "notes": "string (optional)"
  }
  ```

#### Update Weight Entry
- **PUT** `/api/health/weight/:id`
- **Auth**: Required

#### Delete Weight Entry
- **DELETE** `/api/health/weight/:id`
- **Auth**: Required

### Exercise Tracking

#### Get All Exercises
- **GET** `/api/health/exercises`
- **Auth**: Required
- **Response**: 
  ```json
  {
    "success": true,
    "data": [
      {
        "id": 1,
        "date": "2026-01-19",
        "type": "Running",
        "duration": 30,
        "notes": "5k run"
      }
    ]
  }
  ```

#### Create Exercise
- **POST** `/api/health/exercises`
- **Auth**: Required
- **Body**: 
  ```json
  {
    "date": "YYYY-MM-DD",
    "type": "string",
    "duration": number (minutes),
    "notes": "string (optional)"
  }
  ```

#### Update Exercise
- **PUT** `/api/health/exercises/:id`
- **Auth**: Required

#### Delete Exercise
- **DELETE** `/api/health/exercises/:id`
- **Auth**: Required

### Meal Tracking

#### Get All Meals
- **GET** `/api/health/meals`
- **Auth**: Required
- **Response**: 
  ```json
  {
    "success": true,
    "data": [
      {
        "id": 1,
        "date": "2026-01-19",
        "time": "12:00",
        "description": "Grilled chicken salad",
        "calories": 450,
        "notes": "Healthy lunch"
      }
    ]
  }
  ```

#### Create Meal
- **POST** `/api/health/meals`
- **Auth**: Required
- **Body**: 
  ```json
  {
    "date": "YYYY-MM-DD",
    "time": "HH:MM (optional)",
    "description": "string",
    "calories": number (optional),
    "notes": "string (optional)"
  }
  ```

#### Update Meal
- **PUT** `/api/health/meals/:id`
- **Auth**: Required

#### Delete Meal
- **DELETE** `/api/health/meals/:id`
- **Auth**: Required

### Goal Tracking

#### Get All Goals
- **GET** `/api/health/goals`
- **Auth**: Required
- **Response**: 
  ```json
  {
    "success": true,
    "data": [
      {
        "id": 1,
        "title": "Lose 10 pounds",
        "description": "Weight loss goal",
        "deadline": "2026-03-01",
        "completed": false
      }
    ]
  }
  ```

#### Create Goal
- **POST** `/api/health/goals`
- **Auth**: Required
- **Body**: 
  ```json
  {
    "title": "string",
    "description": "string (optional)",
    "deadline": "YYYY-MM-DD (optional)",
    "completed": boolean (default: false)
  }
  ```

#### Update Goal
- **PUT** `/api/health/goals/:id`
- **Auth**: Required

#### Delete Goal
- **DELETE** `/api/health/goals/:id`
- **Auth**: Required

### Summary

#### Get Health Summary
- **GET** `/api/health/summary`
- **Auth**: Required
- **Response**: Returns all weight entries, exercises, meals, and goals in one call
  ```json
  {
    "success": true,
    "data": {
      "weight": [...],
      "exercises": [...],
      "meals": [...],
      "goals": [...]
    }
  }
  ```

---

## Error Responses

All endpoints return errors in the following format:
```json
{
  "success": false,
  "error": "Error message description"
}
```

Common HTTP status codes:
- `200` - Success
- `201` - Created
- `400` - Bad Request (validation error)
- `401` - Unauthorized (missing/invalid token)
- `404` - Not Found
- `500` - Server Error

---

## Notes for Mobile Development

1. **Token Storage**: Store the JWT token securely (use EncryptedSharedPreferences on Android)
2. **Token Expiry**: Tokens expire after 7 days - implement refresh logic or re-login
3. **Base URLs**: Make these configurable for development vs production
4. **Network Calls**: Use Retrofit or Ktor for Android HTTP client
5. **Offline Support**: Consider implementing Room database for offline caching
6. **Error Handling**: Implement proper error handling for network failures
7. **Loading States**: Show loading indicators during API calls
8. **CORS**: Web apps will continue to work as they use session-based auth, not JWT
