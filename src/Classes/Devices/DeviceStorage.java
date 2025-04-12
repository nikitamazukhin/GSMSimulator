package Classes.Devices;

import Classes.Enums.DeviceType;
import Classes.EventObjects.*;
import Interfaces.MessageRecipient;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashSet;

public class DeviceStorage {
    private static final ArrayList<VBD> VBDStorage = new ArrayList<>();
    private static final ArrayList<VRD> VRDStorage = new ArrayList<>();
    private static final ArrayList<VD> stationStorage = new ArrayList<>();
    private static final ArrayList<MessageRecipient> recipientStorage = new ArrayList<>();
    private static final HashSet<Long> registeredNumbers = new HashSet<>();
    private static int maxLayer;
    private static Controller controller;

    public void linkController(Controller controller) {
        DeviceStorage.controller = controller;
    }

    public static long getUniqueNumber() {
        long number = (long) (4.8e10 + (Math.random() * 1e9));
        while (true) {
            if (registeredNumbers.contains(number)) {
                number = (long) (4.8e10 + (Math.random() * 10e8));
            } else {
                registeredNumbers.add(number);
                return number;
            }
        }
    }

    public void populateStationLayers() {
        for (int i = 1; i < 4; i++) {
            maxLayer = i;
            BTS temp;
            BSC temp2;

            if (maxLayer % 2 != 0) {
                temp = new BTS(maxLayer);
                stationStorage.add(temp);
                recipientStorage.add(temp);
            } else {
                temp2 = new BSC(maxLayer);
                stationStorage.add(temp2);
                recipientStorage.add(temp2);
            }
        }
        maxLayer++;
        controller.receiveStationStorageNotification(new StationStorageNotification(this, stationStorage));
    }

    public void removeDevice(DeviceTerminateRequest evt) {
        switch (evt.getDeviceType()) {
            case VBD -> removeVBD(evt.getNumber());
            case BTS, BSC, STATION -> removeStation(evt.getNumber());
            case VRD -> removeVRD(evt.getNumber());
        }
    }

    public void changeDeviceState(DeviceStateRequest evt) {
        switch (evt.getDeviceType()) {
            case VBD -> changeVBDState(evt.getNumber(), evt.getState());
            case VRD -> changeVRDState(evt.getNumber(), evt.getState());
        }
    }

    public void addVBD(VBDCreateRequest evt) {
        VBD temp = new VBD(0, evt.getMessage());
        VBDStorage.add(temp);
        controller.receiveVBDStorageNotification(new VBDStorageNotification(this, VBDStorage));
    }

    public void changeVBDFrequency(VBDFrequencyRequest evt) {
        for (VBD device : VBDStorage) {
            if (device.getName().equals(evt.getNumber())) {
                device.setSleepTime(evt.getSleepTime());
                controller.receiveVBDStorageNotification(new VBDStorageNotification(this, VBDStorage));
                break;
            }
        }
    }

    public void changeVBDState(String number, boolean activeState) {
        for (VBD device : VBDStorage) {
            if (device.getName().equals(number)) {
                if (activeState)
                    device.resumeVBD();
                else
                    device.suspendVBD();
                controller.receiveVBDStorageNotification(new VBDStorageNotification(this, VBDStorage));
                break;
            }
        }
    }

    public void removeVBD(String number) {
        for (VBD device : VBDStorage) {
            if (device.getName().equals(number)) {
                device.interrupt();
                VBDStorage.remove(device);
                controller.receiveVBDStorageNotification(new VBDStorageNotification(this, VBDStorage));
                break;
            }
        }
    }

    public void addVRD(EventObject ignoredEvt) {
        VRD temp = new VRD(maxLayer);
        VRDStorage.add(temp);
        recipientStorage.add(temp);
        controller.receiveVRDStorageNotification(new VRDStorageNotification(this, VRDStorage));
    }

    public static VRD getVRD(String number) throws Exception {
        for (VRD device : VRDStorage) {
            if (device.getName().equals(number)) {
                return device;
            }
        }
        throw new Exception("Could not find Classes.Devices.VRD with number " + number);
    }

    public static int getVRDAmount() {
        return VRDStorage.size();
    }

    public void changeVRDState(String number, boolean resettingState) {
        for (VRD device : VRDStorage) {
            if (device.getName().equals(number)) {
                if (resettingState)
                    device.resumeResetting();
                else
                    device.suspendResetting();
                controller.receiveVRDStorageNotification(new VRDStorageNotification(this, VRDStorage));
                break;
            }
        }
    }

    public void removeVRD(String number) {
        for (VRD device : VRDStorage) {
            if (device.getName().equals(number)) {
                device.interrupt();
                VRDStorage.remove(device);
                controller.receiveVRDStorageNotification(new VRDStorageNotification(this, VRDStorage));
                break;
            }
        }
    }

    public static void getNotifiedByVRD(VRD device) {
        controller.receiveVRDStorageNotification(new VRDStorageNotification(device, VRDStorage));
    }

    public static String getRandomVRDNumber() {
        return VRDStorage.get((int) (Math.random() * VRDStorage.size())).getName();
    }

    public void removeStation(String number) {
        for (VD station : stationStorage) {
            if (station.getName().equals(number)) {
                if (station.getDeviceType() == DeviceType.BSC) {
                    for (VD station2 : stationStorage) {
                        if (station.getLayer() == station2.getLayer()) {
                            removeBSCLayerInternal();
                            return;
                        }
                    }
                }
                station.interrupt();
                stationStorage.remove(station);
                recipientStorage.remove((MessageRecipient) station);
                break;
            }
        }
        controller.receiveStationStorageNotification(new StationStorageNotification(this, stationStorage));
    }

    private static boolean isStationLayerFull(int layer) {
        for (VD station : stationStorage) {
            if (station.getLayer() == layer && ((MessageRecipient) station).getStoredMessageCount() < 5) {
                return false;
            }
        }
        return true;
    }

    public static void getNotifiedByBTS(BTS bts) {
        if (isStationLayerFull(bts.getLayer())) {
            BTS temp = new BTS(bts.getLayer());
            stationStorage.add(temp);
            recipientStorage.add(temp);
            controller.receiveStationStorageNotification(new StationStorageNotification(bts, stationStorage));
        }
    }

    public static void getNotifiedByBSC(BSC bsc) {
        if (isStationLayerFull(bsc.getLayer())) {
            BSC temp = new BSC(bsc.getLayer());
            stationStorage.add(temp);
            recipientStorage.add(temp);
            controller.receiveStationStorageNotification(new StationStorageNotification(bsc, stationStorage));
        }
    }

    public static MessageRecipient getBestRecipientForSender(VD sender) {
        MessageRecipient bestRecipient = null;
        int leastStored = 0;

        for (MessageRecipient recipient : recipientStorage) {
            if (((VD) recipient).getLayer() - sender.getLayer() == 1) {
                int temp = recipient.getStoredMessageCount();

                if (temp == 0)
                    return recipient;

                else if (leastStored == 0 || temp < leastStored) {
                    leastStored = temp;
                    bestRecipient = recipient;
                }
            }
        }
        return bestRecipient;
    }

    public static String getNextStationNumber(VD device) {
        int temp = device.getLayer();
        if (temp == 0 || temp == maxLayer) {
            return null;
        } else {
            for (VD station : stationStorage) {
                if (station.getLayer() == device.getLayer() && stationStorage.indexOf(station) > stationStorage.indexOf(device)) {
                    return "+" + station.getName();
                }
            }
            return "-";
        }
    }

    private void shiftLayersBack(int startFromLayer) {
        for (MessageRecipient recipient : recipientStorage) {
            int tempLayer = ((VD) recipient).getLayer();
            if (tempLayer >= startFromLayer) {
                ((VD) recipient).setLayer(tempLayer - 1);
            }
        }
    }

    private void shiftLayersForward(int startFromLayer) {
        for (MessageRecipient recipient : recipientStorage) {
            int tempLayer = ((VD) recipient).getLayer();
            if (tempLayer >= startFromLayer) {
                ((VD) recipient).setLayer(tempLayer + 1);
            }
        }
    }


    public void addBSCLayer(EventObject ignoredEvt) {
        shiftLayersForward(maxLayer - 1);
        BSC temp = new BSC(maxLayer - 1);
        stationStorage.add(temp);
        recipientStorage.add(temp);
        maxLayer++;
        controller.receiveStationStorageNotification(new StationStorageNotification(this, stationStorage));
    }

    private void removeBSCLayerInternal() {
        if (maxLayer > 4) {
            ArrayList<BSC> toRemove = new ArrayList<>();

            for (VD station : stationStorage) {
                if (station.getLayer() == maxLayer - 2) {
                    ((BSC) station).setAccepting(false);
                    station.interrupt();
                    toRemove.add((BSC) station);
                }
            }

            for (BSC station : toRemove) {
                for (byte[] message : station.getStoredMessages()) {
                    station.sendMessage(message, getBestRecipientForSender(station));
                }
                stationStorage.remove(station);
                recipientStorage.remove(station);
            }

            shiftLayersBack(maxLayer - 1);
            maxLayer--;
            controller.receiveStationStorageNotification(new StationStorageNotification(this, stationStorage));
        } else
            System.out.println("Cannot remove first Classes.Devices.BSC layer");
    }

    public void removeBSCLayer(EventObject ignoredEvt) {
        removeBSCLayerInternal();
    }

    public void windowClose() {
        for (VBD device : VBDStorage) {
            device.interrupt();
            device.saveInfoToFile("VBDInfo.bin");
        }
        for (MessageRecipient recipient : recipientStorage) {
            ((VD) recipient).interrupt();
        }
    }
}
