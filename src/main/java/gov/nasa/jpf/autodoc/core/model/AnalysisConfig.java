package gov.nasa.jpf.autodoc.core.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Configuration for analysis operations.
 * This class defines what types of analysis to perform and how to perform them.
 */
public class AnalysisConfig {
    
    private final boolean analyzeConfigurations;
    private final boolean analyzeTypes;
    private final boolean validateResults;
    private final boolean parallelProcessing;
    private final int threadCount;
    private final Set<String> includePatterns;
    private final Set<String> excludePatterns;
    private final boolean verbose;
    
    private AnalysisConfig(Builder builder) {
        this.analyzeConfigurations = builder.analyzeConfigurations;
        this.analyzeTypes = builder.analyzeTypes;
        this.validateResults = builder.validateResults;
        this.parallelProcessing = builder.parallelProcessing;
        this.threadCount = builder.threadCount;
        this.includePatterns = new HashSet<>(builder.includePatterns);
        this.excludePatterns = new HashSet<>(builder.excludePatterns);
        this.verbose = builder.verbose;
    }
    
    // Getters
    public boolean isAnalyzeConfigurations() {
        return analyzeConfigurations;
    }
    
    public boolean isAnalyzeTypes() {
        return analyzeTypes;
    }
    
    public boolean isValidateResults() {
        return validateResults;
    }
    
    public boolean isParallelProcessing() {
        return parallelProcessing;
    }
    
    public int getThreadCount() {
        return threadCount;
    }
    
    public Set<String> getIncludePatterns() {
        return new HashSet<>(includePatterns);
    }
    
    public Set<String> getExcludePatterns() {
        return new HashSet<>(excludePatterns);
    }
    
    public boolean isVerbose() {
        return verbose;
    }
    
    /**
     * Builder for AnalysisConfig.
     */
    public static class Builder {
        private boolean analyzeConfigurations = true;
        private boolean analyzeTypes = true;
        private boolean validateResults = false;
        private boolean parallelProcessing = true;
        private int threadCount = Runtime.getRuntime().availableProcessors();
        private Set<String> includePatterns = new HashSet<>();
        private Set<String> excludePatterns = new HashSet<>();
        private boolean verbose = false;
        
        public Builder analyzeConfigurations(boolean analyzeConfigurations) {
            this.analyzeConfigurations = analyzeConfigurations;
            return this;
        }
        
        public Builder analyzeTypes(boolean analyzeTypes) {
            this.analyzeTypes = analyzeTypes;
            return this;
        }
        
        public Builder validateResults(boolean validateResults) {
            this.validateResults = validateResults;
            return this;
        }
        
        public Builder parallelProcessing(boolean parallelProcessing) {
            this.parallelProcessing = parallelProcessing;
            return this;
        }
        
        public Builder threadCount(int threadCount) {
            this.threadCount = threadCount;
            return this;
        }
        
        public Builder includePattern(String pattern) {
            this.includePatterns.add(pattern);
            return this;
        }
        
        public Builder excludePattern(String pattern) {
            this.excludePatterns.add(pattern);
            return this;
        }
        
        public Builder verbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }
        
        public AnalysisConfig build() {
            return new AnalysisConfig(this);
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static AnalysisConfig defaultConfig() {
        return builder().build();
    }
    
    public static AnalysisConfig configOnly() {
        return builder()
                .analyzeConfigurations(true)
                .analyzeTypes(false)
                .build();
    }
    
    public static AnalysisConfig typesOnly() {
        return builder()
                .analyzeConfigurations(false)
                .analyzeTypes(true)
                .build();
    }
} 