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