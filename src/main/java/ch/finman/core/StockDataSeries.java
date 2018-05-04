package ch.finman.core;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.MeanAndStandardDeviation;
import org.jfree.data.statistics.Statistics;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.time.ohlc.OHLCItem;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.xy.OHLCDataItem;

import ch.finman.util.LogUtil;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class StockDataSeries {
	
	private String symbol = "";
	private float paidPrice;
	private StockItem stockItem;
	
	private Stock stock;
	private List<HistoricalQuote> records;
	
	private Date start = null;
	private Date end = null;
	
	private MeanAndStandardDeviation meanSd;
	
	private TimeSeries timeSeries;
	private TimeSeries indexedTimeSeries;
	private OHLCSeries ohlcSeries;
	private boolean showSeries = true;
	private double firstPrice;
	private boolean showIndexedSeries = true;
	private Interval interval = Interval.WEEKLY;
	
	private static LogUtil logger = LogUtil.getLogger(StockDataSeries.class);
	
	public StockDataSeries(String symbol) {
		this(symbol, 0);
	}

	public StockDataSeries(String symbol, float pricePaid) {
		this.symbol = symbol;
		this.paidPrice = pricePaid;
		this.indexedTimeSeries = new TimeSeries(symbol);
		this.timeSeries = new TimeSeries(symbol);
		this.ohlcSeries = new OHLCSeries(symbol);
	}
	
	public StockDataSeries(StockItem stockItem) {
		this.symbol = stockItem.getStockSymbol();
		this.paidPrice = stockItem.getPricePaid().floatValue();
		this.stockItem = stockItem;
		this.indexedTimeSeries = new TimeSeries(symbol);
		this.timeSeries = new TimeSeries(symbol);
		this.ohlcSeries = new OHLCSeries(symbol);
	}
	
	public StockDataSeries(String symbol, BigDecimal pricePaid) {
		this(symbol, pricePaid.floatValue());
	}

//	public void initializeStockData() throws IOException {
//		logger.sayOut("Requesting data for: " + symbol);
//		stock = YahooFinance.get(symbol);
//		if(stock != null && stock.isValid()) {
//			logger.sayOut("Requesting history data for: " + stock.getName());
//			records = requestHistory(stock, Interval.WEEKLY);
//			if(!records.isEmpty()) {
//				logger.sayOut("Received " + records.size() + " history data records. ");	
//			} else {
//				logger.sayOut("No history data received.");
//				records = null;
//			}
//		} else {
//			logger.sayOut("No data found for: " + symbol);
//			stock = null;
//		}
//	}
//
	public boolean requestStockData() throws IOException {
		logger.sayOut("Requesting data for: " + symbol);
		stock = YahooFinance.get(symbol);
		if(stock == null || !stock.isValid()) {
			logger.sayOut("No data found for: " + symbol);
			stock = null;
			return false;
		} else {
			logger.sayOut("Retrieved data for: " + symbol);
			return true;
		}
	}

	public boolean requestStockHistory() throws IOException {
		if (records == null) {records = new ArrayList<HistoricalQuote>();}
		logger.sayOut("Requesting history data for: " + stock.getName());
		List<HistoricalQuote> list = stock.getHistory(Interval.DAILY);
		if(!list.isEmpty()) {
			logger.sayOut("Received " + list.size() + " history data records. ");
			records = cleanHistoryList(list);
			logger.sayOut("Cleaned List has " + records.size() + " history data records. ");
			return true;
		} else {
			logger.sayOut("No history data received.");
			return false;
		}
	}
	
	private List<HistoricalQuote> cleanHistoryList(List<HistoricalQuote> recs) {
		List<HistoricalQuote> list = new ArrayList<HistoricalQuote>();
		Iterator<HistoricalQuote> iter = recs.iterator();
		while(iter.hasNext()) {
			HistoricalQuote itm = (HistoricalQuote)iter.next();
			if (itm.getClose() != null) {
				list.add(itm);
			}
		}
		return list;
	}

	private List<HistoricalQuote> requestHistory(Stock stock, Date from, Date to, Interval interval) {
		List<HistoricalQuote> list = new ArrayList<HistoricalQuote>();
		Calendar fromCal = Calendar.getInstance();
		fromCal.setTime(from);
		Calendar toCal = Calendar.getInstance();
		toCal.setTime(to);
		try {
			list = stock.getHistory(fromCal, toCal, interval);
			if(!list.isEmpty()) {
				return list;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Stock getStock() {
		return stock;
	}
	
	protected void updateSeries() {
		Iterator<HistoricalQuote> iter = records.iterator();
		while(iter.hasNext()) {
			HistoricalQuote itm = (HistoricalQuote)iter.next();
			updateRecord(itm);
		}
	}
	
	protected void updateIndexedSeries() {
		firstPrice = (Double) timeSeries.getValue(0);
		Iterator<HistoricalQuote> iter = records.iterator();
		while(iter.hasNext()) {
			HistoricalQuote itm = (HistoricalQuote)iter.next();
			updateIndexedRecord(itm);
		}
	}
	
	protected void resetSeries() {
		indexedTimeSeries.delete(0,  indexedTimeSeries.getItemCount() -1); 
		timeSeries.delete(0,  timeSeries.getItemCount() -1); 
		ohlcSeries.clear(); 
		updateSeries();
		updateIndexedSeries();
	}

	protected void updateRecord(HistoricalQuote itm) {
		Date tp = itm.getDate().getTime();
		RegularTimePeriod rtp = new Minute(tp);
		double close = (itm.getClose() == null) ? 0 : itm.getClose().doubleValue();
		if(tp.before(start) || tp.after(end)) {
			timeSeries.delete(rtp);
		} else {
			timeSeries.addOrUpdate(rtp, close);
		}		
	}
	
	protected void updateOHLCRecord(HistoricalQuote itm) {
		Date tp = itm.getDate().getTime();
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
	
	protected void updateIndexedRecord(HistoricalQuote itm) {
		Date tp = itm.getDate().getTime();
		RegularTimePeriod rtp = new Minute(tp);
		double close = (itm.getClose() == null) ? 0 : itm.getClose().doubleValue();
		if(tp.before(start) || tp.after(end)) {
			indexedTimeSeries.delete(rtp);
		} else {
			indexedTimeSeries.addOrUpdate(rtp, 100*close/firstPrice);
		}	
	}

	protected MeanAndStandardDeviation findTimeSeriesMeanStd() {
		List<TimeSeriesDataItem> itms = timeSeries.getItems();
		Iterator<TimeSeriesDataItem> iter = itms.iterator();
		Number[] vals = new Number[itms.size()];
		int cnt = 0;
		while(iter.hasNext()) {
			TimeSeriesDataItem itm = (TimeSeriesDataItem)iter.next();
			vals[cnt++] = itm.getValue();
		}
		if(vals.length>0) {
			double avg = Statistics.calculateMean(vals, true);
			double std = Statistics.getStdDev(vals);
			logger.sayOut("Mean/Std for " + getStockName() + ": " + avg + " / " + std);
			meanSd = new MeanAndStandardDeviation(avg, std);			
		} else {
			meanSd = new MeanAndStandardDeviation(0, 0);
		}
		return meanSd;
	}

	public int getRecordCount() {
		return records.size();
	}

	public String getStockName() {
		return stock.getName();
	}

	public MeanAndStandardDeviation getMeanStd() {
		if (meanSd == null) return findTimeSeriesMeanStd();
		return meanSd;
	}

	public double getMax() {
		return timeSeries.getMaxY();
	}

	public double getMin() {
		return timeSeries.getMinY();
	}

	public int getCount() {
		return timeSeries.getItemCount();
	}

	public void setStartEndDate(Date start, Date end) {
		this.start = start;
		this.end = end;
		//records = requestHistory(stock, start, end, interval);
		updateSeries();
		updateIndexedSeries();
		findTimeSeriesMeanStd();
	}
	
	public void setInterval(Interval interval) {
		this.interval = interval;
		setStartEndDate(start, end);
	}
	
	public void setStartDate(Date start) {
		setStartEndDate(start, end);
	}
	
	public void setEndDate(Date end) {
		setStartEndDate(start, end);
	}
	
	public void resetStartEndDate() {
		this.start = null;
		this.end = null;
		findStartEndDate();
		updateSeries();
		updateIndexedSeries();
		findTimeSeriesMeanStd();
	}

	private void findStartEndDate() {
		Iterator<HistoricalQuote> iter = records.iterator();
		while(iter.hasNext()) {
			HistoricalQuote itm = (HistoricalQuote)iter.next();
			Date itmDate = itm.getDate().getTime();
			if(start == null) start = itmDate;
			if(end == null) end = itmDate;
			if(itmDate.before(start)) start = itmDate;
			if(itmDate.after(end)) end = itmDate;
		}
	}

	public Date getStartDate() {
		if(getCount()==0) {
			return start;
		}
		return timeSeries.getTimePeriod(0).getStart();
	}

	public double getStartPrice() {
		return firstPrice;
	}

	public Date getEndDate() {
		if(getCount()==0) {
			return end;
		}
		return timeSeries.getTimePeriod(timeSeries.getItemCount() - 1).getStart();
	}

	public TimeSeries getTimeSeries() {
		if(showIndexedSeries ) {
			return indexedTimeSeries;		
		} else {
			return timeSeries;
		}
	}

	public OHLCSeries getOHLCSeries() {
		return ohlcSeries;
	}

	public BigDecimal getPricePaid() {
		return new BigDecimal(paidPrice);
	}

	public boolean getSeriesShow() {
		return showSeries;
	}

	public void setSeriesShow(boolean value) {
		showSeries = value;
	}

	public void setSeriesShowIndexed(boolean value) {
		showIndexedSeries = value;
	}

	

}
