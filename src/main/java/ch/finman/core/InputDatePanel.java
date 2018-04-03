/**
 * 
 */
package ch.finman.core;

import java.awt.GridLayout;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author thomas
 *
 */
class InputDatePanel extends JPanel {

	private static final long serialVersionUID = 2003348322926800153L;
	private JTextField text;
	private JLabel label;
	SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy kk:mm", Locale.ENGLISH);

	public InputDatePanel(String name) {
		text = new JTextField();
		//text.setToolTipText(name);
		//text.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		label = new JLabel(name);
		setLayout(new GridLayout(1, 2));
		add(label);
		add(text);
	}

	public void addFieldListener(FocusListener focusListener) {
		text.addFocusListener(focusListener);
	}

	public String getStringValue() {
		String s = new String();
		s = text.getText();
		return s;
	}

	public Date getDateValue() {
		Date s = null;
		try {
			s = df.parse(text.getText());
		} catch (ParseException pe) {
			//System.err.println("Parse Date Error for " + text.getText());
		}
		return s;
	}

	public void setDateValue(Date s) {
		text.setText(df.format(s));
	}

	public void setEnabled(boolean enabled) {
		text.setEnabled(enabled);
	}
	
	public void setDateValueDifference(int offsetDays) {
	      Calendar cal = new GregorianCalendar();
	      cal.add(Calendar.DAY_OF_MONTH, -offsetDays);
	      setDateValue(cal.getTime());
	}
}

