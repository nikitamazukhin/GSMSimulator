package Classes.Graphics;

import Classes.Devices.Controller;
import Classes.EventObjects.VBDCreateRequest;
import Classes.EventObjects.VBDStorageNotification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VBDView extends JPanel {
    private final JPanel VBDSpace;
    private Controller controller;

    public void linkController(Controller controller) {
        this.controller = controller;
    }

    public VBDView() {
        super(new BorderLayout(0, 5));
        this.setPreferredSize(new Dimension(230, 640));

        VBDSpace = new JPanel();
        VBDSpace.setLayout(new BoxLayout(VBDSpace, BoxLayout.PAGE_AXIS));
        VBDSpace.setMinimumSize(new Dimension(230, 590));

        JScrollPane scrollPane = new JScrollPane(VBDSpace);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(230, 590));

        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(230, 50));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = JOptionPane.showInputDialog("Please type in your message");
                if ((message != null) && (message.length() > 0)) {
                    controller.recognizeVBDCreateRequest(new VBDCreateRequest(this, message));
                }
            }
        });

        this.add(scrollPane, BorderLayout.CENTER);
        this.add(addButton, BorderLayout.SOUTH);
    }

    public void updateVBDGraphics(VBDStorageNotification evt) {
        ArrayList<String> VBDNumbers = evt.getStoredVBDNumbers();
        ArrayList<Integer> VBDSleepTimes = evt.getStoredVBDSleepTimes();
        ArrayList<Boolean> VBDActiveStates = evt.getStoredVBDActiveStates();
        VBDSpace.removeAll();
        VBDSpace.add(Box.createRigidArea(new Dimension(0, 5)));

        VBDVisual temp;

        for (int i = 0; i < VBDNumbers.size(); i++) {
            temp = new VBDVisual(VBDNumbers.get(i), VBDSleepTimes.get(i), VBDActiveStates.get(i));
            temp.linkController(controller);
            VBDSpace.add(temp);
            VBDSpace.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        this.updateUI();
    }
}
