package Classes.EventObjects;

import Classes.Enums.DeviceType;

import java.util.EventObject;

public class DeviceTerminateRequest extends EventObject {
    private final String number;
    private final DeviceType deviceType;

    public DeviceTerminateRequest(Object source, String number, DeviceType deviceType) {
        super(source);
        this.number = number;
        this.deviceType = deviceType;
    }

    public String getNumber() {
        return number;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }
}
