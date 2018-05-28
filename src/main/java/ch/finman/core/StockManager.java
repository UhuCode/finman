package ch.finman.core;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;

import ch.finman.util.LogUtil;

public class StockManager {

	private static final int FIND_START_END_DATE = 10;
	private static final int SET_START_END_DATE = 20;
	private static final int RESET_START_END_DATE = 30;
	private List<StockItem> data;
	private Hashtable<String, StockDataSeries> seriesTable;
	private static LogUtil logger = LogUtil.getLogger(StockManager.class);
	private Date startDate;
	private Date endDate;
	private ProgressInfo pi;

	public StockManager(ProgressInfo progressInfo) {
		pi = progressInfo;
		data = new ArrayList<StockItem>();
		seriesTable = new Hashtable<String, StockDataSeries>();
	}

	public void addStockItem(StockItem stockConfig) {
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

	public void removeAllStockItem(Collection<StockItem> stockItems) {
		if (!stockItems.isEmpty()) {
			logger.sayOut("Removing invalid StockItems.");
			updateProgressInfo("Removing invalid StockItems.");
			data.removeAll(stockItems);
		}
	}

	private int update(StockSeriesTableModel model, TimeSeriesCollection dataSeries, OHLCSeriesCollection ohlcSeriesCollection) {	
		Iterator<StockItem> stockIterator = data.iterator();
		int cnt = 0;
		ArrayList<StockItem> failedList = new ArrayList<StockItem>();
		while(stockIterator.hasNext()) {		
			StockItem stockItem = (StockItem) stockIterator.next();
			try {
				//StockDataSeries stockDataSeries = createStockData(stockItem);
				StockDataSeries stockDataSeries = createDbStockData(stockItem.getStockSymbol());
				seriesTable.put(stockItem.getStockSymbol(), stockDataSeries);
				model.addRow(stockDataSeries);
				if(stockDataSeries.getSeriesShow()) {
					dataSeries.addSeries(stockDataSeries.getTimeSeries());
					ohlcSeriesCollection.addSeries(stockDataSeries.getOHLCSeries());
				}
			} catch (IOException ioe) {
				failedList.add(stockItem);
				updateProgressInfo("Error retrieving Stock Data for: " + stockItem.getStockSymbol());
				updateProgressInfo(ioe.getMessage());
				logger.sayErr("Error retrieving Stock Data for: " + stockItem.getStockSymbol() + " -> " + ioe.getMessage());
			}
			cnt++;
		}
		removeAllStockItem(failedList);
		processDataSeries(FIND_START_END_DATE);
		return cnt;
	}

	public void update(String symbol, StockSeriesTableModel model, TimeSeriesCollection dataSeries, OHLCSeriesCollection ohlcSeriesCollection) {	
			try {
				StockDataSeries stockDataSeries = createDbStockData(symbol);
				seriesTable.put(symbol, stockDataSeries);
				model.addRow(stockDataSeries);
				if(stockDataSeries.getSeriesShow()) {
					dataSeries.addSeries(stockDataSeries.getTimeSeries());
					ohlcSeriesCollection.addSeries(stockDataSeries.getOHLCSeries());
				}
			} catch (IOException ioe) {
				updateProgressInfo("Error retrieving Stock Data for: " + symbol);
				updateProgressInfo(ioe.getMessage());
				logger.sayErr("Error retrieving Stock Data for: " + symbol + " -> " + ioe.getMessage());
			}
	}

	public StockDataSeries createStockData(StockItem stockItem) throws IOException {
		StockDataSeries sds = new StockDataSeries(stockItem.getStockSymbol(), stockItem.getPricePaid());
		if (sds.requestStockData()) {
			try {
				sds.requestStockHistory();
			} catch (IOException ioe) {
				updateProgressInfo("Error retrieving Stock History Data for: " + stockItem.getStockSymbol());
				updateProgressInfo(ioe.getMessage());
				logger.sayErr("Error retrieving Stock History Data for: " + stockItem.getStockSymbol() + " -> " + ioe.getMessage());
			}
		}
		sds.resetStartEndDate();
		return sds;
	}

	public DbStockDataSeries createDbStockData(String symbol) throws IOException {
		DbStockDataSeries sds = new DbStockDataSeries(symbol);
		updateProgressInfo("Processing Stock: " + symbol);
		if (sds.requestStockData()) {
			updateProgressInfo("Processing Stock History: " + sds.getStockName());
			if (!sds.requestStockHistory()) {
				updateProgressInfo("Error retrieving Stock History Data for: " + symbol);
				logger.sayErr("Error retrieving Stock History Data for: " + symbol);
			}
		}
		sds.resetStartEndDate();
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
