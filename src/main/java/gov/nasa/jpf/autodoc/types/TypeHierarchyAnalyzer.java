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

import gov.nasa.jpf.autodoc.core.ClassFile;
import gov.nasa.jpf.autodoc.core.model.UnifiedAnalysisResult;
import gov.nasa.jpf.autodoc.core.model.TypeInfo;
import gov.nasa.jpf.autodoc.core.model.ModelClass;
import gov.nasa.jpf.autodoc.core.model.NativePeer;
import gov.nasa.jpf.autodoc.core.model.Listener;

import java.util.regex.Pattern;

/**
 * Analyzes JPF type hierarchies from class files.
 * Extracts type information, inheritance relationships, and JPF-specific types.
 */
public class TypeHierarchyAnalyzer {
    
    // JPF type patterns
    private static final Pattern LISTENER_PATTERN = Pattern.compile("Listener");
    private static final Pattern INSTRUCTION_FACTORY_PATTERN = Pattern.compile("InstructionFactory");
    private static final Pattern NATIVE_PEER_PATTERN = Pattern.compile("NativePeer");
    private static final Pattern MODEL_CLASS_PATTERN = Pattern.compile("Model");
    
    // JPF package patterns
    private static final Pattern JPF_PACKAGE_PATTERN = Pattern.compile("gov\\.nasa\\.jpf");
    
    public void analyze(ClassFile classFile, UnifiedAnalysisResult result) {
        try {
            // Skip non-JPF classes
            if (!isJPFClass(classFile)) {
                return;
            }
            
            // Analyze type information
            analyzeTypeInfo(classFile, result);
            
            // Analyze model classes
            analyzeModelClasses(classFile, result);
            
            // Analyze native peers
            analyzeNativePeers(classFile, result);
            
            // Analyze listeners
            analyzeListeners(classFile, result);
            
        } catch (Exception e) {
            // Log error but continue with other classes
            System.err.println("Warning: Error analyzing type hierarchy for " + classFile.getClassName() + ": " + e.getMessage());
        }
    }
    
    private boolean isJPFClass(ClassFile classFile) {
        String className = classFile.getClassName();
        return JPF_PACKAGE_PATTERN.matcher(className).find();
    }
    
    private void analyzeTypeInfo(ClassFile classFile, UnifiedAnalysisResult result) {
        String className = classFile.getClassName();
        
        // Determine the type classification
        String type = determineTypeClassification(className);
        
        if (type != null) {
            TypeInfo typeInfo = new TypeInfo(
                className,
                extractSuperClassName(className), // Simplified - would use JPF ClassFile framework
                type
            );
            
            // Add interfaces (simplified)
            typeInfo.addInterface("java.io.Serializable");
            
            // Add methods (simplified)
            typeInfo.addMethod("toString");
            typeInfo.addMethod("equals");
            typeInfo.addMethod("hashCode");
            
            result.addType(typeInfo);
        }
    }
    
    private void analyzeModelClasses(ClassFile classFile, UnifiedAnalysisResult result) {
        String className = classFile.getClassName();
        
        // Look for model classes
        if (className.contains("Model") || className.contains("model")) {
            String standardName = extractStandardClassName(className);
            
            ModelClass modelClass = new ModelClass(className, standardName);
            modelClass.addStdMethod("toString");
            modelClass.addStdMethod("equals");
            modelClass.addStdMethod("hashCode");
            
            result.addModelClass(modelClass);
        }
    }
    
    private void analyzeNativePeers(ClassFile classFile, UnifiedAnalysisResult result) {
        String className = classFile.getClassName();
        
        // Look for native peer classes
        if (className.contains("NativePeer") || className.contains("Peer")) {
            String modelName = extractModelClassName(className);
            
            NativePeer nativePeer = new NativePeer(className, modelName);
            nativePeer.addModelMethod("nativeMethod");
            nativePeer.addModelMethod("getPeer");
            
            result.addNativePeer(nativePeer);
        }
    }
    
    private void analyzeListeners(ClassFile classFile, UnifiedAnalysisResult result) {
        String className = classFile.getClassName();
        
        // Look for listener classes
        if (className.contains("Listener") || className.contains("listener")) {
            String type = determineListenerType(className);
            
            Listener listener = new Listener(className, type);
            result.addListener(listener);
        }
    }
    
    private String determineTypeClassification(String className) {
        if (className.contains("Listener")) {
            return "Listener";
        } else if (className.contains("InstructionFactory")) {
            return "InstructionFactory";
        } else if (className.contains("NativePeer")) {
            return "NativePeer";
        } else if (className.contains("Model")) {
            return "ModelClass";
        } else if (className.contains("ChoiceGenerator")) {
            return "ChoiceGenerator";
        } else if (className.contains("Config")) {
            return "Configuration";
        }
        
        return null;
    }
    
    private String determineListenerType(String className) {
        if (className.contains("Search")) {
            return "SearchListener";
        } else if (className.contains("Property")) {
            return "PropertyListener";
        } else if (className.contains("Error")) {
            return "ErrorListener";
        } else if (className.contains("State")) {
            return "StateListener";
        } else {
            return "GenericListener";
        }
    }
    
    private String extractSuperClassName(String className) {
        // Simplified - in practice, we'd use JPF's ClassFile framework
        // to get the actual superclass from the class file
        
        // Common JPF inheritance patterns
        if (className.contains("Listener")) {
            return "gov.nasa.jpf.Listener";
        } else if (className.contains("InstructionFactory")) {
            return "gov.nasa.jpf.InstructionFactory";
        } else if (className.contains("NativePeer")) {
            return "gov.nasa.jpf.NativePeer";
        } else if (className.contains("ChoiceGenerator")) {
            return "gov.nasa.jpf.ChoiceGenerator";
        }
        
        return "java.lang.Object";
    }
    
    private String extractStandardClassName(String className) {
        // Extract the standard class name that this model class represents
        // Example: "java.lang.StringModel" -> "java.lang.String"
        String name = className.replaceAll(".*\\.", ""); // Remove package
        name = name.replaceAll("Model|Peer", ""); // Remove suffixes
        return "java.lang." + name;
    }
    
    private String extractModelClassName(String className) {
        // Extract the model class name for a native peer
        // Example: "StringNativePeer" -> "StringModel"
        String name = className.replaceAll(".*\\.", ""); // Remove package
        name = name.replaceAll("NativePeer|Peer", ""); // Remove suffixes
        return name + "Model";
    }
} 