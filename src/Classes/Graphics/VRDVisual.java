package Classes.Graphics;

import Classes.Devices.Controller;
import Classes.EventObjects.DeviceStateRequest;
import Classes.EventObjects.DeviceTerminateRequest;
import Classes.Enums.DeviceType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VRDVisual extends JPanel {
    private Controller controller;

    public VRDVisual(String number, int messageCount, boolean isResetting) {
        this.setBackground(new Color(220, 220, 220));
        this.setBorder(BorderFactory.createLineBorder(new Color(245, 160, 40), 2));

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setAlignmentX(CENTER_ALIGNMENT);

        this.setPreferredSize(new Dimension(200, 140));
        this.setMaximumSize(new Dimension(200, 140));


        JLabel labelDescription = new JLabel("Received messages:");
        labelDescription.setAlignmentX(CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel("" + messageCount);
        messageLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel stateLabel1 = new JLabel("Reset # of received messages");
        stateLabel1.setAlignmentX(CENTER_ALIGNMENT);
        JLabel stateLabel2 = new JLabel("every 10 seconds");
        stateLabel2.setAlignmentX(CENTER_ALIGNMENT);

        JCheckBox stateBox = new JCheckBox();
        stateBox.setAlignmentX(CENTER_ALIGNMENT);
        stateBox.setSelected(isResetting);
        stateBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.recognizeDeviceStateRequest(
                        new DeviceStateRequest(this, number, stateBox.isSelected(), DeviceType.VRD)
                );
            }
        });

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
                        new DeviceTerminateRequest(this, number, DeviceType.VRD)
                );
            }
        });

        this.add(Box.createRigidArea(new Dimension(0, 1)));

        this.add(labelDescription);
        this.add(Box.createRigidArea(new Dimension(0, 1)));
        this.add(messageLabel);

        this.add(Box.createRigidArea(new Dimension(0, 2)));

        this.add(stateLabel1);
        this.add(stateLabel2);
        this.add(Box.createRigidArea(new Dimension(0, 1)));
        this.add(stateBox);

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
