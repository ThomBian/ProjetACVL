package view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import controller.command.Command;
import controller.command.CreateInitialState;

public class Button extends JButton implements ActionListener {
	
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
		ImageIcon imgIco = new ImageIcon("src/resources/"+picture);
		Image img = imgIco.getImage();
		BufferedImage bi = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		g.drawImage(img, 0, 0, 50, 50, null);
		ImageIcon newIcon = new ImageIcon(bi);
		this.setIcon(newIcon);	
	}

}
