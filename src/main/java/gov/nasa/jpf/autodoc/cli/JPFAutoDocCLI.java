package gov.nasa.jpf.autodoc.cli;

import gov.nasa.jpf.autodoc.core.AnalysisEngine;
import gov.nasa.jpf.autodoc.core.DefaultAnalysisEngine;
import gov.nasa.jpf.autodoc.core.model.AnalysisConfig;
import gov.nasa.jpf.autodoc.core.model.UnifiedAnalysisResult;
import gov.nasa.jpf.autodoc.output.OutputGenerator;
import gov.nasa.jpf.autodoc.output.OutputGeneratorFactory;
import gov.nasa.jpf.autodoc.output.OutputConfig;
import gov.nasa.jpf.autodoc.output.OutputFormat;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Main CLI class for the JPF AutoDoc tool.
 * Provides a unified interface for analyzing JPF configurations and type hierarchies.
 */
@Command(
    name = "jpfautodoc",
    mixinStandardHelpOptions = true,
    version = "JPF AutoDoc 1.0.0",
    description = "Unified JPF documentation generation tool",
    usageHelpAutoWidth = true
)
public class JPFAutoDocCLI implements Callable<Integer> {
    
    @Option(names = {"-cp", "--classpath"}, 
            description = "Classpath to analyze (directory or JAR file)")
    private String classpath;
    
    @Option(names = {"-o", "--output"}, 
            description = "Output format: ${COMPLETION-CANDIDATES}",
            defaultValue = "MARKDOWN")
    private OutputFormat outputFormat = OutputFormat.MARKDOWN;
    
    @Option(names = {"-f", "--file"}, 
            description = "Output file name")
    private String outputFile;
    
    @Option(names = {"--config-only"}, 
            description = "Analyze only configurations")
    private boolean configOnly = false;
    
    @Option(names = {"--types-only"}, 
            description = "Analyze only type hierarchy")
    private boolean typesOnly = false;
    
    @Option(names = {"--validate"}, 
            description = "Enable validation")
    private boolean validate = false;
    
    @Option(names = {"--parallel"}, 
            description = "Number of parallel threads (default: number of processors)")
    private Integer threadCount;
    
    @Option(names = {"--verbose", "-v"}, 
            description = "Enable verbose output")
    private boolean verbose = false;
    
    @Option(names = {"--include"}, 
            description = "Include pattern for class names")
    private List<String> includePatterns;
    
    @Option(names = {"--exclude"}, 
            description = "Exclude pattern for class names")
    private List<String> excludePatterns;
    
    @Parameters(description = "Target files or directories to analyze")
    private List<String> targets;
    
    private final AnalysisEngine analysisEngine;
    private final OutputGenerator outputGenerator;
    
    public JPFAutoDocCLI() {
        // Initialize with actual implementations
        this.analysisEngine = new DefaultAnalysisEngine();
        this.outputGenerator = null; // Will be selected based on output format
    }
    
    @Override
    public Integer call() throws Exception {
        try {
            // Build analysis configuration
            AnalysisConfig config = buildAnalysisConfig();
            
            // Validate inputs
            if (!validateInputs()) {
                return 1;
            }
            
            // Get output generator for the specified format
            OutputGenerator generator = OutputGeneratorFactory.getGenerator(outputFormat);
            if (generator == null) {
                System.err.println("Error: Unsupported output format: " + outputFormat);
                System.err.println("Supported formats: " + java.util.Arrays.toString(OutputGeneratorFactory.getSupportedFormats()));
                return 1;
            }
            
            // Process targets
            if (targets == null || targets.isEmpty()) {
                System.err.println("Error: No targets specified. Use --help for usage information.");
                return 1;
            }
            
            // Run analysis for each target
            for (String target : targets) {
                System.out.println("Analyzing: " + target);
                
                UnifiedAnalysisResult result = analysisEngine.analyze(target, config);
                
                if (result.isEmpty()) {
                    System.out.println("No analysis results found for: " + target);
                    continue;
                }
                
                // Generate output
                generateOutput(result, target, generator);
                
                // Print summary
                printSummary(result);
            }
            
            return 0;
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            if (verbose) {
                e.printStackTrace();
            }
            return 1;
        }
    }
    
    private AnalysisConfig buildAnalysisConfig() {
        AnalysisConfig.Builder builder = AnalysisConfig.builder()
                .validateResults(validate)
                .verbose(verbose);
        
        // Set analysis types
        if (configOnly) {
            builder.analyzeConfigurations(true)
                   .analyzeTypes(false);
        } else if (typesOnly) {
            builder.analyzeConfigurations(false)
                   .analyzeTypes(true);
        } else {
            builder.analyzeConfigurations(true)
                   .analyzeTypes(true);
        }
        
        // Set parallel processing
        if (threadCount != null) {
            builder.parallelProcessing(true)
                   .threadCount(threadCount);
        } else {
            builder.parallelProcessing(true);
        }
        
        // Add include/exclude patterns
        if (includePatterns != null) {
            for (String pattern : includePatterns) {
                builder.includePattern(pattern);
            }
        }
        
        if (excludePatterns != null) {
            for (String pattern : excludePatterns) {
                builder.excludePattern(pattern);
            }
        }
        
        return builder.build();
    }
    
    private boolean validateInputs() {
        if (classpath == null || classpath.trim().isEmpty()) {
            System.err.println("Error: Classpath is required. Use -cp or --classpath.");
            return false;
        }
        
        return true;
    }
    
    private void generateOutput(UnifiedAnalysisResult result, String target, OutputGenerator generator) {
        try {
            // Build output configuration
            OutputConfig.Builder outputConfigBuilder = OutputConfig.builder()
                .format(outputFormat)
                .verbose(verbose);
            
            if (outputFile != null) {
                outputConfigBuilder.outputFile(outputFile);
            }
            
            OutputConfig outputConfig = outputConfigBuilder.build();
            
            // Generate output
            generator.generate(result, outputConfig);
            
            System.out.println("Output generated: " + (outputFile != null ? outputFile : "default filename"));
            
        } catch (Exception e) {
            System.err.println("Error generating output: " + e.getMessage());
            if (verbose) {
                e.printStackTrace();
            }
        }
    }
    
    private String generateDefaultFileName(String target, OutputFormat format) {
        String baseName = target.replaceAll("[^a-zA-Z0-9]", "_");
        String extension = format.getFileExtension();
        return baseName + "_analysis." + extension;
    }
    
    private void printSummary(UnifiedAnalysisResult result) {
        System.out.println("\nAnalysis Summary:");
        System.out.println("=================");
        System.out.println("Source: " + result.getSourcePath());
        System.out.println("Date: " + result.getAnalysisDate());
        System.out.println("Configurations: " + result.getTotalConfigurations());
        System.out.println("Types: " + result.getTotalTypes());
        
        if (result.hasConfigurations()) {
            System.out.println("\nConfiguration Details:");
            System.out.println("- Config Options: " + result.getConfigOptions().size());
            System.out.println("- Config Annotations: " + result.getConfigAnnotations().size());
            System.out.println("- Choice Generators: " + result.getChoiceGenerators().size());
            System.out.println("- Loggers: " + result.getLoggers().size());
        }
        
        if (result.hasTypes()) {
            System.out.println("\nType Details:");
            System.out.println("- Types: " + result.getTypes().size());
            System.out.println("- Model Classes: " + result.getModelClasses().size());
            System.out.println("- Native Peers: " + result.getNativePeers().size());
            System.out.println("- Listeners: " + result.getListeners().size());
        }
        
        if (result.getValidationReport() != null) {
            System.out.println("\nValidation:");
            System.out.println("- Issues: " + result.getValidationReport().getIssueCount());
            System.out.println("- Valid: " + result.getValidationReport().isValid());
        }
        
        System.out.println();
    }
    
    public static void main(String[] args) {
        int exitCode = new CommandLine(new JPFAutoDocCLI()).execute(args);
        System.exit(exitCode);
    }
} 