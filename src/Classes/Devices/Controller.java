package Classes.Devices;

import Classes.EventObjects.*;
import Classes.Graphics.Graphics;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.EventObject;

public class Controller implements WindowListener {
    private final DeviceStorage devices;
    private final Graphics gui;

    public Controller(DeviceStorage devices, Graphics gui) {
        this.devices = devices;
        this.gui = gui;
    }

    public void recognizeDeviceTerminateRequest(DeviceTerminateRequest evt) {
        devices.removeDevice(evt);
    }

    public void recognizeDeviceStateRequest(DeviceStateRequest evt) {
        devices.changeDeviceState(evt);
    }

    public void recognizeVBDCreateRequest(VBDCreateRequest evt) {
        devices.addVBD(evt);
    }

    public void recognizeVBDFrequencyRequest(VBDFrequencyRequest evt) {
        devices.changeVBDFrequency(evt);
    }

    public void recognizeVRDCreateRequest(EventObject evt) {
        devices.addVRD(evt);
    }

    public void recognizeStationLayerCreateRequest(EventObject evt) {
        devices.addBSCLayer(evt);
    }

    public void recognizeStationLayerTerminateRequest(EventObject evt) {
        devices.removeBSCLayer(evt);
    }

    public void receiveVBDStorageNotification(VBDStorageNotification evt) {
        gui.updateVBDGraphics(evt);
    }

    public void receiveVRDStorageNotification(VRDStorageNotification evt) {
        gui.updateVRDGraphics(evt);
    }

    public void receiveStationStorageNotification(StationStorageNotification evt) {
        gui.updateStationGraphics(evt);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        devices.windowClose();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        System.exit(0);
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
