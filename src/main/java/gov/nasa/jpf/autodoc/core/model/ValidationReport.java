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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents validation results and issues found during analysis.
 */
public class ValidationReport {
    
    private final List<ValidationIssue> issues;
    private final boolean isValid;
    private final String summary;
    
    public ValidationReport() {
        this.issues = new ArrayList<>();
        this.isValid = true;
        this.summary = "";
    }
    
    public ValidationReport(List<ValidationIssue> issues, boolean isValid, String summary) {
        this.issues = new ArrayList<>(issues);
        this.isValid = isValid;
        this.summary = summary;
    }
    
    // Getters
    public List<ValidationIssue> getIssues() {
        return new ArrayList<>(issues);
    }
    
    public boolean isValid() {
        return isValid;
    }
    
    public String getSummary() {
        return summary;
    }
    
    // Methods
    public void addIssue(ValidationIssue issue) {
        issues.add(issue);
    }
    
    public int getIssueCount() {
        return issues.size();
    }
    
    public List<ValidationIssue> getIssuesBySeverity(ValidationIssue.Severity severity) {
        List<ValidationIssue> filtered = new ArrayList<>();
        for (ValidationIssue issue : issues) {
            if (issue.getSeverity() == severity) {
                filtered.add(issue);
            }
        }
        return filtered;
    }
    
    @Override
    public String toString() {
        return "ValidationReport{" +
                "issues=" + issues.size() +
                ", isValid=" + isValid +
                ", summary='" + summary + '\'' +
                '}';
    }
    
    /**
     * Represents a validation issue.
     */
    public static class ValidationIssue {
        
        public enum Severity {
            INFO, WARNING, ERROR, CRITICAL
        }
        
        private final String message;
        private final Severity severity;
        private final String source;
        private final String details;
        
        public ValidationIssue(String message, Severity severity, String source, String details) {
            this.message = message;
            this.severity = severity;
            this.source = source;
            this.details = details;
        }
        
        // Getters
        public String getMessage() {
            return message;
        }
        
        public Severity getSeverity() {
            return severity;
        }
        
        public String getSource() {
            return source;
        }
        
        public String getDetails() {
            return details;
        }
        
        @Override
        public String toString() {
            return "ValidationIssue{" +
                    "message='" + message + '\'' +
                    ", severity=" + severity +
                    ", source='" + source + '\'' +
                    '}';
        }
    }
} 