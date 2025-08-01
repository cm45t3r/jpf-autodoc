/*
 * Copyright (C) 2025, United States Government, as represented by the
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
 */

package gov.nasa.jpf.autodoc.output;

/**
 * Supported output formats for documentation generation.
 */
public enum OutputFormat {
    
    MARKDOWN("md", "Markdown format"),
    XML("xml", "XML format"),
    JSON("json", "JSON format"),
    TEXT("txt", "Plain text format"),
    HTML("html", "HTML format");
    
    private final String fileExtension;
    private final String description;
    
    OutputFormat(String fileExtension, String description) {
        this.fileExtension = fileExtension;
        this.description = description;
    }
    
    public String getFileExtension() {
        return fileExtension;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return name().toLowerCase();
    }
} 