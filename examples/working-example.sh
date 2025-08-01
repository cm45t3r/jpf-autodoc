#!/bin/bash

# JPF AutoDoc - Working Example
# This script demonstrates a working example of JPF AutoDoc

echo "JPF AutoDoc - Working Example"
echo "============================="
echo

# Check if jpfautodoc is available
if [ ! -f "./bin/jpfautodoc" ]; then
    echo "Error: jpfautodoc not found. Please build the project first:"
    echo "  ./gradlew build"
    exit 1
fi

echo "1. Show help and version:"
echo "   ./bin/jpfautodoc --help"
echo "   ./bin/jpfautodoc --version"
echo

echo "2. Analyze JPF AutoDoc types (using the tool's own classes):"
echo "   ./bin/jpfautodoc -cp build/classes/java/main --types-only -o text build/classes/java/main/"
echo

echo "3. Analyze with validation:"
echo "   ./bin/jpfautodoc -cp build/classes/java/main --validate -o text build/classes/java/main/"
echo

echo "4. Analyze with parallel processing:"
echo "   ./bin/jpfautodoc -cp build/classes/java/main --parallel 4 -o text build/classes/java/main/"
echo

echo "5. Analyze with pattern filtering:"
echo "   ./bin/jpfautodoc -cp build/classes/java/main --include 'gov.nasa.jpf.autodoc.core.*' -o text build/classes/java/main/"
echo

echo "Running examples..."
echo

echo "Example 1: Help and version"
./bin/jpfautodoc --version
echo

echo "Example 2: Type analysis"
./bin/jpfautodoc -cp build/classes/java/main --types-only -o text build/classes/java/main/ | head -20
echo "... (output truncated)"
echo

echo "Example 3: Validation"
./bin/jpfautodoc -cp build/classes/java/main --validate -o text build/classes/java/main/ | head -20
echo "... (output truncated)"
echo

echo "Example 4: Parallel processing"
./bin/jpfautodoc -cp build/classes/java/main --parallel 4 -o text build/classes/java/main/ | head -20
echo "... (output truncated)"
echo

echo "Example 5: Pattern filtering"
./bin/jpfautodoc -cp build/classes/java/main --include 'gov.nasa.jpf.autodoc.core.*' -o text build/classes/java/main/ | head -20
echo "... (output truncated)"
echo

echo "All examples completed successfully!"
echo
echo "Note: These examples analyze the JPF AutoDoc tool's own classes."
echo "      For real JPF analysis, use JPF Core classes:"
echo "      ./bin/jpfautodoc -cp /path/to/jpf-core/build/ --config-only -o markdown -f jpf-config.md /path/to/jpf-core/build/" 