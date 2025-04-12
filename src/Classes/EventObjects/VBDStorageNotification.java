package Classes.EventObjects;

import Classes.Devices.VBD;

import java.util.ArrayList;
import java.util.EventObject;

public class VBDStorageNotification extends EventObject {
    private final ArrayList<String> storedVBDNumbers;
    private final ArrayList<Integer> storedVBDSleepTimes;
    private final ArrayList<Boolean> storedVBDActiveStates;

    public VBDStorageNotification(Object source, ArrayList<VBD> storedVBD) {
        super(source);
        storedVBDNumbers = new ArrayList<>();
        storedVBDSleepTimes = new ArrayList<>();
        storedVBDActiveStates = new ArrayList<>();

        for (VBD device : storedVBD) {
            storedVBDNumbers.add(device.getName());
            storedVBDSleepTimes.add(device.getSleepTime());
            storedVBDActiveStates.add(device.getActiveState());
        }
    }

    public ArrayList<String> getStoredVBDNumbers() {
        return storedVBDNumbers;
    }

    public ArrayList<Integer> getStoredVBDSleepTimes() {
        return storedVBDSleepTimes;
    }

    public ArrayList<Boolean> getStoredVBDActiveStates() {
        return storedVBDActiveStates;
    }
}
