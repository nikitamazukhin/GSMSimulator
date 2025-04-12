package Classes.EventObjects;

import java.util.EventObject;

public class VBDCreateRequest extends EventObject {
    private final String message;

    public VBDCreateRequest(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
