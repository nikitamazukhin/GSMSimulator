package Classes.Graphics;

import Classes.Devices.Controller;
import Classes.EventObjects.StationStorageNotification;
import Classes.EventObjects.VBDStorageNotification;
import Classes.EventObjects.VRDStorageNotification;

import javax.swing.*;
import java.awt.*;

public class Graphics extends JFrame {
    private final VBDView leftPanel;
    private final StationView middlePanel;
    private final VRDView rightPanel;

    public Graphics() {
        this.setLayout(new BorderLayout(5, 0));

        leftPanel = new VBDView();
        this.add(leftPanel, BorderLayout.WEST);

        middlePanel = new StationView();
        this.getContentPane().add(middlePanel, BorderLayout.CENTER);

        rightPanel = new VRDView();
        this.getContentPane().add(rightPanel, BorderLayout.EAST);

        this.setSize(1136, 640);
        this.setVisible(true);
    }

    public void linkController(Controller controller) {
        leftPanel.linkController(controller);
        middlePanel.linkController(controller);
        rightPanel.linkController(controller);
        addWindowListener(controller);
    }

    public void updateVBDGraphics(VBDStorageNotification evt) {
        leftPanel.updateVBDGraphics(evt);
    }

    public void updateStationGraphics(StationStorageNotification evt) {
        middlePanel.updateStationGraphics(evt);
    }

    public void updateVRDGraphics(VRDStorageNotification evt) {
        rightPanel.updateVRDGraphics(evt);
    }
}
