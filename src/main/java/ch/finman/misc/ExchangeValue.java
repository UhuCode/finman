/**
 * 
 */
package ch.finman.misc;

import java.util.TimeZone;

/**
 * @author thomas
 *
 */
public class ExchangeValue {
	
	private int id;
	private String shortName;
	private String fullName;
	private TimeZone timeZone;

	public ExchangeValue() {
		
	}
	
	public ExchangeValue(ExchangeValue other) {
		this.id = other.id;
		this.shortName = other.shortName;
		this.fullName = other.fullName;
		this.timeZone = other.timeZone;
	}
	
	/**
	 * @param id
	 * @param shortName
	 * @param fullName
	 * @param timeZone
	 */
	public ExchangeValue(int id, String shortName, String fullName, TimeZone timeZone) {
		this.id = id;
		this.shortName = shortName;
		this.fullName = fullName;
		this.timeZone = timeZone;
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
	 * @return the timeZone
	 */
	public TimeZone getTimeZone() {
		return timeZone;
	}
	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	
	public String toString() {
		return "ExchangeValue: " + 
				"id: " + this.id + " - " + 
				"shortName: " + this.shortName + " - " + 
				"fullName: " + this.fullName + " - " + 
				"timeZone: " + this.timeZone; 
				
	}

}
