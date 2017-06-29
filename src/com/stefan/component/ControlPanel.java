package com.stefan.component;

import com.stefan.objects.Model;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by stersteeg on 14/06/2017.
 */
public class ControlPanel extends JPanel {

    private final Model model;
    private final LinkedHashMap<Long, Integer> messageCounter = new LinkedHashMap();

    private final JLabel carCountLabel;
    private final JLabel messageCountLabel;
    private final JLabel messagePerSecondLabel;

    public ControlPanel(Model model) {
        this.model = model;
        this.setPreferredSize(new Dimension(250, 200));
        this.setVisible(true);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel selectPanel = new JPanel();
        selectPanel.setLayout(new BorderLayout());

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new FlowLayout());

        JButton carButton = new JButton("Select Car");
        JButton roadButton = new JButton("Block Road");

        carButton.setPreferredSize(new Dimension(100, 20));
        carButton.setForeground(model.isCarSelection() ? Color.GREEN : Color.DARK_GRAY);
        carButton.setFocusPainted(false);
        carButton.addActionListener(e -> {
            if(!model.isCarSelection()) {
                model.changeCarSelection();
                model.changeRoadBlocking();
                carButton.setForeground(model.isCarSelection() ? Color.GREEN : Color.DARK_GRAY);
                roadButton.setForeground(model.isRoadBlocking() ? Color.GREEN : Color.DARK_GRAY);
                this.revalidate();
            }
        });

        roadButton.setPreferredSize(new Dimension(100, 20));
        roadButton.setForeground(model.isRoadBlocking() ? Color.GREEN : Color.DARK_GRAY);
        roadButton.setFocusPainted(false);
        roadButton.addActionListener(e -> {
            if(!model.isRoadBlocking()) {
                model.changeCarSelection();
                model.changeRoadBlocking();
                carButton.setForeground(model.isCarSelection() ? Color.GREEN : Color.DARK_GRAY);
                roadButton.setForeground(model.isRoadBlocking() ? Color.GREEN : Color.DARK_GRAY);
                this.revalidate();
            }
        });

        JPanel speedPanel = new JPanel();
        selectPanel.setLayout(new BorderLayout());

        JButton fasterButton = new JButton(">>");
        JButton slowerButton = new JButton("<<");

        fasterButton.setPreferredSize(new Dimension(100, 20));
        fasterButton.setFocusPainted(false);
        fasterButton.addActionListener(e -> {
            double speed = model.getSpeedFactor();
            if(speed < 8) {
                model.setSpeedFactor(speed * 2);
            }
        });

        slowerButton.setPreferredSize(new Dimension(100, 20));
        slowerButton.setFocusPainted(false);
        slowerButton.addActionListener(e -> {
            double speed = model.getSpeedFactor();
            if(speed > 0.25) {
                model.setSpeedFactor(speed / 2);
            }
        });

        carCountLabel = new JLabel("Car count: " + model.getCarCount());
        carCountLabel.setPreferredSize(new Dimension(230, 15));

        messageCountLabel = new JLabel("Message count: " + model.getMessageCount());
        messageCountLabel.setPreferredSize(new Dimension(230, 15));

        messagePerSecondLabel = new JLabel("Message count: " + model.getMessageCount());
        messagePerSecondLabel.setPreferredSize(new Dimension(230, 15));

        detailPanel.add(carCountLabel);
        detailPanel.add(messageCountLabel);
        detailPanel.add(messagePerSecondLabel);

        selectPanel.add(carButton, BorderLayout.WEST);
        selectPanel.add(roadButton, BorderLayout.EAST);

        speedPanel.add(slowerButton, BorderLayout.WEST);
        speedPanel.add(fasterButton, BorderLayout.EAST);

        this.add(selectPanel, BorderLayout.NORTH);
        this.add(detailPanel, BorderLayout.CENTER);
        this.add(speedPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        messageCounter.put(System.currentTimeMillis(), model.getMessageCount());
        for(int i = 0; i < messageCounter.size(); i++) {
            Long key = messageCounter.keySet().iterator().next();
            if(System.currentTimeMillis() - key > 10000) {
                messageCounter.remove(key);
            } else {
                break;
            }
        }
        Long timeKey = messageCounter.keySet().iterator().next();
        double time = System.currentTimeMillis() - timeKey;
        int messagesCount = model.getMessageCount() - messageCounter.get(timeKey);
        int messagePerSecond = (int) (messagesCount / time * 1000 / model.getSpeedFactor());

        carCountLabel.setText("Car count: " + model.getCarCount());
        messageCountLabel.setText("Message count: " + model.getMessageCount());
        messagePerSecondLabel.setText("Messages p/s: " + messagePerSecond);
        this.revalidate();
        super.paintComponent(g);
    }
}
