/**
 * 
 */
package ch.finman.model.logic;

import java.math.BigDecimal;
import java.sql.Date;

import ch.finman.misc.CurrencyDAO;
import ch.finman.misc.CurrencyValue;
import ch.finman.misc.ExchangeDAO;
import ch.finman.misc.ExchangeValue;
import ch.finman.misc.StockCategoryDAO;
import ch.finman.misc.StockCategoryValue;
import ch.finman.misc.StockItemDAO;
import ch.finman.misc.StockItemValue;
import ch.finman.model.Asset;
import ch.finman.model.Portfolio;
import ch.finman.model.StockEntry;
import ch.finman.model.dao.AssetDAO;
import ch.finman.model.dao.PortfolioDAO;
import ch.finman.model.dao.StockDAO;
import ch.finman.util.LogUtil;
import yahoofinance.exchanges.ExchangeTimeZone;

/**
 * @author thomas
 *
 */
public class AssetManager {
	
	private static final LogUtil logger = LogUtil.getLogger(AssetManager.class);
	
	AssetDAO assetDAO;
	StockDAO stockDAO;
	PortfolioDAO portfolioDAO;

	/**
	 * 
	 */
	public AssetManager() {
		assetDAO = new AssetDAO();
		stockDAO = new StockDAO();
		portfolioDAO = new PortfolioDAO();
	}
	
	public void importAsset(String symbol, String portfolioShortName, long amount, BigDecimal price) {
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
		Portfolio portfolio;
		if(!portfolioDAO.exists(portfolioShortName)) {
			logger.sayErr("Portfolio not found in database: " + portfolioShortName);
			portfolio = new Portfolio();
			portfolio.setShortName(portfolioShortName);
			portfolio = portfolioDAO.create(portfolio);
			logger.sayOut("Portfolio created in database: " + portfolio);
		} else {
			portfolio = portfolioDAO.findByShortName(portfolioShortName);
			logger.sayOut("Portfolio found in database: " + portfolio);
		}
		
		addStockToPortfolio(stockItem, portfolio, new Date(System.currentTimeMillis()), amount, price);
		
	}
	
	public Asset addStockToPortfolio(StockEntry stock, Portfolio portfolio, Date transactionDate, long amount, BigDecimal price) {
		Asset vo = new Asset();
		vo.setStock(stock);
		vo.setPortfolio(portfolio);
		vo.setTransactionDate(transactionDate);
		vo.setAmount(amount);
		vo.setPrice(price);
		vo = assetDAO.create(vo);
		return vo;
	}


}
