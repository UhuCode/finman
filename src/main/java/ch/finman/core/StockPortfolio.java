/**
 * 
 */
package ch.finman.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import ch.finman.util.LogUtil;

/**
 * @author thomas
 *
 */
public class StockPortfolio {

	private static final List<String> list = new ArrayList<String>();
	private Hashtable<String, ArrayList<StockItem>> portfolio;
	private static final LogUtil logger = LogUtil.getLogger(StockPortfolio.class);

	public StockPortfolio() {
		portfolio = new Hashtable<String, ArrayList<StockItem>>();
	}

	public void addStockItem(String portfolioName, StockItem stock) {
		if(this.portfolio.containsKey(portfolioName)) {
			ArrayList<StockItem> pfList = portfolio.get(portfolioName);
			if (pfList.contains(stock)) {
				int idx = pfList.indexOf(stock);
				logger.sayOut("StockItem exists in StockPortfolio. Replace object: " + stock.getStockSymbol());
				pfList.set(idx, stock);
			} else {
				pfList.add(stock);
				logger.sayOut("Adding StockItem to StockPortfolio: " + stock.getStockSymbol());
			}

		} else {
			ArrayList<StockItem> pfList = new ArrayList<StockItem>();
			pfList.add(stock);
			portfolio.put(portfolioName, pfList);
		}
	}

}
