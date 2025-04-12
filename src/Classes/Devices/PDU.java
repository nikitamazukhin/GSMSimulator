package Classes.Devices;

public class PDU {

    /*
    Packs a string into semi octets
    */
    public static byte[] packStringIntoSemiOctets(String string) {
        byte[] bytesToPack = new byte[string.length()];
        byte[] packedBytes = new byte[(int) Math.ceil((double) bytesToPack.length / 2)];
        int ind = 0;

        for (int i = 0; i < bytesToPack.length; i++) {
            bytesToPack[i] = (byte) Character.getNumericValue(string.charAt(i));
        }

        for (int i = 0; i < bytesToPack.length; i += 2) {
            if (i == bytesToPack.length - 1) {
                packedBytes[ind] = (byte) (bytesToPack[bytesToPack.length - 1] | 0xF0);
            } else
                packedBytes[ind] = (byte) (bytesToPack[i] | (bytesToPack[i + 1] << 4));
            ind++;
        }

        return packedBytes;
    }

    /*
    Packs the address value into semi-octets,
    adds an octet describing the total amount of octets in the type of address and address value fields,
    adds an octet specifying the type of address
    */
    public static byte[] packSMSCAddress(String address) {
        byte[] packedAddress = packStringIntoSemiOctets(address);
        byte[] result = new byte[2 + packedAddress.length];

        result[0] = (byte) (result.length - 1);
        result[1] = (byte) 0x91;
        System.arraycopy(packedAddress, 0, result, 2, packedAddress.length);

        return result;
    }

    public static byte packFirstOctet() {
        return 0x01;
    }

    public static byte packMessageReference() {
        return 0x00;
    }


    /*
    Packs the address value into semi-octets,
    adds an octet describing the amount of useful semi-octets in the address value,
    adds an octet specifying the type of address
    */
    public static byte[] packDestinationAddress(String address) {
        byte[] packedAddress = packStringIntoSemiOctets(address);
        byte[] result = new byte[2 + packedAddress.length];

        if ((packedAddress[packedAddress.length - 1] & 0xF0) == 0xF0) {
            result[0] = (byte) (packedAddress.length * 2 - 1);
        } else
            result[0] = (byte) (packedAddress.length * 2);

        result[1] = (byte) 0x91;
        System.arraycopy(packedAddress, 0, result, 2, packedAddress.length);

        return result;
    }

    public static byte packProtocolIdentifier() {
        return 0x00;
    }

    public static byte packDataCodingScheme() {
        return 0x00;
    }

    //Packs a String message into a byte array according to standard Classes.Devices.PDU alphabet.
    public static byte[] packUserData(String userData) {
        byte[] toPackBytes = new byte[userData.length()];
        byte[] packedBytes = new byte[(userData.length() - userData.length() / 8) + 1];
        int firstBitsToShift = 0;
        int secondBitsToShift = 7;
        int ind = 1;

        packedBytes[0] = (byte) userData.length();

        //Simply turns chars that need to be packed into bytes
        for (int i = 0; i < userData.length(); i++) {
            toPackBytes[i] = (byte) userData.charAt(i);
        }

        //Packs septets into octets
        for (int i = 0; i < userData.length(); i++) {
            if (firstBitsToShift == 7) {
                firstBitsToShift = 0;
                secondBitsToShift = 7;
            } else {
                if (i == userData.length() - 1) {
                    packedBytes[ind] = (byte) (toPackBytes[i] >> firstBitsToShift);
                } else {
                    packedBytes[ind] = (byte) (toPackBytes[i] >> firstBitsToShift | (toPackBytes[i + 1] << secondBitsToShift));
                }

                firstBitsToShift++;
                secondBitsToShift--;
                ind++;
            }
        }
        return packedBytes;
    }

    /*
    Encodes the message according to the Classes.Devices.PDU format
     */
    public static byte[] encodeMessage(String message, String from, String to) {
        byte[] SMSCAddress = packSMSCAddress(from);
        byte firstOctet = packFirstOctet();
        byte messageReference = packMessageReference();
        byte[] destinationAddress = packDestinationAddress(to);
        byte protocolIdentifier = packProtocolIdentifier();
        byte dataCodingScheme = packDataCodingScheme();
        byte[] userData = packUserData(message);

        byte[] result = new byte[SMSCAddress.length + 2 + destinationAddress.length + 2 + userData.length];
        int ind = 0;

        for (int i = 0; i < SMSCAddress.length; i++, ind++) {
            result[ind] = SMSCAddress[i];
        }

        result[ind] = firstOctet;
        ind++;

        result[ind] = messageReference;
        ind++;

        for (int i = 0; i < destinationAddress.length; i++, ind++) {
            result[ind] = destinationAddress[i];
        }

        result[ind] = protocolIdentifier;
        ind++;

        result[ind] = dataCodingScheme;
        ind++;

        for (int i = 0; i < userData.length; i++, ind++) {
            result[ind] = userData[i];
        }

        return result;
    }

    /*
    Extracts the number of the recipient encoded in a message
     */
    public static String decodeRecipientNumberFromMessage(byte[] encodedMessage) {
        StringBuilder recipientAddress = new StringBuilder();
        int bytesInSMSCAddress = encodedMessage[0];
        int recipientDataStart = bytesInSMSCAddress + 3;
        int recipientAddressStart = bytesInSMSCAddress + 5;
        int usefulOctetCount = encodedMessage[recipientDataStart];

        for (int i = recipientAddressStart, count = 0; count < usefulOctetCount; i++, count += 2) {
            recipientAddress.append(encodedMessage[i] & 0x0F);

            if (count != usefulOctetCount - 1) {
                recipientAddress.append((encodedMessage[i] >> 4) & 0x0F);
            }
        }

        return recipientAddress.toString();
    }
}
