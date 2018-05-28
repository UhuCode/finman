/**
 * 
 */
package ch.finman.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import ch.finman.misc.CurrencyValue;
import ch.finman.misc.PortfolioValue;
import ch.finman.misc.StockCategoryValue;
import ch.finman.misc.StockItemValue;
import ch.finman.model.StockEntry;
import ch.finman.sql.DatabaseController;
import ch.finman.util.LogUtil;

/**
 * @author thomas
 *
 */
public class StockDAO extends AbstractDAO<StockEntry> {
	
	private static final LogUtil logger = LogUtil.getLogger(StockDAO.class);
	
	public StockDAO() {
		super("stock", "id");
	}

    public StockEntry create(StockEntry vo) {
        String query = String.format(
                "INSERT INTO " + table + "(symbol, name, currency, exchange) SELECT '%s', '%s', '%s', '%s' WHERE NOT EXISTS (SELECT 1 FROM " + table + " WHERE symbol = '%s')",
                vo.getSymbol(),
                vo.getName(),
                vo.getCurrency(),
                vo.getExchange(),
                vo.getSymbol()
        );

        try {
            int id = (int) executeInsertStatement(query);
            vo.setId(id);
        } catch (SQLException e) {
        	logger.sayErr("Unable to execute query: " + query);
            e.printStackTrace();
        }
        return vo;
    }

    public int update(StockEntry vo) {
        String query = String.format(
                "UPDATE " + table + " SET symbol = '%s', name = '%s', currency = '%s', exchange = '%s' WHERE id = '%s'",
                vo.getSymbol(),
                vo.getName(),
                vo.getCurrency(),
                vo.getExchange(),
                vo.getId()
        );

        try {
            int cnt = executeUpdateStatement(query);
            return cnt;
        } catch (SQLException e) {
        	logger.sayErr("Unable to execute query: " + query);
            e.printStackTrace();
        }
        return 0;
    }

    public StockEntry findBySymbol(String symbol) {
        String query = String.format("SELECT * FROM " + table + " WHERE symbol = '%s'", symbol);
        try {
            ResultSet resultSet = executeQueryStatement(query);
            if (resultSet.next()) {
                return map(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean exists(String symbol) {
    	StockEntry s = findBySymbol(symbol);
    	return (s == null) ? false : true;
    }

	@Override
	protected StockEntry map(ResultSet rs) throws SQLException {
        return new StockEntry(
                rs.getInt("id"),
                rs.getString("symbol"),
                rs.getString("name"),
                rs.getString("currency"),
                rs.getString("exchange")
        );
	}

}
