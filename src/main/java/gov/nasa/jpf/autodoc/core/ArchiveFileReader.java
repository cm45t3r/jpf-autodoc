/*
 * Copyright (C) 2014, United States Government, as represented by the
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

package gov.nasa.jpf.autodoc.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.util.stream.Collectors;

/**
 * Utility class for reading class files from various archive formats.
 * Supports JAR, ZIP, and other compressed archive formats.
 */
public class ArchiveFileReader {
    
    /**
     * Reads all class files from a JAR file.
     * 
     * @param jarPath Path to the JAR file
     * @return List of ClassFile objects representing the class files in the JAR
     * @throws IOException if the JAR file cannot be read
     */
    public static List<ClassFile> readFromJar(Path jarPath) throws IOException {
        List<ClassFile> classFiles = new ArrayList<>();
        
        try (JarFile jarFile = new JarFile(jarPath.toFile())) {
            Enumeration<JarEntry> entries = jarFile.entries();
            
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                
                if (entryName.endsWith(".class") && !entry.isDirectory()) {
                    try (InputStream inputStream = jarFile.getInputStream(entry)) {
                        byte[] classData = readAllBytes(inputStream);
                        String className = entryNameToClassName(entryName);
                        
                        ClassFile classFile = ClassFile.fromJar(className, classData, jarPath);
                        classFiles.add(classFile);
                    }
                }
            }
        }
        
        return classFiles;
    }
    
    /**
     * Reads all class files from a ZIP file.
     * 
     * @param zipPath Path to the ZIP file
     * @return List of ClassFile objects representing the class files in the ZIP
     * @throws IOException if the ZIP file cannot be read
     */
    public static List<ClassFile> readFromZip(Path zipPath) throws IOException {
        List<ClassFile> classFiles = new ArrayList<>();
        
        try (ZipFile zipFile = new ZipFile(zipPath.toFile())) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();
                
                if (entryName.endsWith(".class") && !entry.isDirectory()) {
                    try (InputStream inputStream = zipFile.getInputStream(entry)) {
                        byte[] classData = readAllBytes(inputStream);
                        String className = entryNameToClassName(entryName);
                        
                        ClassFile classFile = ClassFile.fromJar(className, classData, zipPath);
                        classFiles.add(classFile);
                    }
                }
            }
        }
        
        return classFiles;
    }
    
    /**
     * Reads all class files from a TAR file (including compressed variants).
     * 
     * @param tarPath Path to the TAR file
     * @return List of ClassFile objects representing the class files in the TAR
     * @throws IOException if the TAR file cannot be read
     */
    public static List<ClassFile> readFromTar(Path tarPath) throws IOException {
        List<ClassFile> classFiles = new ArrayList<>();
        
        // For TAR files, we'll use a simple approach with external tools or
        // extract to temporary directory and then read class files
        // This is a simplified implementation - in production you might want to use
        // Apache Commons Compress or similar library for better TAR support
        
        String fileName = tarPath.getFileName().toString().toLowerCase();
        if (fileName.endsWith(".tar.gz") || fileName.endsWith(".tgz")) {
            // Handle gzipped TAR files
            return readFromCompressedTar(tarPath, "gzip");
        } else if (fileName.endsWith(".tar.bz2")) {
            // Handle bzip2 compressed TAR files
            return readFromCompressedTar(tarPath, "bzip2");
        } else if (fileName.endsWith(".tar")) {
            // Handle uncompressed TAR files
            return readFromCompressedTar(tarPath, "none");
        }
        
        throw new IOException("Unsupported TAR format: " + tarPath);
    }
    
    /**
     * Reads class files from any supported archive format.
     * Automatically detects the format based on file extension.
     * 
     * @param archivePath Path to the archive file
     * @return List of ClassFile objects representing the class files in the archive
     * @throws IOException if the archive file cannot be read
     */
    public static List<ClassFile> readFromArchive(Path archivePath) throws IOException {
        String fileName = archivePath.getFileName().toString().toLowerCase();
        
        if (fileName.endsWith(".jar")) {
            return readFromJar(archivePath);
        } else if (fileName.endsWith(".zip")) {
            return readFromZip(archivePath);
        } else if (fileName.endsWith(".tar") || fileName.endsWith(".tar.gz") || 
                   fileName.endsWith(".tgz") || fileName.endsWith(".tar.bz2")) {
            return readFromTar(archivePath);
        } else {
            throw new IOException("Unsupported archive format: " + archivePath);
        }
    }
    
    /**
     * Reads class files from multiple archive files.
     * 
     * @param archivePaths List of paths to archive files
     * @return List of ClassFile objects from all archives
     * @throws IOException if any archive file cannot be read
     */
    public static List<ClassFile> readFromArchives(List<Path> archivePaths) throws IOException {
        List<ClassFile> allClassFiles = new ArrayList<>();
        
        for (Path archivePath : archivePaths) {
            List<ClassFile> classFiles = readFromArchive(archivePath);
            allClassFiles.addAll(classFiles);
        }
        
        return allClassFiles;
    }
    
    /**
     * Reads class files from a directory, including any archive files found within.
     * 
     * @param dirPath Path to the directory
     * @return List of ClassFile objects from the directory and any archives within
     * @throws IOException if any file cannot be read
     */
    public static List<ClassFile> readFromDirectory(Path dirPath) throws IOException {
        List<ClassFile> allClassFiles = new ArrayList<>();
        
        // First, read class files directly from the directory
        try (Stream<Path> paths = Files.walk(dirPath)) {
            paths.filter(path -> path.toString().endsWith(".class"))
                 .forEach(path -> {
                     try {
                         ClassFile classFile = ClassFile.fromFile(path.toFile());
                         allClassFiles.add(classFile);
                     } catch (IOException e) {
                         System.err.println("Warning: Could not read class file: " + path);
                     }
                 });
        }
        
        // Then, look for archive files and read class files from them
        try (Stream<Path> paths = Files.walk(dirPath)) {
            List<Path> archiveFiles = paths
                .filter(path -> isArchiveFile(path))
                .collect(Collectors.toList());
            
            for (Path archivePath : archiveFiles) {
                try {
                    List<ClassFile> archiveClassFiles = readFromArchive(archivePath);
                    allClassFiles.addAll(archiveClassFiles);
                } catch (IOException e) {
                    System.err.println("Warning: Could not read archive: " + archivePath);
                }
            }
        }
        
        return allClassFiles;
    }
    
    /**
     * Checks if a file is a supported archive format.
     * 
     * @param filePath Path to the file
     * @return true if the file is a supported archive format
     */
    public static boolean isArchiveFile(Path filePath) {
        String fileName = filePath.getFileName().toString().toLowerCase();
        return fileName.endsWith(".jar") || 
               fileName.endsWith(".zip") || 
               fileName.endsWith(".tar") || 
               fileName.endsWith(".tar.gz") || 
               fileName.endsWith(".tgz") || 
               fileName.endsWith(".tar.bz2");
    }
    
    /**
     * Converts a JAR/ZIP entry name to a Java class name.
     * 
     * @param entryName The entry name from the archive
     * @return The corresponding Java class name
     */
    private static String entryNameToClassName(String entryName) {
        return entryName
            .replace(".class", "")
            .replace("/", ".")
            .replace("\\", ".");
    }
    
    /**
     * Reads all bytes from an InputStream.
     * 
     * @param inputStream The input stream to read from
     * @return Byte array containing all the data
     * @throws IOException if the stream cannot be read
     */
    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        
        return buffer.toByteArray();
    }
    
    /**
     * Reads from a compressed TAR file.
     * This is a simplified implementation - in production you might want to use
     * Apache Commons Compress or similar library for better TAR support.
     * 
     * @param tarPath Path to the TAR file
     * @param compressionType Type of compression ("gzip", "bzip2", "none")
     * @return List of ClassFile objects from the TAR
     * @throws IOException if the TAR file cannot be read
     */
    private static List<ClassFile> readFromCompressedTar(Path tarPath, String compressionType) throws IOException {
        // This is a placeholder implementation
        // In a real implementation, you would use Apache Commons Compress or similar
        // to properly handle TAR files with various compression formats
        
        List<ClassFile> classFiles = new ArrayList<>();
        
        // For now, we'll throw an exception indicating that TAR support
        // requires additional dependencies
        throw new IOException("TAR file support requires Apache Commons Compress library. " +
                            "Please add the dependency to your build file and implement " +
                            "proper TAR reading functionality.");
        
        // Example implementation with Apache Commons Compress:
        /*
        try (InputStream inputStream = Files.newInputStream(tarPath);
             InputStream decompressedStream = getDecompressedStream(inputStream, compressionType);
             TarArchiveInputStream tarStream = new TarArchiveInputStream(decompressedStream)) {
            
            TarArchiveEntry entry;
            while ((entry = tarStream.getNextTarEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    byte[] classData = readAllBytes(tarStream);
                    String className = entryNameToClassName(entry.getName());
                    ClassFile classFile = ClassFile.fromJar(className, classData, tarPath);
                    classFiles.add(classFile);
                }
            }
        }
        */
    }
} 