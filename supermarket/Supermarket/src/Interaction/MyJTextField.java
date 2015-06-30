package Interaction;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

class MyTextField extends JTextField implements FocusListener {
	private String hint;
	private boolean showingHint;

	public MyTextField(int length) {
		super(length);
		super.addFocusListener(this);
	}
	
	public void setHint(String hint){
		this.hint = hint;
		super.setText(hint);
		this.showingHint = true;
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (this.getText().isEmpty()) {
			super.setText("");
			showingHint = false;
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (this.getText().isEmpty()) {
			super.setText(hint);
			showingHint = true;
		}
	}

	@Override
	public String getText() {
		return showingHint ? "" : super.getText();
	}

	
}