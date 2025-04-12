package Classes.EventObjects;

import java.util.EventObject;

public class VBDFrequencyRequest extends EventObject {
    private final String number;
    private final int sleepTime;

    public VBDFrequencyRequest(Object source, String number, int sleepTime) {
        super(source);
        this.number = number;
        this.sleepTime = sleepTime;
    }

    public String getNumber() {
        return number;
    }

    public int getSleepTime() {
        return sleepTime;
    }
}
