# ScreenVocab - Vision & Scope Document

**Version 1.0**  
**Prepared by:** Tram Anh Nguyen  
**Date:** July 6, 2025

---

## 1. Business Requirements

### 1.1 Background

Language learners often lose connection with their target language during busy periods, leading to gradual vocabulary decay. Traditional study apps require dedicated time and motivation, which many users struggle to maintain consistently. 

**Personal Origin Story**: The creator has maintained an HSK vocabulary wallpaper for months, finding that even casual, partial viewing of words provides valuable daily language exposure and psychological connection to Chinese learning.

### 1.2 Business Problem or Opportunity

**Situation**: Users unlock their phones 50-100 times daily but get zero language learning benefit from this routine action.

**Problem**: Vocabulary learning apps require active engagement, which creates friction. When life gets busy, users stop opening apps and lose language connection entirely.

**Implication**: Without consistent exposure, vocabulary retention drops and learners feel disconnected from their target language, leading to motivation loss and abandonment.

**Benefit**: Transform routine phone unlocks into micro-learning moments. Even casual glances at vocabulary maintain language connection and provide cumulative learning benefits.

**Vision**: Turn every lock screen into a personalized vocabulary reference that maintains daily language exposure without requiring additional time or effort.

### 1.3 Business Objectives

- **Primary**: Deliver functional Android app within 1 week (July 13, 2025)
- **Secondary**: Deliver web app version within the same 1-week timeline
- **Academic**: Demonstrate cross-platform development with shared cloud backend
- **Long-term**: Expand features and refine user experience based on usage data

### 1.4 Success Metrics

**Academic Success (Primary):**
- Both Android app and web app submitted on time
- Real-time data sync between platforms demonstrated
- Clean, professional UI on both platforms
- Impressive cross-platform demo for presentation

**Technical Success (Secondary):**
- Firebase integration working across platforms
- Canvas wallpaper generation on both Android and web
- Shared backend architecture demonstrating scalability principles

### 1.5 Vision Statement

**For** language learners who want to maintain daily connection with their target language  
**Who** struggle with consistent vocabulary app usage  
**ScreenVocab** is a vocabulary wallpaper generator  
**That** transforms routine phone unlocks into passive learning moments  
**Unlike** traditional flashcard apps that require dedicated study time  
**Our product** provides effortless vocabulary exposure through beautiful, customizable lock screen designs

### 1.6 Business Risks

- **Low engagement**: Users may not change wallpapers regularly
- **Visual overload**: Too many words could make wallpapers cluttered or hard to read
- **Technical constraints**: Android wallpaper APIs vary across manufacturers
- **Learning effectiveness**: Passive exposure alone may have limited retention benefits

**Risk Mitigation:**
- Start with personal use to validate core concept
- Limit initial word count to 10-15 per wallpaper (based on HSK chart insights)
- Focus on making wallpapers aesthetically pleasing first
- Accept that partial/occasional viewing still provides value

### 1.7 Assumptions and Dependencies

- **Core assumption**: Brief, casual vocabulary exposure provides meaningful language connection
- **Technical**: Android Canvas API can generate quality wallpapers
- **User behavior**: People will manually set wallpapers if they find them valuable
- **Learning theory**: Spaced repetition through wallpaper rotation will improve retention

---

## 2. Scope and Limitations

### 2.1 Major Features

1. **User Authentication** (Firebase Auth - optional for wallpaper creation)
2. **Vocabulary Management** (add/edit/delete words, with cloud sync for registered users)
3. **Wallpaper Generator** with Canvas API and multiple themes
4. **Gallery Integration** for saving wallpapers to device
5. **Cross-platform Foundation** (architected for web app expansion)

### 2.2 Scope of Initial Release (1-Week MVP)

**Android App:**
- User registration/login with Firebase Auth
- Basic word input and management
- Canvas wallpaper generation (2-3 themes)
- Save wallpapers to device gallery
- Real-time sync with cloud database

**Web App:**
- Same Firebase authentication and data
- HTML5 Canvas wallpaper generation
- Responsive design for desktop/mobile browsers
- Download wallpapers functionality
- Seamless cross-platform experience

**Shared Backend:**
- Firebase Firestore for data storage
- Real-time synchronization between platforms
- User account management
- Word collection storage

### 2.3 Scope of Subsequent Releases

**Post-Academic Phase:**
- Guest mode functionality (create wallpapers without signup)
- Advanced theming and customization options
- Spaced repetition algorithms for word rotation
- Import/export functionality (CSV, integration with other apps)
- Social features and community sharing
- iOS native app development

### 2.4 Limitations and Exclusions

- No iOS support initially
- No audio pronunciation or detailed definitions
- No social features or user accounts
- No automatic wallpaper setting (manual process)
- No integration with existing language learning platforms

---

## 3. Business Context

### 3.1 Stakeholder Profiles

| Stakeholder | Roles | Interests | Influence | Needs | Concerns |
|-------------|-------|-----------|-----------|--------|----------|
| **Creator/Primary User** | Developer & Test User | Personal language learning tool | High | Simple, beautiful vocabulary exposure | Time investment vs. learning benefit |
| **Language Learning Community** | Potential Users | Effective, low-effort learning tools | Medium | Customizable, multi-language support | App complexity, visual appeal |
| **Casual Language Learners** | Secondary Users | Maintaining language connection | Low | Effortless daily exposure | Overwhelming information density |

### 3.2 Project Priorities

| Dimension | Driver | Constraint | Degree of Freedom |
|-----------|---------|-------------|-------------------|
| **Schedule** | Personal use by August 2025 | 6-8 weeks development time | Minimal - hard deadline |
| **Features** | Must generate readable wallpapers | Limit to core functionality | Can defer advanced features |
| **Quality** | Wallpapers must be aesthetically pleasing | Limited design skills/time | High - core to success |
| **Scope** | Solve personal problem first | Solo developer, no team | Can expand if personally successful |

### 3.3 Deployment Considerations

- **Target Platform**: Android 8.0+ (API 26+) for broader compatibility
- **Permissions**: Storage access for wallpaper saving
- **Distribution**: Initially personal use, possibly F-Droid or Play Store later
- **Offline-first**: No internet required after installation
- **Device Support**: Focus on common Android phones (Samsung, Pixel, OnePlus)

---

## 4. Development Strategy

### 4.1 Technology Decisions

**Android App (Week 1):**
- **Language**: Kotlin (modern, concise, teacher-approved)
- **Database**: Firebase Firestore + Authentication
- **UI Framework**: Material Design Components
- **Canvas**: Android Canvas API for wallpaper generation
- **Architecture**: MVVM with LiveData

**Web App (Week 2+):**
- **Framework**: React.js or Vue.js
- **Database**: Same Firebase (seamless sync)
- **Canvas**: HTML5 Canvas API
- **Hosting**: Firebase Hosting for integrated ecosystem

### 4.2 Development Timeline

**Day 1-2: Shared Backend & Architecture**
- Firebase project setup (Auth + Firestore)
- Database schema design
- Authentication flow design
- Wallpaper generation logic planning

**Day 3-4: Android Development**
- Android Studio project setup
- Firebase SDK integration
- Canvas wallpaper generation
- Basic UI with Material Design

**Day 5-6: Web Development**
- React project setup
- Firebase Web SDK integration
- HTML5 Canvas wallpaper generation
- Responsive UI development

**Day 7: Integration & Demo**
- Cross-platform sync testing
- UI polish and bug fixes
- Demo preparation and documentation

### 4.3 Success Validation

The app succeeds if the creator uses it daily for 30+ days and finds value in the passive vocabulary exposure, similar to the positive experience with the HSK wallpaper.

---

*This document reflects a personal project born from real experience with vocabulary wallpapers. The primary goal is solving a genuine personal problem, with potential community sharing as a secondary benefit.*