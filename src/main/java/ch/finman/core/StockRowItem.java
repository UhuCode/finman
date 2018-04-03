package ch.finman.core;

import java.util.List;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.ohlc.OHLCSeries;

import yahoofinance.Stock;

public class StockRowItem {
	
	Stock stock;
	Stock stockIndex;
	
	StockItem stockItem;
	StockItem stockIndexItem;
	
	List<?> stockHistoryList;
	List<?> stockIndexHistoryList;
	
	double indexStartPrice;
	double stockStartPrice;
	
	private TimeSeries timeSeries = null;
	private OHLCSeries ohlcSeries;
	
	public StockRowItem(Stock stock, StockItem stockItem) {
		this.stock = stock;
		this.stockItem = stockItem;
	}

	public StockRowItem(Stock stock, StockItem stockItem, Stock stockIndex, StockItem stockIndexItem) {
		this.stock = stock;
		this.stockItem = stockItem;
		this.stockIndex = stockIndex;
		this.stockIndexItem = stockIndexItem;
	}

	public Stock getStock() {
		return stock;
	}
	public StockItem getStockItem() {
		return stockItem;
	}

	public void setStartPrice(double val) {
		this.stockStartPrice = val;	
	}
	
	public double getStartPrice() {
		return stockStartPrice;	
	}

	public void setTimeSeries(TimeSeries timeSeries) {
		this.timeSeries = timeSeries;	
	}
	
	public TimeSeries getTimeSeries() {
		return timeSeries;	
	}

	public void setStockHistoryList(List<?> stockHistoryList) {
		this.stockHistoryList = stockHistoryList;
	}

	public void setOHLCSeries(OHLCSeries ohlcSeries) {
		this.ohlcSeries = ohlcSeries;
	}

	public OHLCSeries getOHLCSeries() {
		return ohlcSeries;
	}

}
