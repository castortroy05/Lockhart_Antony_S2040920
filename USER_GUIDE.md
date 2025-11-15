# Traffic Scotland App - User Guide

Welcome to the Traffic Scotland App! This guide will help you get the most out of the app and its features.

## 📱 Table of Contents

1. [Getting Started](#getting-started)
2. [Main Menu](#main-menu)
3. [Viewing Traffic Data](#viewing-traffic-data)
4. [Digital Road Sign Display](#digital-road-sign-display)
5. [Search & Filter](#search--filter)
6. [Troubleshooting](#troubleshooting)
7. [Tips & Tricks](#tips--tricks)

---

## 🚀 Getting Started

### System Requirements
- **Android Version:** 7.0 (Nougat) or higher
- **Internet Connection:** Required for loading traffic data
- **Permissions:**
  - Internet access (for downloading traffic updates)
  - Network state (for connectivity checks)

### First Launch
1. Open the Traffic Scotland app
2. Ensure you have an active internet connection
3. The main menu will display 5 options

---

## 🏠 Main Menu

The main menu provides quick access to different traffic information categories:

### 1. 📅 Current Incidents
**Purpose:** View active traffic incidents and accidents

**What you'll see:**
- Traffic accidents
- Vehicle breakdowns
- Road closures
- Emergency incidents
- Hazards on the road

**Best used for:**
- Planning your journey
- Finding alternative routes
- Real-time incident awareness

---

### 2. 🚧 Planned Roadworks
**Purpose:** View scheduled roadworks

**What you'll see:**
- Future roadwork projects
- Scheduled maintenance
- Planned closures
- Start and end dates
- Affected road sections

**Best used for:**
- Long-term journey planning
- Avoiding future disruptions
- Understanding upcoming changes

---

### 3. 🔧 Current Roadworks
**Purpose:** View ongoing roadworks

**What you'll see:**
- Active roadworks
- Current lane closures
- Traffic management in place
- Temporary speed limits
- Diversion information

**Best used for:**
- Today's journey planning
- Understanding current delays
- Finding quickest routes

---

### 4. 📊 Load All Items
**Purpose:** View all traffic data in one place

**What you'll see:**
- All incidents combined
- All roadworks (current & planned)
- Traffic status measurements
- Travel time data
- Variable Message Signs (VMS)

**Best used for:**
- Comprehensive overview
- General awareness
- Searching across all categories

---

### 5. 🚦 Road Sign Display **(NEW!)**
**Purpose:** View VMS messages in digital road sign style

**What you'll see:**
- Authentic LED-style messages
- Auto-cycling traffic alerts
- Manual message browsing

**Best used for:**
- Quick message overview
- Offline message viewing
- Familiar road sign interface

---

## 📊 Viewing Traffic Data

### Loading Data

1. **Select a category** from the main menu
2. **Wait for loading** - You'll see "Loading..." message
3. **View results** - Data appears in a scrollable list

### Understanding the Data Display

Each traffic item shows:

```
┌─────────────────────────────────────┐
│ 🔴 A9-INCIDENT-12345               │ ← Item ID
├─────────────────────────────────────┤
│ Accident on M8 Westbound           │ ← Description
│ Between J16 and J17                │ ← Location
├─────────────────────────────────────┤
│ Start: Mon, 15 Jan 2025 08:30:00   │ ← Start date
│ End: Mon, 15 Jan 2025 12:00:00     │ ← Expected end
├─────────────────────────────────────┤
│ Duration: 2 days                   │ ← Color-coded
│ [VIEW ON MAP]                      │ ← Map button
└─────────────────────────────────────┘
```

### Duration Color Coding

- 🟢 **Green** - Less than 7 days
- 🟠 **Orange** - 7 to 30 days
- 🔴 **Red** - More than 30 days

### Map Integration

Tap **[VIEW ON MAP]** to:
- Open location in Google Maps
- See exact incident position
- Get directions
- View alternative routes

---

## 🚦 Digital Road Sign Display

### Overview
The Road Sign Display shows VMS (Variable Message Sign) messages in an authentic LED-style format, just like real motorway signs.

### Opening Road Sign Display

1. Tap **"Road Sign Display"** button (orange) on main menu
2. Display opens immediately
3. No internet required if data is cached

### Display Layout

```
┌─────────────────────────────────────┐
│  VMS: A9-NORTH-001 (2/15)          │ ← VMS location & count
├─────────────────────────────────────┤
│                                     │
│    ACCIDENT ON M8 WESTBOUND        │ ← LED-style message
│    DELAYS EXPECTED                 │ ← (Amber text)
│                                     │
├─────────────────────────────────────┤
│            ● ● ●                    │ ← LED indicators
└─────────────────────────────────────┘
     [  ⏸ PAUSE  ]                      ← Controls
     [⏮ PREVIOUS] [NEXT ⏭]
```

### Auto-Play Feature

**Default behavior:**
- Messages automatically cycle every **5 seconds**
- Smooth fade-in transitions
- Continuous loop (wraps around)

**To pause auto-play:**
- Tap **⏸ PAUSE** button
- Button changes to **▶ PLAY**
- Message stays on screen

**To resume auto-play:**
- Tap **▶ PLAY** button
- Auto-cycling resumes

### Manual Navigation

**Previous Message:**
- Tap **⏮ PREVIOUS** button
- Auto-play stops automatically
- Wraps to last message from first

**Next Message:**
- Tap **NEXT ⏭** button
- Auto-play stops automatically
- Wraps to first message from last

### Message Features

**Long Messages:**
- Automatically scroll horizontally
- Marquee effect for readability
- Continuous scrolling

**Message Information:**
- VMS unit reference (e.g., "A9-NORTH-001")
- Current position (e.g., "2/15")
- Total number of messages

### LED-Style Aesthetics

- **Amber Text** (#FFB000) - Standard road sign color
- **Black Background** - Simulates real VMS units
- **Monospace Font** - Authentic digital display
- **Glow Effect** - LED-style illumination
- **Indicator Dots** - Three amber dots for realism

### Offline Viewing

✅ **Works without internet!**
- Views cached VMS data
- Shows previously loaded messages
- No data charges while offline

**To load new messages:**
1. Return to main menu
2. Tap "Load All Items" (requires internet)
3. Go back to Road Sign Display

---

## 🔍 Search & Filter

### Text Search

1. Enter search term in **search box**
2. Tap **Search** button (or press enter on keyboard)
3. Results filter instantly

**Search works on:**
- Road names (M8, A9, etc.)
- Location descriptions
- Incident IDs
- Message content

**Example searches:**
- `M8` - All M8 incidents
- `accident` - All accidents
- `J16` - Junction 16 incidents

### Date Search

1. Tap **date search field**
2. Select date from **calendar picker**
3. View items for that date

**Date format:** DD/MM/YYYY

**Tips:**
- Search for specific incident dates
- Find roadworks on travel date
- Plan around scheduled works

### Refresh Data

**To get latest updates:**
1. Tap **↻ Refresh** button
2. Wait for data reload
3. New information displays

**Note:** Requires internet connection

---

## ❓ Troubleshooting

### Common Issues

#### "Please Check Your Internet Connection"

**Problem:** No network access

**Solutions:**
1. Check WiFi or mobile data is enabled
2. Verify signal strength
3. Try switching between WiFi/mobile data
4. Restart the app

---

#### No Data Showing

**Problem:** Empty list after loading

**Solutions:**
1. Check internet connection
2. Tap "Refresh" button
3. Try "Load All Items" option
4. Wait a few moments for server response

---

#### Road Sign Display Shows "NO MESSAGES AVAILABLE"

**Problem:** No VMS data loaded

**Solutions:**
1. Return to main menu
2. Tap "Load All Items"
3. Wait for loading to complete
4. Return to Road Sign Display

---

#### Map Button Not Working

**Problem:** Google Maps not opening

**Solutions:**
1. Ensure Google Maps is installed
2. Update Google Maps to latest version
3. Check location permissions
4. Restart device if needed

---

### Performance Tips

**For smooth experience:**
- ✅ Close other apps to free memory
- ✅ Use WiFi for faster loading
- ✅ Clear app cache if sluggish
- ✅ Update to latest app version

---

## 💡 Tips & Tricks

### Best Practices

**1. Pre-load Data Before Travel**
- Load all items before leaving WiFi
- View offline during journey
- Reduce data usage

**2. Use Road Sign Display Offline**
- Perfect for passengers
- No data needed
- Quick message overview

**3. Bookmark Frequent Routes**
- Use search for your routes
- Faster than browsing all items
- Example: "M8" or "A9"

**4. Check Before Long Journeys**
- Load planned roadworks
- Check incident reports
- Plan alternative routes

**5. Enable Dark Mode**
- Better for night driving
- Reduces eye strain
- Automatic with system settings

### Advanced Features

**Multi-Word Search:**
- Search: `M8 westbound`
- Narrows results effectively

**Date Range Planning:**
- Check start AND end dates
- Plan around multi-day roadworks

**Map Quick Access:**
- Tap map button instantly
- Get directions immediately
- Share location with others

### Accessibility

**Text Size:**
- Uses system font size settings
- Adjustable in Android settings
- App respects preferences

**Color Contrast:**
- High contrast in dark mode
- Readable in bright sunlight
- Color-blind friendly design

**Screen Readers:**
- Compatible with TalkBack
- Descriptive button labels
- Accessible navigation

---

## 📞 Support

### Getting Help

**For technical issues:**
- Check this guide first
- Review troubleshooting section
- Update to latest version

**For feedback:**
- Report bugs through app store
- Suggest features
- Share your experience

---

## 🎯 Quick Reference

| Action | Steps |
|--------|-------|
| View incidents | Main Menu → Current Incidents |
| View roadworks | Main Menu → Current/Planned Roadworks |
| View all data | Main Menu → Load All Items |
| Road sign display | Main Menu → Road Sign Display |
| Search traffic | Load data → Enter search → Tap Search |
| View on map | Find item → Tap [VIEW ON MAP] |
| Refresh data | Tap ↻ Refresh button |
| Pause auto-play | Road Sign → Tap ⏸ PAUSE |
| Next message | Road Sign → Tap NEXT ⏭ |
| Previous message | Road Sign → Tap ⏮ PREVIOUS |

---

## 📱 App Version

**Current Version:** 1.0 (Latest Update)

**What's New:**
- ✅ Digital Road Sign Display
- ✅ Material Design 3
- ✅ Improved security
- ✅ Better offline support
- ✅ Android 15 support

---

## 🚗 Safe Driving Reminder

**IMPORTANT:**
- ⚠️ Do NOT use phone while driving
- ⚠️ Pull over safely to check app
- ⚠️ Use hands-free for navigation
- ⚠️ Safety first, always!

---

**Enjoy safer travels with Traffic Scotland!** 🚦✨

*Last updated: January 2025*
