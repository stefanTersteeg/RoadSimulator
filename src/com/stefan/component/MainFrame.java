package com.stefan.component;

import com.stefan.objects.Model;

import javax.swing.*;
import java.awt.*;

/**
 * Created by stersteeg on 20/06/2017.
 */
public class MainFrame extends JFrame {

    private final Model model;

    public MainFrame() {
        super("Road Simulator");
        this.model = new Model();

        PaintPanel panel  = new PaintPanel(model);
        ControlPanel controlPanel  = new ControlPanel(model);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Thread t = new Thread(() -> {
            while(true) {
                try {
                    repaint();
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        });
        t.start();
    }
}
