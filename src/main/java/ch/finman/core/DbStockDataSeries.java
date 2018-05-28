/**
 * 
 */
package ch.finman.core;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;

import ch.finman.model.StockEntry;
import ch.finman.model.StockHistoryEntry;
import ch.finman.model.logic.StockEntityManager;
import ch.finman.util.LogUtil;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * @author thomas
 *
 */
public class DbStockDataSeries extends StockDataSeries {
	
	private static LogUtil logger = LogUtil.getLogger(DbStockDataSeries.class);
	
	private String symbol;
	private StockEntry stockEntry;
	private List<StockHistoryEntry> records;
	
	private StockEntityManager stockEntityManager;

	/**
	 * @param symbol
	 */
	public DbStockDataSeries(String symbol) {
		super(symbol);
		this.symbol = symbol;
		stockEntityManager = new StockEntityManager();
	}

	public boolean requestStockData() {
		logger.sayOut("Requesting data for: " + symbol);
		//stockEntityManager.updateStock(symbol);
		stock = stockEntityManager.getStock(symbol);
		stockEntry = stockEntityManager.getStockEntry(symbol);
		if(stockEntry == null) {
			logger.sayErr("No data found for: " + symbol);
			stockEntry = null;
			return false;
		} else {
			logger.sayOut("Retrieved data for: " + symbol);
			return true;
		}
	}

	public boolean requestStockHistory() {
		if (records == null) {records = new ArrayList<StockHistoryEntry>();}
		logger.sayOut("Requesting history data for: " + stockEntry.getName());
		records = stockEntityManager.getStockHistoryEntries(stockEntry);
		if(!records.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	protected void updateSeries() {
		Iterator<StockHistoryEntry> iter = records.iterator();
		while(iter.hasNext()) {
			StockHistoryEntry itm = (StockHistoryEntry)iter.next();
			updateRecord(itm);
		}
	}
	
	protected void updateIndexedSeries() {
		firstPrice = (Double) timeSeries.getValue(0);
		Iterator<StockHistoryEntry> iter = records.iterator();
		while(iter.hasNext()) {
			StockHistoryEntry itm = (StockHistoryEntry)iter.next();
			updateIndexedRecord(itm);
		}
	}

	protected void updateRecord(StockHistoryEntry itm) {
		Date tp = itm.getHistoryDate();
		RegularTimePeriod rtp = new Minute(tp);
		double close = (itm.getClose() == null) ? 0 : itm.getClose().doubleValue();
		if(tp.before(start) || tp.after(end)) {
			timeSeries.delete(rtp);
		} else {
			timeSeries.addOrUpdate(rtp, close);
		}		
	}
	
	protected void updateOHLCRecord(StockHistoryEntry itm) {
		Date tp = itm.getHistoryDate();
		RegularTimePeriod rtp = new Minute(tp);
		double open = (itm.getOpen() == null) ? 0 : itm.getOpen().doubleValue();
		double high = (itm.getHigh() == null) ? 0 : itm.getHigh().doubleValue();
		double low = (itm.getLow() == null) ? 0 : itm.getLow().doubleValue();
		double close = (itm.getClose() == null) ? 0 : itm.getClose().doubleValue();
		if(tp.before(start) || tp.after(end)) {
			ohlcSeries.remove(rtp);
		} else {
			if(ohlcSeries.indexOf(rtp) <0) {
				ohlcSeries.add(rtp, open, high, low, close);
			}
		}	
	}
	
	protected void updateIndexedRecord(StockHistoryEntry itm) {
		Date tp = itm.getHistoryDate();
		RegularTimePeriod rtp = new Minute(tp);
		double close = (itm.getClose() == null) ? 0 : itm.getClose().doubleValue();
		if(tp.before(start) || tp.after(end)) {
			indexedTimeSeries.delete(rtp);
		} else {
			indexedTimeSeries.addOrUpdate(rtp, 100*close/firstPrice);
		}	
	}

	public int getRecordCount() {
		return records.size();
	}

	public String getStockName() {
		return stockEntry.getName();
	}
	
	public void resetStartEndDate() {
		this.start = null;
		this.end = null;
		findStartEndDate();
		updateSeries();
		updateIndexedSeries();
		findTimeSeriesMeanStd();
	}

	protected void findStartEndDate() {
		Iterator<StockHistoryEntry> iter = records.iterator();
		while(iter.hasNext()) {
			StockHistoryEntry itm = (StockHistoryEntry)iter.next();
			Date itmDate = itm.getHistoryDate();
			if(start == null) start = itmDate;
			if(end == null) end = itmDate;
			if(itmDate.before(start)) start = itmDate;
			if(itmDate.after(end)) end = itmDate;
		}
	}



}
