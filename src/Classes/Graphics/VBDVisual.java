package Classes.Graphics;

import Classes.Devices.Controller;
import Classes.Enums.DeviceType;
import Classes.EventObjects.DeviceStateRequest;
import Classes.EventObjects.DeviceTerminateRequest;
import Classes.EventObjects.VBDFrequencyRequest;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public class VBDVisual extends JPanel {
    private Controller controller;

    public VBDVisual(String number, int sleepTime, boolean activeState) {
        this.setBackground(new Color(220, 220, 220));
        this.setBorder(BorderFactory.createLineBorder(new Color(25, 120, 200), 2));

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setAlignmentX(CENTER_ALIGNMENT);

        this.setPreferredSize(new Dimension(200, 200));
        this.setMaximumSize(new Dimension(200, 200));

        JLabel numberLabel = new JLabel("Device number");
        numberLabel.setAlignmentX(CENTER_ALIGNMENT);

        JTextField deviceNumber = new JTextField("+" + number);
        deviceNumber.setHorizontalAlignment(SwingConstants.CENTER);
        deviceNumber.setAlignmentX(CENTER_ALIGNMENT);
        deviceNumber.setEditable(false);
        deviceNumber.setPreferredSize(new Dimension(175, 23));
        deviceNumber.setMaximumSize(new Dimension(175, 23));

        JLabel sliderLabel = new JLabel("Message sending frequency");
        sliderLabel.setAlignmentX(CENTER_ALIGNMENT);

        JSlider frequencySlider = new JSlider(1000, 9000, 10000 - sleepTime);
        frequencySlider.setAlignmentX(CENTER_ALIGNMENT);
        frequencySlider.setPreferredSize(new Dimension(175, 48));
        frequencySlider.setMaximumSize(new Dimension(175, 48));
        frequencySlider.setMinorTickSpacing(2000);
        frequencySlider.setMajorTickSpacing(4000);
        frequencySlider.setPaintTicks(true);

        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(1000, new JLabel("Slow"));
        labels.put(5000, new JLabel("Medium"));
        labels.put(9000, new JLabel("Fast"));

        frequencySlider.setLabelTable(labels);
        frequencySlider.setPaintLabels(true);
        frequencySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!frequencySlider.getValueIsAdjusting()) {
                    controller.recognizeVBDFrequencyRequest(
                            new VBDFrequencyRequest(this, number, 10000 - frequencySlider.getValue())
                    );
                }
            }
        });

        JLabel stateLabel = new JLabel("Current device state");
        stateLabel.setAlignmentX(CENTER_ALIGNMENT);

        JComboBox<String> deviceStateBox = new JComboBox<>(new String[]{"Active", "Waiting"});
        if (activeState)
            deviceStateBox.setSelectedItem("Active");
        else
            deviceStateBox.setSelectedItem("Waiting");

        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        deviceStateBox.setRenderer(renderer);
        deviceStateBox.setAlignmentX(CENTER_ALIGNMENT);
        deviceStateBox.setPreferredSize(new Dimension(175, 23));
        deviceStateBox.setMaximumSize(new Dimension(175, 23));
        deviceStateBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.recognizeDeviceStateRequest(
                        new DeviceStateRequest(this, number, deviceStateBox.getSelectedIndex() == 0, DeviceType.VBD)
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
                        new DeviceTerminateRequest(this, number, DeviceType.VBD)
                );
            }
        });

        this.add(Box.createRigidArea(new Dimension(0, 1)));

        this.add(numberLabel);
        this.add(Box.createRigidArea(new Dimension(0, 1)));
        this.add(deviceNumber);

        this.add(Box.createRigidArea(new Dimension(0, 2)));

        this.add(sliderLabel);
        this.add(Box.createRigidArea(new Dimension(0, 1)));
        this.add(frequencySlider);

        this.add(Box.createRigidArea(new Dimension(0, 2)));

        this.add(stateLabel);
        this.add(Box.createRigidArea(new Dimension(0, 1)));
        this.add(deviceStateBox);

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
