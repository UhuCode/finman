package ch.finman.core;

import java.math.BigDecimal;
import java.util.Arrays;

import org.jfree.data.statistics.MeanAndStandardDeviation;

import yahoofinance.Stock;


public class StockSeriesTableModel extends RowTableModel<StockDataSeries> {
	
	public static String[] COLUMN_HEADERS = {
		"Show",
		"Stock",
		"Price",
		"52W Change",
		"52W Change %",
		//"Mean",
		//"StDev",
		"50d Change %",
		"200d Change %"
	};

	protected StockSeriesTableModel() {
		super(Arrays.asList(COLUMN_HEADERS));
		
		setColumnClass(0, Boolean.class);
		setColumnClass(1, String.class);
		setColumnClass(2, BigDecimal.class);
		//setColumnClass(3, String.class);
		//setColumnClass(4, BigDecimal.class);
		setColumnClass(3, Double.class);
		setColumnClass(4, Double.class);
		setColumnClass(5, Double.class);
		setColumnClass(6, Double.class);
		//setColumnClass(9, Double.class);
		
		setColumnEditable(0, true);
		setColumnEditable(1, false);
		setColumnEditable(2, false);
		setColumnEditable(3, false);
		setColumnEditable(4, false);
		setColumnEditable(5, false);
		setColumnEditable(6, false);
//		setColumnEditable(7, false);
//		setColumnEditable(8, false);
//		setColumnEditable(9, false);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		StockDataSeries row = getRow(rowIndex);
		Stock stock = row.getStock();
		
		BigDecimal price = stock.getQuote().getPrice();
		double currPrice = (price != null) ? price.doubleValue() : 0;
		double startPrice = row.getStartPrice();
		BigDecimal priceAvg50 = stock.getQuote().getPriceAvg50();
		BigDecimal priceAvg50Change = stock.getQuote().getChangeFromAvg50InPercent();
		BigDecimal priceAvg200 = stock.getQuote().getPriceAvg200();
		BigDecimal priceAvg200Change = stock.getQuote().getChangeFromAvg200InPercent();
		MeanAndStandardDeviation meanSd = row.getMeanStd();
		
		double v;
		switch(columnIndex) {
		case 0:
			return row.getSeriesShow();
		case 1:
			return stock.getSymbol() + " " + stock.getCurrency() + " ("+ stock.getName() + ")";
		case 2:
			return price ;
		case 3:
			return currPrice - startPrice;
		case 4:
			//return stock.getCurrency();
			return 100*currPrice/startPrice;
			//return meanSd.getMeanValue();
		//case 5:
			//return priceAvg50;
			//return meanSd.getStandardDeviationValue();
		case 5:
			v = (priceAvg50Change != null) ? priceAvg50Change.doubleValue() : 0;
			return v;
		case 6:
			v = (priceAvg200Change != null) ? priceAvg200Change.doubleValue() : 0;
			return v;
			//return priceAvg200;
//		case 7:
//			v = (priceAvg200Change != null) ? priceAvg200Change.doubleValue() : 0;
//			return v;
//		case 8:
//			return meanSd.getMeanValue();
//		case 9:
//			return meanSd.getStandardDeviationValue();
//			
//			double currPrice = (price != null) ? price.doubleValue() : 0;
//			double paidPrice = (pricePaid != null) ? pricePaid.doubleValue() : 0;
//			return currPrice - paidPrice;
//		case 8:
//			double p50 = (priceAvg50 != null) ? priceAvg50.doubleValue() : 0;
//			double p200 = (priceAvg200 != null) ? priceAvg200.doubleValue() : 0;
//			return p50/p200;
//		case 9:
//			return row.getK3Value();
		}
		return null;
	}
	
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		
		StockDataSeries row = getRow(rowIndex);

		switch (columnIndex) {
		case 0: 
			row.setSeriesShow((Boolean) value);
			break;
		default: 
		}
		fireTableCellUpdated(rowIndex, columnIndex);
	}

}
