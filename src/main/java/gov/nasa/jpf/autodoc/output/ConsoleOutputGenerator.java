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

import java.io.PrintStream;

/**
 * Simple console output generator for testing analysis engines.
 * Outputs analysis results to the console in a readable format.
 */
public class ConsoleOutputGenerator implements OutputGenerator {
    
    private final PrintStream out;
    
    public ConsoleOutputGenerator() {
        this.out = System.out;
    }
    
    public ConsoleOutputGenerator(PrintStream out) {
        this.out = out;
    }
    
    @Override
    public void generate(UnifiedAnalysisResult result, OutputConfig config) throws OutputGenerationException {
        try {
            out.println("=== JPF AutoDoc Analysis Results ===");
            out.println("Source: " + result.getSourcePath());
            out.println("Date: " + result.getAnalysisDate());
            out.println();
            
            // Configuration Options
            if (!result.getConfigOptions().isEmpty()) {
                out.println("Configuration Options (" + result.getConfigOptions().size() + "):");
                result.getConfigOptions().values().forEach(option -> {
                    out.println("  - " + option.getName() + " (" + option.getType() + ")");
                });
                out.println();
            }
            
            // Configuration Annotations
            if (!result.getConfigAnnotations().isEmpty()) {
                out.println("Configuration Annotations (" + result.getConfigAnnotations().size() + "):");
                result.getConfigAnnotations().values().forEach(annotation -> {
                    out.println("  - " + annotation.getName() + " (" + annotation.getType() + ")");
                });
                out.println();
            }
            
            // Choice Generators
            if (!result.getChoiceGenerators().isEmpty()) {
                out.println("Choice Generators (" + result.getChoiceGenerators().size() + "):");
                result.getChoiceGenerators().values().forEach(choice -> {
                    out.println("  - " + choice.getName() + " (" + choice.getType() + ")");
                });
                out.println();
            }
            
            // Loggers
            if (!result.getLoggers().isEmpty()) {
                out.println("Loggers (" + result.getLoggers().size() + "):");
                result.getLoggers().values().forEach(logger -> {
                    out.println("  - " + logger.getName() + " (" + logger.getType() + ")");
                });
                out.println();
            }
            
            // Types
            if (!result.getTypes().isEmpty()) {
                out.println("Types (" + result.getTypes().size() + "):");
                result.getTypes().values().forEach(type -> {
                    out.println("  - " + type.getName() + " (" + type.getType() + ")");
                });
                out.println();
            }
            
            // Model Classes
            if (!result.getModelClasses().isEmpty()) {
                out.println("Model Classes (" + result.getModelClasses().size() + "):");
                result.getModelClasses().values().forEach(model -> {
                    out.println("  - " + model.getName() + " -> " + model.getStdName());
                });
                out.println();
            }
            
            // Native Peers
            if (!result.getNativePeers().isEmpty()) {
                out.println("Native Peers (" + result.getNativePeers().size() + "):");
                result.getNativePeers().values().forEach(peer -> {
                    out.println("  - " + peer.getName() + " -> " + peer.getModelName());
                });
                out.println();
            }
            
            // Listeners
            if (!result.getListeners().isEmpty()) {
                out.println("Listeners (" + result.getListeners().size() + "):");
                result.getListeners().values().forEach(listener -> {
                    out.println("  - " + listener.getName() + " (" + listener.getType() + ")");
                });
                out.println();
            }
            
            // Cross References
            if (!result.getCrossReferences().isEmpty()) {
                out.println("Cross References (" + result.getCrossReferences().size() + "):");
                result.getCrossReferences().values().forEach(crossRef -> {
                    out.println("  - " + crossRef.getSourceName() + " -> " + crossRef.getTargetName() + " (" + crossRef.getRelationship() + ")");
                });
                out.println();
            }
            
            // Summary
            out.println("=== Summary ===");
            out.println("Total Configuration Options: " + result.getConfigOptions().size());
            out.println("Total Configuration Annotations: " + result.getConfigAnnotations().size());
            out.println("Total Choice Generators: " + result.getChoiceGenerators().size());
            out.println("Total Loggers: " + result.getLoggers().size());
            out.println("Total Types: " + result.getTypes().size());
            out.println("Total Model Classes: " + result.getModelClasses().size());
            out.println("Total Native Peers: " + result.getNativePeers().size());
            out.println("Total Listeners: " + result.getListeners().size());
            out.println("Total Cross References: " + result.getCrossReferences().size());
            
        } catch (Exception e) {
            throw new OutputGenerationException("Failed to generate console output", e);
        }
    }
    
    @Override
    public boolean supportsFormat(OutputFormat format) {
        return format == OutputFormat.TEXT;
    }
} 