/**
 * 
 */
package ch.finman.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author thomas
 *
 */
public class StockHistoryEntry {
	
	private int id;
	private StockEntry stockEntry;
	private Date historyDate;
	private BigDecimal open;
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal close;
	private long volume;
	private Timestamp lastModified;

	/**
	 * @param id
	 * @param stockEntry
	 * @param historyDate
	 * @param open
	 * @param high
	 * @param low
	 * @param close
	 * @param volume
	 * @param lastModified
	 */
	public StockHistoryEntry(int id, StockEntry stockEntry, Date historyDate, BigDecimal open, BigDecimal high,
			BigDecimal low, BigDecimal close, long volume, Timestamp lastModified) {
		this.id = id;
		this.stockEntry = stockEntry;
		this.historyDate = historyDate;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.lastModified = lastModified;
	}

	/**
	 * 
	 */
	public StockHistoryEntry() {
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
	 * @return the stockEntry
	 */
	public StockEntry getStockEntry() {
		return stockEntry;
	}

	/**
	 * @param stockEntry the stockEntry to set
	 */
	public void setStockEntry(StockEntry stockEntry) {
		this.stockEntry = stockEntry;
	}

	/**
	 * @return the historyDate
	 */
	public Date getHistoryDate() {
		return historyDate;
	}

	/**
	 * @param historyDate the historyDate to set
	 */
	public void setHistoryDate(Date historyDate) {
		this.historyDate = historyDate;
	}

	/**
	 * @return the open
	 */
	public BigDecimal getOpen() {
		return open;
	}

	/**
	 * @param open the open to set
	 */
	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	/**
	 * @return the high
	 */
	public BigDecimal getHigh() {
		return high;
	}

	/**
	 * @param high the high to set
	 */
	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	/**
	 * @return the low
	 */
	public BigDecimal getLow() {
		return low;
	}

	/**
	 * @param low the low to set
	 */
	public void setLow(BigDecimal low) {
		this.low = low;
	}

	/**
	 * @return the close
	 */
	public BigDecimal getClose() {
		return close;
	}

	/**
	 * @param close the close to set
	 */
	public void setClose(BigDecimal close) {
		this.close = close;
	}

	/**
	 * @return the volume
	 */
	public long getVolume() {
		return volume;
	}

	/**
	 * @param volume the volume to set
	 */
	public void setVolume(long volume) {
		this.volume = volume;
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

}
