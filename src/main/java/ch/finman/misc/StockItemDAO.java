/**
 * 
 */
package ch.finman.misc;

import java.sql.ResultSet;
import java.sql.SQLException;

import ch.finman.model.dao.AbstractDAO;
import ch.finman.sql.DatabaseController;
import ch.finman.util.LogUtil;

/**
 * @author thomas
 *
 */
public class StockItemDAO extends AbstractDAO<StockItemValue> {
	
	private static final LogUtil logger = LogUtil.getLogger(StockItemDAO.class);
	
	StockCategoryDAO stockCategoryDAO;
	CurrencyDAO currencyDAO;
	ExchangeDAO exchangeDAO;

	public StockItemDAO() {
		super("stockitem", "id");
		stockCategoryDAO = new StockCategoryDAO();
		currencyDAO = new CurrencyDAO();
		exchangeDAO = new ExchangeDAO();
	}

    public void create(StockItemValue vo) {
        String query = String.format(
                "INSERT INTO " + table + "(symbol, short_name, full_name, stockcategory_id, currency_id, exchange_id) SELECT '%s', '%s', '%s', '%s', '%s', '%s' WHERE NOT EXISTS (SELECT 1 FROM " + table + " WHERE symbol = '%s')",
                vo.getSymbol(),
                vo.getShortName(),
                vo.getFullName(),
                vo.getStockCategory().getId(),
                vo.getCurrency().getId(),
                vo.getExchange().getId(),
                vo.getSymbol()
        );

        try {
            DatabaseController.executeUpdateStatement(query);
        } catch (SQLException e) {
        	logger.sayErr("Unable to execute query: " + query);
            e.printStackTrace();
        }
    }

    public StockItemValue findBySymbol(String symbol) {
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
	protected StockItemValue map(ResultSet rs) throws SQLException {
        return new StockItemValue(
                rs.getInt("id"),
                rs.getString("symbol"),
                rs.getString("short_name"),
                rs.getString("full_name"),
                stockCategoryDAO.findByPk(rs.getInt("stockcategory_id")),
                currencyDAO.findByPk(rs.getInt("currency_id")),
                exchangeDAO.findByPk(rs.getInt("exchange_id")),
                rs.getTimestamp("last_modified")
        );
	}

}
