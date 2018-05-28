/**
 * 
 */
package ch.finman.misc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import ch.finman.model.dao.AbstractDAO;
import ch.finman.sql.DatabaseController;
import ch.finman.util.LogUtil;

/**
 * @author thomas
 *
 */
public class StockCategoryDAO extends AbstractDAO<StockCategoryValue> {

	private static final LogUtil logger = LogUtil.getLogger(StockCategoryDAO.class);
	
	public StockCategoryDAO() {
		super("stockcategory", "id");
	}

    public void create(StockCategoryValue vo) {
        String query = String.format(
                "INSERT INTO " + table + "(name_en, name_de) SELECT '%s', '%s' WHERE NOT EXISTS (SELECT 1 FROM " + table + " WHERE name_en = '%s')",
                vo.getNameEn(),
                vo.getNameDe(),
                vo.getNameEn()
        );

        try {
            DatabaseController.executeUpdateStatement(query);
        } catch (SQLException e) {
        	logger.sayErr("Unable to execute query: " + query);
            e.printStackTrace();
        }
    }

    public StockCategoryValue findByName(String name, Locale locale) {
    	String colName = "name_en";
    	if (locale != null && locale == Locale.GERMAN) {
    		colName = "name_de";
    	}
        String query = String.format("SELECT * FROM " + table + " WHERE " + colName + " = '%s'", name);
        try {
            ResultSet resultSet = DatabaseController.executeQueryStatement(query);
            if (resultSet.next()) {
                return map(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

	@Override
	protected StockCategoryValue map(ResultSet rs) throws SQLException {
        return new StockCategoryValue(
                rs.getInt("id"),
                rs.getString("name_en"),
                rs.getString("name_de"));
	}

}
