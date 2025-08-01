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