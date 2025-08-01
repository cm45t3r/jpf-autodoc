package gov.nasa.jpf.autodoc.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a JPF configuration option extracted from bytecode analysis.
 */
public class ConfigOption {
    
    private final String name;
    private final String className;
    private final String type;
    private final List<Value> values;
    private final String comment;
    private final ConfigAnnotation annotation;
    private final boolean hasImplementation;
    private final boolean hasAnnotation;
    private final String sourceMethod;
    
    public ConfigOption(String name, String className, String type, String sourceMethod) {
        this.name = name;
        this.className = className;
        this.type = type;
        this.values = new ArrayList<>();
        this.comment = "";
        this.annotation = null;
        this.hasImplementation = true;
        this.hasAnnotation = false;
        this.sourceMethod = sourceMethod;
    }
    
    public ConfigOption(String name, String className, String type, String sourceMethod, 
                       List<Value> values, String comment) {
        this.name = name;
        this.className = className;
        this.type = type;
        this.values = new ArrayList<>(values);
        this.comment = comment;
        this.annotation = null;
        this.hasImplementation = true;
        this.hasAnnotation = false;
        this.sourceMethod = sourceMethod;
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
    
    public List<Value> getValues() {
        return new ArrayList<>(values);
    }
    
    public String getComment() {
        return comment;
    }
    
    public ConfigAnnotation getAnnotation() {
        return annotation;
    }
    
    public boolean hasImplementation() {
        return hasImplementation;
    }
    
    public boolean hasAnnotation() {
        return hasAnnotation;
    }
    
    public String getSourceMethod() {
        return sourceMethod;
    }
    
    // Setters
    public void setAnnotation(ConfigAnnotation annotation) {
        // Note: This would require making annotation non-final, but for now we'll use a builder pattern
    }
    
    public void addValue(Value value) {
        values.add(value);
    }
    
    public void addValues(List<Value> newValues) {
        values.addAll(newValues);
    }
    
    public void setComment(String comment) {
        // Note: This would require making comment non-final
    }
    
    @Override
    public String toString() {
        return "ConfigOption{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", type='" + type + '\'' +
                ", values=" + values +
                ", sourceMethod='" + sourceMethod + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigOption that = (ConfigOption) o;
        return Objects.equals(name, that.name) && 
               Objects.equals(className, that.className);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, className);
    }
    
    /**
     * Represents a configuration value.
     */
    public static class Value {
        private final String value;
        private final String type;
        private final boolean isDefault;
        
        public Value(String value, String type, boolean isDefault) {
            this.value = value;
            this.type = type;
            this.isDefault = isDefault;
        }
        
        public String getValue() {
            return value;
        }
        
        public String getType() {
            return type;
        }
        
        public boolean isDefault() {
            return isDefault;
        }
        
        @Override
        public String toString() {
            return "Value{" +
                    "value='" + value + '\'' +
                    ", type='" + type + '\'' +
                    ", isDefault=" + isDefault +
                    '}';
        }
    }
} 