# Navix Browser

A modern, Java-based web browser built on the Java Chromium Embedded Framework (JCEF), offering a feature-rich browsing experience with enhanced privacy and customization options.

## 🌟 Features

### Core Browser Features
- **Tabbed Browsing**: Multiple tabs with intuitive navigation
- **Modern UI Themes**: Choose from Modern, System, or Cross-Platform themes
- **Custom New Tab Page**: Beautiful start page with quick access to popular sites
- **Search Integration**: Customizable search engines with suggestions
- **Download Manager**: Advanced download management with pause/resume capabilities

### Privacy & Security
- **Ad Blocking**: Built-in ad blocker for cleaner browsing
- **Tracker Blocking**: Protection against tracking scripts
- **Safe Browsing**: Google Safe Browsing integration for malware protection
- **Custom User Agent**: Platform-specific user agent strings

### Customization
- **Flexible Settings**: Comprehensive settings panel for customization
- **Hardware Acceleration**: Optional hardware acceleration support
- **Debug Mode**: Built-in debugging capabilities for development
- **Force Dark Mode**: System-wide dark mode enforcement

## 🔧 System Requirements

- **Java**: Java 11 or higher
- **Operating Systems**: Windows, macOS, Linux
- **Memory**: Minimum 4GB RAM recommended
- **Storage**: At least 1GB free disk space

## 🚀 Installation & Setup

### Prerequisites
1. Install Java 11 or higher
2. Ensure `JAVA_HOME` is properly set
3. Install Maven for building from source

### Building from Source
```bash
# Clone the repository
git clone https://github.com/UnknownCoder56/navixbrowser.git
cd navixbrowser

# Create the SECRETS.java file (required for Google Safe Browsing)
# Copy the template and add your Google API key:
cp src/main/java/com/uniqueapps/navixbrowser/object/SECRETS.java.template src/main/java/com/uniqueapps/navixbrowser/object/SECRETS.java
# Edit SECRETS.java and replace "your-google-api-key-here" with your actual API key

# Build the project
mvn clean compile

# Package the application
mvn package

# Run the application
java -jar target/navixbrowser-2.0-jar-with-dependencies.jar
```

### First-Time Setup
On first run, Navix will:
- Download the JCEF bundle automatically
- Create configuration directories (`data/`, `cache/`, `themes/`)
- Initialize default settings

## ⚙️ Configuration

### Settings Overview
Access settings through the browser menu or by navigating to `navix://settings`

| Setting | Description | Default |
|---------|-------------|---------|
| **HAL** | Hardware Acceleration | Enabled |
| **OSR** | Off-Screen Rendering | Disabled |
| **Launch Maximized** | Start browser maximized | Enabled |
| **Search Engine** | Default search provider | Google |
| **Theme** | UI appearance | Modern |
| **Ad Block** | Block advertisements | Enabled |
| **Tracker Block** | Block tracking scripts | Enabled |
| **Safe Browsing** | Malware protection | Enabled |
| **Search Suggestions** | Auto-complete suggestions | Enabled |
| **Debug Port** | Remote debugging port | 8090 |

### Custom Search Engines
You can configure custom search engines by modifying the search engine URL in settings. Use `%s` as the placeholder for search terms.

Examples:
- Google: `https://google.com/search?q=%s`
- DuckDuckGo: `https://duckduckgo.com/?q=%s`
- Bing: `https://www.bing.com/search?q=%s`

### Advanced Configuration
- **Custom Arguments**: Add Chromium command-line arguments in settings
- **Debug Mode**: Enable remote debugging on port 8090 (configurable)
- **Data Directory**: User data stored in `./data/`
- **Cache Directory**: Browser cache in `./cache/`

## 🖥️ Usage

### Basic Navigation
- **New Tab**: `Ctrl+T` (Cmd+T on macOS)
- **Close Tab**: Click the × button or `Ctrl+W`
- **Refresh**: `F5` or `Ctrl+R`
- **Back/Forward**: Use navigation buttons or browser shortcuts

### Download Management
- Downloads are managed in a dedicated tab
- Pause, resume, or cancel downloads
- View download history and file locations

### Themes
Switch between three UI themes:
1. **Modern**: FlatLaf-based modern interface
2. **System**: Native operating system theme
3. **Cross-Platform**: Java Swing default theme

## 🛠️ Development

### Project Structure
```
navixbrowser/
├── src/main/java/com/uniqueapps/navixbrowser/
│   ├── Main.java                 # Application entry point
│   ├── component/                # UI components
│   ├── handler/                  # Event handlers
│   ├── listener/                 # Event listeners
│   └── object/                   # Data objects
├── src/main/resources/           # Application resources
│   ├── images/                   # Icons and images
│   ├── themes/                   # UI theme files
│   └── resources/                # HTML and CSS files
└── pom.xml                       # Maven configuration
```

### Key Dependencies
- **JCEF**: Java Chromium Embedded Framework
- **FlatLaf**: Modern Look and Feel
- **Google Safe Browsing API**: Security features
- **Apache Tika**: File type detection

### Debug Mode
Enable debug mode to access Chromium DevTools:
1. Set debug port in settings (default: 8090)
2. Navigate to `http://localhost:8090` in any browser
3. Access full Chromium debugging capabilities

## 📝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Coding Standards
- Follow Java naming conventions
- Add Javadoc comments for public methods
- Ensure thread safety for UI operations
- Test on multiple platforms when possible

## 🐛 Troubleshooting

### Common Issues

**Build Errors**
- Ensure Java 11+ is installed and `JAVA_HOME` is set
- Verify Maven is properly installed
- Create the `SECRETS.java` file with valid API keys

**Runtime Issues**
- Check that JCEF bundle downloaded successfully
- Verify sufficient disk space for cache and data directories
- Try disabling hardware acceleration if experiencing graphics issues

**Performance Issues**
- Enable hardware acceleration (HAL) if available
- Increase Java heap size: `java -Xmx4g -jar navixbrowser.jar`
- Clear browser cache and data directories

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🔗 Links

- [JCEF Documentation](https://bitbucket.org/chromiumembedded/java-cef)
- [FlatLaf Documentation](https://www.formdev.com/flatlaf/)
- [Report Issues](https://github.com/UnknownCoder56/navixbrowser/issues)

## 📊 Version Information

**Current Version**: 2.1  
**JCEF Version**: 126.2.0  
**Java Requirement**: 11+  

---

*Made with ❤️ using Java and JCEF*
