package gov.nasa.jpf.autodoc.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ArchiveFileReaderTest {
    
    @TempDir
    Path tempDir;
    
    private Path testJarPath;
    private Path testZipPath;
    
    @BeforeEach
    void setUp() throws IOException {
        // Create test JAR file
        testJarPath = tempDir.resolve("test.jar");
        createTestJar(testJarPath);
        
        // Create test ZIP file
        testZipPath = tempDir.resolve("test.zip");
        createTestZip(testZipPath);
    }
    
    @Test
    void testReadFromJar() throws IOException {
        // When
        List<ClassFile> classFiles = ArchiveFileReader.readFromJar(testJarPath);
        
        // Then
        assertThat(classFiles).isNotEmpty();
        assertThat(classFiles).hasSize(2); // Two test classes
        
        // Verify class names
        List<String> classNames = classFiles.stream()
            .map(ClassFile::getClassName)
            .collect(java.util.stream.Collectors.toList());
        
        assertThat(classNames).contains("gov.nasa.jpf.TestClass");
        assertThat(classNames).contains("gov.nasa.jpf.AnotherClass");
        
        // Verify source type
        classFiles.forEach(classFile -> {
            assertThat(classFile.getSourceType()).isEqualTo("jar");
            assertThat(classFile.getSourcePath()).isEqualTo(testJarPath);
        });
    }
    
    @Test
    void testReadFromZip() throws IOException {
        // When
        List<ClassFile> classFiles = ArchiveFileReader.readFromZip(testZipPath);
        
        // Then
        assertThat(classFiles).isNotEmpty();
        assertThat(classFiles).hasSize(2); // Two test classes
        
        // Verify class names
        List<String> classNames = classFiles.stream()
            .map(ClassFile::getClassName)
            .collect(java.util.stream.Collectors.toList());
        
        assertThat(classNames).contains("gov.nasa.jpf.TestClass");
        assertThat(classNames).contains("gov.nasa.jpf.AnotherClass");
        
        // Verify source type
        classFiles.forEach(classFile -> {
            assertThat(classFile.getSourceType()).isEqualTo("jar"); // Uses fromJar method
            assertThat(classFile.getSourcePath()).isEqualTo(testZipPath);
        });
    }
    
    @Test
    void testReadFromArchive() throws IOException {
        // Test JAR
        List<ClassFile> jarClassFiles = ArchiveFileReader.readFromArchive(testJarPath);
        assertThat(jarClassFiles).isNotEmpty();
        assertThat(jarClassFiles).hasSize(2);
        
        // Test ZIP
        List<ClassFile> zipClassFiles = ArchiveFileReader.readFromArchive(testZipPath);
        assertThat(zipClassFiles).isNotEmpty();
        assertThat(zipClassFiles).hasSize(2);
    }
    
    @Test
    void testReadFromArchives() throws IOException {
        // When
        List<ClassFile> classFiles = ArchiveFileReader.readFromArchives(
            List.of(testJarPath, testZipPath)
        );
        
        // Then
        assertThat(classFiles).isNotEmpty();
        assertThat(classFiles).hasSize(4); // 2 from JAR + 2 from ZIP
    }
    
    @Test
    void testReadFromDirectory() throws IOException {
        // Create a directory with class files and archives
        Path testDir = tempDir.resolve("test-dir");
        Files.createDirectories(testDir);
        
        // Copy archives to directory
        Files.copy(testJarPath, testDir.resolve("test.jar"));
        Files.copy(testZipPath, testDir.resolve("test.zip"));
        
        // Create a class file directly in directory
        Path classFile = testDir.resolve("TestClass.class");
        Files.write(classFile, createTestClassBytes());
        
        // When
        List<ClassFile> classFiles = ArchiveFileReader.readFromDirectory(testDir);
        
        // Then
        assertThat(classFiles).isNotEmpty();
        assertThat(classFiles).hasSize(5); // 2 from JAR + 2 from ZIP + 1 direct class file
    }
    
    @Test
    void testIsArchiveFile() {
        // Test supported formats
        assertThat(ArchiveFileReader.isArchiveFile(tempDir.resolve("test.jar"))).isTrue();
        assertThat(ArchiveFileReader.isArchiveFile(tempDir.resolve("test.zip"))).isTrue();
        assertThat(ArchiveFileReader.isArchiveFile(tempDir.resolve("test.tar"))).isTrue();
        assertThat(ArchiveFileReader.isArchiveFile(tempDir.resolve("test.tar.gz"))).isTrue();
        assertThat(ArchiveFileReader.isArchiveFile(tempDir.resolve("test.tgz"))).isTrue();
        assertThat(ArchiveFileReader.isArchiveFile(tempDir.resolve("test.tar.bz2"))).isTrue();
        
        // Test unsupported formats
        assertThat(ArchiveFileReader.isArchiveFile(tempDir.resolve("test.txt"))).isFalse();
        assertThat(ArchiveFileReader.isArchiveFile(tempDir.resolve("test.class"))).isFalse();
    }
    
    @Test
    void testReadFromTarThrowsException() {
        // TAR support is not fully implemented yet
        Path tarPath = tempDir.resolve("test.tar");
        
        assertThatThrownBy(() -> ArchiveFileReader.readFromTar(tarPath))
            .isInstanceOf(IOException.class)
            .hasMessageContaining("TAR file support requires Apache Commons Compress");
    }
    
    @Test
    void testReadFromUnsupportedArchive() {
        Path unsupportedPath = tempDir.resolve("test.xyz");
        
        assertThatThrownBy(() -> ArchiveFileReader.readFromArchive(unsupportedPath))
            .isInstanceOf(IOException.class)
            .hasMessageContaining("Unsupported archive format");
    }
    
    @Test
    void testReadFromNonExistentArchive() {
        Path nonExistentPath = tempDir.resolve("nonexistent.jar");
        
        assertThatThrownBy(() -> ArchiveFileReader.readFromJar(nonExistentPath))
            .isInstanceOf(IOException.class);
    }
    
    private void createTestJar(Path jarPath) throws IOException {
        try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(jarPath))) {
            // Add first class
            JarEntry entry1 = new JarEntry("gov/nasa/jpf/TestClass.class");
            jos.putNextEntry(entry1);
            jos.write(createTestClassBytes());
            jos.closeEntry();
            
            // Add second class
            JarEntry entry2 = new JarEntry("gov/nasa/jpf/AnotherClass.class");
            jos.putNextEntry(entry2);
            jos.write(createTestClassBytes());
            jos.closeEntry();
            
            // Add a non-class file (should be ignored)
            JarEntry textEntry = new JarEntry("readme.txt");
            jos.putNextEntry(textEntry);
            jos.write("This is a test".getBytes());
            jos.closeEntry();
        }
    }
    
    private void createTestZip(Path zipPath) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            // Add first class
            ZipEntry entry1 = new ZipEntry("gov/nasa/jpf/TestClass.class");
            zos.putNextEntry(entry1);
            zos.write(createTestClassBytes());
            zos.closeEntry();
            
            // Add second class
            ZipEntry entry2 = new ZipEntry("gov/nasa/jpf/AnotherClass.class");
            zos.putNextEntry(entry2);
            zos.write(createTestClassBytes());
            zos.closeEntry();
            
            // Add a non-class file (should be ignored)
            ZipEntry textEntry = new ZipEntry("readme.txt");
            zos.putNextEntry(textEntry);
            zos.write("This is a test".getBytes());
            zos.closeEntry();
        }
    }
    
    private byte[] createTestClassBytes() {
        // Create a minimal valid Java class file
        // This is a simplified version - in a real test you might want to use
        // a proper class file generator or include actual test class files
        
        return new byte[] {
            (byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE, // Magic number
            0x00, 0x00, 0x00, 0x34, // Version
            0x00, 0x21, // Constant pool count
            // ... rest of minimal class file structure
        };
    }
} 