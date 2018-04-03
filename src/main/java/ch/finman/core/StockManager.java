package ch.finman.core;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Week;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

import ch.finman.util.LogUtil;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class StockManager {

	private static final int FIND_START_END_DATE = 10;
	private static final int SET_START_END_DATE = 20;
	private static final int RESET_START_END_DATE = 30;
	private List<StockItem> data;
	private Hashtable<String, StockDataSeries> seriesTable;
	private static LogUtil logger = LogUtil.getLogger(StockManager.class);
	private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy kk:mm", Locale.ENGLISH);
	private Date startDate;
	private Date endDate;
	private ProgressInfo pi;

	public StockManager(ProgressInfo progressInfo) {
		pi = progressInfo;
		data = new ArrayList<StockItem>();
		seriesTable = new Hashtable<String, StockDataSeries>();
	}

	public void addStockConfig(StockItem stockConfig) {
		if(data.contains(stockConfig)) {
			int idx = data.indexOf(stockConfig);
			data.set(idx, stockConfig);
			logger.sayOut("StockItem exists. Replace object with: " + stockConfig.getStockSymbol());
			updateProgressInfo("StockItem exists. Replace object with: " + stockConfig.getStockSymbol());
		} else {
			data.add(stockConfig);
			logger.sayOut("Adding StockItem to list: " + stockConfig.getStockSymbol());
			updateProgressInfo("Adding StockItem to list: " + stockConfig.getStockSymbol());
		}
	}

	public void update(StockSeriesTableModel model, TimeSeriesCollection dataSeries, OHLCSeriesCollection ohlcSeriesCollection) throws IOException {
		
		Iterator<StockItem> stockIterator = data.iterator();	
		while(stockIterator.hasNext()) {		
			StockItem stockItem = (StockItem) stockIterator.next();
			StockDataSeries stockDataSeries = createStockData(stockItem);
			seriesTable.put(stockItem.getStockSymbol(), stockDataSeries);
			model.addRow(stockDataSeries);
			dataSeries.addSeries(stockDataSeries.getTimeSeries());
			//dataSeries.addSeries(stockDataSeries.getTimeSeries());
			ohlcSeriesCollection.addSeries(stockDataSeries.getOHLCSeries());
		}		
		processDataSeries(FIND_START_END_DATE);
	}

	public StockDataSeries createStockData(StockItem stockItem) throws IOException {
		StockDataSeries sds = new StockDataSeries(stockItem.getStockSymbol(), stockItem.getPricePaid());
		return sds;
	}

	public void setStartEndDate(Date filterStartDate, Date filterEndDate) {
		this.startDate = filterStartDate;
		this.endDate = filterEndDate;
		processDataSeries(SET_START_END_DATE);
	}

	public void resetStartEndDate() {
		this.startDate = null;
		this.endDate = null;
		processDataSeries(RESET_START_END_DATE);
		findSeriesStartEndDate();
	}

	public void findSeriesStartEndDate() {
		processDataSeries(FIND_START_END_DATE);
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void processDataSeries(int procType) {
		Enumeration<String> keys = seriesTable.keys();
		int cnt=0;
		while(keys.hasMoreElements()) {
			processDataSeries(procType, getSeries(keys.nextElement()));
		}
	}

	protected void processDataSeries(int procFlag, StockDataSeries drs) {
		switch(procFlag) {
		case FIND_START_END_DATE:
			Date seriesStart = drs.getStartDate();
			Date seriesEnd = drs.getEndDate();
			if(startDate == null) startDate = seriesStart;
			if(endDate == null) endDate = seriesEnd;
			if(seriesStart.before(startDate)) startDate = seriesStart;
			if(seriesEnd.after(endDate)) endDate = seriesEnd;	
			break;
		case SET_START_END_DATE:
			drs.setStartEndDate(startDate, endDate);
			break;
		case RESET_START_END_DATE:
			drs.resetStartEndDate();
			break;
		}
	}

	public StockDataSeries getSeries(String key) {
		if (seriesTable.containsKey(key)) {
			return seriesTable.get(key);
		} else {
			return null;
		}
	}
	
	public void updateProgressInfo(String s) {
		pi.setNote(s);
	}
	

}
