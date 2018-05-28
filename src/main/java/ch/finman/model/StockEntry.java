/**
 * 
 */
package ch.finman.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import ch.finman.core.StockItem;

/**
 * @author thomas
 *
 */
public class StockEntry {
	
	private int id;
	private String symbol;
	private String name;
	private String currency;
	private String exchange;
	

	/**
	 * @param id
	 * @param symbol
	 * @param shortName
	 * @param fullName
	 * @param currency
	 * @param exchange
	 */
	public StockEntry(int id, String symbol, String name, String currency, String exchange) {
		this.id = id;
		this.symbol = symbol;
		this.name = name;
		this.currency = currency;
		this.exchange = exchange;
	}


	/**
	 * 
	 */
	public StockEntry() {
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}


	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}


	/**
	 * @return the exchange
	 */
	public String getExchange() {
		return exchange;
	}


	/**
	 * @param exchange the exchange to set
	 */
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	
	public String toString() {
		return "Stock: " + 
				"id: " + this.id + " - " + 
				"symbol: " + this.symbol + " - " + 
				"name: " + this.name + " - " + 
				"currency: " + this.currency + " - " + 
				"exchange: " + this.exchange; 				
	}



}
