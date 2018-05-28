/**
 * 
 */
package ch.finman.misc;

import ch.finman.core.StockItem;
import ch.finman.util.LogUtil;
import yahoofinance.Stock;
import yahoofinance.exchanges.ExchangeTimeZone;

/**
 * @author thomas
 *
 */
public class StockItemManager {
	
	private static final LogUtil logger = LogUtil.getLogger(StockItemManager.class);
	
	StockItemDAO stockItemDAO;
	StockCategoryDAO stockCategoryDAO;
	CurrencyDAO currencyDAO;
	ExchangeDAO exchangeDAO;

	/**
	 * 
	 */
	public StockItemManager() {
		stockItemDAO = new StockItemDAO();
		stockCategoryDAO = new StockCategoryDAO();
		currencyDAO = new CurrencyDAO();
		exchangeDAO = new ExchangeDAO();
	}
	
	public StockItemValue createStockItem(Stock stock) {
		StockCategoryValue stockCategoryValue = findStockCategory(stock.getSymbol());
		CurrencyValue currencyValue = findCurrency(stock.getCurrency());
		ExchangeValue exchangeValue = getExchange(stock);
		
		StockItemValue vo = new StockItemValue();
		vo.setSymbol(stock.getSymbol());
		vo.setShortName(stock.getSymbol());
		vo.setFullName(stock.getName());
		vo.setStockCategory(stockCategoryValue);
		vo.setCurrency(currencyValue);
		vo.setExchange(exchangeValue);
		stockItemDAO.create(vo);
		return findStockItem(stock.getSymbol());
	}

	private StockItemValue findStockItem(String symbol) {
		return stockItemDAO.findBySymbol(symbol);
	}

	private ExchangeValue findExchange(Stock stock) {
		ExchangeValue vo = exchangeDAO.findByShortName(stock.getStockExchange());
		return vo;
	}

	private ExchangeValue createExchange(Stock stock) {
			ExchangeValue vo = new ExchangeValue();
			vo.setShortName(stock.getStockExchange());
			vo.setFullName(stock.getStockExchange());
			vo.setTimeZone(ExchangeTimeZone.get(stock.getSymbol()));
			exchangeDAO.create(vo);
			return findExchange(stock);
	}

	private ExchangeValue getExchange(Stock stock) {
		ExchangeValue vo = findExchange(stock);
		if (vo == null) {
			vo = createExchange(stock);
		}
		return vo;
	}

	private CurrencyValue findCurrency(String currency) {
		CurrencyValue vo = currencyDAO.findBySymbol(currency);
		return vo;
	}

	private StockCategoryValue findStockCategory(String stockSymbol) {
		StockCategoryValue vo = stockCategoryDAO.findByPk(1);
		return vo;
	}

}
