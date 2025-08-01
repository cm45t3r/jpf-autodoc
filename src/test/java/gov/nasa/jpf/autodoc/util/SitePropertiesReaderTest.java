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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class SitePropertiesReaderTest {

    @TempDir
    Path tempDir;

    private Path sitePropertiesFile;
    private String originalUserHome;

    @BeforeEach
    void setUp() throws IOException {
        sitePropertiesFile = tempDir.resolve("site.properties");
        originalUserHome = System.getProperty("user.home");
        // Set user.home to temp directory to avoid reading real site.properties
        System.setProperty("user.home", tempDir.toString());
    }

    @Test
    void testGetJpfCorePathWithAbsolutePath() throws IOException {
        // Create site.properties with absolute path
        String content = "jpf-core = /opt/jpf/jpf-core\nextensions=${jpf-core}";
        Files.write(sitePropertiesFile, content.getBytes());

        // Test that the path is read correctly
        String result = SitePropertiesReader.getJpfCorePath();
        assertThat(result).isEqualTo("/opt/jpf/jpf-core");
    }

    @Test
    void testGetJpfCorePathWithUserHomeVariable() throws IOException {
        // Create site.properties with user.home variable
        String content = "jpf-core = ${user.home}/workspace/jpf/jpf-core\nextensions=${jpf-core}";
        Files.write(sitePropertiesFile, content.getBytes());

        // Test that the variable is resolved correctly
        String result = SitePropertiesReader.getJpfCorePath();
        assertThat(result).isEqualTo(tempDir.toString() + "/workspace/jpf/jpf-core");
    }

    @Test
    void testGetJpfCorePathWithRelativePath() throws IOException {
        // Create site.properties with relative path
        String content = "jpf-core = ../jpf-core\nextensions=${jpf-core}";
        Files.write(sitePropertiesFile, content.getBytes());

        // Test that the relative path is preserved
        String result = SitePropertiesReader.getJpfCorePath();
        assertThat(result).isEqualTo("../jpf-core");
    }

    @Test
    void testGetJpfCorePathWithEmptyValue() throws IOException {
        // Create site.properties with empty jpf-core value
        String content = "jpf-core = \nextensions=${jpf-core}";
        Files.write(sitePropertiesFile, content.getBytes());

        // Test that empty value returns null
        String result = SitePropertiesReader.getJpfCorePath();
        assertThat(result).isNull();
    }

    @Test
    void testGetJpfCorePathWithWhitespace() throws IOException {
        // Create site.properties with whitespace
        String content = "jpf-core =   /opt/jpf/jpf-core  \nextensions=${jpf-core}";
        Files.write(sitePropertiesFile, content.getBytes());

        // Test that whitespace is trimmed
        String result = SitePropertiesReader.getJpfCorePath();
        assertThat(result).isEqualTo("/opt/jpf/jpf-core");
    }

    @Test
    void testGetJpfCorePathWhenFileDoesNotExist() {
        // Test when no site.properties file exists
        String result = SitePropertiesReader.getJpfCorePath();
        assertThat(result).isNull();
    }

    @Test
    void testGetJpfCorePathWithInvalidProperties() throws IOException {
        // Create site.properties with invalid format
        String content = "invalid format\njpf-core = /opt/jpf/jpf-core";
        Files.write(sitePropertiesFile, content.getBytes());

        // Test that it still reads the valid property
        String result = SitePropertiesReader.getJpfCorePath();
        assertThat(result).isEqualTo("/opt/jpf/jpf-core");
    }

    @Test
    void testGetJpfCoreJarLocations() throws IOException {
        // Create site.properties
        String content = "jpf-core = /opt/jpf/jpf-core\nextensions=${jpf-core}";
        Files.write(sitePropertiesFile, content.getBytes());

        // Test that JAR locations are generated correctly
        String[] locations = SitePropertiesReader.getJpfCoreJarLocations();
        assertThat(locations).hasSize(2);
        assertThat(locations[0]).isEqualTo("/opt/jpf/jpf-core/build/libs/jpf-core-DEVELOPMENT-SNAPSHOT.jar");
        assertThat(locations[1]).isEqualTo("/opt/jpf/jpf-core/build/jpf.jar");
    }

    @Test
    void testGetJpfCoreJarLocationsWhenNoSiteProperties() {
        // Test when no site.properties exists
        String[] locations = SitePropertiesReader.getJpfCoreJarLocations();
        assertThat(locations).isEmpty();
    }
} 