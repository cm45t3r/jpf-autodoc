/*
 * Copyright (C) 2025, United States Government, as represented by the
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

import java.util.*;

/**
 * Represents type hierarchy information extracted from class analysis.
 */
public class TypeInfo {
    
    private final String name;
    private final String superName;
    private final Set<String> interfaces;
    private final Set<String> methods;
    private final String type; // Listener, InstructionFactory, etc.
    private final List<String> ancestors;
    private final Set<String> overriddenMethods;
    private final String location;
    private final String project;
    private final int flags;
    
    public TypeInfo(String name, String superName, String type) {
        this.name = name;
        this.superName = superName;
        this.type = type;
        this.interfaces = new LinkedHashSet<>();
        this.methods = new LinkedHashSet<>();
        this.ancestors = new ArrayList<>();
        this.overriddenMethods = new LinkedHashSet<>();
        this.location = "";
        this.project = "";
        this.flags = 0;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getSuperName() {
        return superName;
    }
    
    public Set<String> getInterfaces() {
        return new LinkedHashSet<>(interfaces);
    }
    
    public Set<String> getMethods() {
        return new LinkedHashSet<>(methods);
    }
    
    public String getType() {
        return type;
    }
    
    public List<String> getAncestors() {
        return new ArrayList<>(ancestors);
    }
    
    public Set<String> getOverriddenMethods() {
        return new LinkedHashSet<>(overriddenMethods);
    }
    
    public String getLocation() {
        return location;
    }
    
    public String getProject() {
        return project;
    }
    
    public int getFlags() {
        return flags;
    }
    
    // Setters
    public void addInterface(String interfaceName) {
        interfaces.add(interfaceName);
    }
    
    public void addMethod(String method) {
        methods.add(method);
    }
    
    public void addAncestor(String ancestor) {
        ancestors.add(ancestor);
    }
    
    public void addOverriddenMethod(String method) {
        overriddenMethods.add(method);
    }
    
    public void setLocation(String location) {
        // Note: This would require making location non-final
    }
    
    public void setProject(String project) {
        // Note: This would require making project non-final
    }
    
    public void setFlags(int flags) {
        // Note: This would require making flags non-final
    }
    
    @Override
    public String toString() {
        return "TypeInfo{" +
                "name='" + name + '\'' +
                ", superName='" + superName + '\'' +
                ", type='" + type + '\'' +
                ", interfaces=" + interfaces +
                ", methods=" + methods.size() +
                ", ancestors=" + ancestors +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeInfo typeInfo = (TypeInfo) o;
        return Objects.equals(name, typeInfo.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
} 