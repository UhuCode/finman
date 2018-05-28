/**
 * 
 */
package ch.finman.misc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ch.finman.model.dao.AbstractDAO;
import ch.finman.sql.DatabaseController;
import ch.finman.util.LogUtil;

/**
 * @author thomas
 *
 */
public class CurrencyDAO extends AbstractDAO<CurrencyValue> {

	private static final LogUtil logger = LogUtil.getLogger(CurrencyDAO.class);
	
		public CurrencyDAO() {
			super("currency", "id");
		}

	    public void create(CurrencyValue vo) {
	        String query = String.format(
	                "INSERT INTO " + table + "(symbol, name) SELECT '%s', '%s' WHERE NOT EXISTS (SELECT 1 FROM " + table + " WHERE symbol = '%s')",
	                vo.getSymbol(),
	                vo.getName(),
	                vo.getSymbol()
	        );

	        try {
	            DatabaseController.executeUpdateStatement(query);
	        } catch (SQLException e) {
	        	logger.sayErr("Unable to execute query: " + query);
	            e.printStackTrace();
	        }
	    }

	    public CurrencyValue findBySymbol(String symbol) {
	        String query = String.format("SELECT * FROM " + table + " WHERE symbol = '%s'", symbol);
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
		protected CurrencyValue map(ResultSet rs) throws SQLException {
            return new CurrencyValue(
                    rs.getInt("id"),
                    rs.getString("symbol"),
                    rs.getString("name"));
		}






}
