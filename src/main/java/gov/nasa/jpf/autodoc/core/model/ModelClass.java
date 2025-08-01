package gov.nasa.jpf.autodoc.core.model;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a JPF model class that models a standard library class.
 */
public class ModelClass {
    
    private final String name;
    private final String stdName;
    private final Set<String> stdMethods;
    
    public ModelClass(String name, String stdName) {
        this.name = name;
        this.stdName = stdName;
        this.stdMethods = new LinkedHashSet<>();
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getStdName() {
        return stdName;
    }
    
    public Set<String> getStdMethods() {
        return new LinkedHashSet<>(stdMethods);
    }
    
    // Setters
    public void addStdMethod(String method) {
        stdMethods.add(method);
    }
    
    @Override
    public String toString() {
        return "ModelClass{" +
                "name='" + name + '\'' +
                ", stdName='" + stdName + '\'' +
                ", stdMethods=" + stdMethods +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelClass that = (ModelClass) o;
        return Objects.equals(name, that.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
} 