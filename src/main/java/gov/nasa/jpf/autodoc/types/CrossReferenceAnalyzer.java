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

package gov.nasa.jpf.autodoc.types;

import gov.nasa.jpf.autodoc.core.model.UnifiedAnalysisResult;
import gov.nasa.jpf.autodoc.core.model.CrossReference;

import java.util.UUID;

/**
 * Analyzes cross-references between JPF components.
 * Finds relationships between listeners, configuration options, type hierarchies, etc.
 */
public class CrossReferenceAnalyzer {
    
    public void analyze(UnifiedAnalysisResult result) {
        try {
            // Find relationships between configuration options and types
            analyzeConfigTypeRelationships(result);
            
            // Find relationships between listeners and configuration options
            analyzeListenerConfigRelationships(result);
            
            // Find relationships between model classes and native peers
            analyzeModelPeerRelationships(result);
            
            // Find inheritance relationships
            analyzeInheritanceRelationships(result);
            
        } catch (Exception e) {
            System.err.println("Warning: Error analyzing cross-references: " + e.getMessage());
        }
    }
    
    private void analyzeConfigTypeRelationships(UnifiedAnalysisResult result) {
        // Find configuration options that reference specific types
        result.getConfigOptions().values().forEach(configOption -> {
            result.getTypes().values().forEach(typeInfo -> {
                if (configOption.getClassName().equals(typeInfo.getName())) {
                    CrossReference crossRef = new CrossReference(
                        generateId(),
                        "ConfigOption",
                        "TypeInfo",
                        configOption.getName(),
                        typeInfo.getName(),
                        "IMPLEMENTATION"
                    );
                    result.addCrossReference(crossRef);
                }
            });
        });
    }
    
    private void analyzeListenerConfigRelationships(UnifiedAnalysisResult result) {
        // Find listeners that have configuration options
        result.getListeners().values().forEach(listener -> {
            result.getConfigOptions().values().forEach(configOption -> {
                if (configOption.getClassName().contains("Listener") || 
                    listener.getName().contains(configOption.getName())) {
                    CrossReference crossRef = new CrossReference(
                        generateId(),
                        "Listener",
                        "ConfigOption",
                        listener.getName(),
                        configOption.getName(),
                        "CONFIGURATION"
                    );
                    result.addCrossReference(crossRef);
                }
            });
        });
    }
    
    private void analyzeModelPeerRelationships(UnifiedAnalysisResult result) {
        // Find relationships between model classes and native peers
        result.getModelClasses().values().forEach(modelClass -> {
            result.getNativePeers().values().forEach(nativePeer -> {
                if (nativePeer.getModelName().equals(modelClass.getName()) ||
                    modelClass.getStdName().contains(nativePeer.getName())) {
                    CrossReference crossRef = new CrossReference(
                        generateId(),
                        "ModelClass",
                        "NativePeer",
                        modelClass.getName(),
                        nativePeer.getName(),
                        "IMPLEMENTATION"
                    );
                    result.addCrossReference(crossRef);
                }
            });
        });
    }
    
    private void analyzeInheritanceRelationships(UnifiedAnalysisResult result) {
        // Find inheritance relationships between types
        result.getTypes().values().forEach(typeInfo -> {
            result.getTypes().values().forEach(otherType -> {
                if (!typeInfo.getName().equals(otherType.getName()) &&
                    typeInfo.getSuperName().equals(otherType.getName())) {
                    CrossReference crossRef = new CrossReference(
                        generateId(),
                        "TypeInfo",
                        "TypeInfo",
                        typeInfo.getName(),
                        otherType.getName(),
                        "INHERITANCE"
                    );
                    result.addCrossReference(crossRef);
                }
            });
        });
    }
    
    private String generateId() {
        return UUID.randomUUID().toString();
    }
} 