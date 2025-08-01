package gov.nasa.jpf.autodoc.output;

import java.util.Objects;

/**
 * Configuration for output generation.
 */
public class OutputConfig {
    
    private final String outputFile;
    private final OutputFormat format;
    private final boolean verbose;
    private final boolean includeValidation;
    private final boolean includeCrossReferences;
    
    private OutputConfig(Builder builder) {
        this.outputFile = builder.outputFile;
        this.format = builder.format;
        this.verbose = builder.verbose;
        this.includeValidation = builder.includeValidation;
        this.includeCrossReferences = builder.includeCrossReferences;
    }
    
    // Getters
    public String getOutputFile() {
        return outputFile;
    }
    
    public OutputFormat getFormat() {
        return format;
    }
    
    public boolean isVerbose() {
        return verbose;
    }
    
    public boolean isIncludeValidation() {
        return includeValidation;
    }
    
    public boolean isIncludeCrossReferences() {
        return includeCrossReferences;
    }
    
    /**
     * Builder for OutputConfig.
     */
    public static class Builder {
        private String outputFile;
        private OutputFormat format = OutputFormat.MARKDOWN;
        private boolean verbose = false;
        private boolean includeValidation = true;
        private boolean includeCrossReferences = true;
        
        public Builder outputFile(String outputFile) {
            this.outputFile = outputFile;
            return this;
        }
        
        public Builder format(OutputFormat format) {
            this.format = format;
            return this;
        }
        
        public Builder verbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }
        
        public Builder includeValidation(boolean includeValidation) {
            this.includeValidation = includeValidation;
            return this;
        }
        
        public Builder includeCrossReferences(boolean includeCrossReferences) {
            this.includeCrossReferences = includeCrossReferences;
            return this;
        }
        
        public OutputConfig build() {
            return new OutputConfig(this);
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    @Override
    public String toString() {
        return "OutputConfig{" +
                "outputFile='" + outputFile + '\'' +
                ", format=" + format +
                ", verbose=" + verbose +
                '}';
    }
} 