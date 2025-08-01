package gov.nasa.jpf.autodoc.core;

import gov.nasa.jpf.autodoc.core.model.AnalysisConfig;
import gov.nasa.jpf.autodoc.core.model.UnifiedAnalysisResult;
import gov.nasa.jpf.autodoc.options.ConfigurationAnalyzer;
import gov.nasa.jpf.autodoc.types.TypeHierarchyAnalyzer;
import gov.nasa.jpf.autodoc.types.CrossReferenceAnalyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import java.util.stream.Collectors;

/**
 * Default implementation of the AnalysisEngine interface.
 * Coordinates configuration analysis, type hierarchy analysis, and cross-reference analysis.
 */
public class DefaultAnalysisEngine implements AnalysisEngine {
    
    private final ConfigurationAnalyzer configAnalyzer;
    private final TypeHierarchyAnalyzer typeAnalyzer;
    private final CrossReferenceAnalyzer crossRefAnalyzer;
    
    public DefaultAnalysisEngine() {
        this.configAnalyzer = new ConfigurationAnalyzer();
        this.typeAnalyzer = new TypeHierarchyAnalyzer();
        this.crossRefAnalyzer = new CrossReferenceAnalyzer();
    }
    
    @Override
    public UnifiedAnalysisResult analyze(ClassFileSet files, AnalysisConfig config) {
        UnifiedAnalysisResult result = new UnifiedAnalysisResult("ClassFileSet", config);
        
        try {
            // Process files in parallel if configured
            if (config.isParallelProcessing()) {
                return analyzeParallel(files, config);
            } else {
                return analyzeSequential(files, config);
            }
        } catch (Exception e) {
            throw new RuntimeException("Analysis failed", e);
        }
    }
    
    @Override
    public UnifiedAnalysisResult analyze(ClassFile classFile, AnalysisConfig config) {
        UnifiedAnalysisResult result = new UnifiedAnalysisResult(classFile.getSourcePath().toString(), config);
        
        try {
            if (config.isAnalyzeConfigurations()) {
                configAnalyzer.analyze(classFile, result);
            }
            
            if (config.isAnalyzeTypes()) {
                typeAnalyzer.analyze(classFile, result);
            }
            
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Analysis failed for " + classFile.getClassName(), e);
        }
    }
    
    @Override
    public UnifiedAnalysisResult analyze(String source, AnalysisConfig config) {
        Path sourcePath = Paths.get(source);
        
        if (!Files.exists(sourcePath)) {
            throw new IllegalArgumentException("Source path does not exist: " + source);
        }
        
        UnifiedAnalysisResult result = new UnifiedAnalysisResult(source, config);
        
        try {
            if (Files.isDirectory(sourcePath)) {
                return analyzeDirectory(sourcePath, config);
            } else if (source.endsWith(".jar")) {
                return analyzeJar(sourcePath, config);
            } else {
                return analyzeFile(sourcePath, config);
            }
        } catch (Exception e) {
            throw new RuntimeException("Analysis failed for " + source, e);
        }
    }
    
    private UnifiedAnalysisResult analyzeParallel(ClassFileSet files, AnalysisConfig config) {
        UnifiedAnalysisResult result = new UnifiedAnalysisResult("Parallel Analysis", config);
        
        int threadCount = config.getThreadCount();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        try {
            // Submit analysis tasks
            var futures = files.stream()
                .map(classFile -> executor.submit(() -> analyze(classFile, config)))
                .collect(Collectors.toList());
            
            // Collect results
            for (Future<UnifiedAnalysisResult> future : futures) {
                UnifiedAnalysisResult partialResult = future.get();
                result.merge(partialResult);
            }
            
            // Perform cross-reference analysis
            if (config.isAnalyzeTypes()) {
                crossRefAnalyzer.analyze(result);
            }
            
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Parallel analysis failed", e);
        } finally {
            executor.shutdown();
        }
    }
    
    private UnifiedAnalysisResult analyzeSequential(ClassFileSet files, AnalysisConfig config) {
        UnifiedAnalysisResult result = new UnifiedAnalysisResult("Sequential Analysis", config);
        
        for (ClassFile classFile : files) {
            UnifiedAnalysisResult partialResult = analyze(classFile, config);
            result.merge(partialResult);
        }
        
        // Perform cross-reference analysis
        if (config.isAnalyzeTypes()) {
            crossRefAnalyzer.analyze(result);
        }
        
        return result;
    }
    
    private UnifiedAnalysisResult analyzeDirectory(Path dirPath, AnalysisConfig config) throws IOException {
        ClassFileSet files = new ClassFileSet();
        
        try (Stream<Path> paths = Files.walk(dirPath)) {
            paths.filter(path -> path.toString().endsWith(".class"))
                 .forEach(path -> {
                     try {
                         ClassFile classFile = ClassFile.fromFile(path.toFile());
                         if (shouldInclude(classFile, config)) {
                             files.add(classFile);
                         }
                     } catch (IOException e) {
                         // Log warning but continue
                         System.err.println("Warning: Could not read class file: " + path);
                     }
                 });
        }
        
        return analyze(files, config);
    }
    
    private UnifiedAnalysisResult analyzeJar(Path jarPath, AnalysisConfig config) throws IOException {
        ClassFileSet files = new ClassFileSet();
        
        // TODO: Implement JAR file analysis
        // This will require using java.util.jar.JarFile to read class files from JAR
        System.err.println("Warning: JAR analysis not yet implemented");
        
        return new UnifiedAnalysisResult(jarPath.toString(), config);
    }
    
    private UnifiedAnalysisResult analyzeFile(Path filePath, AnalysisConfig config) throws IOException {
        if (!filePath.toString().endsWith(".class")) {
            throw new IllegalArgumentException("File is not a .class file: " + filePath);
        }
        
        ClassFile classFile = ClassFile.fromFile(filePath.toFile());
        return analyze(classFile, config);
    }
    
    private boolean shouldInclude(ClassFile classFile, AnalysisConfig config) {
        String className = classFile.getClassName();
        
        // Check include patterns
        if (!config.getIncludePatterns().isEmpty()) {
            boolean included = config.getIncludePatterns().stream()
                .anyMatch(pattern -> className.matches(pattern));
            if (!included) {
                return false;
            }
        }
        
        // Check exclude patterns
        return config.getExcludePatterns().stream()
            .noneMatch(pattern -> className.matches(pattern));
    }
} 