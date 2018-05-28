/**
 * 
 */
package ch.finman.misc;

import java.sql.Timestamp;

/**
 * @author thomas
 *
 */
public class StockItemValue {
	
	private int id;
	private String symbol;
	private String shortName;
	private String fullName;
	private StockCategoryValue stockCategory;
	private CurrencyValue currency;
	private ExchangeValue exchange;
	private Timestamp lastModified;
	

	/**
	 * @param id
	 * @param symbol
	 * @param shortName
	 * @param fullName
	 * @param stockCategoryValue
	 * @param currencyValue
	 * @param exchangeValue
	 * @param timestamp
	 */
	public StockItemValue(int id, String symbol, String shortName, String fullName, StockCategoryValue stockCategoryValue, CurrencyValue currencyValue,
			ExchangeValue exchangeValue, Timestamp timestamp) {
		this.id = id;
		this.symbol = symbol;
		this.shortName = shortName;
		this.fullName = fullName;
		this.stockCategory = stockCategoryValue;
		this.currency = currencyValue;
		this.exchange = exchangeValue;
		this.lastModified = timestamp;
	}


	/**
	 * 
	 */
	public StockItemValue() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}


	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}


	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}


	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}


	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	/**
	 * @return the stockCategory
	 */
	public StockCategoryValue getStockCategory() {
		return stockCategory;
	}


	/**
	 * @param stockCategory the stockCategory to set
	 */
	public void setStockCategory(StockCategoryValue stockCategory) {
		this.stockCategory = stockCategory;
	}


	/**
	 * @return the currency
	 */
	public CurrencyValue getCurrency() {
		return currency;
	}


	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(CurrencyValue currency) {
		this.currency = currency;
	}


	/**
	 * @return the exchange
	 */
	public ExchangeValue getExchange() {
		return exchange;
	}


	/**
	 * @param exchange the exchange to set
	 */
	public void setExchange(ExchangeValue exchange) {
		this.exchange = exchange;
	}
	
	/**
	 * @return the lastModified
	 */
	public Timestamp getLastModified() {
		return lastModified;
	}
	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}
	
	public String toString() {
		return "StockItemValue: " + 
				"id: " + this.id + " - " + 
				"shortName: " + this.shortName + " - " + 
				"fullName: " + this.fullName + " - " + 
				"stockCategory: " + this.stockCategory.getNameEn() + " - " + 
				"currency: " + this.currency.getSymbol() + " - " + 
				"exchange: " + this.exchange.getShortName() + " - " + 
				"lastModified: " + this.lastModified; 
				
	}



}
