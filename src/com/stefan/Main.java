package com.stefan;

import com.stefan.component.MainFrame;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;

import javax.swing.*;

public class Main extends JFrame {

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(false);
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
            } catch (Exception e) {
                System.out.println("Substance Graphite failed to initialize");
            }
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
