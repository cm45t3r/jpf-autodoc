# JPF AutoDoc

[![CI](https://github.com/cm45t3r/jpf-autodoc/workflows/CI/badge.svg)](https://github.com/cm45t3r/jpf-autodoc/actions)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java](https://img.shields.io/badge/Java-11%2B-blue.svg)](https://openjdk.java.net/)
[![Gradle](https://img.shields.io/badge/Gradle-8.0%2B%20%7C%209.0%20Compatible-blue.svg)](https://gradle.org/)
[![Tests](https://img.shields.io/badge/Tests-41%2F41%20passing-brightgreen.svg)](https://github.com/cm45t3r/jpf-autodoc/actions)
[![Coverage](https://img.shields.io/badge/Coverage-100%25-brightgreen.svg)](https://codecov.io/gh/cm45t3r/jpf-autodoc)

A unified documentation generation tool for Java PathFinder (JPF) that combines configuration analysis and type hierarchy analysis with modern architecture and tooling.

## Overview

JPF AutoDoc is a modern, unified tool that replaces the functionality of both `jpf-autodoc-options` and `jpf-autodoc-types` with:

- **Java 11 Compatibility**: Modern Java features and module system support
- **Gradle Build System**: Modern dependency management and build automation
- **Jackson XML**: Modern XML handling instead of Castor
- **Picocli CLI**: Modern command-line interface with automatic help generation
- **Parallel Processing**: Multi-threaded analysis for better performance
- **Comprehensive Testing**: Unit, integration, and output validation tests
- **Advanced Caching**: Intelligent output caching with memory management
- **Multiple Output Formats**: Markdown, XML, JSON, HTML, and Console output
- **Archive Support**: Read class files from JAR, ZIP, and other archive formats

## Features

### Configuration Analysis
- Extracts JPF configuration options from bytecode
- Analyzes `@JPFOption` annotations
- Identifies choice generators and loggers
- Cross-references implementation vs. documentation

### Type Hierarchy Analysis
- Analyzes JPF listener implementations
- Documents instruction factory patterns
- Maps native peer classes to model classes
- Tracks inheritance hierarchies

### Output Formats
- **Markdown**: Modern documentation format with tables and navigation
- **XML**: Structured data export using Jackson
- **JSON**: Machine-readable output for APIs
- **HTML**: Web-ready documentation with CSS styling
- **Console**: Readable text output for testing and debugging

### Advanced Features
- **Parallel Processing**: Multi-threaded analysis and output generation
- **Intelligent Caching**: Output caching with memory and size limits
- **Memory Optimization**: Efficient handling of large datasets
- **Performance Monitoring**: Built-in timing and metrics
- **Validation Reports**: Comprehensive validation with detailed reporting
- **Archive Analysis**: Support for JAR, ZIP, and other archive formats

## Quick Start

### Prerequisites
- **Java 11+**: For compilation and runtime
- **Java 17+**: For Gradle 9.0 compatibility (daemon only)
- **Gradle 8.0+**: For building and dependency management (9.0 compatible)
- **JPF Core**: Optional, for enhanced analysis capabilities

#### Gradle 9.0 Compatibility Setup
```bash
# Automatic setup (recommended)
./setup-gradle.sh

# Verify configuration
./gradlew checkGradleCompatibility
```

For detailed setup instructions, see [README_GRADLE_SETUP.md](README_GRADLE_SETUP.md).

### Building
```bash
# Clone the repository
git clone https://github.com/cm45t3r/jpf-autodoc.git
cd jpf-autodoc

# Build the project
./gradlew build

# Create executable JAR
./gradlew createExecutableJar
```

### Running Analysis
```bash
# Show help
./bin/jpfautodoc --help

# Show version
./bin/jpfautodoc --version

# Analyze JPF configuration options
./bin/jpfautodoc -cp /path/to/jpf-core --config-only -o markdown -f config.md /path/to/jpf-core

# Analyze type hierarchies
./bin/jpfautodoc -cp /path/to/jpf-core --types-only -o xml -f types.xml /path/to/jpf-core

# Analyze from JAR file
./bin/jpfautodoc -cp jpf-core.jar --config-only -o markdown -f config.md jpf-core.jar

# Analyze from ZIP archive
./bin/jpfautodoc -cp jpf-core.zip --types-only -o xml -f types.xml jpf-core.zip

# Full analysis with all output formats
./bin/jpfautodoc -cp /path/to/jpf-core --verbose --parallel 4 -o html -f docs.html /path/to/jpf-core

# Run with validation enabled
./bin/jpfautodoc -cp /path/to/jpf-core --validate --parallel 4 /path/to/jpf-core
```

## Command Line Options

### Basic Options
- `-cp, --classpath <path>`: Classpath to analyze (supports directories, JAR, ZIP, and other archive files)
- `-o, --output <format>`: Output format (markdown, xml, json, html, text)
- `-f, --file <file>`: Output file path
- `--config-only`: Analyze only configuration options
- `--types-only`: Analyze only type hierarchies
- `--verbose`: Include detailed metadata in output

### Advanced Options
- `--parallel <threads>`: Number of parallel threads (default: number of processors)
- `--validate`: Enable validation reports in output
- `--include <patterns>`: Include pattern for class names
- `--exclude <patterns>`: Exclude pattern for class names

### Output Formats
- `markdown`: Comprehensive markdown documentation
- `xml`: Structured XML using Jackson
- `json`: Machine-readable JSON
- `html`: Beautiful HTML with CSS styling
- `text`: Simple console output

## Examples

### Basic Configuration Analysis
```bash
./bin/jpfautodoc -cp ../jpf-core/build/ --config-only -o markdown -f jpf-config.md ../jpf-core/build/
```

### Type Hierarchy Analysis
```bash
./bin/jpfautodoc -cp ../jpf-core/build/ --types-only -o xml -f jpf-types.xml ../jpf-core/build/
```

### Archive File Analysis
```bash
# Analyze from JAR file
./bin/jpfautodoc -cp jpf-core.jar --config-only -o markdown -f jpf-config.md jpf-core.jar

# Analyze from ZIP archive
./bin/jpfautodoc -cp jpf-core.zip --types-only -o xml -f jpf-types.xml jpf-core.zip

# Analyze from directory containing archives
./bin/jpfautodoc -cp /path/to/libs/ --verbose -o html -f analysis.html /path/to/libs/
```

### Full Analysis with Multiple Formats
```bash
# Generate all formats in parallel
./bin/jpfautodoc -cp ../jpf-core/build/ --verbose --parallel 4 \
  -o markdown -f jpf-analysis.md \
  -o xml -f jpf-analysis.xml \
  -o json -f jpf-analysis.json \
  -o html -f jpf-analysis.html \
  ../jpf-core/build/
```

### Performance Optimized Analysis
```bash
./bin/jpfautodoc -cp ../jpf-core/build/ \
  --parallel 8 \
  --verbose \
  -o markdown -f jpf-analysis.md \
  ../jpf-core/build/
```

## Architecture

### Core Components
1. **Analysis Engine**: Unified analysis of JPF components
2. **Output Generators**: Multiple format support with caching
3. **Validation System**: Comprehensive validation and reporting
4. **Caching System**: Performance optimization with intelligent caching
5. **Testing Framework**: Comprehensive test coverage
6. **Archive Reader**: Support for JAR, ZIP, and other archive formats

### Modern Technologies
- **Java 11**: Modern Java features and performance
- **Gradle**: Modern build system with dependency management
- **Jackson**: Modern XML/JSON processing
- **JUnit 5**: Modern testing framework
- **Picocli**: Modern CLI framework

## Testing

### Run All Tests
```bash
./gradlew test
```

### Run Specific Test Categories
```bash
# Unit tests
./gradlew test --tests "*Test"

# Performance tests
./gradlew test --tests "*PerformanceTest"

# Integration tests
./gradlew test --tests "*IntegrationTest"
```

### Test Coverage
- **Total Tests**: 41
- **Passing Tests**: 41 (100%)
- **Test Categories**:
  - ClassFile Tests: 5 tests
  - Output Cache Tests: 10 tests
  - Output Generator Tests: 9 tests
  - Output Integration Tests: 6 tests
  - Output Performance Tests: 7 tests
  - Archive Reader Tests: 4 tests

## Performance

### Benchmarks
- **Concurrent Generation**: < 10 seconds for 4 output formats
- **Large Dataset Handling**: < 15 seconds for 500+ configuration options
- **Memory Usage**: Efficient handling of large datasets
- **Cache Performance**: Intelligent caching with configurable limits

### Optimization Features
- **Parallel Processing**: Multi-threaded analysis and output generation
- **Memory Management**: Configurable memory limits and efficient usage
- **Intelligent Caching**: Output caching with expiration and size limits
- **Performance Monitoring**: Built-in timing and metrics

## Migration from Legacy Tools

### From jpf-autodoc-options
```bash
# Old approach
ant -f build.xml -Dclasspath=/path/to/jpf-core

# New approach
./bin/jpfautodoc -cp /path/to/jpf-core --config-only -o xml -f options.xml /path/to/jpf-core
```

### From jpf-autodoc-types
```bash
# Old approach
ant -f build.xml -Dclasspath=/path/to/jpf-core

# New approach
./bin/jpfautodoc -cp /path/to/jpf-core --types-only -o xml -f types.xml /path/to/jpf-core
```

## Development

### Building from Source
```bash
git clone https://github.com/cm45t3r/jpf-autodoc.git
cd jpf-autodoc
./gradlew build
```

### Running Tests
```bash
./gradlew test
```

### Creating Distribution
```bash
./gradlew createExecutableJar
./gradlew distZip
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## Community

- [Contributing Guidelines](CONTRIBUTING.md) - How to contribute to JPF AutoDoc
- [Code of Conduct](CODE_OF_CONDUCT.md) - Community standards and guidelines
- [Issue Templates](.github/ISSUE_TEMPLATE/) - Templates for bug reports and feature requests
- [Pull Request Template](.github/PULL_REQUEST_TEMPLATE.md) - Guidelines for pull requests

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

**Copyright Notice**: The Java source files contain NASA copyright notices as they are part of the JPF ecosystem. The LICENSE file contains the standard Apache License 2.0 template.

## Support

For issues and questions:
- Check the test suite for usage examples
- Review the CLI help: `./bin/jpfautodoc --help`
- Run the demo script: `./demo-cli.sh`

---

**Status: Production Ready ✅**

The JPF AutoDoc system provides a modern, comprehensive solution for JPF documentation generation with advanced features, comprehensive testing, and production-ready quality. 