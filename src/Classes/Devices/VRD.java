package Classes.Devices;

import Classes.Enums.DeviceType;
import Interfaces.MessageRecipient;

public class VRD extends VD implements MessageRecipient {
    private int receiveCount;
    private volatile boolean isResetting;

    public VRD(int layer) {
        super(DeviceType.VRD, layer, 10000);
        receiveCount = 0;
        isResetting = false;
        start();
    }

    @Override
    public int getStoredMessageCount() {
        return receiveCount;
    }

    public void suspendResetting() {
        isResetting = false;
    }

    public void resumeResetting() {
        synchronized (this) {
            isResetting = true;
            notify();
        }
    }

    public boolean getResettingState() {
        return isResetting;
    }

    @Override
    public void receiveMessage(byte[] encodedMessage) {
        receiveCount++;
        DeviceStorage.getNotifiedByVRD(this);
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (this) {
                    while (isResetting) {
                        wait(getSleepTime());
                        receiveCount = 0;
                        DeviceStorage.getNotifiedByVRD(this);
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Classes.Devices.VRD " + getName() + " terminated");
                return;
            }
        }
    }
}
