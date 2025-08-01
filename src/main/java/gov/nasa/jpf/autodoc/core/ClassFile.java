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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Represents a Java class file for analysis.
 * This class encapsulates the class file data and metadata.
 */
public class ClassFile {
    
    private final String className;
    private final byte[] data;
    private final Path sourcePath;
    private final String sourceType; // "file", "jar", "directory"
    
    public ClassFile(String className, byte[] data, Path sourcePath, String sourceType) {
        this.className = className;
        this.data = data;
        this.sourcePath = sourcePath;
        this.sourceType = sourceType;
    }
    
    public static ClassFile fromFile(File file) throws IOException {
        // Convert file path to class name
        String filePath = file.getAbsolutePath();
        String className = filePath
            .replace(".class", "")
            .replaceAll(".*classes/", "") // Remove path up to classes directory
            .replaceAll(".*build/", "") // Remove path up to build directory
            .replace("/", ".")
            .replace("\\", ".");
        
        byte[] data = java.nio.file.Files.readAllBytes(file.toPath());
        return new ClassFile(className, data, file.toPath(), "file");
    }
    
    public static ClassFile fromJar(String className, byte[] data, Path jarPath) {
        return new ClassFile(className, data, jarPath, "jar");
    }
    
    public static ClassFile fromDirectory(String className, byte[] data, Path dirPath) {
        return new ClassFile(className, data, dirPath, "directory");
    }
    
    // Getters
    public String getClassName() {
        return className;
    }
    
    public byte[] getData() {
        return data;
    }
    
    public Path getSourcePath() {
        return sourcePath;
    }
    
    public String getSourceType() {
        return sourceType;
    }
    
    @Override
    public String toString() {
        return "ClassFile{" +
                "className='" + className + '\'' +
                ", sourcePath=" + sourcePath +
                ", sourceType='" + sourceType + '\'' +
                '}';
    }
} 