package gov.nasa.jpf.autodoc.core.model;

import java.util.Objects;

/**
 * Represents a JPF listener class.
 */
public class Listener {
    
    private final String name;
    private final String type;
    
    public Listener(String name, String type) {
        this.name = name;
        this.type = type;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return "Listener{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Listener listener = (Listener) o;
        return Objects.equals(name, listener.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
} 