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

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

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
    
    private static final Logger logger = Logger.getLogger(JPFAutoDocCLI.class.getName());
    
    @Option(names = {"-cp", "--classpath"}, 
            description = "Classpath to analyze (directory, JAR, ZIP, or other archive files)")
    private String classpath;
    
    @Option(names = {"-o", "--output"}, 
            description = "Output format: ${COMPLETION-CANDIDATES}",
            defaultValue = "MARKDOWN")
    private OutputFormat outputFormat = OutputFormat.MARKDOWN;
    
    @Option(names = {"--outputs"}, 
            description = "Multiple output formats (comma-separated): markdown,xml,json,html,text")
    private String multipleOutputs;
    
    @Option(names = {"-f", "--file"}, 
            description = "Output file name")
    private String outputFile;
    
    @Option(names = {"--files"}, 
            description = "Multiple output file names (comma-separated, must match number of formats)")
    private String multipleFiles;
    
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
    
    @Option(names = {"--max-memory"}, 
            description = "Maximum memory usage in MB (default: 2048)")
    private Integer maxMemory = 2048;
    
    @Option(names = {"--show-progress"}, 
            description = "Show progress during analysis")
    private boolean showProgress = false;
    
    @Option(names = {"--timeout"}, 
            description = "Analysis timeout in seconds (default: 300)")
    private Integer timeout = 300;
    
    @Option(names = {"--config-file"}, 
            description = "Configuration file path")
    private String configFile;
    
    @Option(names = {"--output-dir"}, 
            description = "Output directory for generated files")
    private String outputDir;
    
    @Option(names = {"--overwrite"}, 
            description = "Overwrite existing output files")
    private boolean overwrite = false;
    
    @Parameters(description = "Target files or directories to analyze")
    private List<String> targets;
    
    private final AnalysisEngine analysisEngine;
    private final OutputGenerator outputGenerator;
    
    public JPFAutoDocCLI() {
        // Initialize with actual implementations
        this.analysisEngine = new DefaultAnalysisEngine();
        this.outputGenerator = null; // Will be selected based on output format
        
        // Configure logging
        configureLogging();
    }
    
    private void configureLogging() {
        // Remove default handlers
        Logger rootLogger = Logger.getLogger("");
        for (java.util.logging.Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        
        // Add custom handler with formatter
        StreamHandler handler = new StreamHandler(System.err, new SimpleFormatter());
        handler.setLevel(Level.ALL);
        rootLogger.addHandler(handler);
        
        // Set default level
        rootLogger.setLevel(Level.WARNING);
    }
    
    @Override
    public Integer call() throws Exception {
        try {
            // Set logging level based on verbose flag
            if (verbose) {
                Logger.getLogger("").setLevel(Level.INFO);
                logger.info("Starting JPF AutoDoc analysis with verbose logging");
            } else {
                Logger.getLogger("").setLevel(Level.WARNING);
            }
            
            // Validate inputs first
            if (!validateInputs()) {
                return 1;
            }
            
            // Build analysis configuration
            AnalysisConfig config = buildAnalysisConfig();
            logger.info("Analysis configuration built: " + config.toString());
            
            // Perform analysis for each target
            for (String target : targets) {
                try {
                    logger.info("Starting analysis of target: " + target);
                    System.out.println("Analyzing: " + target);
                    
                    // Perform analysis
                    UnifiedAnalysisResult result = analysisEngine.analyze(target, config);
                    logger.info("Analysis completed for target: " + target + 
                              " - Configurations: " + result.getTotalConfigurations() + 
                              ", Types: " + result.getTotalTypes());
                    
                    // Generate output
                    if (multipleOutputs != null) {
                        generateMultipleOutputs(result, target);
                    } else {
                        OutputGenerator generator = OutputGeneratorFactory.getGenerator(outputFormat);
                        if (generator == null) {
                            logger.severe("Unsupported output format: " + outputFormat);
                            System.err.println("Error: Unsupported output format: " + outputFormat);
                            System.err.println("Supported formats: " + java.util.Arrays.toString(OutputGeneratorFactory.getSupportedFormats()));
                            return 1;
                        }
                        generateOutput(result, target, generator);
                    }
                    
                    // Print summary if verbose
                    if (verbose) {
                        printSummary(result);
                    }
                    
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error analyzing target: " + target, e);
                    System.err.println("Error analyzing target '" + target + "': " + e.getMessage());
                    if (verbose) {
                        e.printStackTrace();
                    }
                    return 1;
                }
            }
            
            logger.info("All analysis completed successfully");
            return 0;
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during analysis", e);
            System.err.println("Unexpected error: " + e.getMessage());
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
        
        // Set analysis type
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
        
        // Load configuration from file if specified
        if (configFile != null) {
            try {
                loadConfigurationFromFile(builder);
            } catch (Exception e) {
                logger.warning("Could not load configuration from file: " + e.getMessage());
            }
        }
        
        return builder.build();
    }
    
    private void loadConfigurationFromFile(AnalysisConfig.Builder builder) throws Exception {
        logger.info("Loading configuration from file: " + configFile);
        
        java.util.Properties props = new java.util.Properties();
        try (java.io.FileInputStream fis = new java.io.FileInputStream(configFile)) {
            props.load(fis);
        }
        
        // Load analysis settings
        String configOnlyStr = props.getProperty("config-only");
        if (configOnlyStr != null) {
            boolean configOnly = Boolean.parseBoolean(configOnlyStr);
            builder.analyzeConfigurations(configOnly)
                   .analyzeTypes(!configOnly);
            logger.info("Loaded config-only setting: " + configOnly);
        }
        
        String typesOnlyStr = props.getProperty("types-only");
        if (typesOnlyStr != null) {
            boolean typesOnly = Boolean.parseBoolean(typesOnlyStr);
            builder.analyzeConfigurations(!typesOnly)
                   .analyzeTypes(typesOnly);
            logger.info("Loaded types-only setting: " + typesOnly);
        }
        
        // Load validation setting
        String validateStr = props.getProperty("validate");
        if (validateStr != null) {
            boolean validate = Boolean.parseBoolean(validateStr);
            builder.validateResults(validate);
            logger.info("Loaded validate setting: " + validate);
        }
        
        // Load parallel processing settings
        String parallelStr = props.getProperty("parallel");
        if (parallelStr != null) {
            try {
                int threads = Integer.parseInt(parallelStr);
                if (threads > 0 && threads <= 32) {
                    builder.parallelProcessing(true)
                           .threadCount(threads);
                    logger.info("Loaded parallel threads setting: " + threads);
                } else {
                    logger.warning("Invalid thread count in config file: " + threads);
                }
            } catch (NumberFormatException e) {
                logger.warning("Invalid parallel setting in config file: " + parallelStr);
            }
        }
        
        // Load include patterns
        String includePatternsStr = props.getProperty("include-patterns");
        if (includePatternsStr != null) {
            String[] patterns = includePatternsStr.split(",");
            for (String pattern : patterns) {
                String trimmed = pattern.trim();
                if (!trimmed.isEmpty()) {
                    builder.includePattern(trimmed);
                    logger.info("Loaded include pattern: " + trimmed);
                }
            }
        }
        
        // Load exclude patterns
        String excludePatternsStr = props.getProperty("exclude-patterns");
        if (excludePatternsStr != null) {
            String[] patterns = excludePatternsStr.split(",");
            for (String pattern : patterns) {
                String trimmed = pattern.trim();
                if (!trimmed.isEmpty()) {
                    builder.excludePattern(trimmed);
                    logger.info("Loaded exclude pattern: " + trimmed);
                }
            }
        }
        
        // Load verbose setting
        String verboseStr = props.getProperty("verbose");
        if (verboseStr != null) {
            boolean verbose = Boolean.parseBoolean(verboseStr);
            builder.verbose(verbose);
            logger.info("Loaded verbose setting: " + verbose);
        }
        
        logger.info("Configuration file loaded successfully");
    }
    
    private boolean validateInputs() {
        // Validate classpath
        if (classpath == null || classpath.trim().isEmpty()) {
            System.err.println("Error: Classpath is required. Use -cp or --classpath.");
            return false;
        }
        
        // Validate classpath exists
        File classpathFile = new File(classpath);
        if (!classpathFile.exists()) {
            System.err.println("Error: Classpath does not exist: " + classpath);
            return false;
        }
        
        // Validate targets
        if (targets == null || targets.isEmpty()) {
            System.err.println("Error: At least one target must be specified.");
            return false;
        }
        
        // Validate each target exists
        for (String target : targets) {
            File targetFile = new File(target);
            if (!targetFile.exists()) {
                System.err.println("Error: Target does not exist: " + target);
                return false;
            }
        }
        
        // Validate output options
        if (configOnly && typesOnly) {
            System.err.println("Error: Cannot use both --config-only and --types-only simultaneously.");
            return false;
        }
        
        // Validate thread count
        if (threadCount != null && (threadCount < 1 || threadCount > 32)) {
            System.err.println("Error: Thread count must be between 1 and 32.");
            return false;
        }
        
        // Validate memory limit
        if (maxMemory != null && (maxMemory < 512 || maxMemory > 16384)) {
            System.err.println("Error: Memory limit must be between 512 and 16384 MB.");
            return false;
        }
        
        // Validate timeout
        if (timeout != null && (timeout < 30 || timeout > 3600)) {
            System.err.println("Error: Timeout must be between 30 and 3600 seconds.");
            return false;
        }
        
        // Validate config file if specified
        if (configFile != null) {
            File configFileObj = new File(configFile);
            if (!configFileObj.exists()) {
                System.err.println("Error: Configuration file does not exist: " + configFile);
                return false;
            }
            if (!configFileObj.canRead()) {
                System.err.println("Error: Configuration file is not readable: " + configFile);
                return false;
            }
        }
        
        // Validate output directory if specified
        if (outputDir != null) {
            File outputDirObj = new File(outputDir);
            if (!outputDirObj.exists()) {
                if (!outputDirObj.mkdirs()) {
                    System.err.println("Error: Cannot create output directory: " + outputDir);
                    return false;
                }
            } else if (!outputDirObj.isDirectory()) {
                System.err.println("Error: Output directory path is not a directory: " + outputDir);
                return false;
            } else if (!outputDirObj.canWrite()) {
                System.err.println("Error: Output directory is not writable: " + outputDir);
                return false;
            }
        }
        
        // Validate output file extension matches format
        if (outputFile != null) {
            String expectedExtension = outputFormat.getFileExtension();
            if (!outputFile.toLowerCase().endsWith("." + expectedExtension)) {
                System.err.println("Warning: Output file extension doesn't match format. Expected: ." + expectedExtension);
            }
            
            // Check if file exists and overwrite is not enabled
            File outputFileObj = new File(outputFile);
            if (outputFileObj.exists() && !overwrite) {
                System.err.println("Error: Output file already exists: " + outputFile + ". Use --overwrite to overwrite.");
                return false;
            }
        }
        
        // Validate multiple outputs
        if (multipleOutputs != null) {
            String[] formats = multipleOutputs.split(",");
            if (formats.length == 0) {
                System.err.println("Error: No output formats specified in --outputs");
                return false;
            }
            
            for (String formatStr : formats) {
                String trimmed = formatStr.trim().toUpperCase();
                try {
                    OutputFormat.valueOf(trimmed);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error: Invalid output format: " + formatStr);
                    return false;
                }
            }
            
            // Validate multiple files if specified
            if (multipleFiles != null) {
                String[] files = multipleFiles.split(",");
                if (files.length != formats.length) {
                    System.err.println("Error: Number of files (" + files.length + 
                                     ") must match number of formats (" + formats.length + ")");
                    return false;
                }
                
                for (String file : files) {
                    String trimmed = file.trim();
                    if (trimmed.isEmpty()) {
                        System.err.println("Error: Empty file name in --files");
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    private void generateOutput(UnifiedAnalysisResult result, String target, OutputGenerator generator) {
        try {
            // Build output configuration
            OutputConfig.Builder outputConfigBuilder = OutputConfig.builder()
                .format(outputFormat)
                .verbose(verbose);
            
            // Set output file path
            String finalOutputFile = outputFile;
            if (outputFile == null) {
                finalOutputFile = generateDefaultFileName(target, outputFormat);
            }
            
            // Prepend output directory if specified
            if (outputDir != null) {
                File outputDirFile = new File(outputDir);
                finalOutputFile = new File(outputDirFile, new File(finalOutputFile).getName()).getPath();
            }
            
            outputConfigBuilder.outputFile(finalOutputFile);
            
            // Check if file exists and handle overwrite
            File outputFileObj = new File(finalOutputFile);
            if (outputFileObj.exists() && !overwrite) {
                logger.warning("Output file already exists, skipping: " + finalOutputFile);
                System.out.println("Skipping existing file: " + finalOutputFile + " (use --overwrite to overwrite)");
                return;
            }
            
            OutputConfig outputConfig = outputConfigBuilder.build();
            
            // Generate output
            generator.generate(result, outputConfig);
            
            logger.info("Output generated successfully: " + finalOutputFile);
            System.out.println("Output generated: " + finalOutputFile);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error generating output", e);
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
    
    private void generateMultipleOutputs(UnifiedAnalysisResult result, String target) {
        try {
            String[] formats = multipleOutputs.split(",");
            String[] files = multipleFiles != null ? multipleFiles.split(",") : null;
            
            if (files != null && files.length != formats.length) {
                logger.severe("Number of files must match number of formats");
                System.err.println("Error: Number of files (" + files.length + 
                                 ") must match number of formats (" + formats.length + ")");
                return;
            }
            
            logger.info("Generating multiple outputs: " + multipleOutputs);
            
            for (int i = 0; i < formats.length; i++) {
                String formatStr = formats[i].trim().toUpperCase();
                OutputFormat format;
                try {
                    format = OutputFormat.valueOf(formatStr);
                } catch (IllegalArgumentException e) {
                    logger.warning("Invalid output format: " + formatStr);
                    System.err.println("Warning: Invalid output format: " + formatStr + ", skipping");
                    continue;
                }
                
                OutputGenerator generator = OutputGeneratorFactory.getGenerator(format);
                if (generator == null) {
                    logger.warning("Unsupported output format: " + format);
                    System.err.println("Warning: Unsupported output format: " + format + ", skipping");
                    continue;
                }
                
                // Set output file for this format
                String originalOutputFile = outputFile;
                if (files != null && i < files.length) {
                    outputFile = files[i].trim();
                } else {
                    outputFile = generateDefaultFileName(target, format);
                }
                
                try {
                    generateOutput(result, target, generator);
                } finally {
                    // Restore original output file
                    outputFile = originalOutputFile;
                }
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error generating multiple outputs", e);
            System.err.println("Error generating multiple outputs: " + e.getMessage());
            if (verbose) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        int exitCode = new CommandLine(new JPFAutoDocCLI()).execute(args);
        System.exit(exitCode);
    }
} 