# JPF AutoDoc Usage Examples

This document provides examples of how to use the JPF AutoDoc tool.

## Basic Usage

### Analyze JPF Core
```bash
# Analyze JPF core with both configurations and types
./bin/jpfautodoc -cp ../jpf-core/build/ -o markdown -f jpf-complete.md

# Output: Comprehensive documentation including all configuration options and type hierarchies
```

### Configuration Analysis Only
```bash
# Analyze only configuration options
./bin/jpfautodoc -cp ../jpf-core/build/ --config-only -o xml -f config.xml

# Output: XML file with all JPF configuration options, annotations, and choice generators
```

### Type Hierarchy Analysis Only
```bash
# Analyze only type hierarchies
./bin/jpfautodoc -cp ../jpf-core/build/ --types-only -o markdown -f types.md

# Output: Markdown file with listener, instruction factory, and native peer hierarchies
```

## Advanced Usage

### Parallel Processing
```bash
# Use 8 parallel threads for faster analysis
./bin/jpfautodoc -cp ../jpf-core/build/ --parallel 8 -o markdown -f jpf-fast.md
```

### Pattern Filtering
```bash
# Analyze only listener classes
./bin/jpfautodoc -cp ../jpf-core/build/ --include "gov.nasa.jpf.listener.*" -o markdown

# Exclude test classes
./bin/jpfautodoc -cp ../jpf-core/build/ --exclude ".*Test.*" -o markdown
```

### Validation
```bash
# Enable validation to check for inconsistencies
./bin/jpfautodoc -cp ../jpf-core/build/ --validate -o markdown -f jpf-validated.md
```

## Output Formats

### Markdown Output
```markdown
# JPF Configuration Options

## gov.nasa.jpf.Config

### log.level
- **Type**: String
- **Default**: "info"
- **Description**: Logging level for JPF
- **Source**: gov.nasa.jpf.Config
- **Has Implementation**: ✅
- **Has Annotation**: ✅

### vm.max_transitions
- **Type**: Integer
- **Default**: 10000
- **Description**: Maximum number of transitions to explore
- **Source**: gov.nasa.jpf.Config
- **Has Implementation**: ✅
- **Has Annotation**: ❌

# JPF Type Hierarchy

## Listeners

### gov.nasa.jpf.listener.DeadlockAnalyzer
- **Type**: Listener
- **Superclass**: gov.nasa.jpf.JPFListener
- **Interfaces**: []
- **Methods**: 15
- **Overridden Methods**: 8

### gov.nasa.jpf.listener.CoverageAnalyzer
- **Type**: Listener
- **Superclass**: gov.nasa.jpf.JPFListener
- **Interfaces**: []
- **Methods**: 12
- **Overridden Methods**: 6
```

### XML Output
```xml
<?xml version="1.0" encoding="UTF-8"?>
<jpf-analysis>
  <metadata>
    <analysis-date>2024-01-15T10:30:00Z</analysis-date>
    <source-path>../jpf-core/build/</source-path>
    <config-options-count>45</config-options-count>
    <types-count>23</types-count>
  </metadata>
  
  <config-options>
    <config-option name="log.level" className="gov.nasa.jpf.Config" type="String">
      <values>
        <value type="String" isDefault="true">info</value>
      </values>
      <comment>Logging level for JPF</comment>
    </config-option>
  </config-options>
  
  <types>
    <type name="gov.nasa.jpf.listener.DeadlockAnalyzer" type="Listener">
      <superclass>gov.nasa.jpf.JPFListener</superclass>
      <interfaces/>
      <methods>
        <method>void instructionExecuted(ThreadInfo, Instruction, Instruction)</method>
        <method>void threadStarted(ThreadInfo)</method>
      </methods>
    </type>
  </types>
</jpf-analysis>
```

## Expected Output Structure

### Configuration Analysis
- **Config Options**: All JPF configuration parameters
- **Config Annotations**: @JPFOption annotations
- **Choice Generators**: Non-deterministic decision points
- **Loggers**: Logger configurations

### Type Hierarchy Analysis
- **Listeners**: Event listener implementations
- **Instruction Factories**: Bytecode instruction factories
- **Native Peers**: Native method handling
- **Model Classes**: Library class modeling

### Cross-References
- **Implementation vs Annotation**: Consistency checking
- **Type Relationships**: Inheritance and interface relationships
- **Method Overrides**: Superclass method implementations

### Validation
- **Consistency Issues**: Missing implementations or annotations
- **Type Conflicts**: Conflicting type definitions
- **Documentation Gaps**: Missing documentation

## Performance Considerations

### Small Projects (< 100 classes)
- Single-threaded analysis is sufficient
- Memory usage: ~50MB
- Analysis time: ~10-30 seconds

### Medium Projects (100-1000 classes)
- Use 4-8 parallel threads
- Memory usage: ~200MB
- Analysis time: ~1-5 minutes

### Large Projects (> 1000 classes)
- Use 8+ parallel threads
- Memory usage: ~500MB+
- Analysis time: ~5-15 minutes

## Troubleshooting

### Common Issues

1. **Classpath not found**
   ```
   Error: Classpath is required. Use -cp or --classpath.
   ```
   Solution: Provide the correct path to JPF core build directory

2. **No analysis results**
   ```
   No analysis results found for: /path/to/jpf-core
   ```
   Solution: Ensure the classpath contains compiled .class files

3. **Memory issues**
   ```
   java.lang.OutOfMemoryError
   ```
   Solution: Increase JVM heap size: `java -Xmx2g -jar jpf-autodoc.jar`

### Debug Mode
```bash
# Enable verbose output for debugging
./bin/jpfautodoc -cp ../jpf-core/build/ --verbose -o markdown
``` 