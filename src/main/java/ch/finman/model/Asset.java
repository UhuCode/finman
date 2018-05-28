/**
 * 
 */
package ch.finman.model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author thomas
 *
 */
public class Asset {
	private int id;
	private StockEntry stock;
	private Portfolio portfolio;
	private Date transactionDate;
	private long amount;
	private BigDecimal price;

	
	/**
	 */
	public Asset() {
	}
	
	/**
	 * @param id
	 * @param stock
	 * @param portfolio
	 * @param transactionDate
	 * @param amount
	 * @param price
	 */
	public Asset(int id, StockEntry stock, Portfolio portfolio, Date transactionDate, long amount, BigDecimal price) {
		this.id = id;
		this.stock = stock;
		this.portfolio = portfolio;
		this.transactionDate = transactionDate;
		this.amount = amount;
		this.price = price;
	}
	
	/**
	 * @param other Asset
	 */
	public Asset(Asset other) {
		this.id = other.id;
		this.stock = other.stock;
		this.portfolio = other.portfolio;
		this.transactionDate = other.transactionDate;
		this.amount = other.amount;
		this.price = other.price;
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
	 * @return the stock
	 */
	public StockEntry getStock() {
		return stock;
	}
	/**
	 * @param stock the stock to set
	 */
	public void setStock(StockEntry stock) {
		this.stock = stock;
	}
	/**
	 * @return the portfolio
	 */
	public Portfolio getPortfolio() {
		return portfolio;
	}
	/**
	 * @param portfolio the portfolio to set
	 */
	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}
	/**
	 * @return the transactionDate
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}
	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	/**
	 * @return the amount
	 */
	public long getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(long amount) {
		this.amount = amount;
	}
	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	/**
	 * @return the sum
	 */
	public BigDecimal getTotal() {
		return getPrice().multiply(BigDecimal.valueOf(getAmount()));
	}
	
	public String toString() {
		return "Asset: " + 
				"id: " + this.id + " - " + 
				"stock: " + this.stock + " - " + 
				"portfolio: " + this.portfolio + " - " + 
				"transactionDate: " + this.transactionDate + " - " + 
				"amount: " + this.amount + " - " + 
				"price: " + this.price; 				
	}



}
