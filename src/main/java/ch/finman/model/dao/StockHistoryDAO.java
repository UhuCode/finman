/**
 * 
 */
package ch.finman.model.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.finman.model.Asset;
import ch.finman.model.StockEntry;
import ch.finman.model.StockHistoryEntry;
import ch.finman.util.LogUtil;

/**
 * @author thomas
 *
 */
public class StockHistoryDAO extends AbstractDAO<StockHistoryEntry> {

	private static final LogUtil logger = LogUtil.getLogger(StockHistoryDAO.class);
	
	private StockDAO stockDAO;
	
	public StockHistoryDAO() {
		super("stockhistory", "id");
		stockDAO = new StockDAO();
	}

    public StockHistoryEntry create(StockHistoryEntry vo) {
        String query = String.format(
                "INSERT INTO " + table + "(stock_id, history_date, open, high, low, close, volume) SELECT '%s', '%s', '%s', '%s', '%s', '%s', '%s' WHERE NOT EXISTS (SELECT 1 FROM " + table + " WHERE stock_id = '%s' AND history_date = '%s')",
                vo.getStockEntry().getId(),
                vo.getHistoryDate(),
                vo.getOpen(),
                vo.getHigh(),
                vo.getLow(),
                vo.getClose(),
                vo.getVolume(),
                vo.getStockEntry().getId(),
                vo.getHistoryDate()
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

    public int update(StockHistoryEntry vo) {
        String query = String.format(
                "UPDATE " + table + " SET open = '%s', high = '%s', low = '%s', close = '%s', volume = '%s' WHERE stock_id = '%s' AND history_date = '%s'",
                vo.getOpen(),
                vo.getHigh(),
                vo.getLow(),
                vo.getClose(),
                vo.getVolume(),
                vo.getStockEntry().getId(),
                vo.getHistoryDate()
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

    public List<StockHistoryEntry> findByStock(StockEntry stock) {
    	return findByStock(stock.getId());
    }

    public List<StockHistoryEntry> findByStock(int stockId) {
        String query = String.format("SELECT * FROM " + table + " WHERE stock_id = '%s'", stockId);
        try {
            ResultSet resultSet = executeQueryStatement(query);
			List<StockHistoryEntry> list = new ArrayList<StockHistoryEntry>();
			while (resultSet.next()) {
				list.add(map(resultSet));
			}
			return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public StockHistoryEntry findByHistoryDate(StockHistoryEntry stock) {
    	return findByHistoryDate(stock.getStockEntry().getId(), stock.getHistoryDate());
    }

    public StockHistoryEntry findByHistoryDate(int stockId, Date historyDate) {
        String query = String.format("SELECT * FROM " + table + " WHERE stock_id = '%s' AND history_date = '%s'", stockId, historyDate);
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

	@Override
	protected StockHistoryEntry map(ResultSet rs) throws SQLException {
        return new StockHistoryEntry(
                rs.getInt("id"),
                stockDAO.findByPk(rs.getInt("stock_id")),
                rs.getDate("history_date"),
                rs.getBigDecimal("open"),
                rs.getBigDecimal("high"),
                rs.getBigDecimal("low"),
                rs.getBigDecimal("close"),
                rs.getLong("volume"),
                rs.getTimestamp("last_modified")
        );
	}

}
