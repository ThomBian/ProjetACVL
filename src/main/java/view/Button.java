package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import controller.command.Command;
import controller.command.CreateInitialState;

public class Button extends JButton implements ActionListener{
	
	private Command command = new CreateInitialState();

	private String tooltip;

	public Button(String text) {
		super(text);
		tooltip = text;
		this.setToolTipText(text);
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		command.execute();
	}

	public Button(String tooltip, Command command) {
		this(tooltip);
		this.command = command;
	}
	
		
}
