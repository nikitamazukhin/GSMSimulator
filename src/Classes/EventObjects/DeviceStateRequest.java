package Classes.EventObjects;

import Classes.Enums.DeviceType;

import java.util.EventObject;

public class DeviceStateRequest extends EventObject {
    private final String number;
    private final boolean state;
    private final DeviceType deviceType;

    public DeviceStateRequest(Object source, String number, boolean state, DeviceType deviceType) {
        super(source);
        this.number = number;
        this.state = state;
        this.deviceType = deviceType;
    }

    public String getNumber() {
        return number;
    }

    public boolean getState() {
        return state;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }
}
