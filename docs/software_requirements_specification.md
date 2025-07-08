# ScreenVocab - Software Requirements Specification

**Version 1.0**  
**Prepared by:** Tram Anh Nguyen  
**Date:** July 7, 2025

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Overall Description](#2-overall-description)
3. [System Features](#3-system-features)
4. [Data Requirements](#4-data-requirements)
5. [External Interface Requirements](#5-external-interface-requirements)
6. [Quality Attributes](#6-quality-attributes)
7. [Other Requirements](#7-other-requirements)
8. [Appendix A: Glossary](#appendix-a-glossary)

---

## Revision History

| **Name** | **Date** | **Reason For Changes** | **Version** |
|----------|----------|------------------------|-------------|
| Initial Draft | July 7, 2025 | Initial SRS creation | 1.0 |

---

## 1. Introduction

### 1.1 Purpose

This Software Requirements Specification (SRS) defines the functional and non-functional requirements for ScreenVocab, a cross-platform vocabulary wallpaper generator. The document is intended for developers, project managers, testers, and academic evaluators who need to understand the system's capabilities and constraints.

The primary audience includes:
- Mobile app developers (Android/Kotlin)
- Web developers (React/JavaScript)
- Firebase backend integrators
- Academic project evaluators

### 1.2 Document Conventions

- **High Priority:** Core functionality required for MVP (1-week timeline)
- **Medium Priority:** Important features for complete user experience
- **Low Priority:** Enhancement features for future releases
- **REQ-XXX:** Unique requirement identifiers for traceability
- **Bold text:** Critical constraints or requirements
- *Italics:* User interface elements and system responses

### 1.3 Project Scope

ScreenVocab transforms routine phone unlocks into vocabulary learning opportunities by generating custom wallpapers containing user-defined word collections. The system consists of:

1. **Android mobile application** (primary platform)
2. **Web application** (secondary platform)
3. **Firebase cloud backend** (shared data layer)

The application enables users to create vocabulary collections, generate grid-based wallpapers similar to HSK reference charts, and maintain cross-platform synchronization of their learning materials.

### 1.4 References

- Vision & Scope Document v1.0 (July 6, 2025)
- Firebase Documentation: https://firebase.google.com/docs
- Android Canvas API Documentation
- HTML5 Canvas API Documentation
- Material Design Guidelines

---

## 2. Overall Description

### 2.1 Product Perspective

ScreenVocab is a new, standalone product that addresses the gap between traditional vocabulary learning apps and passive learning methods. The system architecture consists of:

```
[Android App] ←→ [Firebase Cloud] ←→ [Web App]
     ↓               ↓                 ↓
[Device Storage] [Cloudinary CDN] [Browser Storage]
```

The product integrates with:
- **Firebase Authentication** for user management
- **Firebase Firestore** for data synchronization
- **Android Gallery** for wallpaper storage
- **Web Browser Download** for file saving
- **Cloudinary CDN** for image storage and optimization

### 2.2 User Classes and Characteristics

| User Class | Characteristics | Technical Expertise | Primary Needs |
|------------|-----------------|-------------------|---------------|
| **Primary Language Learner** | Active vocabulary study, smartphone-dependent | Low-Medium | Effortless daily exposure, beautiful wallpapers |
| **Casual Language Enthusiast** | Occasional learning, aesthetic preference | Low | Simple word input, attractive designs |
| **Academic User** | Structured learning, multiple languages | Medium | Organization features, import/export capabilities |

**Favored User Class:** Primary Language Learner - drives core feature priorities and UI design decisions.

### 2.3 Operating Environment

**Mobile Platform:**
- **Android:** API Level 26+ (Android 8.0+), targeting API 34
- **Memory:** Minimum 2GB RAM, 50MB storage space
- **Network:** WiFi or cellular for sync; offline functionality required

**Web Platform:**
- **Browsers:** Chrome 90+, Firefox 88+, Safari 14+, Edge 90+
- **Features:** HTML5 Canvas support, localStorage API
- **Responsive:** Desktop (1920x1080+), tablet (768px+), mobile (375px+)

**Cloud Infrastructure:**
- **Firebase:** Authentication, Firestore, Hosting
- **Geographic:** Global deployment, emphasis on Asia-Pacific region

### 2.4 Design and Implementation Constraints

**Technical Constraints:**
- **Timeline:** 7-day development sprint (hard deadline)
- **Platforms:** Android-first development, web app secondary
- **Backend:** Firebase ecosystem only (no custom server)
- **Canvas API:** Limited to 2D graphics, no 3D rendering

**Business Constraints:**
- **Budget:** Free-tier Firebase limits
- **Team:** Solo developer
- **Academic:** Must demonstrate cross-platform sync for evaluation

**Regulatory Constraints:**
- **Data Privacy:** GDPR compliance for European users
- **Content:** No automatic translation services (avoid API costs)

### 2.5 Assumptions and Dependencies

**Assumptions:**
- Users will manually set generated wallpapers (no automatic wallpaper setting)
- Firebase free tier sufficient for development and initial testing
- Canvas API performance acceptable for 150-word wallpapers
- Users prefer grid-based layouts over artistic arrangements

**Dependencies:**
- Firebase service availability and API stability
- Android Canvas API compatibility across device manufacturers
- HTML5 Canvas support in target browsers
- Internet connectivity for initial setup and sync

---

## 3. System Features

### 3.1 User Authentication and Account Management

**Priority:** High  
**Feature ID:** REQ-AUTH

#### Description
Firebase-based authentication system supporting email/password registration with optional guest mode for trial usage.

#### Stimulus/Response Sequences
1. **Guest Access:** User opens app → Direct access to word input → Single wallpaper generation allowed
2. **Registration:** User clicks "Sign Up" → Enter email/password → Email verification → Account creation
3. **Login:** User enters credentials → Firebase authentication → Redirect to dashboard
4. **Account Upgrade:** Guest user attempts second wallpaper → Registration prompt → Account creation

#### Functional Requirements
- **REQ-AUTH-001:** System shall authenticate users via Firebase Auth with email/password
- **REQ-AUTH-002:** Guest users shall create exactly one wallpaper without registration
- **REQ-AUTH-003:** System shall prompt guest users for registration when attempting second wallpaper
- **REQ-AUTH-004:** System shall maintain login state across app sessions
- **REQ-AUTH-005:** System shall provide password reset functionality via email

### 3.2 Vocabulary Collection Management

**Priority:** High  
**Feature ID:** REQ-VOCAB

#### Description
Core functionality for creating, editing, and organizing vocabulary words into collections with flexible field structure supporting multiple languages.

#### Stimulus/Response Sequences
1. **Add Words:** User clicks "Add Word" → Input form with 2-3 fields → Save to collection
2. **Create Collection:** User adds words → System suggests "Save to collection?" → User names collection
3. **Edit Words:** User selects word → Edit form → Save changes → Real-time sync
4. **Import Data:** User selects CSV/Excel file → System parses → Preview import → Confirm

#### Functional Requirements
- **REQ-VOCAB-001:** System shall support word entries with 2-3 configurable text fields
- **REQ-VOCAB-002:** System shall organize words into user-defined collections
- **REQ-VOCAB-003:** System shall limit collections to 150 words maximum
- **REQ-VOCAB-004:** System shall provide manual word input interface
- **REQ-VOCAB-005:** System shall support CSV/Excel import functionality *(Medium Priority)*
- **REQ-VOCAB-006:** System shall support copy-paste word input *(Medium Priority)*
- **REQ-VOCAB-007:** System shall validate word entries (no duplicates within collection)
- **REQ-VOCAB-008:** System shall sync word collections across platforms in real-time

### 3.3 Wallpaper Generation Engine

**Priority:** High  
**Feature ID:** REQ-WALLPAPER

#### Description
Canvas-based wallpaper generation creating grid-layout designs similar to HSK reference charts with customizable themes and automatic text sizing.

#### Stimulus/Response Sequences
1. **Create Wallpaper:** User selects collection → Choose theme → Preview layout → Generate wallpaper
2. **Auto-Layout:** System calculates optimal grid (5x10, 10x15, etc.) based on word count
3. **Manual Layout:** User overrides auto-layout → Selects custom grid dimensions
4. **Text Sizing:** System detects minimum readable size → Warns if text too small → Suggests splitting collection

#### Functional Requirements
- **REQ-WALL-001:** System shall generate wallpapers at 1080x2340 resolution (primary)
- **REQ-WALL-002:** System shall arrange words in grid layout with automatic spacing
- **REQ-WALL-003:** System shall support 4 theme options: HSK Pink, Clean White, Soft Gray, Night Mode
- **REQ-WALL-004:** System shall calculate optimal grid dimensions automatically
- **REQ-WALL-005:** System shall allow user override of grid dimensions
- **REQ-WALL-006:** System shall provide full-screen preview before generation
- **REQ-WALL-007:** System shall complete wallpaper generation within 3 seconds
- **REQ-WALL-008:** System shall detect and warn when text becomes unreadable (< 16px)
- **REQ-WALL-009:** System shall save wallpapers in PNG format (primary) and JPG format (secondary)
- **REQ-WALL-010:** System shall provide user-configurable text hierarchy (which field appears larger)

### 3.4 Cross-Platform Synchronization

**Priority:** High  
**Feature ID:** REQ-SYNC

#### Description
Real-time data synchronization between Android and web platforms using Firebase Firestore, ensuring consistent user experience across devices.

#### Stimulus/Response Sequences
1. **Real-time Sync:** User adds word on Android → Data syncs to Firebase → Web app updates automatically
2. **Offline Changes:** User edits offline → Changes queue locally → Sync when connection restored
3. **Conflict Resolution:** Same word edited on both platforms → Last-write-wins strategy

#### Functional Requirements
- **REQ-SYNC-001:** System shall sync user data within 2 seconds when online
- **REQ-SYNC-002:** System shall queue changes for sync when offline
- **REQ-SYNC-003:** System shall resolve conflicts using last-write-wins strategy
- **REQ-SYNC-004:** System shall maintain offline functionality for 30 days
- **REQ-SYNC-005:** System shall provide sync status indicators to users

### 3.5 File Management and Sharing

**Priority:** Medium  
**Feature ID:** REQ-FILES

#### Description
Wallpaper storage, organization, and sharing capabilities with device integration and cloud backup.

#### Stimulus/Response Sequences
1. **Save to Device:** User generates wallpaper → System saves to device gallery → Confirmation message
2. **View History:** User accesses wallpaper history → Grid view of generated wallpapers
3. **Share Wallpaper:** User selects wallpaper → Share via system share sheet *(Registered users only)*

#### Functional Requirements
- **REQ-FILES-001:** System shall save wallpapers to device gallery/downloads
- **REQ-FILES-002:** System shall maintain history of generated wallpapers
- **REQ-FILES-003:** System shall provide wallpaper sharing functionality for registered users
- **REQ-FILES-004:** System shall support both PNG and JPG export formats
- **REQ-FILES-005:** Web version shall generate mobile (9:16) and desktop (16:9) wallpapers
- **REQ-FILES-006:** System shall upload wallpapers to Cloudinary cloud storage
- **REQ-FILES-007:** System shall generate thumbnails (200x356) for wallpaper previews
- **REQ-FILES-008:** System shall provide wallpaper access across devices via cloud URLs
- **REQ-FILES-009:** System shall automatically optimize images for web delivery
- **REQ-FILES-010:** System shall support two visibility levels: private and public
- **REQ-FILES-011:** System shall generate unique shareable URLs for public wallpapers
- **REQ-FILES-012:** System shall provide web-based wallpaper viewing interface

---

## 4. Data Requirements

### 4.1 Logical Data Model

```
User
├── userId: string (Firebase UID)
├── profile: UserProfile
├── collections: Collection[]
└── wallpapers: Wallpaper[]

UserProfile
├── email: string
├── displayName: string
├── createdAt: timestamp
├── isGuest: boolean
└── lastSyncAt: timestamp

Collection
├── collectionId: string
├── name: string
├── words: Word[]
├── createdAt: timestamp
└── updatedAt: timestamp

Word
├── wordId: string
├── primaryText: string (required)
├── secondaryText: string (required)
├── tertiaryText: string (optional)
├── language: string
├── position: number
└── createdAt: timestamp

Wallpaper
├── wallpaperId: string
├── collectionId: string
├── theme: string
├── gridDimensions: {rows: number, cols: number}
├── textHierarchy: string[]
├── cloudinaryUrl: string (full resolution)
├── thumbnailUrl: string (200x356 preview)
├── localFileUrl: string (device storage path)
├── createdAt: timestamp
└── metadata: {resolution: string, format: string, fileSize: number}
```

### 4.2 Data Dictionary

| Field | Type | Length | Required | Description |
|-------|------|--------|----------|-------------|
| **userId** | String | 28 chars | Yes | Firebase authentication UID |
| **primaryText** | String | 50 chars | Yes | Main word/phrase in target language |
| **secondaryText** | String | 100 chars | Yes | Translation or pronunciation |
| **tertiaryText** | String | 100 chars | No | Additional info (IPA, furigana, etc.) |
| **language** | String | 5 chars | Yes | ISO language code (en, zh, ja, etc.) |
| **theme** | Enum | N/A | Yes | HSK_PINK, CLEAN_WHITE, SOFT_GRAY, NIGHT_MODE |
| **gridDimensions** | Object | N/A | Yes | {rows: 1-20, cols: 1-20} |

### 4.3 Data Acquisition, Integrity, Retention, and Disposal

**Data Acquisition:**
- User manual input via mobile/web interfaces
- CSV/Excel file import (parsed client-side)
- Copy-paste bulk input

**Data Integrity:**
- Firebase Firestore automatic validation
- Client-side validation before submission
- No duplicate words within same collection
- Text field length limits enforced

**Data Retention:**
- User data retained until account deletion
- Guest user data deleted after 7 days of inactivity
- Wallpaper files stored both locally and on Cloudinary CDN
- Cloud storage includes full images and auto-generated thumbnails

**Data Disposal:**
- Account deletion removes all user data within 30 days
- Local wallpaper files managed by user
- Temporary files cleaned up after generation

---

## 5. External Interface Requirements

### 5.1 User Interfaces

#### 5.1.1 Mobile App Interface (Android)

**Screen Layout Requirements:**
- **REQ-UI-001:** App shall follow Material Design 3 guidelines
- **REQ-UI-002:** All screens shall support portrait orientation primarily
- **REQ-UI-003:** Text input fields shall support international keyboards
- **REQ-UI-004:** Navigation shall use bottom navigation bar for main sections

**Key Screens:**
1. **Authentication Screen:** Email/password inputs, guest mode button
2. **Dashboard:** Collection list, "Create Wallpaper" FAB, sync status
3. **Word Input Screen:** Dynamic form with 2-3 text fields
4. **Wallpaper Generator:** Theme selection, grid controls, preview area
5. **Settings Screen:** Account management, export options

**Error Handling:**
- **REQ-UI-005:** System shall display user-friendly error messages
- **REQ-UI-006:** Network errors shall show retry options
- **REQ-UI-007:** Validation errors shall highlight specific fields

#### 5.1.2 Web App Interface

**Responsive Design:**
- **REQ-UI-008:** Interface shall adapt to screen sizes 320px to 1920px+
- **REQ-UI-009:** Desktop version shall utilize larger screen real estate
- **REQ-UI-010:** Mobile web version shall match Android app functionality

### 5.2 Software Interfaces

#### 5.2.1 Firebase Integration

**Authentication API:**
- **REQ-API-001:** System shall integrate Firebase Auth SDK v9+
- **REQ-API-002:** Authentication state shall persist across sessions
- **REQ-API-003:** Password reset shall use Firebase email templates

**Firestore Database:**
- **REQ-API-004:** System shall use Firestore Web SDK v9+ for real-time sync
- **REQ-API-005:** Offline persistence shall be enabled for mobile app
- **REQ-API-006:** Security rules shall enforce user data isolation

#### 5.2.2 Canvas API Integration

**Android Canvas:**
- **REQ-API-007:** System shall use Android Canvas API for wallpaper generation
- **REQ-API-008:** Canvas operations shall be performed on background thread
- **REQ-API-009:** Generated bitmaps shall be saved as PNG/JPG files

**HTML5 Canvas:**
- **REQ-API-010:** Web version shall use HTML5 Canvas for wallpaper generation
- **REQ-API-011:** Canvas shall support high-DPI displays
- **REQ-API-012:** Generated images shall be downloadable via blob URLs

#### 5.2.3 Cloudinary Integration

-   **REQ-API-013:** System shall integrate Cloudinary SDK for image uploads
-   **REQ-API-014:** System shall use Cloudinary transformations for thumbnail generation
-   **REQ-API-015:** System shall handle Cloudinary upload failures with retry logic
-   **REQ-API-016:** System shall utilize Cloudinary's auto-optimization features

### 5.3 Hardware Interfaces

**Android Device Requirements:**
- **REQ-HW-001:** System shall support ARM and x86 architectures
- **REQ-HW-002:** Minimum 2GB RAM for stable wallpaper generation
- **REQ-HW-003:** Storage write access for saving wallpapers to gallery

**Web Platform Requirements:**
- **REQ-HW-004:** System shall support devices with minimum 1GB RAM
- **REQ-HW-005:** Hardware acceleration shall be utilized when available

### 5.4 Communications Interfaces

**Network Requirements:**
- **REQ-NET-001:** System shall support HTTPS connections only
- **REQ-NET-002:** API calls shall timeout after 10 seconds
- **REQ-NET-003:** System shall gracefully handle network interruptions
- **REQ-NET-004:** Offline mode shall queue sync operations

**Data Transfer:**
- **REQ-NET-005:** Word synchronization shall use minimal bandwidth
- **REQ-NET-006:** System shall upload full-resolution images to Cloudinary
- **REQ-NET-007:** System shall prioritize thumbnail loading for wallpaper lists
- **REQ-NET-008:** System shall implement progressive image loading (thumbnail → full)

---

## 6. Quality Attributes

### 6.1 Usability

**Ease of Use:**
- **REQ-USAB-001:** New users shall create their first wallpaper within 3 minutes
- **REQ-USAB-002:** Word input shall require maximum 2 taps per word
- **REQ-USAB-003:** Wallpaper generation shall require maximum 3 taps from dashboard

**Accessibility:**
- **REQ-USAB-004:** App shall support screen readers for visually impaired users
- **REQ-USAB-005:** Text shall maintain 4.5:1 contrast ratio minimum
- **REQ-USAB-006:** Touch targets shall be minimum 44dp on Android, 44px on web

**Learning Curve:**
- **REQ-USAB-007:** App shall provide onboarding with sample vocabulary
- **REQ-USAB-008:** Complex features shall include contextual help

### 6.2 Performance

**Response Times:**
- **REQ-PERF-001:** App startup shall complete within 2 seconds
- **REQ-PERF-002:** Wallpaper generation shall complete within 3 seconds
- **REQ-PERF-003:** Data sync shall complete within 2 seconds for typical collections
- **REQ-PERF-004:** UI interactions shall respond within 100ms

**Resource Usage:**
- **REQ-PERF-005:** App shall use maximum 100MB RAM during normal operation
- **REQ-PERF-006:** Wallpaper generation shall not cause UI freezing
- **REQ-PERF-007:** Battery usage shall be minimal (no background processing)

**Scalability:**
- **REQ-PERF-008:** System shall handle 10,000 words per user
- **REQ-PERF-009:** Canvas operations shall scale with device capabilities
- **REQ-PERF-010:** Thumbnail images shall load within 1 second
- **REQ-PERF-011:** Full image downloads shall show progress indicators
- **REQ-PERF-012:** System shall cache thumbnails locally for 7 days

### 6.3 Security

**Data Protection:**
- **REQ-SEC-001:** User passwords shall be handled exclusively by Firebase Auth
- **REQ-SEC-002:** All data transmission shall use HTTPS/TLS 1.3
- **REQ-SEC-003:** User data shall be isolated using Firebase security rules
- **REQ-SEC-004:** Guest user data shall be automatically purged after 7 days

**Privacy:**
- **REQ-SEC-005:** System shall not collect analytics data initially
- **REQ-SEC-006:** User vocabulary shall never be shared with third parties
- **REQ-SEC-007:** System shall comply with GDPR requirements for European users

### 6.4 Reliability

**Error Handling:**
- **REQ-REL-001:** System shall recover gracefully from network failures
- **REQ-REL-002:** Canvas generation failures shall allow retry without data loss
- **REQ-REL-003:** App crashes shall preserve user input in draft state

**Data Integrity:**
- **REQ-REL-004:** Offline changes shall sync without data loss
- **REQ-REL-005:** Concurrent edits shall be resolved using last-write-wins
- **REQ-REL-006:** System shall detect and prevent duplicate word entries

### 6.5 Compatibility

**Platform Compatibility:**
- **REQ-COMP-001:** Android app shall support API levels 26-34
- **REQ-COMP-002:** Web app shall support browsers released within 2 years
- **REQ-COMP-003:** Data format shall be compatible between platforms

**Device Compatibility:**
- **REQ-COMP-004:** App shall adapt to screen sizes from 5" to 7" on Android
- **REQ-COMP-005:** Web app shall support screen sizes from 320px to 4K displays

---

## 7. Other Requirements

### 7.1 Legal and Regulatory Requirements

**Data Protection:**
- **REQ-LEGAL-001:** System shall comply with GDPR Article 17 (Right to be Forgotten)
- **REQ-LEGAL-002:** Privacy policy shall be accessible and clearly written
- **REQ-LEGAL-003:** User consent shall be obtained for data collection

**Intellectual Property:**
- **REQ-LEGAL-004:** System shall not infringe on existing wallpaper app patents
- **REQ-LEGAL-005:** Generated wallpapers shall be owned by users
- **REQ-LEGAL-006:** HSK vocabulary usage shall comply with fair use guidelines
- **REQ-LEGAL-007:** System shall comply with Cloudinary's terms of service
- **REQ-LEGAL-008:** Image storage shall respect user privacy via Cloudinary security features

### 7.2 Installation and Deployment

**Android Deployment:**
- **REQ-DEPLOY-001:** APK shall be under 50MB in size
- **REQ-DEPLOY-002:** App shall request minimal permissions (storage, network)
- **REQ-DEPLOY-003:** Installation shall not require root access

**Web Deployment:**
- **REQ-DEPLOY-004:** Web app shall be hosted on Firebase Hosting
- **REQ-DEPLOY-005:** Progressive Web App features shall be implemented
- **REQ-DEPLOY-006:** HTTPS shall be enforced for all connections

### 7.3 Operational Requirements

**Monitoring:**
- **REQ-OPS-001:** System shall log critical errors to Firebase Crashlytics
- **REQ-OPS-002:** Performance metrics shall be tracked for optimization
- **REQ-OPS-003:** User feedback mechanism shall be implemented

**Maintenance:**
- **REQ-OPS-004:** System shall support remote configuration updates
- **REQ-OPS-005:** Database schema shall support backward compatibility
- **REQ-OPS-006:** Rollback procedures shall be documented

---

## Appendix A: Glossary

**Canvas API:** Graphics API for drawing 2D graphics programmatically  
**Collection:** User-defined group of vocabulary words  
**Firebase:** Google's mobile and web development platform  
**Firestore:** NoSQL document database provided by Firebase  
**Guest Mode:** Limited app functionality without user registration  
**Grid Layout:** Arrangement of words in rows and columns  
**HSK:** Hanyu Shuiping Kaoshi - Chinese proficiency test  
**Material Design:** Google's design system for Android applications  
**MVP:** Minimum Viable Product - core features for initial release  
**PWA:** Progressive Web App - web app with native app features  
**Real-time Sync:** Immediate data synchronization across devices  
**Responsive Design:** UI that adapts to different screen sizes  
**SRS:** Software Requirements Specification  
**Wallpaper:** Background image displayed on device home/lock screen  
**Cloudinary:** Cloud-based image and video management platform  
**CDN:** Content Delivery Network for fast global image delivery  
**Thumbnail:** Small preview image optimized for quick loading  

---

*This SRS defines the complete requirements for ScreenVocab v1.0, focusing on core functionality deliverable within a 1-week development timeline while establishing the foundation for future feature expansion.*