/**
 * 
 */
package ch.finman.misc;

import java.util.List;

/**
 * @author thomas
 *
 */
public class CurrencyValue {
	
	private int id;
	private String symbol;
	private String name;
	private List<StockItemValue> stockItems;

	/**
	 */
	public CurrencyValue() {
		
	}
	
	/**
	 * @param CurrencyValue
	 */
	public CurrencyValue(CurrencyValue other) {
		this.id = other.id;
		this.symbol = other.symbol;
		this.name = other.name;
	}
	
	/**
	 * @param id
	 * @param symbol
	 * @param name
	 */
	public CurrencyValue(int id, String symbol, String name) {
		this.id = id;
		this.symbol = symbol;
		this.name = name;
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
	 * @return the stockItems
	 */
	public List<StockItemValue> getStockItems() {
		return stockItems;
	}

	/**
	 * @param stockItems the stockItems to set
	 */
	public void setStockItems(List<StockItemValue> stockItems) {
		this.stockItems = stockItems;
	}

	public String toString() {
		return "CurrencyValue: " + 
				"id: " + this.id + " - " + 
				"symbol: " + this.symbol + " - " + 
				"name: " + this.name; 
	}


}
