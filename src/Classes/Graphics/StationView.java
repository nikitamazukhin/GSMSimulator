package Classes.Graphics;

import Classes.Devices.Controller;
import Classes.EventObjects.StationStorageNotification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;

public class StationView extends JPanel {
    private final JPanel content;
    private Controller controller;

    public void linkController(Controller controller) {
        this.controller = controller;
    }

    public StationView() {
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(676, 640));
        GridBagConstraints constraints = new GridBagConstraints();

        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.LINE_AXIS));
        content.setPreferredSize(new Dimension(676, 590));
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 0, 2, 0);
        constraints.gridwidth = 2;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;

        this.add(content, constraints);

        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(760, 50));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.recognizeStationLayerCreateRequest(new EventObject(this));
            }
        });
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 0, 0, 2);
        constraints.anchor = GridBagConstraints.LAST_LINE_START;
        constraints.ipady = 23;
        constraints.weightx = 0.5;
        constraints.gridx = 0;
        constraints.gridy = 1;
        this.add(addButton, constraints);

        JButton killButton = new JButton("Remove");
        killButton.setPreferredSize(new Dimension(760, 50));
        killButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.recognizeStationLayerTerminateRequest(new EventObject(this));
            }
        });
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 2, 0, 0);
        constraints.anchor = GridBagConstraints.LAST_LINE_END;
        constraints.ipady = 23;
        constraints.weightx = 0.5;
        constraints.gridx = 1;
        constraints.gridy = 1;
        this.add(killButton, constraints);
    }

    public void updateStationGraphics(StationStorageNotification evt) {
        ArrayList<String> StationNumbers = evt.getStoredStationNumbers();
        ArrayList<Integer> StationLayers = evt.getStoredStationLayers();
        ArrayList<String> StationNextNumbers = evt.getStoredStationNextNumbers();

        content.removeAll();
        StationVisual temp;
        JPanel tempBSCLayer;

        JPanel BTSView1 = new JPanel();
        BTSView1.setLayout(new BoxLayout(BTSView1, BoxLayout.PAGE_AXIS));
        BTSView1.setAlignmentX(LEFT_ALIGNMENT);
        BTSView1.setPreferredSize(new Dimension(1500, 970));

        JPanel BTSView2 = new JPanel();
        BTSView2.setLayout(new BoxLayout(BTSView2, BoxLayout.PAGE_AXIS));
        BTSView2.setAlignmentX(RIGHT_ALIGNMENT);
        BTSView2.setPreferredSize(new Dimension(1500, 970));

        ArrayList<JPanel> BSCViews = new ArrayList<>();


        int tempLayer;
        int maxStationLayer = 0;

        for (int tempVal : StationLayers) {
            if (maxStationLayer < tempVal) {
                maxStationLayer = tempVal;
            }
        }

        for (int i = 0; i < StationNumbers.size(); i++) {
            tempLayer = StationLayers.get(i);
            if (tempLayer == 1) {
                temp = new StationVisual(StationNumbers.get(i), StationNextNumbers.get(i));
                temp.linkController(controller);
                BTSView1.add(temp);
                BTSView1.add(Box.createRigidArea(new Dimension(0, 5)));

            } else if (tempLayer == maxStationLayer) {
                temp = new StationVisual(StationNumbers.get(i), StationNextNumbers.get(i));
                temp.linkController(controller);
                BTSView2.add(temp);
                BTSView2.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        for (int i = 2; i < maxStationLayer; i++) {
            tempBSCLayer = new JPanel();
            tempBSCLayer.setLayout(new BoxLayout(tempBSCLayer, BoxLayout.PAGE_AXIS));
            tempBSCLayer.setAlignmentX(CENTER_ALIGNMENT);
            tempBSCLayer.setPreferredSize(new Dimension(1500, 970));

            for (int j = 0; j < StationNumbers.size(); j++) {
                if (StationLayers.get(j) == i) {
                    temp = new StationVisual(StationNumbers.get(j), StationNextNumbers.get(j));
                    temp.linkController(controller);
                    tempBSCLayer.add(temp);
                    tempBSCLayer.add(Box.createRigidArea(new Dimension(0, 5)));
                }
            }
            BSCViews.add(tempBSCLayer);
        }

        content.add(BTSView1);
        content.add(Box.createRigidArea(new Dimension(5, 0)));

        for (JPanel BSCLayer : BSCViews) {
            content.add(BSCLayer);
            content.add(Box.createRigidArea(new Dimension(5, 0)));
        }

        content.add(BTSView2);
        content.add(Box.createRigidArea(new Dimension(5, 0)));

        content.updateUI();
    }
}
