# Navix Browser

Navix is a modern, feature-rich web browser built with Java and the Java Chromium Embedded Framework (JCEF). It provides a lightweight yet powerful browsing experience with built-in privacy features, customizable themes, and extensive configuration options.

## Features

### 🌐 **Core Browsing**
- Full web browsing capabilities powered by Chromium Embedded Framework
- Tabbed browsing interface with tab management
- Download manager with progress tracking
- Built-in new tab page with quick access to popular sites
- Search suggestions and customizable search engines

### 🛡️ **Privacy & Security**
- **Ad Blocking**: Built-in ad blocker to improve browsing speed and privacy
- **Tracker Protection**: Block tracking scripts and analytics
- **Safe Browsing**: Google Safe Browsing integration for malware protection
- **Crypto Miner Protection**: Blocks cryptocurrency mining scripts

### 🎨 **Themes & Customization**
- **Modern Theme**: Sleek FlatLaf-based interface with light/dark variants
- **System Theme**: Adapts to your operating system's look and feel
- **Cross-Platform Theme**: Consistent appearance across all platforms
- **Force Dark Mode**: Override website themes with dark mode
- **Customizable Background Colors**: Set custom background colors for modern theme

### ⚙️ **Advanced Configuration**
- Hardware Acceleration (HAL) support
- Off-Screen Rendering (OSR) options
- Custom launch arguments for Chromium
- Debug port configuration for development
- Configurable new tab URL
- Launch maximized option

### 🔧 **Developer Features**
- DevTools integration for web development
- Debug port access for remote debugging
- Detailed logging system
- Print functionality

## Screenshots

*New Tab Page with quick access links*
*Settings panel with extensive customization options*

## Requirements

- **Java**: Java 11 or higher
- **Operating System**: Windows, macOS, or Linux
- **Memory**: At least 512MB RAM (1GB+ recommended)

## Installation

### Option 1: Download Pre-built Release
1. Download the latest release from the [Releases](https://github.com/UnknownCoder56/navixbrowser/releases) page
2. Extract the archive
3. Run the executable JAR file

### Option 2: Build from Source

#### Prerequisites
- Java Development Kit (JDK) 11 or higher
- Apache Maven 3.6 or higher

#### Build Steps
```bash
# Clone the repository
git clone https://github.com/UnknownCoder56/navixbrowser.git
cd navixbrowser

# Build the project
mvn clean package

# Run the browser
java -jar target/navixbrowser-2.0-jar-with-dependencies.jar
```

## Configuration

### Google Safe Browsing Setup
To enable Safe Browsing protection:

1. Obtain a Google Safe Browsing API key from the [Google Cloud Console](https://console.cloud.google.com/)
2. Replace `YOUR_GOOGLE_API_KEY_HERE` in `src/main/java/com/uniqueapps/navixbrowser/object/SECRETS.java`
3. Rebuild the project

### Settings Configuration
The browser stores settings in the system preferences. You can configure:

- **Search Engine**: Default search engine URL
- **Theme**: Choose between Modern, System, or Cross-Platform themes
- **Privacy Options**: Toggle ad blocking, tracker protection, and safe browsing
- **Launch Options**: Set maximized launch, new tab URL
- **Advanced**: Custom Chromium arguments, debug port settings

Access settings through the browser's settings tab or menu.

## Usage

### Basic Browsing
1. Launch Navix Browser
2. Enter URLs in the address bar or use the search functionality
3. Open new tabs with Ctrl+T (or Cmd+T on macOS)
4. Navigate with standard browser shortcuts

### Privacy Features
- **Ad Blocking**: Automatically enabled by default
- **Tracker Protection**: Blocks common tracking scripts and analytics
- **Safe Browsing**: Warns about malicious websites (requires API key setup)

### Downloads
- Downloads are managed through the built-in download manager
- Access downloads through the downloads tab
- Downloads are saved to the configured downloads directory

### Themes
1. Open Settings
2. Navigate to Theme section
3. Choose from:
   - **Modern**: Clean, modern interface with customizable colors
   - **System**: Matches your operating system's theme
   - **Cross-Platform**: Consistent Java Swing look and feel

## Development

### Project Structure
```
src/
├── main/
│   ├── java/com/uniqueapps/navixbrowser/
│   │   ├── Main.java              # Application entry point
│   │   ├── component/             # UI components
│   │   ├── handler/              # Request and event handlers
│   │   ├── listener/             # Event listeners
│   │   └── object/               # Data objects
│   └── resources/
│       ├── resources/            # Web resources (new tab pages, CSS)
│       ├── themes/              # Theme configuration files
│       └── images/              # Application icons and images
```

### Key Components
- **Main.java**: Application startup and configuration
- **BrowserWindow.java**: Main browser window
- **BrowserTabbedPane.java**: Tab management
- **UserSettings.java**: Configuration management
- **NavixResourceRequestHandler.java**: Request filtering and ad blocking

### Building and Testing
```bash
# Compile only
mvn compile

# Run tests (if available)
mvn test

# Create distributable JAR
mvn package

# Clean build artifacts
mvn clean
```

### Adding Features
1. Fork the repository
2. Create a feature branch
3. Implement your changes
4. Test thoroughly
5. Submit a pull request

## Dependencies

- **JCEF Maven**: Chromium Embedded Framework for Java
- **FlatLaf**: Modern look and feel for Swing applications
- **Google Safe Browsing API**: Malware and phishing protection
- **Apache Tika**: MIME type detection for downloads

## Contributing

Contributions are welcome! Please follow these guidelines:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Coding Standards
- Follow Java naming conventions
- Add JavaDoc comments for public methods
- Ensure code compiles without warnings
- Test your changes thoroughly

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

- **Issues**: Report bugs or request features on [GitHub Issues](https://github.com/UnknownCoder56/navixbrowser/issues)
- **Discussions**: Join community discussions on [GitHub Discussions](https://github.com/UnknownCoder56/navixbrowser/discussions)

## Acknowledgments

- **Chromium Embedded Framework**: For providing the web engine
- **FlatLaf**: For the modern look and feel
- **JCEF Maven**: For simplified JCEF integration
- **Google Safe Browsing**: For security protection

---

*Navix Browser v2.1 - A modern, privacy-focused browser built with Java*
