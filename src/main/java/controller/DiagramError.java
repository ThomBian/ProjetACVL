package controller;
/**
 * Created by Thomas on 28/10/15.
 */
public class DiagramError {
    private String message;

    public DiagramError(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
