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

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a collection of class files for analysis.
 * This class provides efficient access and iteration over class files.
 */
public class ClassFileSet implements Iterable<ClassFile> {
    
    private final List<ClassFile> files;
    private final Map<String, ClassFile> classNameIndex;
    
    public ClassFileSet() {
        this.files = new ArrayList<>();
        this.classNameIndex = new HashMap<>();
    }
    
    public ClassFileSet(Collection<ClassFile> files) {
        this();
        addAll(files);
    }
    
    public void add(ClassFile classFile) {
        files.add(classFile);
        classNameIndex.put(classFile.getClassName(), classFile);
    }
    
    public void addAll(Collection<ClassFile> classFiles) {
        for (ClassFile classFile : classFiles) {
            add(classFile);
        }
    }
    
    public ClassFile getByClassName(String className) {
        return classNameIndex.get(className);
    }
    
    public boolean contains(String className) {
        return classNameIndex.containsKey(className);
    }
    
    public List<ClassFile> getFiles() {
        return new ArrayList<>(files);
    }
    
    public Set<String> getClassNames() {
        return new HashSet<>(classNameIndex.keySet());
    }
    
    public int size() {
        return files.size();
    }
    
    public boolean isEmpty() {
        return files.isEmpty();
    }
    
    public List<ClassFile> filterBySourceType(String sourceType) {
        return files.stream()
                .filter(file -> sourceType.equals(file.getSourceType()))
                .collect(Collectors.toList());
    }
    
    public List<ClassFile> filterByClassNamePattern(String pattern) {
        return files.stream()
                .filter(file -> file.getClassName().matches(pattern))
                .collect(Collectors.toList());
    }
    
    @Override
    public Iterator<ClassFile> iterator() {
        return files.iterator();
    }
    
    public java.util.stream.Stream<ClassFile> stream() {
        return files.stream();
    }
} 