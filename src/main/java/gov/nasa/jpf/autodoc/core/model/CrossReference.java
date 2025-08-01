package gov.nasa.jpf.autodoc.core.model;

import java.util.Objects;

/**
 * Represents a cross-reference between different analysis results.
 */
public class CrossReference {
    
    private final String id;
    private final String sourceType;
    private final String targetType;
    private final String sourceName;
    private final String targetName;
    private final String relationship;
    
    public CrossReference(String id, String sourceType, String targetType, 
                        String sourceName, String targetName, String relationship) {
        this.id = id;
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.sourceName = sourceName;
        this.targetName = targetName;
        this.relationship = relationship;
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getSourceType() {
        return sourceType;
    }
    
    public String getTargetType() {
        return targetType;
    }
    
    public String getSourceName() {
        return sourceName;
    }
    
    public String getTargetName() {
        return targetName;
    }
    
    public String getRelationship() {
        return relationship;
    }
    
    @Override
    public String toString() {
        return "CrossReference{" +
                "id='" + id + '\'' +
                ", sourceType='" + sourceType + '\'' +
                ", targetType='" + targetType + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", targetName='" + targetName + '\'' +
                ", relationship='" + relationship + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrossReference that = (CrossReference) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
} 