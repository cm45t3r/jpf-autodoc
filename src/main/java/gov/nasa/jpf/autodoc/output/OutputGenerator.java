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

package gov.nasa.jpf.autodoc.output;

import gov.nasa.jpf.autodoc.core.model.UnifiedAnalysisResult;

/**
 * Interface for output generators that create documentation in different formats.
 */
public interface OutputGenerator {
    
    /**
     * Generates output from the analysis result according to the configuration.
     * 
     * @param result The unified analysis result
     * @param config The output configuration
     * @throws OutputGenerationException if output generation fails
     */
    void generate(UnifiedAnalysisResult result, OutputConfig config) throws OutputGenerationException;
    
    /**
     * Checks if this generator supports the given output format.
     * 
     * @param format The output format to check
     * @return true if the format is supported
     */
    boolean supportsFormat(OutputFormat format);
} 