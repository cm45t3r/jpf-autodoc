#!/bin/bash

# JPF AutoDoc - Gradle 9.0 Compatibility Setup Script
# This script helps configure your environment for Gradle 9.0 compatibility

set -e

echo "🔧 JPF AutoDoc - Gradle 9.0 Compatibility Setup"
echo "================================================="

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to get Java version
get_java_version() {
    if command_exists java; then
        java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1
    else
        echo "0"
    fi
}

# Check current Java version
CURRENT_JAVA=$(get_java_version)
echo "📋 Current Java version: $CURRENT_JAVA"

# Check if Java 17+ is available
JAVA_17_AVAILABLE=false
if [ "$CURRENT_JAVA" -ge 17 ]; then
    JAVA_17_AVAILABLE=true
    echo "✅ Java 17+ is available"
else
    echo "❌ Java 17+ is required for Gradle 9.0 compatibility"
fi

# Detect OS and package manager
OS="unknown"
if [[ "$OSTYPE" == "darwin"* ]]; then
    OS="macos"
    if command_exists brew; then
        PACKAGE_MANAGER="brew"
    fi
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    OS="linux"
    if command_exists apt; then
        PACKAGE_MANAGER="apt"
    elif command_exists yum; then
        PACKAGE_MANAGER="yum"
    fi
fi

echo "📱 Detected OS: $OS"

# Install Java 17 if needed
if [ "$JAVA_17_AVAILABLE" = false ]; then
    echo ""
    echo "🚀 Installing Java 17..."
    
    case "$PACKAGE_MANAGER" in
        "brew")
            echo "Using Homebrew to install OpenJDK 17..."
            brew install openjdk@17
            JAVA_17_HOME="/usr/local/Cellar/openjdk@17/*/libexec/openjdk.jdk/Contents/Home"
            JAVA_17_HOME=$(echo $JAVA_17_HOME)  # Expand glob
            ;;
        "apt")
            echo "Using APT to install OpenJDK 17..."
            sudo apt update
            sudo apt install -y openjdk-17-jdk
            JAVA_17_HOME="/usr/lib/jvm/java-17-openjdk-amd64"
            ;;
        "yum")
            echo "Using YUM to install OpenJDK 17..."
            sudo yum install -y java-17-openjdk-devel
            JAVA_17_HOME="/usr/lib/jvm/java-17-openjdk"
            ;;
        *)
            echo "❌ Automatic installation not supported for your system."
            echo "Please install Java 17+ manually from https://adoptium.net/"
            echo "Then update gradle.properties with:"
            echo "org.gradle.java.home=/path/to/java17"
            exit 1
            ;;
    esac
else
    # Use current Java if it's 17+
    JAVA_17_HOME="$JAVA_HOME"
    if [ -z "$JAVA_17_HOME" ]; then
        JAVA_17_HOME=$(dirname $(dirname $(which java)))
    fi
fi

echo "📂 Java 17 Home: $JAVA_17_HOME"

# Update gradle.properties
echo ""
echo "📝 Updating gradle.properties..."

# Create backup
if [ -f "gradle.properties" ]; then
    cp gradle.properties gradle.properties.backup
    echo "✅ Created backup: gradle.properties.backup"
fi

# Update or add org.gradle.java.home
if grep -q "org.gradle.java.home" gradle.properties 2>/dev/null; then
    # Uncomment and update existing line
    sed -i.tmp "s|# org.gradle.java.home=.*|org.gradle.java.home=$JAVA_17_HOME|g" gradle.properties
    sed -i.tmp "s|org.gradle.java.home=.*|org.gradle.java.home=$JAVA_17_HOME|g" gradle.properties
    rm gradle.properties.tmp
else
    # Add new line
    echo "org.gradle.java.home=$JAVA_17_HOME" >> gradle.properties
fi

echo "✅ Updated gradle.properties"

# Test the configuration
echo ""
echo "🧪 Testing Gradle configuration..."
if ./gradlew --version > /dev/null 2>&1; then
    echo "✅ Gradle configuration successful!"
    
    # Run build to check for deprecation warnings
    echo ""
    echo "🔍 Checking for deprecation warnings..."
    if ./gradlew build --warning-mode all 2>&1 | grep -q "Deprecated Gradle features"; then
        echo "⚠️  Still seeing deprecation warnings. Manual configuration may be needed."
        echo "Please check README_GRADLE_SETUP.md for troubleshooting."
    else
        echo "✅ No deprecation warnings found!"
    fi
else
    echo "❌ Gradle configuration failed. Please check manually."
    echo "See README_GRADLE_SETUP.md for troubleshooting."
fi

echo ""
echo "🎉 Setup complete!"
echo ""
echo "📚 For more information, see README_GRADLE_SETUP.md"
echo "🔧 To verify toolchain setup: ./gradlew -q javaToolchains"
