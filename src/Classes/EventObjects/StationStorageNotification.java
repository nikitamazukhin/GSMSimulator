package Classes.EventObjects;

import Classes.Devices.DeviceStorage;
import Classes.Devices.VD;

import java.util.ArrayList;
import java.util.EventObject;

public class StationStorageNotification extends EventObject {
    private final ArrayList<String> storedStationNumbers;
    private final ArrayList<Integer> storedStationLayers;
    private final ArrayList<String> storedStationNextNumbers;

    public StationStorageNotification(Object source, ArrayList<VD> storedStations) {
        super(source);
        storedStationNumbers = new ArrayList<>();
        storedStationLayers = new ArrayList<>();
        storedStationNextNumbers = new ArrayList<>();

        for (VD station : storedStations) {
            storedStationNumbers.add(station.getName());
            storedStationLayers.add(station.getLayer());
            storedStationNextNumbers.add(DeviceStorage.getNextStationNumber(station));
        }
    }

    public ArrayList<String> getStoredStationNumbers() {
        return storedStationNumbers;
    }

    public ArrayList<Integer> getStoredStationLayers() {
        return storedStationLayers;
    }

    public ArrayList<String> getStoredStationNextNumbers() {
        return storedStationNextNumbers;
    }
}
