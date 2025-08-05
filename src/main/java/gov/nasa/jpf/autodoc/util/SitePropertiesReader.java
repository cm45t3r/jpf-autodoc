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

package gov.nasa.jpf.autodoc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Utility class to read JPF site.properties file and extract jpf-core location.
 * Supports reading from multiple standard locations:
 * - ../site.properties (relative to current directory)
 * - ~/.jpf/site.properties (user home directory)
 */
public class SitePropertiesReader {
    
    private static final String[] SITE_PROPERTIES_LOCATIONS = {
        "../site.properties",
        System.getProperty("user.home") + "/.jpf/site.properties"
    };
    
    // Flag to disable site.properties reading (for testing)
    private static boolean disabled = false;
    
    /**
     * Disable site.properties reading (for testing purposes).
     */
    public static void disable() {
        disabled = true;
    }
    
    /**
     * Enable site.properties reading (default behavior).
     */
    public static void enable() {
        disabled = false;
    }
    
    /**
     * Check if site.properties reading is disabled.
     */
    public static boolean isDisabled() {
        return disabled;
    }
    
    /**
     * Reads the jpf-core location from site.properties file.
     * 
     * @return The jpf-core path if found, null otherwise
     */
    public static String getJpfCorePath() {
        if (disabled) {
            return null;
        }
        
        for (String location : SITE_PROPERTIES_LOCATIONS) {
            String jpfCorePath = readJpfCorePathFromFile(location);
            if (jpfCorePath != null) {
                return jpfCorePath;
            }
        }
        return null;
    }
    
    /**
     * Reads jpf-core path from a specific site.properties file.
     * 
     * @param filePath The path to the site.properties file
     * @return The jpf-core path if found, null otherwise
     */
    private static String readJpfCorePathFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.canRead()) {
            return null;
        }
        
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(file)) {
            props.load(input);
            String jpfCorePath = props.getProperty("jpf-core");
            if (jpfCorePath != null && !jpfCorePath.trim().isEmpty()) {
                // Resolve any variables in the path
                return resolvePath(jpfCorePath);
            }
        } catch (IOException e) {
            // Log error but continue to next location
            System.err.println("Warning: Could not read site.properties from " + filePath + ": " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Resolves path variables like ${user.home}.
     * 
     * @param path The path that may contain variables
     * @return The resolved path
     */
    public static String resolvePath(String path) {
        String resolved = path;
        
        // Replace ${user.home} with actual user home
        if (resolved.contains("${user.home}")) {
            resolved = resolved.replace("${user.home}", System.getProperty("user.home"));
        }
        
        // Replace ${jpf-core} with the jpf-core path (recursive resolution)
        if (resolved.contains("${jpf-core}")) {
            // For now, just remove this variable as it would cause infinite recursion
            resolved = resolved.replace("${jpf-core}", "");
        }
        
        return resolved.trim();
    }
    
    /**
     * Gets the jpf-core JAR path from site.properties.
     * 
     * @return The path to jpf-core JAR file, or null if not found
     */
    public static String getJpfCoreJarPath() {
        String jpfCorePath = getJpfCorePath();
        if (jpfCorePath == null) {
            return null;
        }
        
        // Check for JAR in build/libs/ directory
        Path jarPath = Paths.get(jpfCorePath, "build", "libs", "jpf-core-DEVELOPMENT-SNAPSHOT.jar");
        if (Files.exists(jarPath)) {
            return jarPath.toString();
        }
        
        // Check for JAR in build/ directory
        jarPath = Paths.get(jpfCorePath, "build", "jpf.jar");
        if (Files.exists(jarPath)) {
            return jarPath.toString();
        }
        
        return null;
    }
    
    /**
     * Gets all possible jpf-core JAR locations based on site.properties.
     * 
     * @return Array of possible JAR paths
     */
    public static String[] getJpfCoreJarLocations() {
        String jpfCorePath = getJpfCorePath();
        if (jpfCorePath == null) {
            return new String[0];
        }
        
        return new String[]{
            Paths.get(jpfCorePath, "build", "libs", "jpf-core-DEVELOPMENT-SNAPSHOT.jar").toString(),
            Paths.get(jpfCorePath, "build", "jpf.jar").toString()
        };
    }
} 