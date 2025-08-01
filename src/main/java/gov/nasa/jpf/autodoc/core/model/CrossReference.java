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