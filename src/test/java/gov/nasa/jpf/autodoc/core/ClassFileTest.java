package gov.nasa.jpf.autodoc.core;

import gov.nasa.jpf.autodoc.core.model.AnalysisConfig;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for core classes to verify the setup is working.
 */
public class ClassFileTest {
    
    @Test
    void testClassFileSetCreation() {
        ClassFileSet fileSet = new ClassFileSet();
        assertThat(fileSet).isNotNull();
        assertThat(fileSet.isEmpty()).isTrue();
        assertThat(fileSet.size()).isEqualTo(0);
    }
    
    @Test
    void testAnalysisConfigBuilder() {
        AnalysisConfig config = AnalysisConfig.builder()
                .analyzeConfigurations(true)
                .analyzeTypes(false)
                .validateResults(true)
                .verbose(true)
                .build();
        
        assertThat(config).isNotNull();
        assertThat(config.isAnalyzeConfigurations()).isTrue();
        assertThat(config.isAnalyzeTypes()).isFalse();
        assertThat(config.isValidateResults()).isTrue();
        assertThat(config.isVerbose()).isTrue();
    }
    
    @Test
    void testAnalysisConfigDefaults() {
        AnalysisConfig config = AnalysisConfig.defaultConfig();
        
        assertThat(config).isNotNull();
        assertThat(config.isAnalyzeConfigurations()).isTrue();
        assertThat(config.isAnalyzeTypes()).isTrue();
        assertThat(config.isValidateResults()).isFalse();
        assertThat(config.isParallelProcessing()).isTrue();
    }
    
    @Test
    void testAnalysisConfigConfigOnly() {
        AnalysisConfig config = AnalysisConfig.configOnly();
        
        assertThat(config).isNotNull();
        assertThat(config.isAnalyzeConfigurations()).isTrue();
        assertThat(config.isAnalyzeTypes()).isFalse();
    }
    
    @Test
    void testAnalysisConfigTypesOnly() {
        AnalysisConfig config = AnalysisConfig.typesOnly();
        
        assertThat(config).isNotNull();
        assertThat(config.isAnalyzeConfigurations()).isFalse();
        assertThat(config.isAnalyzeTypes()).isTrue();
    }
} 