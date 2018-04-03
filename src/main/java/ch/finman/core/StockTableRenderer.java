package ch.finman.core;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
 
public class StockTableRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
 
    public StockTableRenderer() {
    }
 
    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {
    	Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    	//StockTableModel model = (StockTableModel) table.getModel();
    	StockSeriesTableModel model = (StockSeriesTableModel) table.getModel();
		Object val = model.getValueAt(row, column);
		Double v ;
		if(val instanceof Double) {
			v = (Double)val;
		} else {
			if(val instanceof BigDecimal) {
				v = ((BigDecimal)val).doubleValue();
			} else {
				return c;
			}
		}
		
		switch(column) {
		case 3:
			if(v>= 0) {
				c.setBackground(Color.green);
			} else {
				c.setBackground(Color.red);
			}
			break;
		case 4:
			if(v>= 100) {
				c.setBackground(Color.green);
			} else {
				c.setBackground(Color.red);
			}
			break;
		case 5:
			if(v>= 0) {
				c.setBackground(Color.green);
			} else {
				c.setBackground(Color.red);
			}
			break;
		case 6:
			if(v>= 0) {
				c.setBackground(Color.green);
			} else {
				c.setBackground(Color.red);
			}
			break;
		default:
				c.setBackground(null);
		}
        return c;
    }
}