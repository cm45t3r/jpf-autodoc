package gov.nasa.jpf.autodoc.core.model;

import java.util.Objects;

/**
 * Represents a JPF choice generator extracted from bytecode analysis.
 */
public class ChoiceGenerator {
    
    private final String name;
    private final String className;
    private final String methodName;
    private final String type;
    
    public ChoiceGenerator(String name, String className, String methodName, String type) {
        this.name = name;
        this.className = className;
        this.methodName = methodName;
        this.type = type;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getClassName() {
        return className;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return "ChoiceGenerator{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChoiceGenerator that = (ChoiceGenerator) o;
        return Objects.equals(name, that.name) && 
               Objects.equals(className, that.className);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, className);
    }
} 