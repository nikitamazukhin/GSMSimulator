package Classes.Devices;

import Classes.Enums.DeviceType;
import Classes.SavingSystem.FileWriting;
import Interfaces.MessageRecipient;

import java.io.FileOutputStream;
import java.io.IOException;

public class VBD extends VD {
    private final String message;
    private int sentMessageCount;
    private volatile boolean isActive;

    public VBD(int layer, String message) {
        super(DeviceType.VBD, layer, 5000);
        this.message = message;
        sentMessageCount = 0;
        isActive = true;
        start();
    }

    public void suspendVBD() {
        isActive = false;
    }

    public void resumeVBD() {
        synchronized (this) {
            isActive = true;
            notify();
        }
    }

    public boolean getActiveState() {
        return isActive;
    }

    public void sendMessage(byte[] message, MessageRecipient recipient) {
        if (recipient != null) {
            recipient.receiveMessage(message);
            sentMessageCount++;
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (this) {
                    while (!isActive)
                        wait();
                }
            } catch (InterruptedException e) {
                System.out.println("Classes.Devices.VBD " + getName() + " terminated");
                return;
            }
            try {
                sleep(getSleepTime());
                if (DeviceStorage.getVRDAmount() != 0) {
                    sendMessage(PDU.encodeMessage(message, getName(), DeviceStorage.getRandomVRDNumber()), DeviceStorage.getBestRecipientForSender(this));
                }
            } catch (InterruptedException e) {
                System.out.println("Classes.Devices.VBD " + getName() + " terminated");
                return;
            }
        }
    }

    public void saveInfoToFile(String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename, true)) {
            fos.write(getName().getBytes());
            fos.write('\n');

            fos.write(FileWriting.getLittleEndianBytes(sentMessageCount));
            fos.write('\n');

            fos.write(message.getBytes());
            fos.write('\n');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
