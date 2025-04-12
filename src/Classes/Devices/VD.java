package Classes.Devices;

import Classes.Enums.DeviceType;

abstract public class VD extends Thread {
    private final DeviceType deviceType;
    private int layer;
    private int sleepTime;

    public VD(DeviceType deviceType, int layer, int sleepTime) {
        super("" + DeviceStorage.getUniqueNumber());
        this.deviceType = deviceType;
        this.layer = layer;
        this.sleepTime = sleepTime;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }
}
