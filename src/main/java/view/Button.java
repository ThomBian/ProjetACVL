package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		this.setIcon(new ImageIcon("/acvlmnta/src/resources/"+picture));
	}

}
