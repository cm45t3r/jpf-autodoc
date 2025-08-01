package gov.nasa.jpf.autodoc.core.model;

import java.util.Objects;

/**
 * Represents a JPF logger configuration extracted from bytecode analysis.
 */
public class LoggerConfig {
    
    private final String name;
    private final String className;
    private final String type;
    
    public LoggerConfig(String name, String className, String type) {
        this.name = name;
        this.className = className;
        this.type = type;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getClassName() {
        return className;
    }
    
    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return "LoggerConfig{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoggerConfig that = (LoggerConfig) o;
        return Objects.equals(name, that.name) && 
               Objects.equals(className, that.className);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, className);
    }
} 