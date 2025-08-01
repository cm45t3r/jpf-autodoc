#!/bin/bash

# JPF AutoDoc - Test Examples
# This script demonstrates working examples that can be tested immediately

echo "JPF AutoDoc - Test Examples"
echo "==========================="
echo

# Check if jpfautodoc is available
if [ ! -f "./bin/jpfautodoc" ]; then
    echo "Error: jpfautodoc not found. Please build the project first:"
    echo "  ./gradlew build"
    exit 1
fi

echo "Testing basic functionality..."
echo

# Test 1: Help and version
echo "1. Testing help and version commands:"
echo "   ./bin/jpfautodoc --help"
echo "   ./bin/jpfautodoc --version"
echo

# Test 2: Basic analysis with current build
echo "2. Testing basic analysis with current build:"
echo "   ./bin/jpfautodoc -cp build/classes/java/main --config-only -o text -f test-config.txt build/classes/java/main/"
echo

# Test 3: Type analysis
echo "3. Testing type analysis:"
echo "   ./bin/jpfautodoc -cp build/classes/java/main --types-only -o text -f test-types.txt build/classes/java/main/"
echo

# Test 4: Validation
echo "4. Testing validation:"
echo "   ./bin/jpfautodoc -cp build/classes/java/main --validate -o text -f test-validated.txt build/classes/java/main/"
echo

# Test 5: Parallel processing
echo "5. Testing parallel processing:"
echo "   ./bin/jpfautodoc -cp build/classes/java/main --parallel 4 -o text -f test-parallel.txt build/classes/java/main/"
echo

# Test 6: Pattern filtering
echo "6. Testing pattern filtering:"
echo "   ./bin/jpfautodoc -cp build/classes/java/main --include 'gov.nasa.jpf.autodoc.core.*' -o text -f test-filtered.txt build/classes/java/main/"
echo

# Test 7: Multiple output formats
echo "7. Testing multiple output formats:"
echo "   ./bin/jpfautodoc -cp build/classes/java/main \\"
echo "     -o markdown -f test-output.md \\"
echo "     -o xml -f test-output.xml \\"
echo "     -o json -f test-output.json \\"
echo "     build/classes/java/main/"
echo

echo "Running actual tests..."
echo

# Run the tests
echo "Test 1: Help and version"
./bin/jpfautodoc --help > /dev/null && echo "✅ Help command works" || echo "❌ Help command failed"
./bin/jpfautodoc --version > /dev/null && echo "✅ Version command works" || echo "❌ Version command failed"
echo

echo "Test 2: Basic analysis"
./bin/jpfautodoc -cp build/classes/java/main --config-only -o text -f test-config.txt build/classes/java/main/ 2>/dev/null
if [ -f "test-config.txt" ]; then
    echo "✅ Basic analysis works (output file created)"
    rm test-config.txt
else
    echo "❌ Basic analysis failed"
fi
echo

echo "Test 3: Type analysis"
./bin/jpfautodoc -cp build/classes/java/main --types-only -o text -f test-types.txt build/classes/java/main/ 2>/dev/null
if [ -f "test-types.txt" ]; then
    echo "✅ Type analysis works (output file created)"
    rm test-types.txt
else
    echo "❌ Type analysis failed"
fi
echo

echo "Test 4: Validation"
./bin/jpfautodoc -cp build/classes/java/main --validate -o text -f test-validated.txt build/classes/java/main/ 2>/dev/null
if [ -f "test-validated.txt" ]; then
    echo "✅ Validation works (output file created)"
    rm test-validated.txt
else
    echo "❌ Validation failed"
fi
echo

echo "Test 5: Parallel processing"
./bin/jpfautodoc -cp build/classes/java/main --parallel 4 -o text -f test-parallel.txt build/classes/java/main/ 2>/dev/null
if [ -f "test-parallel.txt" ]; then
    echo "✅ Parallel processing works (output file created)"
    rm test-parallel.txt
else
    echo "❌ Parallel processing failed"
fi
echo

echo "Test 6: Pattern filtering"
./bin/jpfautodoc -cp build/classes/java/main --include 'gov.nasa.jpf.autodoc.core.*' -o text -f test-filtered.txt build/classes/java/main/ 2>/dev/null
if [ -f "test-filtered.txt" ]; then
    echo "✅ Pattern filtering works (output file created)"
    rm test-filtered.txt
else
    echo "❌ Pattern filtering failed"
fi
echo

echo "Test 7: Multiple output formats"
./bin/jpfautodoc -cp build/classes/java/main \
  -o markdown -f test-output.md \
  -o xml -f test-output.xml \
  -o json -f test-output.json \
  build/classes/java/main/ 2>/dev/null

output_files=("test-output.md" "test-output.xml" "test-output.json")
all_created=true
for file in "${output_files[@]}"; do
    if [ -f "$file" ]; then
        echo "✅ Created $file"
        rm "$file"
    else
        echo "❌ Failed to create $file"
        all_created=false
    fi
done

if [ "$all_created" = true ]; then
    echo "✅ Multiple output formats work"
else
    echo "❌ Multiple output formats failed"
fi
echo

echo "All tests completed!"
echo
echo "Note: These tests use the JPF AutoDoc classes themselves as the target."
echo "      For real JPF analysis, you would use JPF Core classes instead."
echo
echo "Example with JPF Core:"
echo "  ./bin/jpfautodoc -cp /path/to/jpf-core/build/ --config-only -o markdown -f jpf-config.md /path/to/jpf-core/build/" 