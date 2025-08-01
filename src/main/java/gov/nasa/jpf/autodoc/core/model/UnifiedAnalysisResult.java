package gov.nasa.jpf.autodoc.core.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Unified analysis result containing all extracted information from both
 * configuration analysis and type hierarchy analysis.
 */
public class UnifiedAnalysisResult {
    
    // Configuration analysis results
    private final Map<String, ConfigOption> configOptions;
    private final Map<String, ConfigAnnotation> configAnnotations;
    private final Map<String, ChoiceGenerator> choiceGenerators;
    private final Map<String, LoggerConfig> loggers;
    
    // Type hierarchy analysis results
    private final Map<String, TypeInfo> types;
    private final Map<String, ModelClass> modelClasses;
    private final Map<String, NativePeer> nativePeers;
    private final Map<String, Listener> listeners;
    
    // Cross-references and validation
    private final Map<String, CrossReference> crossReferences;
    private ValidationReport validationReport;
    
    // Metadata
    private final Date analysisDate;
    private final String sourcePath;
    private final AnalysisConfig config;
    
    public UnifiedAnalysisResult(String sourcePath, AnalysisConfig config) {
        this.configOptions = new ConcurrentHashMap<>();
        this.configAnnotations = new ConcurrentHashMap<>();
        this.choiceGenerators = new ConcurrentHashMap<>();
        this.loggers = new ConcurrentHashMap<>();
        this.types = new ConcurrentHashMap<>();
        this.modelClasses = new ConcurrentHashMap<>();
        this.nativePeers = new ConcurrentHashMap<>();
        this.listeners = new ConcurrentHashMap<>();
        this.crossReferences = new ConcurrentHashMap<>();
        this.analysisDate = new Date();
        this.sourcePath = sourcePath;
        this.config = config;
    }
    
    // Configuration analysis methods
    public void addConfigOption(ConfigOption option) {
        configOptions.put(option.getName(), option);
    }
    
    public void addConfigAnnotation(ConfigAnnotation annotation) {
        configAnnotations.put(annotation.getName(), annotation);
    }
    
    public void addChoiceGenerator(ChoiceGenerator cg) {
        choiceGenerators.put(cg.getName(), cg);
    }
    
    public void addLogger(LoggerConfig logger) {
        loggers.put(logger.getName(), logger);
    }
    
    // Type hierarchy analysis methods
    public void addType(TypeInfo type) {
        types.put(type.getName(), type);
    }
    
    public void addModelClass(ModelClass modelClass) {
        modelClasses.put(modelClass.getName(), modelClass);
    }
    
    public void addNativePeer(NativePeer nativePeer) {
        nativePeers.put(nativePeer.getName(), nativePeer);
    }
    
    public void addListener(Listener listener) {
        listeners.put(listener.getName(), listener);
    }
    
    // Cross-reference methods
    public void addCrossReference(CrossReference crossRef) {
        crossReferences.put(crossRef.getId(), crossRef);
    }
    
    public void setValidationReport(ValidationReport report) {
        this.validationReport = report;
    }
    
    // Getters
    public Map<String, ConfigOption> getConfigOptions() {
        return new HashMap<>(configOptions);
    }
    
    public Map<String, ConfigAnnotation> getConfigAnnotations() {
        return new HashMap<>(configAnnotations);
    }
    
    public Map<String, ChoiceGenerator> getChoiceGenerators() {
        return new HashMap<>(choiceGenerators);
    }
    
    public Map<String, LoggerConfig> getLoggers() {
        return new HashMap<>(loggers);
    }
    
    public Map<String, TypeInfo> getTypes() {
        return new HashMap<>(types);
    }
    
    public Map<String, ModelClass> getModelClasses() {
        return new HashMap<>(modelClasses);
    }
    
    public Map<String, NativePeer> getNativePeers() {
        return new HashMap<>(nativePeers);
    }
    
    public Map<String, Listener> getListeners() {
        return new HashMap<>(listeners);
    }
    
    public Map<String, CrossReference> getCrossReferences() {
        return new HashMap<>(crossReferences);
    }
    
    public ValidationReport getValidationReport() {
        return validationReport;
    }
    
    public Date getAnalysisDate() {
        return analysisDate;
    }
    
    public String getSourcePath() {
        return sourcePath;
    }
    
    public AnalysisConfig getConfig() {
        return config;
    }
    
    // Utility methods
    public boolean hasConfigurations() {
        return !configOptions.isEmpty() || !configAnnotations.isEmpty();
    }
    
    public boolean hasTypes() {
        return !types.isEmpty() || !modelClasses.isEmpty() || !nativePeers.isEmpty();
    }
    
    public int getTotalConfigurations() {
        return configOptions.size() + configAnnotations.size() + choiceGenerators.size() + loggers.size();
    }
    
    public int getTotalTypes() {
        return types.size() + modelClasses.size() + nativePeers.size() + listeners.size();
    }
    
    public boolean isEmpty() {
        return !hasConfigurations() && !hasTypes();
    }
    
    @Override
    public String toString() {
        return "UnifiedAnalysisResult{" +
                "configOptions=" + configOptions.size() +
                ", configAnnotations=" + configAnnotations.size() +
                ", types=" + types.size() +
                ", modelClasses=" + modelClasses.size() +
                ", nativePeers=" + nativePeers.size() +
                ", listeners=" + listeners.size() +
                ", sourcePath='" + sourcePath + '\'' +
                '}';
    }
} 