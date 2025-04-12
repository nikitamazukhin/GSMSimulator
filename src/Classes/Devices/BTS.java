package Classes.Devices;

import Classes.Enums.DeviceType;
import Interfaces.MessageRecipient;

import java.util.ArrayList;

public class BTS extends VD implements MessageRecipient {
    private final ArrayList<byte[]> messageStorage;

    public BTS(int layer) {
        super(DeviceType.BTS, layer, 3000);
        messageStorage = new ArrayList<>();
        start();
    }

    public void sendMessage(byte[] message, MessageRecipient recipient) {
        if (recipient != null) {
            recipient.receiveMessage(message);
            messageStorage.remove(message);
        }
    }

    public byte[] getOldestMessageInStorage() {
        return messageStorage.get(0);
    }

    @Override
    public int getStoredMessageCount() {
        return messageStorage.size();
    }

    @Override
    public void receiveMessage(byte[] encodedMessage) {
        messageStorage.add(encodedMessage);
        if (getStoredMessageCount() == 5) {
            DeviceStorage.getNotifiedByBTS(this);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(getSleepTime());
                if (getStoredMessageCount() != 0) {
                    if (getLayer() == 1) {
                        BSC temp = (BSC) DeviceStorage.getBestRecipientForSender(this);
                        if (temp.isAccepting())
                            sendMessage(getOldestMessageInStorage(), temp);
                    } else {
                        byte[] message = getOldestMessageInStorage();
                        sendMessage(message, DeviceStorage.getVRD(PDU.decodeRecipientNumberFromMessage(message)));
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Classes.Devices.BTS " + getName() + " terminated");
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
