#!/bin/bash

# Script to update copyright year from 2014 to 2025 in all Java files
# Usage: ./update-copyright-year.sh

echo "Updating copyright year from 2014 to 2025 in Java files..."

# Find all Java files and update the copyright year
find src/main/java src/test/java -name "*.java" | while read -r file; do
    echo "Processing: $file"
    
    # Create temporary file
    temp_file=$(mktemp)
    
    # Replace 2014 with 2025 in the copyright line
    sed 's/Copyright (C) 2014, United States Government/Copyright (C) 2025, United States Government/' "$file" > "$temp_file"
    
    # Replace original file
    mv "$temp_file" "$file"
    
    echo "  Updated copyright year in $file"
done

echo "Copyright year updated in all Java files." 