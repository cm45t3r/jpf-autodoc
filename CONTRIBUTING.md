# Contributing to JPF AutoDoc

Thank you for your interest in contributing to JPF AutoDoc! This document provides guidelines and information for contributors.

## Getting Started

### Prerequisites
- Java 11 or higher
- Gradle 7.0 or higher
- Git

### Development Setup
1. Fork the repository
2. Clone your fork:
   ```bash
   git clone https://github.com/your-username/jpf-autodoc.git
   cd jpf-autodoc
   ```
3. Build the project:
   ```bash
   ./gradlew build
   ```
4. Run tests:
   ```bash
   ./gradlew test
   ```

## Development Workflow

### Making Changes
1. Create a feature branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```
2. Make your changes
3. Add tests for new functionality
4. Ensure all tests pass:
   ```bash
   ./gradlew test
   ```
5. Commit your changes with a descriptive message
6. Push to your fork and create a pull request

### Code Style
- Follow Java coding conventions
- Use meaningful variable and method names
- Add Javadoc comments for public methods
- Keep methods focused and concise
- Write unit tests for new functionality

### Testing
- All new features must include tests
- Tests should be in the appropriate test package
- Use descriptive test method names
- Ensure good test coverage

## Pull Request Guidelines

### Before Submitting
- [ ] Code follows the project's style guidelines
- [ ] All tests pass
- [ ] Documentation is updated
- [ ] No new warnings are generated
- [ ] Performance impact is considered

### Pull Request Template
Use the provided pull request template and fill out all relevant sections.

## Issue Guidelines

### Bug Reports
- Use the bug report template
- Include steps to reproduce
- Provide error messages and stack traces
- Include environment information

### Feature Requests
- Use the feature request template
- Describe the use case clearly
- Consider alternatives
- Provide examples if possible

## Architecture Overview

### Core Components
- **Analysis Engine**: `gov.nasa.jpf.autodoc.core`
- **Configuration Analysis**: `gov.nasa.jpf.autodoc.options`
- **Type Analysis**: `gov.nasa.jpf.autodoc.types`
- **Output Generation**: `gov.nasa.jpf.autodoc.output`
- **CLI Interface**: `gov.nasa.jpf.autodoc.cli`

### Adding New Features
1. **Output Formats**: Extend `OutputGenerator` interface
2. **Analysis Types**: Extend analysis engine components
3. **Archive Support**: Add to `ArchiveFileReader`
4. **CLI Options**: Update `JPFAutoDocCLI`

## Testing Guidelines

### Unit Tests
- Test individual components in isolation
- Use descriptive test names
- Mock external dependencies
- Test both success and failure cases

### Integration Tests
- Test component interactions
- Test end-to-end workflows
- Test with real JPF components

### Performance Tests
- Test with large datasets
- Monitor memory usage
- Test concurrent operations

## Documentation

### Code Documentation
- Add Javadoc for public APIs
- Include usage examples
- Document configuration options
- Update README for new features

### User Documentation
- Update README.md for new features
- Add examples to documentation
- Update migration guide if needed

## Release Process

### Versioning
- Follow semantic versioning (MAJOR.MINOR.PATCH)
- Update version in `build.gradle`
- Create release notes

### Release Checklist
- [ ] All tests pass
- [ ] Documentation is updated
- [ ] Version is updated
- [ ] Release notes are prepared
- [ ] Distribution builds successfully

## Community Guidelines

### Communication
- Be respectful and inclusive
- Provide constructive feedback
- Help other contributors
- Follow the project's code of conduct

### Getting Help
- Check existing issues and pull requests
- Search documentation
- Ask questions in issues
- Join community discussions

## License
By contributing to JPF AutoDoc, you agree that your contributions will be licensed under the Apache License 2.0. 