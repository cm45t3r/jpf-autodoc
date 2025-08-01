package gov.nasa.jpf.autodoc.core.model;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a JPF native peer class.
 */
public class NativePeer {
    
    private final String name;
    private final String modelName;
    private final Set<String> modelMethods;
    
    public NativePeer(String name, String modelName) {
        this.name = name;
        this.modelName = modelName;
        this.modelMethods = new LinkedHashSet<>();
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getModelName() {
        return modelName;
    }
    
    public Set<String> getModelMethods() {
        return new LinkedHashSet<>(modelMethods);
    }
    
    // Setters
    public void addModelMethod(String method) {
        modelMethods.add(method);
    }
    
    @Override
    public String toString() {
        return "NativePeer{" +
                "name='" + name + '\'' +
                ", modelName='" + modelName + '\'' +
                ", modelMethods=" + modelMethods +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NativePeer that = (NativePeer) o;
        return Objects.equals(name, that.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
} 