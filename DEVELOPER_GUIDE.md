# Traffic Scotland App - Developer Guide

Comprehensive development documentation for the Traffic Scotland Android application.

## 📋 Table of Contents

1. [Project Overview](#project-overview)
2. [Setup & Configuration](#setup--configuration)
3. [Architecture](#architecture)
4. [Key Components](#key-components)
5. [Database Schema](#database-schema)
6. [API Integration](#api-integration)
7. [Security](#security)
8. [Building & Testing](#building--testing)
9. [Contributing](#contributing)
10. [Troubleshooting](#troubleshooting)

---

## 🎯 Project Overview

### Technology Stack

```
├── Language: Java 17
├── Build System: Gradle 8.9
├── Min SDK: 24 (Android 7.0 Nougat)
├── Target SDK: 35 (Android 15)
├── Compile SDK: 35
└── Android Gradle Plugin: 8.5.1
```

### Key Features
- Real-time traffic data from Traffic Scotland DATEX II API
- SQLite database for offline caching
- Material Design 3 UI
- Digital road sign display (VMS)
- Dark mode support
- Google Maps integration

---

## 🛠 Setup & Configuration

### Prerequisites

**Required:**
- JDK 17 or higher
- Android Studio Hedgehog (2023.1.1) or newer
- Android SDK 35
- Gradle 8.9+

**Recommended:**
- 8GB RAM minimum
- Physical Android device or emulator (API 24+)

### Initial Setup

**1. Clone the repository:**
```bash
git clone https://github.com/castortroy05/Lockhart_Antony_S2040920.git
cd Lockhart_Antony_S2040920
```

**2. Create secrets.properties file:**

Create `secrets.properties` in the project root:

```properties
# Traffic Scotland API Credentials
CLIENT_ID=your-client-id-here
CLIENT_KEY=your-client-key-here
```

⚠️ **IMPORTANT:** Never commit this file to version control!

**3. Open in Android Studio:**
```bash
studio .
```

**4. Sync Gradle:**
- Android Studio will prompt to sync
- Wait for dependency downloads
- Resolve any SDK issues

**5. Build the project:**
```bash
./gradlew clean build
```

### Configuration Files

#### secrets.properties
```properties
# API credentials for Traffic Scotland DATEX II API
CLIENT_ID=your-client-id
CLIENT_KEY=your-client-key
```

#### gradle.properties (optional)
```properties
# JVM Options
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8

# Android Build Options
android.useAndroidX=true
android.enableJetifier=true

# Enable Build Cache
org.gradle.caching=true
```

---

## 🏗 Architecture

### Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/org/me/gcu/lockhart_antony_s2040920/
│   │   │   ├── MainActivity.java              # Entry point, main menu
│   │   │   ├── NetworkActions.java            # Results display activity
│   │   │   ├── RoadSignActivity.java          # Digital road sign display
│   │   │   ├── DataManager.java               # SQLite database manager
│   │   │   ├── DataLoader.java                # Network data fetching
│   │   │   ├── DatexParser.java               # DATEX II XML parser
│   │   │   ├── UIManager.java                 # UI utilities
│   │   │   ├── SearchManager.java             # Search & filter logic
│   │   │   ├── ItemAdapter.java               # ListView adapter
│   │   │   ├── Constants.java                 # App constants
│   │   │   │
│   │   │   └── models/                        # Data models
│   │   │       ├── DatexItem.java             # Base model
│   │   │       ├── Roadwork.java              # Abstract roadwork
│   │   │       ├── CurrentRoadwork.java
│   │   │       ├── FutureRoadwork.java
│   │   │       ├── UnplannedEvent.java
│   │   │       ├── TrafficStatusMeasurement.java
│   │   │       ├── TravelTimeMeasurement.java
│   │   │       ├── VMSUnit.java
│   │   │       └── VMSMessage.java
│   │   │
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml          # Main menu layout
│   │   │   │   ├── activity_results.xml       # Results list layout
│   │   │   │   ├── activity_road_sign.xml     # Road sign display
│   │   │   │   └── list_item.xml              # List item template
│   │   │   │
│   │   │   ├── values/
│   │   │   │   ├── colors.xml                 # Color definitions
│   │   │   │   ├── strings.xml                # String resources
│   │   │   │   └── themes.xml                 # MD3 light theme
│   │   │   │
│   │   │   └── values-night/
│   │   │       └── themes.xml                 # MD3 dark theme
│   │   │
│   │   └── AndroidManifest.xml
│   │
│   └── test/                                   # Unit tests
│
├── build.gradle                                # App-level build config
└── proguard-rules.pro                          # ProGuard rules

build.gradle                                    # Project-level build config
settings.gradle                                 # Gradle settings
secrets.properties                              # API credentials (gitignored)
```

### Architecture Pattern

**Model-View-Controller (MVC) with Manager classes:**

```
┌─────────────────┐
│  View Layer     │  Activities, Layouts
│  (UI)           │  MainActivity, NetworkActions, RoadSignActivity
└────────┬────────┘
         │
┌────────▼────────┐
│ Controller      │  UIManager, SearchManager
│ Layer           │  Event handlers, Business logic
└────────┬────────┘
         │
┌────────▼────────┐
│ Model Layer     │  Data models, Database
│ (Data)          │  DatexItem, DataManager, DataLoader
└─────────────────┘
```

### Data Flow

```
User Action
    ↓
Activity (MainActivity/NetworkActions)
    ↓
Manager (UIManager/DataLoader)
    ↓
Network/Database (DataLoader/DataManager)
    ↓
Parser (DatexParser)
    ↓
Model Objects (DatexItem subclasses)
    ↓
Database (SQLite via DataManager)
    ↓
UI Update (ItemAdapter/ListView)
```

---

## 🔑 Key Components

### MainActivity.java

**Purpose:** Entry point, main menu

**Key Methods:**
```java
@Override
protected void onCreate(Bundle savedInstanceState)
// Setup UI and button listeners

@Override
public void onClick(View v)
// Handle button clicks, route to appropriate activity

private String getAuthHeader()
// Generate HTTP Basic Auth header from BuildConfig

private boolean isNetworkAvailable()
// Check network connectivity using NetworkCapabilities

private void showNetworkAlert()
// Display alert when no network available
```

**Button Routing:**
- `incidentButton` → UnplannedEvents/Content.xml
- `plannedButton` → FutureRoadworks/Content.xml
- `currentButton` → CurrentRoadworks/Content.xml
- `allButton` → All data sources
- `roadSignButton` → RoadSignActivity (offline capable)

---

### NetworkActions.java

**Purpose:** Display traffic data results

**Key Methods:**
```java
@Override
protected void onCreate(Bundle savedInstanceState)
// Initialize components, setup UI, load data

private void loadAllData()
// Fetch data from API via DataLoader

private void updateUI()
// Query database and refresh ListView

private void searchRoad(String searchText)
// Filter results by text search

private void searchDate(String dateSearchText)
// Filter results by date

private void refreshData()
// Clear cache and reload from API
```

**Components:**
- `DataLoader` - Asynchronous data fetching
- `UIManager` - UI updates and utilities
- `SearchManager` - Search and filter logic
- `DataManager` - Database operations

---

### RoadSignActivity.java

**Purpose:** Digital road sign display for VMS messages

**Key Features:**
```java
// Auto-play with Handler
private Handler autoPlayHandler;
private Runnable autoPlayRunnable;
private static final int AUTO_PLAY_DELAY = 5000; // 5 seconds

// Animation
private ValueAnimator scrollAnimator;

// Navigation
private void showNextMessage()
private void showPreviousMessage()

// Auto-play control
private void startAutoPlay()
private void stopAutoPlay()
private void toggleAutoPlay()

// Lifecycle management
@Override
protected void onPause() {
    super.onPause();
    stopAutoPlay(); // Pause when backgrounded
}

@Override
protected void onResume() {
    super.onResume();
    if (isAutoPlaying) startAutoPlay(); // Resume when foregrounded
}
```

**UI Features:**
- LED-style amber text with glow effect
- Auto-cycling messages (5-second intervals)
- Manual navigation (previous/next)
- Play/pause control
- Message count display
- Smooth fade-in animations

---

### DataManager.java

**Purpose:** SQLite database operations

**Database:**
- Name: `traffic_data.db`
- Version: 1

**Tables:**
```sql
-- Roadworks (current and future)
CREATE TABLE roadworks (
    id TEXT PRIMARY KEY,
    type TEXT,
    publication_time TEXT,
    description TEXT,
    location TEXT,
    start_date TEXT,
    end_date TEXT
);

-- Unplanned events (incidents)
CREATE TABLE events (
    id TEXT PRIMARY KEY,
    type TEXT,
    publication_time TEXT,
    description TEXT,
    location TEXT
);

-- Traffic status measurements
CREATE TABLE traffic_status (
    id TEXT PRIMARY KEY,
    publication_time TEXT,
    site_reference TEXT,
    measurement_time TEXT,
    traffic_status TEXT
);

-- Travel time measurements
CREATE TABLE travel_time (
    id TEXT PRIMARY KEY,
    publication_time TEXT,
    site_reference TEXT,
    measurement_time TEXT,
    travel_time REAL,
    free_flow_travel_time REAL
);

-- Variable Message Signs
CREATE TABLE vms (
    id TEXT PRIMARY KEY,
    publication_time TEXT,
    vms_unit_reference TEXT,
    message_time TEXT,
    message_text TEXT
);
```

**Key Methods:**
```java
// Insert operations
public void insertRoadwork(Roadwork roadwork)
public void insertUnplannedEvent(UnplannedEvent event)
public void insertTrafficStatus(TrafficStatusMeasurement status)
public void insertTravelTime(TravelTimeMeasurement travelTime)
public void insertVMS(VMSUnit vms)

// Query operations (all use parameterized queries)
public List<Roadwork> getAllRoadworks()
public List<UnplannedEvent> getAllUnplannedEvents()
public List<TrafficStatusMeasurement> getAllTrafficStatuses()
public List<TravelTimeMeasurement> getAllTravelTimes()
public List<VMSUnit> getAllVMSUnits()

// Maintenance
public void clearAllData()
```

**Security Features:**
- ✅ Parameterized queries (prevents SQL injection)
- ✅ Try-finally blocks (prevents resource leaks)
- ✅ Proper cursor management

---

### DataLoader.java

**Purpose:** Asynchronous network data fetching

**API Endpoints:**
```java
private static final String[] ENDPOINTS = {
    "CurrentRoadworks/Content.xml",
    "FutureRoadworks/Content.xml",
    "UnplannedEvents/Content.xml",
    "TrafficStatusData/content.xml",
    "TravelTimeData/content.xml",
    "VMS/content.xml"
};
```

**Connection Configuration:**
```java
connection.setRequestMethod("GET");
connection.setConnectTimeout(15000);  // 15 seconds
connection.setReadTimeout(10000);     // 10 seconds
connection.setRequestProperty("Authorization", authHeader);
```

**Threading:**
- Uses `ExecutorService` for background tasks
- Single-threaded executor (sequential downloads)
- Callbacks to UI thread via `runOnUiThread()`

**Methods:**
```java
public void loadAllData(String authHeader, LoadDataCallback callback)
// Load data from all 6 endpoints

private void loadData(String url, String authHeader)
// Download and parse single endpoint

public void shutdown()
// Clean shutdown of executor
```

---

### DatexParser.java

**Purpose:** Parse DATEX II XML format

**Supported Types:**
1. CurrentRoadwork
2. FutureRoadwork
3. UnplannedEvent
4. TrafficStatusMeasurement
5. TravelTimeMeasurement
6. VMSUnit (with VMSMessage)

**Parsing Strategy:**
- Uses Android's `XmlPullParser` (SAX-style)
- Event-driven parsing
- Nested element handling
- Date/time conversion

**Key Methods:**
```java
public static List<DatexItem> parse(InputStream inputStream, String endpoint)
// Main parsing method

private static String parseLocation(XmlPullParser parser)
// Extract latitude/longitude

private static String parseValidity(XmlPullParser parser)
// Extract start/end dates

private static String formatDate(String isoDate)
// Convert ISO 8601 to readable format
```

**Date Formats:**
```java
// Input: ISO 8601
"2025-01-15T08:30:00Z"

// Output: Readable
"Wed, 15 Jan 2025 08:30:00 GMT"
```

---

## 💾 Database Schema

### Entity-Relationship Diagram

```
┌─────────────────┐
│   DatexItem     │  (Abstract base)
│  (Base Model)   │
└────────┬────────┘
         │
         ├─────────┬─────────────┬────────────────┬─────────────┐
         │         │             │                │             │
    ┌────▼────┐ ┌─▼──────┐ ┌────▼────────┐ ┌────▼────────┐ ┌──▼─────┐
    │Roadwork │ │Unplanned│ │TrafficStatus│ │TravelTime   │ │VMSUnit │
    │(Abstract│ │ Event   │ │Measurement  │ │Measurement  │ │        │
    └────┬────┘ └─────────┘ └─────────────┘ └─────────────┘ └────┬───┘
         │                                                         │
    ┌────┴────┬──────────┐                                  ┌─────▼──────┐
    │Current  │  Future  │                                  │VMSMessage  │
    │Roadwork │ Roadwork │                                  │(Contained) │
    └─────────┴──────────┘                                  └────────────┘
```

### Table Relationships

**roadworks**
- Stores both CurrentRoadwork and FutureRoadwork
- `type` column distinguishes: "Current" or "Future"

**events**
- Stores UnplannedEvent data
- `type` column always "Unplanned"

**traffic_status**
- Stores TrafficStatusMeasurement
- Real-time traffic flow data

**travel_time**
- Stores TravelTimeMeasurement
- Compares actual vs free-flow times

**vms**
- Stores VMSUnit messages
- One row per VMS message
- Composite key: `vms_id + message_time`

---

## 🌐 API Integration

### Traffic Scotland DATEX II API

**Base URL:**
```
https://datex2.trafficscotland.org/rest/2.3/publications/
```

**Authentication:**
```
Authorization: Basic base64(CLIENT_ID:CLIENT_KEY)
```

### Endpoints

| Endpoint | Data Type | Update Frequency |
|----------|-----------|------------------|
| `CurrentRoadworks/Content.xml` | Active roadworks | 15 minutes |
| `FutureRoadworks/Content.xml` | Planned roadworks | Daily |
| `UnplannedEvents/Content.xml` | Incidents/accidents | Real-time |
| `TrafficStatusData/content.xml` | Traffic flow | 5 minutes |
| `TravelTimeData/content.xml` | Journey times | 5 minutes |
| `VMS/content.xml` | Road sign messages | Real-time |

### Request Example

```http
GET /rest/2.3/publications/UnplannedEvents/Content.xml HTTP/1.1
Host: datex2.trafficscotland.org
Authorization: Basic base64encodedcredentials
```

### Response Format

```xml
<?xml version="1.0" encoding="UTF-8"?>
<d2LogicalModel>
    <payloadPublication>
        <situation>
            <situationRecord>
                <id>INCIDENT-123</id>
                <validityStatus>active</validityStatus>
                <overallStartTime>2025-01-15T08:30:00Z</overallStartTime>
                <overallEndTime>2025-01-15T12:00:00Z</overallEndTime>
                <groupOfLocations>
                    <locationForDisplay>
                        <latitude>55.8642</latitude>
                        <longitude>-4.2518</longitude>
                    </locationForDisplay>
                </groupOfLocations>
                <generalPublicComment>
                    <comment>Accident on M8 Westbound between J16 and J17</comment>
                </generalPublicComment>
            </situationRecord>
        </situation>
    </payloadPublication>
</d2LogicalModel>
```

---

## 🔒 Security

### Best Practices Implemented

**1. Credentials Management**
```java
// ✅ GOOD - Credentials from BuildConfig
String auth = BuildConfig.CLIENT_ID + ":" + BuildConfig.CLIENT_KEY;

// ❌ BAD - Hardcoded credentials (removed)
// String CLIENT_ID = "hardcoded-value";
```

**2. Network Security**
```xml
<!-- AndroidManifest.xml -->
<!-- ✅ GOOD - HTTPS only (no cleartext) -->
<application
    android:usesCleartextTraffic="false">  <!-- removed: was true -->
```

**3. SQL Injection Prevention**
```java
// ✅ GOOD - Parameterized queries
cursor = db.query(
    TABLE_EVENTS,
    null,
    COLUMN_TYPE + " = ?",
    new String[]{"Unplanned"},  // Safe parameters
    null, null, null
);

// ❌ BAD - String concatenation (removed)
// String query = "SELECT * FROM events WHERE type = '" + userInput + "'";
```

**4. Resource Management**
```java
// ✅ GOOD - Try-finally for cleanup
Cursor cursor = null;
try {
    cursor = db.query(...);
    // Process cursor
} finally {
    if (cursor != null) {
        cursor.close();
    }
    db.close();
}
```

### Security Checklist

- [x] API credentials externalized (secrets.properties)
- [x] HTTPS enforced (no cleartext traffic)
- [x] SQL injection prevented (parameterized queries)
- [x] Resource leaks fixed (try-finally blocks)
- [x] Proper permission declarations
- [x] No sensitive data logged
- [x] ProGuard rules for release builds

---

## 🧪 Building & Testing

### Build Commands

**Debug Build:**
```bash
./gradlew assembleDebug
```

**Release Build:**
```bash
./gradlew assembleRelease
```

**Clean Build:**
```bash
./gradlew clean build
```

**Run Tests:**
```bash
./gradlew test
./gradlew connectedAndroidTest
```

### Build Variants

```gradle
buildTypes {
    release {
        minifyEnabled false
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
    debug {
        debuggable true
    }
}
```

### Testing Strategy

**Unit Tests** (`src/test/`):
- Model class tests
- Parser logic tests
- Utility method tests

**Instrumented Tests** (`src/androidTest/`):
- Database operations
- UI interactions
- Activity lifecycle

**Manual Testing Checklist:**
- [ ] Main menu buttons functional
- [ ] Data loading from all endpoints
- [ ] Search functionality
- [ ] Date filtering
- [ ] Road sign auto-play
- [ ] Road sign manual controls
- [ ] Map integration
- [ ] Offline functionality
- [ ] Dark mode switching
- [ ] Network error handling

---

## 🤝 Contributing

### Code Style

**Java Conventions:**
- Indent: 4 spaces
- Max line length: 120 characters
- JavaDoc for all public methods
- Meaningful variable names

**Example:**
```java
/**
 * Parses DATEX II XML input stream into DatexItem objects.
 *
 * @param inputStream The XML data stream
 * @param endpoint The API endpoint identifier
 * @return List of parsed DatexItem objects
 * @throws Exception if parsing fails
 */
public static List<DatexItem> parse(InputStream inputStream, String endpoint) throws Exception {
    // Implementation
}
```

### Git Workflow

**Branch Naming:**
```
feature/description
bugfix/description
hotfix/description
```

**Commit Messages:**
```
Type: Short description (50 chars max)

Longer description if needed. Explain what and why,
not how. Reference issue numbers.

- Bullet points for details
- Keep lines under 72 characters

Fixes #123
```

**Types:**
- `feat:` New feature
- `fix:` Bug fix
- `docs:` Documentation
- `style:` Formatting
- `refactor:` Code restructuring
- `test:` Adding tests
- `chore:` Maintenance

### Pull Request Process

1. Create feature branch
2. Make changes with tests
3. Update documentation
4. Run all tests
5. Create PR with description
6. Address review comments
7. Merge when approved

---

## 🐛 Troubleshooting

### Common Build Issues

**Issue: "BuildConfig cannot be resolved"**

**Solution:**
```bash
# Ensure secrets.properties exists
touch secrets.properties

# Add credentials
echo "CLIENT_ID=your-id" >> secrets.properties
echo "CLIENT_KEY=your-key" >> secrets.properties

# Clean and rebuild
./gradlew clean build
```

---

**Issue: "SDK not found"**

**Solution:**
```bash
# Install required SDK
sdkmanager "platforms;android-35"
sdkmanager "build-tools;35.0.0"

# Sync gradle
./gradlew --refresh-dependencies
```

---

**Issue: "Dependency resolution failed"**

**Solution:**
```bash
# Clear Gradle cache
rm -rf ~/.gradle/caches/

# Clean project
./gradlew clean

# Rebuild
./gradlew build --refresh-dependencies
```

---

### Runtime Issues

**Issue: "Network request fails"**

**Check:**
1. Internet permission in AndroidManifest.xml
2. Network state permission
3. API credentials in secrets.properties
4. HTTPS URL (not HTTP)

---

**Issue: "Database query returns empty"**

**Check:**
1. Data loaded from API first
2. Correct table name in query
3. Database version number
4. Log statements for debugging

---

## 📚 Additional Resources

### Documentation
- [Android Developers](https://developer.android.com/)
- [Material Design 3](https://m3.material.io/)
- [DATEX II Standard](https://www.datex2.eu/)
- [Gradle Documentation](https://docs.gradle.org/)

### Tools
- [Android Studio](https://developer.android.com/studio)
- [Sourcetree](https://www.sourcetreeapp.com/) - Git GUI
- [Postman](https://www.postman.com/) - API testing
- [DB Browser for SQLite](https://sqlitebrowser.org/) - Database viewer

---

## 📝 Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | Jan 2025 | Major security update, MD3, Road sign display |
| 0.9.0 | Jul 2024 | Initial database implementation |
| 0.8.0 | Jun 2024 | Network operations split |

---

**Happy coding! 🚀**

*Last updated: January 2025*
