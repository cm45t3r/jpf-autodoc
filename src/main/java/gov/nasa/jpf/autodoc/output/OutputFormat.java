package gov.nasa.jpf.autodoc.output;

/**
 * Supported output formats for documentation generation.
 */
public enum OutputFormat {
    
    MARKDOWN("md", "Markdown format"),
    XML("xml", "XML format"),
    JSON("json", "JSON format"),
    TEXT("txt", "Plain text format"),
    HTML("html", "HTML format");
    
    private final String fileExtension;
    private final String description;
    
    OutputFormat(String fileExtension, String description) {
        this.fileExtension = fileExtension;
        this.description = description;
    }
    
    public String getFileExtension() {
        return fileExtension;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return name().toLowerCase();
    }
} 