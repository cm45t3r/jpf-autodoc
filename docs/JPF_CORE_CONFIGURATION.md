# JPF Core Configuration

This document explains how to configure the jpf-core dependency for different scenarios.

## Overview

JPF AutoDoc depends on jpf-core, which is not published to Maven Central. The build system automatically searches for jpf-core JAR files in multiple locations, with **site.properties** having the highest priority.

## Automatic Search Locations (in priority order)

The build system automatically searches for `jpf-core-DEVELOPMENT-SNAPSHOT.jar` in the following locations:

1. **site.properties locations** (highest priority)
   - `../site.properties` → `{jpf-core}/build/libs/`
   - `../site.properties` → `{jpf-core}/build/`
   - `~/.jpf/site.properties` → `{jpf-core}/build/libs/`
   - `~/.jpf/site.properties` → `{jpf-core}/build/`

2. **Default locations**
   - `../jpf-core/build/libs/` (default - parent directory)
   - `../jpf-core/build/` (parent directory, direct build folder)
   - `./jpf-core/build/libs/` (current directory)
   - `./jpf-core/build/` (current directory, direct build folder)

3. **Custom system property**
   - `{jpf.core.path}/build/libs/` (custom path via system property)
   - `{jpf.core.path}/build/` (custom path via system property)

## Configuration Options

### Option 1: site.properties (Recommended)

Create a `site.properties` file in one of these locations:

#### Local site.properties (../site.properties)
```properties
jpf-core = ${user.home}/workspace/jpf/jpf-core
extensions=${jpf-core}
```

#### Global site.properties (~/.jpf/site.properties)
```properties
jpf-core = /opt/jpf/jpf-core
extensions=${jpf-core}
```

**Directory structure:**
```
workspace/
├── site.properties                    # Local configuration
├── jpf-core/
│   └── build/
│       └── libs/
│           └── jpf-core-DEVELOPMENT-SNAPSHOT.jar
└── jpf-autodoc/
    └── build.gradle
```

### Option 2: Default Layout

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

### Option 3: Same Directory Layout

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

### Option 4: Custom Path via System Property

If jpf-core is in a different location, use the `jpf.core.path` system property:

```bash
# Set custom path
./gradlew build -Djpf.core.path=/path/to/jpf-core

# Example
./gradlew build -Djpf.core.path=/opt/jpf/jpf-core
```

### Option 5: Manual JAR Placement

You can manually copy the jpf-core JAR to any of the search locations:

```bash
# Copy to default location
cp /path/to/jpf-core.jar ../jpf-core/build/libs/jpf-core-DEVELOPMENT-SNAPSHOT.jar

# Or copy to current directory
mkdir -p ./jpf-core/build/libs/
cp /path/to/jpf-core.jar ./jpf-core/build/libs/jpf-core-DEVELOPMENT-SNAPSHOT.jar
```

## site.properties Format

The `site.properties` file uses standard Java properties format:

```properties
# Basic configuration
jpf-core = /path/to/jpf-core

# With variable substitution
jpf-core = ${user.home}/workspace/jpf/jpf-core

# Multiple extensions (optional)
extensions=${jpf-core}
```

### Variable Substitution

The following variables are supported:

- `${user.home}` - Replaced with the user's home directory
- `${jpf-core}` - Replaced with the jpf-core path (for extensions)

### Example site.properties files

#### Local development:
```properties
jpf-core = ${user.home}/workspace/jpf/jpf-core
extensions=${jpf-core}
```

#### System-wide installation:
```properties
jpf-core = /opt/jpf/jpf-core
extensions=${jpf-core}
```

#### Relative path:
```properties
jpf-core = ../jpf-core
extensions=${jpf-core}
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

1. **Check site.properties**: Verify the jpf-core path in site.properties is correct
2. **Check JAR location**: Verify the JAR exists in the expected location
3. **Use system property**: Specify custom path with `-Djpf.core.path=`
4. **Copy JAR**: Manually copy the JAR to a search location
5. **Build jpf-core**: If you have jpf-core source, build it first

### Error: Multiple JAR files found

If multiple JAR files are found, the build system will use the first one found (highest priority). To ensure the correct JAR is used:

1. **Check site.properties**: Ensure site.properties points to the correct location
2. **Remove duplicate JARs** from other search locations
3. **Use system property** to specify exact path
4. **Check JAR contents** to ensure it's the correct version

### Debugging site.properties

To debug site.properties configuration:

```bash
# Check if site.properties is being read
./gradlew getJpfCorePaths

# Check the resolved paths
./gradlew build --info
```

## CI/CD Configuration

For CI/CD environments, the workflow automatically:

1. **Clones jpf-core** to the parent directory
2. **Builds jpf-core** with Java 11
3. **Copies JAR** to the expected location
4. **Creates site.properties** pointing to the cloned jpf-core
5. **Builds jpf-autodoc** with the dependency

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

## Priority Order Summary

1. **site.properties** (highest priority)
   - `../site.properties`
   - `~/.jpf/site.properties`

2. **Default locations**
   - `../jpf-core/build/libs/`
   - `../jpf-core/build/`
   - `./jpf-core/build/libs/`
   - `./jpf-core/build/`

3. **System property**
   - `-Djpf.core.path=/path/to/jpf-core`

The build system will use the first JAR file found in the highest priority location. 