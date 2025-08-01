#!/bin/bash

# JPF AutoDoc - Archive Analysis Examples
# This script demonstrates how to analyze JPF components from various archive formats

echo "JPF AutoDoc - Archive Analysis Examples"
echo "======================================="
echo

# Check if jpfautodoc is available
if [ ! -f "./bin/jpfautodoc" ]; then
    echo "Error: jpfautodoc not found. Please build the project first:"
    echo "  ./gradlew build"
    exit 1
fi

echo "1. Analyzing from JAR file (jpf-core.jar)"
echo "   ./bin/jpfautodoc -cp jpf-core.jar --config-only -o markdown -f jpf-config.md jpf-core.jar"
echo

echo "2. Analyzing from ZIP archive (jpf-core.zip)"
echo "   ./bin/jpfautodoc -cp jpf-core.zip --types-only -o xml -f jpf-types.xml jpf-core.zip"
echo

echo "3. Analyzing from directory containing archives"
echo "   ./bin/jpfautodoc -cp /path/to/libs/ --verbose -o html -f analysis.html /path/to/libs/"
echo

echo "4. Full analysis from JAR with multiple output formats"
echo "   ./bin/jpfautodoc -cp jpf-core.jar --verbose --parallel 4 \\"
echo "     -o markdown -f jpf-analysis.md \\"
echo "     -o xml -f jpf-analysis.xml \\"
echo "     -o json -f jpf-analysis.json \\"
echo "     -o html -f jpf-analysis.html \\"
echo "     jpf-core.jar"
echo

echo "5. Performance optimized analysis from archive"
echo "   ./bin/jpfautodoc -cp jpf-core.jar \\"
echo "     --parallel 8 \\"
echo "     --verbose \\"
echo "     -o markdown -f jpf-analysis.md \\"
echo "     jpf-core.jar"
echo

echo "6. Analysis with validation enabled"
echo "   ./bin/jpfautodoc -cp jpf-core.jar \\"
echo "     --validate \\"
echo "     --parallel 4 \\"
echo "     -o markdown -f jpf-validated.md \\"
echo "     jpf-core.jar"
echo

echo "Supported Archive Formats:"
echo "  - JAR files (.jar)"
echo "  - ZIP files (.zip)"
echo "  - TAR files (.tar, .tar.gz, .tgz, .tar.bz2) - requires Apache Commons Compress"
echo

echo "Example with actual files:"
echo "  # If you have jpf-core.jar in the current directory:"
echo "  ./bin/jpfautodoc -cp jpf-core.jar --config-only -o markdown -f config.md jpf-core.jar"
echo
echo "  # If you have jpf-core.zip:"
echo "  ./bin/jpfautodoc -cp jpf-core.zip --types-only -o xml -f types.xml jpf-core.zip"
echo
echo "  # If you have a directory with multiple archives:"
echo "  ./bin/jpfautodoc -cp ./libs/ --verbose -o html -f analysis.html ./libs/"
echo

echo "Note: The tool automatically detects archive formats and extracts class files for analysis."
echo "      It supports nested archives and can process multiple archive types in a single run."
echo
echo "Performance Tips:"
echo "  - Use --parallel for faster analysis on large archives"
echo "  - Use --validate to check for consistency issues"
echo "  - Use --verbose for detailed output and debugging" 