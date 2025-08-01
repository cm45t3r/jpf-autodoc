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

package gov.nasa.jpf.autodoc.options;

import gov.nasa.jpf.autodoc.core.ClassFile;
import gov.nasa.jpf.autodoc.core.model.UnifiedAnalysisResult;
import gov.nasa.jpf.autodoc.core.model.ConfigOption;
import gov.nasa.jpf.autodoc.core.model.ConfigAnnotation;
import gov.nasa.jpf.autodoc.core.model.ChoiceGenerator;
import gov.nasa.jpf.autodoc.core.model.LoggerConfig;

import java.util.regex.Pattern;

/**
 * Analyzes JPF configuration options from class files.
 * Extracts configuration options, annotations, choice generators, and loggers.
 */
public class ConfigurationAnalyzer {
    
    // JPF-specific patterns
    private static final Pattern JPF_OPTION_PATTERN = Pattern.compile("@JPFOption");
    private static final Pattern CONFIG_OPTION_PATTERN = Pattern.compile("get(\\w+)Option");
    private static final Pattern CHOICE_GENERATOR_PATTERN = Pattern.compile("ChoiceGenerator");
    private static final Pattern LOGGER_PATTERN = Pattern.compile("Logger");
    
    // JPF package patterns
    private static final Pattern JPF_PACKAGE_PATTERN = Pattern.compile("gov\\.nasa\\.jpf");
    
    public void analyze(ClassFile classFile, UnifiedAnalysisResult result) {
        try {
            // Skip non-JPF classes
            if (!isJPFClass(classFile)) {
                return;
            }
            
            System.out.println("DEBUG: Analyzing JPF class: " + classFile.getClassName());
            
            // Analyze configuration options
            analyzeConfigurationOptions(classFile, result);
            
            // Analyze JPF annotations
            analyzeJPFAnnotations(classFile, result);
            
            // Analyze choice generators
            analyzeChoiceGenerators(classFile, result);
            
            // Analyze loggers
            analyzeLoggers(classFile, result);
            
        } catch (Exception e) {
            // Log error but continue with other classes
            System.err.println("Warning: Error analyzing configuration for " + classFile.getClassName() + ": " + e.getMessage());
        }
    }
    
    private boolean isJPFClass(ClassFile classFile) {
        String className = classFile.getClassName();
        // Be more lenient - accept any class in the JPF package
        return className.startsWith("gov.nasa.jpf");
    }
    
    private void analyzeConfigurationOptions(ClassFile classFile, UnifiedAnalysisResult result) {
        String className = classFile.getClassName();
        
        // Look for configuration option patterns in the class
        // This is a simplified implementation - in practice, we'd use JPF's ClassFile framework
        // to parse the bytecode and extract method signatures
        
        // Example: Look for methods that might be configuration options
        if (className.contains("Config") || className.contains("Option") || className.contains("Event")) {
            // This is a configuration-related class
            ConfigOption option = new ConfigOption(
                extractOptionName(className),
                className,
                "String", // Default type
                "get" + extractOptionName(className) + "Option"
            );
            
            result.addConfigOption(option);
            System.out.println("DEBUG: Added config option: " + option.getName());
        }
    }
    
    private void analyzeJPFAnnotations(ClassFile classFile, UnifiedAnalysisResult result) {
        String className = classFile.getClassName();
        
        // Look for classes with JPF annotations
        // In practice, we'd parse the class file to find annotations
        if (className.contains("Option") || className.contains("Config")) {
            ConfigAnnotation annotation = new ConfigAnnotation(
                extractOptionName(className),
                className,
                "String", // Default type
                "default", // Default value
                "JPF configuration option", // Comment
                "JPFOption"
            );
            
            result.addConfigAnnotation(annotation);
        }
    }
    
    private void analyzeChoiceGenerators(ClassFile classFile, UnifiedAnalysisResult result) {
        String className = classFile.getClassName();
        
        // Look for choice generator classes
        if (className.contains("ChoiceGenerator") || className.contains("Choice")) {
            ChoiceGenerator choiceGen = new ChoiceGenerator(
                extractChoiceGeneratorName(className),
                className,
                "generate", // Default method
                "ChoiceGenerator"
            );
            
            result.addChoiceGenerator(choiceGen);
        }
    }
    
    private void analyzeLoggers(ClassFile classFile, UnifiedAnalysisResult result) {
        String className = classFile.getClassName();
        
        // Look for logger classes
        if (className.contains("Logger") || className.contains("Log")) {
            LoggerConfig logger = new LoggerConfig(
                extractLoggerName(className),
                className,
                "Logger"
            );
            
            result.addLogger(logger);
        }
    }
    
    private String extractOptionName(String className) {
        // Extract option name from class name
        // Example: "ConfigOption" -> "config"
        String name = className.replaceAll(".*\\.", ""); // Remove package
        name = name.replaceAll("Config|Option|JPF", ""); // Remove common suffixes
        return name.isEmpty() ? "unknown" : name.toLowerCase();
    }
    
    private String extractChoiceGeneratorName(String className) {
        // Extract choice generator name from class name
        String name = className.replaceAll(".*\\.", ""); // Remove package
        name = name.replaceAll("ChoiceGenerator|Choice", ""); // Remove common suffixes
        return name.isEmpty() ? "unknown" : name;
    }
    
    private String extractLoggerName(String className) {
        // Extract logger name from class name
        String name = className.replaceAll(".*\\.", ""); // Remove package
        name = name.replaceAll("Logger|Log", ""); // Remove common suffixes
        return name.isEmpty() ? "unknown" : name;
    }
} 