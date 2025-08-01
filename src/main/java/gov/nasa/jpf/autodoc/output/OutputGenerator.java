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