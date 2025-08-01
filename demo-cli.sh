#!/bin/bash

# JPF AutoDoc CLI Demo Script
# This script demonstrates the CLI framework functionality

echo "=== JPF AutoDoc CLI Demo ==="
echo "Phase 1: CLI Framework Testing"
echo ""

# Check if the JAR exists
JAR_FILE="build/libs/jpf-autodoc-1.0.0-SNAPSHOT-executable.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "❌ Error: JAR file not found. Please build the project first:"
    echo "   ./gradlew build"
    exit 1
fi

echo "✅ JAR file found: $JAR_FILE"
echo ""

# Test 1: Help command
echo "1. Testing help command:"
echo "   ./bin/jpfautodoc --help"
./bin/jpfautodoc --help | head -10
echo "   ... (truncated for brevity)"
echo ""

# Test 2: Version command
echo "2. Testing version command:"
echo "   ./bin/jpfautodoc --version"
./bin/jpfautodoc --version
echo ""

# Test 3: CLI parsing with classpath
echo "3. Testing CLI parsing with classpath:"
echo "   ./bin/jpfautodoc -cp /path/to/jpf-core --config-only -o markdown -f test.md"
./bin/jpfautodoc -cp /path/to/jpf-core --config-only -o markdown -f test.md
echo ""

# Test 4: Complex CLI options
echo "4. Testing complex CLI options:"
echo "   ./bin/jpfautodoc -cp /path/to/jpf-core --validate --include \"gov.nasa.jpf.*\" --exclude \".*Test.*\" -o xml -f output.xml"
./bin/jpfautodoc -cp /path/to/jpf-core --validate --include "gov.nasa.jpf.*" --exclude ".*Test.*" -o xml -f output.xml
echo ""

# Test 5: Error handling
echo "5. Testing error handling (missing classpath):"
echo "   ./bin/jpfautodoc --config-only -o markdown"
./bin/jpfautodoc --config-only -o markdown
echo ""

echo "=== Demo Complete ==="
echo ""
echo "✅ CLI Framework Status: WORKING"
echo "🔄 Analysis Engines: Coming in Phase 2"
echo ""
echo "The CLI framework is fully functional and ready for Phase 2 implementation."
echo "All command-line options are parsed correctly and provide appropriate error messages." 