/**
 * 
 */
package ch.finman.misc;

/**
 * @author thomas
 *
 */
public class StockCategoryValue {
	
	private int id;
	private String nameEn;
	private String nameDe;

	/**
	 */
	public StockCategoryValue() {
		
	}
	
	/**
	 * @param StockCategoryValue
	 */
	public StockCategoryValue(StockCategoryValue other) {
		this.id = other.id;
		this.nameEn = other.nameEn;
		this.nameDe = other.nameDe;
	}
	
	/**
	 * @param id
	 * @param nameEn
	 * @param nameDe
	 */
	public StockCategoryValue(int id, String nameEn, String nameDe) {
		this.id = id;
		this.nameEn = nameEn;
		this.nameDe = nameDe;
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
	 * @return the nameEn
	 */
	public String getNameEn() {
		return nameEn;
	}

	/**
	 * @param nameEn the nameEn to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	/**
	 * @return the nameDe
	 */
	public String getNameDe() {
		return nameDe;
	}

	/**
	 * @param nameDe the nameDe to set
	 */
	public void setNameDe(String nameDe) {
		this.nameDe = nameDe;
	}

	public String toString() {
		return "StockCategoryValue: " + 
				"id: " + this.id + " - " + 
				"nameEn: " + this.nameEn + " - " + 
				"nameDe: " + this.nameDe; 
	}


}
