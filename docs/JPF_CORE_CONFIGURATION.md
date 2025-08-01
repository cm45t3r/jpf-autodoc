# JPF Core Configuration

This document explains how to configure the jpf-core dependency for different scenarios.

## Overview

JPF AutoDoc depends on jpf-core, which is not published to Maven Central. The build system automatically searches for jpf-core JAR files in multiple locations.

## Automatic Search Locations

The build system automatically searches for `jpf-core-DEVELOPMENT-SNAPSHOT.jar` in the following locations (in order):

1. `../jpf-core/build/libs/` (default - parent directory)
2. `../jpf-core/build/` (parent directory, direct build folder)
3. `./jpf-core/build/libs/` (current directory)
4. `./jpf-core/build/` (current directory, direct build folder)
5. `{jpf.core.path}/build/libs/` (custom path via system property)
6. `{jpf.core.path}/build/` (custom path via system property)

## Configuration Options

### Option 1: Default Layout (Recommended)

If you have jpf-core in the parent directory:

```
workspace/
├── jpf-core/
│   └── build/
│       └── libs/
│           └── jpf-core-DEVELOPMENT-SNAPSHOT.jar
└── jpf-autodoc/
    └── build.gradle
```

### Option 2: Same Directory Layout

If you have jpf-core in the same directory:

```
workspace/
├── jpf-core/
│   └── build/
│       └── libs/
│           └── jpf-core-DEVELOPMENT-SNAPSHOT.jar
└── jpf-autodoc/
    └── build.gradle
```

### Option 3: Custom Path via System Property

If jpf-core is in a different location, use the `jpf.core.path` system property:

```bash
# Set custom path
./gradlew build -Djpf.core.path=/path/to/jpf-core

# Example
./gradlew build -Djpf.core.path=/opt/jpf/jpf-core
```

### Option 4: Manual JAR Placement

You can manually copy the jpf-core JAR to any of the search locations:

```bash
# Copy to default location
cp /path/to/jpf-core.jar ../jpf-core/build/libs/jpf-core-DEVELOPMENT-SNAPSHOT.jar

# Or copy to current directory
mkdir -p ./jpf-core/build/libs/
cp /path/to/jpf-core.jar ./jpf-core/build/libs/jpf-core-DEVELOPMENT-SNAPSHOT.jar
```

## Building jpf-core

If you don't have jpf-core built yet:

```bash
# Clone jpf-core
git clone https://github.com/javapathfinder/jpf-core.git

# Build jpf-core
cd jpf-core
./gradlew buildJars

# The JAR will be created as build/jpf.jar
# Copy it to the expected location
mkdir -p build/libs
cp build/jpf.jar build/libs/jpf-core-DEVELOPMENT-SNAPSHOT.jar
```

## Troubleshooting

### Error: Could not find jpf-core-DEVELOPMENT-SNAPSHOT

This means the JAR file is not found in any of the search locations. Solutions:

1. **Check JAR location**: Verify the JAR exists in one of the search paths
2. **Use system property**: Specify custom path with `-Djpf.core.path=`
3. **Copy JAR**: Manually copy the JAR to a search location
4. **Build jpf-core**: If you have jpf-core source, build it first

### Error: Multiple JAR files found

If multiple JAR files are found, the build system will use the first one found. To ensure the correct JAR is used:

1. **Remove duplicate JARs** from other search locations
2. **Use system property** to specify exact path
3. **Check JAR contents** to ensure it's the correct version

## CI/CD Configuration

For CI/CD environments, the workflow automatically:

1. **Clones jpf-core** to the parent directory
2. **Builds jpf-core** with Java 11
3. **Copies JAR** to the expected location
4. **Builds jpf-autodoc** with the dependency

This ensures the dependency is always available in CI environments.

## Advanced Configuration

For advanced users, you can modify the `build.gradle` file to add custom search paths:

```gradle
repositories {
    mavenCentral()
    flatDir {
        dirs = [
            // Add your custom paths here
            '/custom/path/to/jpf-core/build/libs',
            // ... existing paths
        ].findAll { it && new File(it).exists() }
    }
}
``` 