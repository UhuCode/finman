/*
 * Created on 28.05.2006
 *
 * Author: kohler
 * 
 */
package ch.finman.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class ProgressInfo extends JPanel {

	private static final long serialVersionUID = 4400125842910453513L;
	JProgressBar progressBar = new JProgressBar();
	JLabel progressString = new JLabel();
	JTextArea progressArea = new JTextArea();
	JButton cancelButton = new JButton();
	boolean cancel = false;
	boolean alive = false;
	
	public ProgressInfo() {
		if (System.getProperty("develop") != null) {
			//cancelButton.setIcon(new ImageIcon("images/stop16.png"));
		} else {
			//cancelButton.setIcon(new ImageIcon(this.getClass().getResource("/images/stop16.png")));
		}
		cancelButton.setText("Cancel");
		
		setNote("ready.");
		setIndeterminate(false);
		setLayout(new BorderLayout());
			setPreferredSize(new Dimension(1400, 100));

			//progressString.setPreferredSize(new Dimension(1600, 16));
			progressBar.setPreferredSize(new Dimension(1400, 16));
			progressArea.setEditable(false);
  			DefaultCaret caret = (DefaultCaret)progressArea.getCaret();
  			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
  			add(new JScrollPane(progressArea), BorderLayout.CENTER);
			add(progressBar, BorderLayout.SOUTH);
			cancelButton.setPreferredSize(new Dimension(16, 16));
			cancelButton.addActionListener(new CancelAction());
			cancelButton.setEnabled(false);
	}
	
	public void setProgress(int value) {
		if (!progressBar.isIndeterminate()) {
			progressBar.setValue(value);			
		}
		cancelButton.setEnabled(true);
	}
	
	public void setNote(String note) {
		//progressString.setText("> " + note);
		if(progressArea != null) {
			progressArea.append("> " + note +"\n");
			progressArea.moveCaretPosition(progressArea.getDocument().getLength());
		}
	}
	
	public void setIndeterminate(boolean enable) {
		if(progressBar.isIndeterminate() != enable) {
			progressBar.setIndeterminate(enable);
		}
	}
	
	public void start() {
		cancelButton.setEnabled(true);
		alive = true;
	}
	
	public void reset() {
		setNote("ready.");
		setIndeterminate(false);
		setProgress(0);
		cancelButton.setEnabled(false);
		cancel = false;
		alive = false;
	}
	
	public boolean isCancelled() {
		return cancel;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	class CancelAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			cancel = true;
		}		
	}
}
