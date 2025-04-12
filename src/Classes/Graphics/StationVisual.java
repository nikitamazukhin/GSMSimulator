package Classes.Graphics;

import Classes.Devices.Controller;
import Classes.EventObjects.DeviceTerminateRequest;
import Classes.Enums.DeviceType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StationVisual extends JPanel {
    private Controller controller;

    public StationVisual(String number, String nextNumber) {
        this.setBackground(new Color(220, 220, 220));
        this.setBorder(BorderFactory.createLineBorder(new Color(115, 215, 115), 2));

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setAlignmentX(CENTER_ALIGNMENT);

        this.setPreferredSize(new Dimension(200, 85));
        this.setMaximumSize(new Dimension(200, 85));

        JLabel labelDescription = new JLabel("Next station number");
        labelDescription.setAlignmentX(CENTER_ALIGNMENT);

        JLabel nextStationNumber = new JLabel(nextNumber);
        nextStationNumber.setAlignmentX(CENTER_ALIGNMENT);

        JLabel buttonLabel = new JLabel("Terminate device operation");
        buttonLabel.setAlignmentX(CENTER_ALIGNMENT);

        JButton killButton = new JButton("Press me!");
        killButton.setAlignmentX(CENTER_ALIGNMENT);
        killButton.setPreferredSize(new Dimension(175, 23));
        killButton.setMaximumSize(new Dimension(175, 23));
        killButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.recognizeDeviceTerminateRequest(
                        new DeviceTerminateRequest(this, number, DeviceType.STATION)
                );
            }
        });

        this.add(Box.createRigidArea(new Dimension(0, 1)));

        this.add(labelDescription);
        this.add(Box.createRigidArea(new Dimension(0, 1)));
        this.add(nextStationNumber);

        this.add(Box.createRigidArea(new Dimension(0, 2)));

        this.add(buttonLabel);
        this.add(Box.createRigidArea(new Dimension(0, 1)));
        this.add(killButton);

        this.add(Box.createRigidArea(new Dimension(0, 1)));
    }

    public void linkController(Controller controller) {
        this.controller = controller;
    }
}
