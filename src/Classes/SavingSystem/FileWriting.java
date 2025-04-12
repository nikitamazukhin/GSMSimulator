package Classes.SavingSystem;

public class FileWriting {
    public static byte[] getLittleEndianBytes(int n) {
        byte[] messageCountBytes = new byte[4];

        for (int i = 0; i < messageCountBytes.length; i++) {
            messageCountBytes[3 - i] = (byte) ((n >> (24 - 8 * i)) & 0xFF);
        }

        return messageCountBytes;
    }
}
