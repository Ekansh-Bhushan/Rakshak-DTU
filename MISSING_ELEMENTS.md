# Missing Elements & Incomplete Features Documentation

This document outlines incomplete or missing elements in the **Rakshak-DTU** campus vehicle monitoring Android application.

---

## 🔴 HIGH PRIORITY - Critical Missing Elements

### 1. CSV Import Functionality (Core Feature Missing)

#### Vehicles Import Dialog
- **File:** `RegisterVehiclePopup.kt` - `ImportVehiclesDialog` composable (lines 163-288)
- **Status:** UI implemented but **upload logic is missing**
- **Missing Code:** Line 277 - `onClick = { /* TODO: upload logic */ }`
- **Required Implementation:**
  ```kotlin
  // TODO: Implement CSV parsing and upload
  // 1. Parse CSV with columns: name, fathersName, dept, dateOfIssue, vehicleType, stickerNo, vehicleNo, mobileNo
  // 2. Iterate and call viewModel.addAVehicleData() for each row
  // 3. Show progress indicator during upload
  // 4. Display success/failure count after completion
  ```

#### Cameras Import Dialog
- **File:** `CameraAddFormPopup.kt` - `ImportCameraDialog` composable (lines 162-328)
- **Status:** UI implemented but **upload logic is missing**
- **Missing Code:** Line 317 - `onClick = { /* TODO: upload logic */ }`
- **Required Implementation:**
  ```kotlin
  // TODO: Implement CSV parsing and upload
  // 1. Parse CSV with columns: lat, long, cameraType, cameraLocation
  // 2. Iterate and call viewModel.addACameraData() for each row
  // 3. Validate coordinate bounds (lat: -90 to 90, long: -180 to 180)
  ```

---

### 2. Date/Time Formatting Utility

- **Files:** `DashBoardScreen.kt`, `LogScreen.kt`, `VehiclePathScreen.kt`
- **Issue:** Each screen implements its own date formatting, causing inconsistency
- **Solution:** Create shared utility `DateTimeUtils.kt`:
  ```kotlin
  object DateTimeUtils {
      fun formatForDisplay(isoTime: String): String
      fun formatDuration(seconds: Long?): String
      fun formatDate(date: String): String
  }
  ```

---

### 3. AuthInterceptor Token Refresh Issues

- **File:** `AuthInterceptor.kt`
- **Critical Issues:**
  1. **Blocking calls:** Uses `runBlocking` which blocks the main thread
  2. **No logging:** Failed refresh attempts are silent
  3. **Tight coupling:** `AuthRepository` instantiated inside interceptor
  4. **URL mismatch:** `ApiClient` uses `auth/` base path, but interceptor calls non-auth endpoints

- **Quick Fix Needed:**
  - Add proper error logging for token refresh failures
  - Ensure fallback to login screen on refresh failure

---

### 4. Missing Permissions (Runtime Errors)

- **File:** `AndroidManifest.xml`
- **Add these permissions:**
  ```xml
  <!-- For CSV file import on Android 10+ -->
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
  
  <!-- For map features -->
  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
  <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
  ```

---

## 🟠 MEDIUM PRIORITY - Recommended Improvements

### Form Validation
- **Files:** `RegisterVehiclePopup.kt`, `CameraAddFormPopup.kt`
- Add vehicle number format validation (DL/XX pattern)
- Add mobile number validation beyond just length check
- Add coordinate bounds validation for camera latitude/longitude

### API Client Consolidation
- **Files:** `ApiClient.kt` and `AllAPIClient.kt`
- Both clients exist but should be unified for consistent auth handling

### Auto-refresh After Mutations
- **File:** `VehicleViewModel.kt`, `CameraViewModel.kt`
- Add automatic list refresh after add/edit/delete operations

---

*Generated on: 2026-07-09*

---

## 3. Auth & Security Concerns

### AuthInterceptor
- **File:** `AuthInterceptor.kt`
- **Status:** Partial implementation
- **Missing:**
  - Token refresh uses `runBlocking` which blocks the main thread - should use proper async handling
  - No logging for failed refresh attempts
  - `AuthRepository` is instantiated inside interceptor instead of injected
  - Uses `ApiClient.triggerForceLogout()` but `ApiClient` uses a different base URL (`auth/` path)

### Session Management
- **File:** `data/TokenManager.kt`
- **Status:** Needs verification of:
  - Token persistence security (uses SharedPreferences - may need EncryptedSharedPreferences)
  - Token expiration handling
  - Refresh token rotation

---

## 4. UI/UX Missing Elements

### Dashboard Screen
- **File:** `DashBoardScreen.kt` (lines 136-138)
- **Missing:**
  - `VehicleEntriesChart()` - Chart is commented out, needs real data from API
  - Time formatter for `entryTime` in recent scans (line 153)
  - Real-time data updates (currently only fetches on screen load)

### RecentScanActivityCard
- **File:** `CarData.kt` (lines 122-127)
- **Issue:** Empty text field - potential placeholder for additional info

### Date Formatting
- **File:** Multiple files
- **Missing:** Consistent date/time formatting utility - each screen has its own implementation

---

## 5. API Integration Gaps

### Device API Service
- **File:** `DeviceAPIService.kt`
- **Issue:** Service is initialized in `AllAPIClient` but **no UI or ViewModel uses it**
- **Potential Missing Features:**
  - Device registration/management functionality
  - Hardware integration endpoints

### Authentication Repository
- **File:** `AuthRepository.kt`
- **Issue:** Uses `ApiClient` (different API client) instead of `AllAPIClient`, creating inconsistency
- **Missing:**
  - `refreshAccessToken` method to handle token refresh without requiring full re-authentication

### Missing Camera Update API Call
- **File:** `CameraViewModel.kt` line 72-79
- **Issue:** `editCamera` method calls `getAllCameraDetails` after update, but there's no method to update camera in the repository correctly (uses wrong endpoint naming)

---

## 6. Missing Permission Declarations

- **File:** `AndroidManifest.xml`
- **Missing:**
  - No storage permissions declared for file import functionality (needed for Android 10+)
  - Consider adding `android.permission.READ_EXTERNAL_STORAGE` for file picker access
  - No location permissions (may be needed for map features)

---

## 7. Missing Navigation Destinations

- **File:** `Navigation.kt`
- **Missing Screens in Navigation Graph:**
  - `Screen.UpdatePasswordScreen` is defined in `Screen.kt` but may not be fully integrated
  - No deep linking support
  - No navigation animations

---

## 8. Missing Error Handling

### Network Error Handling
- **Files:** All ViewModel files
- **Missing:**
  - No network connectivity check before API calls
  - No retry mechanism for failed requests
  - No offline caching/fallback

### Form Validation
- **Files:** `RegisterVehiclePopup.kt`, `CameraAddFormPopup.kt`
- **Missing:**
  - Vehicle number format validation
  - Mobile number validation beyond length check
  - Date of issue future date prevention (partially implemented)
  - Latitude/longitude bounds validation for camera coordinates

---

## 9. Missing Repository Methods

### vehicleRepository
- **File:** `vehicleRepository.kt`
- **Missing:**
  - No dedicated `searchVehicles` method (uses `getAllVehicles` with query)
  - No pagination support for vehicle list

### cameraRepository
- **File:** `CameraRepository.kt`
- **Missing:**
  - No search/filter capability for cameras
  - No pagination support

---

## 10. Missing Unit Tests

- **Directory:** `app/src/test/java/eu/ekansh/rakshakdtu`
- **File:** `ExampleUnitTest.kt` - Only contains placeholder test
- **Missing:** Tests for:
  - ViewModel logic
  - Repository API calls
  - Data parsing
  - Authentication flow

---

## 11. Missing Documentation

### API Documentation
- No API service documentation for:
  - Expected response codes
  - Error message formats
  - Rate limiting behavior

### Code Documentation
- Inline KDoc comments are missing for:
  - Public methods in ViewModels
  - Repository functions
  - Composable parameters

---

## 12. Potential Architecture Improvements

### Dependency Injection
- **Status:** No DI framework (Hilt/Dagger) implemented
- All repositories are instantiated manually
- Makes testing difficult

### Single Source of Truth
- **Issue:** Multiple ViewModels hold separate data sources
- No shared data layer for campus statistics

### State Management
- **Issue:** Uses `mutableStateOf` directly instead of `StateFlow`
- May cause issues with complex state flows

---

## 13. Firebase Messaging Service

- **File:** `CampusFirebaseMessagingService.kt`
- **Status:** Well-implemented with:
  - `onNewToken` for token refresh handling
  - `onMessageReceived` for notification processing
  - Custom notification channel with alert type colors
  - PendingIntent to open app on notification tap with extras

---

## 14. Implementation Verification Checklist

After updates, verify:

| Component | File | Status |
|-----------|------|--------|
| TokenManager | `data/TokenManager.kt` | ✅ Uses DataStore (secure) |
| FCM Service | `CampusFirebaseMessagingService.kt` | ✅ Complete implementation |
| AuthInterceptor | `AuthInterceptor.kt` | ⚠️ Uses `runBlocking` (blocking) |
| API Clients | `ApiClient.kt`, `AllAPIClient.kt` | ⚠️ Two separate clients |

---

## Priority Summary

| Priority | Item | Impact |
|----------|------|--------|
| 🔴 High | CSV Import logic (both vehicles & cameras) | Core functionality missing |
| 🔴 High | Date/time formatting utility | Inconsistent UX |
| 🔴 High | AuthInterceptor token refresh error handling | Auth flow can break silently |
| 🟠 Medium | Form validation improvements | Data quality |
| 🟠 Medium | Missing permission declarations | Runtime errors on Android 10+ |
| 🟠 Medium | API client inconsistency (Api vs AllAPIClient) | Maintenance overhead |
| 🟢 Low | Chart data integration | Nice-to-have dashboard feature |
| 🟢 Low | DI framework integration | Architecture improvement |
| 🟢 Low | Unit tests | QA confidence |

---

## 14. Additional Code Issues

### API Client Inconsistency
- **Files:** `ApiClient.kt` and `AllAPIClient.kt`
- **Issue:** Two separate API client implementations exist
  - `ApiClient` - Used by `AuthRepository`
  - `AllAPIClient` - Used by `vehicleRepository`, `cameraRepository`, `LogRepository`
- **Impact:** Split API configuration, inconsistent headers/auth handling

### ApiClient.kt Missing Content
- **File:** `ApiClient.kt` - Only shows initialization, may be missing API service definitions
- **Check Needed:** Verify `apiService` definition for auth endpoints

### VehicleViewModel Refresh Issue
- **File:** `VehicleViewModel.kt`
- **Issue:** After adding/editing/deleting vehicles, the list doesn't automatically refresh
- **Missing:** Auto-refresh after mutations

### Camera Table Missing Features
- **File:** `Camera.kt` - `CameraTable` composable
- **Missing:**
  - No loading state handling when deleting cameras
  - No confirmation dialog before delete action

---

*Generated on: 2026-07-09*