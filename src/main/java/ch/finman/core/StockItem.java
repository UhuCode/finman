package ch.finman.core;

import java.math.BigDecimal;

public class StockItem {
	
	private boolean show = true;
	private String symbol = "";
	private BigDecimal pricePaid = new BigDecimal(0);
	
	public StockItem(String symbol, BigDecimal pricePaid) {
		this.symbol = symbol;
		this.pricePaid = pricePaid;
	}

	public String getStockSymbol() {
		return symbol;
	}
	
	public BigDecimal getPricePaid() {
		return pricePaid;
	}

	public boolean getShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;	
	}

}
