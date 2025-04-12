package Classes.Devices;

import Classes.Enums.DeviceType;
import Interfaces.MessageRecipient;

import java.util.ArrayList;

public class BSC extends VD implements MessageRecipient {
    private final ArrayList<byte[]> messageStorage;
    private volatile boolean isAccepting;

    public BSC(int layer) {
        super(DeviceType.BSC, layer, 5000);
        messageStorage = new ArrayList<>();
        isAccepting = true;
        start();
    }

    public void setAccepting(boolean accepting) {
        isAccepting = accepting;
    }

    public boolean isAccepting() {
        return isAccepting;
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

    public ArrayList<byte[]> getStoredMessages() {
        return messageStorage;
    }

    @Override
    public void receiveMessage(byte[] encodedMessage) {
        messageStorage.add(encodedMessage);
        if (getStoredMessageCount() == 5) {
            DeviceStorage.getNotifiedByBSC(this);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(getSleepTime() + (int) (Math.random() * 10));
                if (getStoredMessageCount() != 0) {
                    sendMessage(getOldestMessageInStorage(), DeviceStorage.getBestRecipientForSender(this));
                }
            } catch (InterruptedException e) {
                System.out.println("Classes.Devices.BSC " + getName() + " terminated");
                return;
            }
        }
    }
}
