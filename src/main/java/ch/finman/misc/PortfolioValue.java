/**
 * 
 */
package ch.finman.misc;

import java.sql.Timestamp;
import java.util.TimeZone;

/**
 * @author thomas
 *
 */
public class PortfolioValue {
	
	private int id;
	private String shortName;
	private String fullName;
	private Timestamp lastModified;

	public PortfolioValue() {
		
	}
	
	public PortfolioValue(PortfolioValue other) {
		this.id = other.id;
		this.shortName = other.shortName;
		this.fullName = other.fullName;
		this.lastModified = other.lastModified;
	}
	
	/**
	 * @param id
	 * @param shortName
	 * @param fullName
	 * @param timeZone
	 */
	public PortfolioValue(int id, String shortName, String fullName, Timestamp lastModified) {
		this.id = id;
		this.shortName = shortName;
		this.fullName = fullName;
		this.lastModified = lastModified;
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
		return "PortfolioValue: " + 
				"id: " + this.id + " - " + 
				"shortName: " + this.shortName + " - " + 
				"fullName: " + this.fullName + " - " + 
				"lastModified: " + this.lastModified; 
				
	}

}
