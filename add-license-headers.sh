#!/bin/bash

# Script to add Apache License 2.0 headers to all Java files
# Usage: ./add-license-headers.sh

LICENSE_HEADER='/*
 * Copyright (C) 2014, United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * The Java Pathfinder AutoDoc tool is licensed under the
 * Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */'

echo "Adding Apache License 2.0 headers to Java files..."

# Find all Java files
find src/main/java src/test/java -name "*.java" | while read -r file; do
    echo "Processing: $file"
    
    # Check if file already has a license header
    if grep -q "Copyright (C) 2014, United States Government" "$file"; then
        echo "  Skipping $file (already has license header)"
        continue
    fi
    
    # Create temporary file with license header
    temp_file=$(mktemp)
    
    # Add license header
    echo "$LICENSE_HEADER" > "$temp_file"
    echo "" >> "$temp_file"
    
    # Add original file content
    cat "$file" >> "$temp_file"
    
    # Replace original file
    mv "$temp_file" "$file"
    
    echo "  Added license header to $file"
done

echo "License headers added to all Java files." 