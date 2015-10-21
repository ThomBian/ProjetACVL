package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import model.CreateInitialState;

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
	
		
}
