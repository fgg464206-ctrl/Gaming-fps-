# Gaming FPS Monitor - Shizuku-Based Performance Tracker

A comprehensive Android application providing real-time FPS overlay and gaming performance monitoring using Shizuku framework.

## ✨ Features

### 🎮 FPS Overlay
- Real-time FPS counter displayed as an overlay
- Current FPS tracking
- Customizable overlay position
- Non-intrusive floating window

### 📊 Performance Monitoring
- CPU Usage tracking
- Memory consumption monitoring
- Device temperature monitoring
- Battery level tracking
- Real-time performance metrics broadcast

### 🔐 Shizuku Integration
- Privileged system access via Shizuku
- Shell command execution capabilities
- Permission management

## 📁 Project Structure

```
Gaming-fps-/
├── src/main/java/com/fgg464206/gamingfps/
│   ├── ui/
│   │   └── MainActivity.kt
│   ├── service/
│   │   ├── FpsOverlayService.kt
│   │   └── PerformanceMonitorService.kt
│   └── shizuku/
│       └── ShizukuManager.kt
├── build.gradle
├── AndroidManifest.xml
└── README.md
```

## 🛠️ Requirements

- Android 9.0+ (API 28+)
- Shizuku v13.1.0+
- System Alert Window permission

## 📥 Installation

1. **Build the APK**
   ```bash
   ./gradlew build
   ```

2. **Install on Device**
   ```bash
   adb install app/build/outputs/apk/debug/Gaming-fps-*.apk
   ```

3. **Grant Permissions**
   - Enable Shizuku on your device
   - Grant permissions when requested
   - Allow overlay permission in system settings

## 🚀 Usage

1. Open Gaming FPS Monitor
2. Check Shizuku connection status
3. Enable FPS Overlay toggle to start FPS monitoring
4. Enable Performance Monitor toggle for system metrics

## 📦 Dependencies

- androidx.appcompat:appcompat:1.6.1
- androidx.core:core:1.12.0
- dev.rikka.shizuku:api:13.1.0
- dev.rikka.shizuku:provider:13.1.0
- org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3

## 🔐 Permissions

```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="moe.shizuku.privileged.api.PERMISSION_MANAGER_API" />
```

## 🐛 Troubleshooting

- **Shizuku Not Available**: Ensure Shizuku is installed and running
- **Overlay Not Showing**: Check SYSTEM_ALERT_WINDOW permission
- **Performance Metrics Not Updating**: Verify /proc/ and /sys/ directory access

## 📄 License

MIT License

## 👨‍💻 Author

**fgg464206-ctrl** - Initial development
