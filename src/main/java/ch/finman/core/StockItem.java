package ch.finman.core;

import java.math.BigDecimal;

public class StockItem {
	
	private boolean show = true;
	private String symbol;
	private BigDecimal pricePaid;
	private int stockAmount;
	private String portfolio;
	
	public StockItem(String symbol) {
		this(symbol, new BigDecimal(0), 0);
	}

	public StockItem(String symbol, BigDecimal pricePaid) {
		this(symbol, pricePaid, 0);
	}

	public StockItem(String symbol, BigDecimal pricePaid, int stockAmount) {
		this.symbol = symbol;
		this.pricePaid = pricePaid;
		this.stockAmount = stockAmount;
	}

	public StockItem(String symbol, BigDecimal pricePaid, int stockAmount, String portfolio) {
		this.symbol = symbol;
		this.pricePaid = pricePaid;
		this.stockAmount = stockAmount;
		this.portfolio = portfolio;
	}

	public String getStockSymbol() {
		return symbol;
	}
	
	public BigDecimal getPricePaid() {
		return pricePaid;
	}

	public int getStockAmount() {
		return stockAmount;
	}

	public BigDecimal getPaidValue() {
		return pricePaid.multiply(new BigDecimal(stockAmount));
	}

	public boolean getShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;	
	}
	
	public static StockItem create(String symbol) {
		return new StockItem(symbol);
	}
	public static StockItem create(String symbol, BigDecimal pricePaid) {
		return new StockItem(symbol, pricePaid);
	}
	public static StockItem create(String symbol, BigDecimal pricePaid, int stockAmount) {
		return new StockItem(symbol, pricePaid, stockAmount);
	}

	public static StockItem create(String portfolio, String[] elements) {
		if (elements.length == 0) return null;
		
		String symbol = "";
		BigDecimal price = BigDecimal.ZERO;
		int count = 0;
		
		if (elements.length >= 1) {
			symbol = elements[0];
		}
		if (elements.length >= 2) {
			price = new BigDecimal(elements[1]);
		}
		if (elements.length >= 3) {
			count = Integer.parseInt(elements[2]);
		}
		return new StockItem(symbol, price, count, portfolio);
	}

}
