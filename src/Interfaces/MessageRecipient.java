package Interfaces;

public interface MessageRecipient {
    int getStoredMessageCount();

    void receiveMessage(byte[] encodedMessage);
}
