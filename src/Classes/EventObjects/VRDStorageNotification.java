package Classes.EventObjects;

import Classes.Devices.VRD;

import java.util.ArrayList;
import java.util.EventObject;

public class VRDStorageNotification extends EventObject {
    private final ArrayList<String> storedVRDNumbers;
    private final ArrayList<Integer> storedVRDReceiveCounts;
    private final ArrayList<Boolean> storedVRDResettingStates;

    public VRDStorageNotification(Object source, ArrayList<VRD> storedVRD) {
        super(source);
        storedVRDNumbers = new ArrayList<>();
        storedVRDReceiveCounts = new ArrayList<>();
        storedVRDResettingStates = new ArrayList<>();

        for (VRD device : storedVRD) {
            storedVRDNumbers.add(device.getName());
            storedVRDReceiveCounts.add(device.getStoredMessageCount());
            storedVRDResettingStates.add(device.getResettingState());
        }
    }

    public ArrayList<String> getStoredVRDNumbers() {
        return storedVRDNumbers;
    }

    public ArrayList<Integer> getStoredVRDReceiveCounts() {
        return storedVRDReceiveCounts;
    }

    public ArrayList<Boolean> getStoredVRDResettingStates() {
        return storedVRDResettingStates;
    }
}
