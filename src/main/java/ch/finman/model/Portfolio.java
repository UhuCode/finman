/**
 * 
 */
package ch.finman.model;

import java.sql.Timestamp;

/**
 * @author thomas
 *
 */
public class Portfolio {
	
	private int id;
	private String shortName;
	private String fullName;

	/**
	 * @param id
	 * @param shortName
	 * @param fullName
	 */
	public Portfolio(int id, String shortName, String fullName) {
		this.id = id;
		this.shortName = shortName;
		this.fullName = fullName;
	}

	/**
	 * @param other Portfolio
	 */
	public Portfolio(Portfolio other) {
		this.id = other.id;
		this.shortName = other.shortName;
		this.fullName = other.fullName;
	}

	/**
	 * 
	 */
	public Portfolio() {

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

}
