package gov.nasa.jpf.autodoc.output;

/**
 * Exception thrown when output generation fails.
 */
public class OutputGenerationException extends Exception {
    
    public OutputGenerationException(String message) {
        super(message);
    }
    
    public OutputGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public OutputGenerationException(Throwable cause) {
        super(cause);
    }
} 