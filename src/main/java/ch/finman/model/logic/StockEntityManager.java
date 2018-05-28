/**
 * 
 */
package ch.finman.model.logic;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import ch.finman.misc.CurrencyValue;
import ch.finman.misc.ExchangeValue;
import ch.finman.misc.StockCategoryValue;
import ch.finman.misc.StockItemValue;
import ch.finman.model.Portfolio;
import ch.finman.model.StockEntry;
import ch.finman.model.StockHistoryEntry;
import ch.finman.model.dao.StockDAO;
import ch.finman.model.dao.StockHistoryDAO;
import ch.finman.util.LogUtil;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * @author thomas
 *
 */
public class StockEntityManager {

	private static final LogUtil logger = LogUtil.getLogger(StockEntityManager.class);
	
	private StockDAO stockDAO;
	private StockHistoryDAO stockHistoryDAO;
	private YahooManager yahooManager;
	
	
	/**
	 * 
	 */
	public StockEntityManager() {
		stockDAO = new StockDAO();
		stockHistoryDAO = new StockHistoryDAO();
		yahooManager = new YahooManager();

	}
	
	public List<StockEntry> getStockEntries() {
		return stockDAO.findAll();
	}
	
	public StockEntry getStockEntry(String symbol) {
		return stockDAO.findBySymbol(symbol);
	}
	
	public Stock getStock(String symbol) {
		try {
			return yahooManager.getStock(symbol);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<StockHistoryEntry> getStockHistoryEntries(StockEntry stockEntry) {
		return stockHistoryDAO.findByStock(stockEntry);
	}
	
	public void importStock(String symbol) {
		StockEntry stockItem;
		if(!stockDAO.exists(symbol)) {
			logger.sayErr("Stock Symbol not found in database: " + symbol);
			stockItem = new StockEntry();
			stockItem.setSymbol(symbol);
			stockItem = stockDAO.create(stockItem);
			logger.sayOut("Stock Item created in database: " + stockItem);
		} else {
			stockItem = stockDAO.findBySymbol(symbol);
			logger.sayOut("Stock Item found in database: " + stockItem);
		}
	}
	
	public void updateStock(String symbol) {
		if (symbol == null || symbol.isEmpty()) {
			return;
		}
		StockEntry stockEntry = stockDAO.findBySymbol(symbol);
		if (stockEntry == null) {
			stockEntry = new StockEntry();
			stockEntry.setSymbol(symbol);
			stockEntry.setName("");
			stockEntry.setCurrency("");
			stockEntry.setExchange("");
			stockEntry = stockDAO.create(stockEntry);
		}
		try {
			Stock stock = yahooManager.requestStockData(symbol);
			if (stock == null) {
				logger.sayErr("Stock data from Yahoo is empty for " + symbol);
				return;
			}
			stockEntry.setName(stock.getName());
			stockEntry.setCurrency(stock.getCurrency());
			stockEntry.setExchange(stock.getStockExchange());
			
			int cnt = stockDAO.update(stockEntry);
			if (cnt==0) {
				logger.sayErr("Couldn't update: " + stockEntry);
				return;
			}
			logger.sayOut("Stock updated: " + stockEntry);
			
			List<HistoricalQuote> list = yahooManager.requestHistory(stock);
			
			for(HistoricalQuote hq : list) {
				StockHistoryEntry he = new StockHistoryEntry();
				he.setStockEntry(stockEntry);
				he.setHistoryDate(new Date(hq.getDate().getTimeInMillis()));
				he.setOpen(hq.getOpen());
				he.setHigh(hq.getHigh());
				he.setLow(hq.getLow());
				he.setClose(hq.getClose());
				he.setVolume(hq.getVolume());
				updateStockHistory(he);
			}
			
		} catch (IOException e) {
			logger.sayErr("Error requesting Stock Data from Yahoo: " + e.getMessage());
		}
	}
	
	public void updateStockHistory(StockHistoryEntry stockHistoryEntry) {
		if (stockHistoryEntry == null) {
			return;
		}
		StockHistoryEntry she = stockHistoryDAO.findByHistoryDate(stockHistoryEntry);
		if(she == null) {
			she = stockHistoryDAO.create(stockHistoryEntry);
		}
		
		int cnt = stockHistoryDAO.update(she);
		if (cnt==0) {
			logger.sayErr("Couldn't update: " + she);
			return;
		}
		logger.sayOut("Stock History updated: " + she);
	}

}
