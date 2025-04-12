package Classes.Graphics;

import Classes.Devices.Controller;
import Classes.EventObjects.VRDStorageNotification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;

public class VRDView extends JPanel {
    private final JPanel VRDSpace;
    private Controller controller;

    public void linkController(Controller controller) {
        this.controller = controller;
    }

    public VRDView() {
        super(new BorderLayout(5, 5));
        this.setPreferredSize(new Dimension(230, 640));

        VRDSpace = new JPanel();
        VRDSpace.setLayout(new BoxLayout(VRDSpace, BoxLayout.PAGE_AXIS));
        VRDSpace.setMinimumSize(new Dimension(230, 590));

        JScrollPane scrollPane = new JScrollPane(VRDSpace);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(230, 590));

        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(230, 50));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.recognizeVRDCreateRequest(new EventObject(this));
            }
        });

        this.add(scrollPane, BorderLayout.CENTER);
        this.add(addButton, BorderLayout.SOUTH);
    }

    public void updateVRDGraphics(VRDStorageNotification evt) {
        ArrayList<String> VRDNumbers = evt.getStoredVRDNumbers();
        ArrayList<Integer> VRDReceiveCounts = evt.getStoredVRDReceiveCounts();
        ArrayList<Boolean> VRDResettingStates = evt.getStoredVRDResettingStates();
        VRDSpace.removeAll();
        VRDSpace.add(Box.createRigidArea(new Dimension(0, 5)));

        VRDVisual temp;

        for (int i = 0; i < VRDNumbers.size(); i++) {
            temp = new VRDVisual(VRDNumbers.get(i), VRDReceiveCounts.get(i), VRDResettingStates.get(i));
            temp.linkController(controller);
            VRDSpace.add(temp);
            VRDSpace.add(Box.createRigidArea(new Dimension(5, 5)));
        }

        this.updateUI();
    }
}
