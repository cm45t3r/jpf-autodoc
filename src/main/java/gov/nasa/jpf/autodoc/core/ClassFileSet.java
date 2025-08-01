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