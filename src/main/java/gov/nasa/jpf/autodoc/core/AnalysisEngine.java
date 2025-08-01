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

package gov.nasa.jpf.autodoc.core;

import gov.nasa.jpf.autodoc.core.model.UnifiedAnalysisResult;
import gov.nasa.jpf.autodoc.core.model.AnalysisConfig;

/**
 * Core analysis engine interface that coordinates all analysis operations.
 * This is the main entry point for performing JPF documentation analysis.
 */
public interface AnalysisEngine {
    
    /**
     * Analyzes a set of class files according to the given configuration.
     * 
     * @param files The class files to analyze
     * @param config The analysis configuration
     * @return Unified analysis result containing all extracted information
     */
    UnifiedAnalysisResult analyze(ClassFileSet files, AnalysisConfig config);
    
    /**
     * Analyzes a single class file.
     * 
     * @param classFile The class file to analyze
     * @param config The analysis configuration
     * @return Unified analysis result for the single file
     */
    UnifiedAnalysisResult analyze(ClassFile classFile, AnalysisConfig config);
    
    /**
     * Analyzes files from a directory or JAR file.
     * 
     * @param source The directory or JAR file to analyze
     * @param config The analysis configuration
     * @return Unified analysis result for all files in the source
     */
    UnifiedAnalysisResult analyze(String source, AnalysisConfig config);
} 