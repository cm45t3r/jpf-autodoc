/*
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
 */

package gov.nasa.jpf.autodoc.core;

import gov.nasa.jpf.autodoc.core.model.AnalysisConfig;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for core classes to verify the setup is working.
 */
public class ClassFileTest {
    
    @Test
    void testClassFileSetCreation() {
        ClassFileSet fileSet = new ClassFileSet();
        assertThat(fileSet).isNotNull();
        assertThat(fileSet.isEmpty()).isTrue();
        assertThat(fileSet.size()).isEqualTo(0);
    }
    
    @Test
    void testAnalysisConfigBuilder() {
        AnalysisConfig config = AnalysisConfig.builder()
                .analyzeConfigurations(true)
                .analyzeTypes(false)
                .validateResults(true)
                .verbose(true)
                .build();
        
        assertThat(config).isNotNull();
        assertThat(config.isAnalyzeConfigurations()).isTrue();
        assertThat(config.isAnalyzeTypes()).isFalse();
        assertThat(config.isValidateResults()).isTrue();
        assertThat(config.isVerbose()).isTrue();
    }
    
    @Test
    void testAnalysisConfigDefaults() {
        AnalysisConfig config = AnalysisConfig.defaultConfig();
        
        assertThat(config).isNotNull();
        assertThat(config.isAnalyzeConfigurations()).isTrue();
        assertThat(config.isAnalyzeTypes()).isTrue();
        assertThat(config.isValidateResults()).isFalse();
        assertThat(config.isParallelProcessing()).isTrue();
    }
    
    @Test
    void testAnalysisConfigConfigOnly() {
        AnalysisConfig config = AnalysisConfig.configOnly();
        
        assertThat(config).isNotNull();
        assertThat(config.isAnalyzeConfigurations()).isTrue();
        assertThat(config.isAnalyzeTypes()).isFalse();
    }
    
    @Test
    void testAnalysisConfigTypesOnly() {
        AnalysisConfig config = AnalysisConfig.typesOnly();
        
        assertThat(config).isNotNull();
        assertThat(config.isAnalyzeConfigurations()).isFalse();
        assertThat(config.isAnalyzeTypes()).isTrue();
    }
} 