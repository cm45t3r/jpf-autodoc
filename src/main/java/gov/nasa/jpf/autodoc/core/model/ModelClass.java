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