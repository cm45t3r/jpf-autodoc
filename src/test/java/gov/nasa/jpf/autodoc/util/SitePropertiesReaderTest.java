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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SitePropertiesReader utility.
 */
class SitePropertiesReaderTest {
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        // Disable site.properties reading to avoid interference from real files
        SitePropertiesReader.disable();
    }
    
    @Test
    void testGetJpfCorePathWhenDisabled() {
        // With site.properties disabled, should return null
        assertNull(SitePropertiesReader.getJpfCorePath());
        assertTrue(SitePropertiesReader.isDisabled());
    }
    
    @Test
    void testGetJpfCoreJarLocationsWhenDisabled() {
        // With site.properties disabled, should return empty array
        String[] locations = SitePropertiesReader.getJpfCoreJarLocations();
        assertEquals(0, locations.length);
    }
    
    @Test
    void testDisableAndEnable() {
        // Initially disabled
        assertTrue(SitePropertiesReader.isDisabled());
        assertNull(SitePropertiesReader.getJpfCorePath());
        
        // Enable
        SitePropertiesReader.enable();
        assertFalse(SitePropertiesReader.isDisabled());
        
        // Disable again
        SitePropertiesReader.disable();
        assertTrue(SitePropertiesReader.isDisabled());
        assertNull(SitePropertiesReader.getJpfCorePath());
    }
    
    @Test
    void testResolvePath() {
        // Test path resolution functionality
        String resolved = SitePropertiesReader.resolvePath("${user.home}/workspace/jpf/jpf-core");
        assertTrue(resolved.contains(System.getProperty("user.home")));
        assertFalse(resolved.contains("${user.home}"));
        
        // Test whitespace trimming
        String trimmed = SitePropertiesReader.resolvePath("   /opt/jpf/jpf-core   ");
        assertEquals("/opt/jpf/jpf-core", trimmed);
        
        // Test empty path
        String empty = SitePropertiesReader.resolvePath("");
        assertEquals("", empty);
    }
} 