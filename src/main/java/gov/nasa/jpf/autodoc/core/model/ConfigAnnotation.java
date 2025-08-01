package gov.nasa.jpf.autodoc.core.model;

import java.util.Objects;

/**
 * Represents a JPF configuration annotation extracted from bytecode analysis.
 */
public class ConfigAnnotation {
    
    private final String name;
    private final String className;
    private final String type;
    private final String value;
    private final String comment;
    private final String annotationType;
    
    public ConfigAnnotation(String name, String className, String type, String value, String comment, String annotationType) {
        this.name = name;
        this.className = className;
        this.type = type;
        this.value = value;
        this.comment = comment;
        this.annotationType = annotationType;
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
    
    public String getValue() {
        return value;
    }
    
    public String getComment() {
        return comment;
    }
    
    public String getAnnotationType() {
        return annotationType;
    }
    
    @Override
    public String toString() {
        return "ConfigAnnotation{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", annotationType='" + annotationType + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigAnnotation that = (ConfigAnnotation) o;
        return Objects.equals(name, that.name) && 
               Objects.equals(className, that.className);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, className);
    }
} 