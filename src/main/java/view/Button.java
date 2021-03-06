package view;

import controller.command.Command;
import controller.command.CreateInitialState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Button extends JButton implements ActionListener {

    /**
     *
     */
    private static final long serialVersionUID = -6991413053863319L;

    private Command command = new CreateInitialState();

    private final String tooltip;

    public Button(String text, Command command) {
        super(text);
        tooltip = text;
        this.setToolTipText(tooltip);
        addActionListener(this);
        this.command = command;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        command.execute();
    }

    public Button(String tooltip, String picture, Command command) {
        this(tooltip, command);
        ImageIcon imgIco = new ImageIcon(getClass().getClassLoader().getResource(picture));
        Image img = imgIco.getImage();
        BufferedImage bi = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 0, 50, 50, null);
        ImageIcon newIcon = new ImageIcon(bi);
        this.setIcon(newIcon);
    }
}
