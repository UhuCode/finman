/**
 * 
 */
package ch.finman.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.finman.model.Asset;
import ch.finman.model.Portfolio;
import ch.finman.model.StockEntry;
import ch.finman.util.LogUtil;

/**
 * @author thomas
 *
 */
public class AssetDAO extends AbstractDAO<Asset> {

	private static final LogUtil logger = LogUtil.getLogger(AssetDAO.class);
	
	private StockDAO stockDAO;
	private PortfolioDAO portfolioDAO;
	
	public AssetDAO() {
		super("asset", "id");
		stockDAO = new StockDAO();
		portfolioDAO = new PortfolioDAO();
	}

    public Asset create(Asset vo) {
        String query = String.format(
                "INSERT INTO " + table + "(stock_id, portfolio_id, transaction_date, amount, price) SELECT '%s', '%s', '%s', '%s', '%s' ",
                vo.getStock().getId(),
                vo.getPortfolio().getId(),
                vo.getTransactionDate(),
                vo.getAmount(),
                vo.getPrice()
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

    public List<Asset> findByStock(StockEntry stock) {
    	return findByStock(stock.getId());
    }

    public List<Asset> findByStock(int stockId) {
        String query = String.format("SELECT * FROM " + table + " WHERE stock_id = '%s'", stockId);
        try {
            ResultSet resultSet = executeQueryStatement(query);
			List<Asset> list = new ArrayList<Asset>();
			while (resultSet.next()) {
				list.add(map(resultSet));
			}
			return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Asset> findByPortfolio(Portfolio portfolio) {
        return findByPortfolio(portfolio.getId());
    }

    public List<Asset> findByPortfolio(int portfolioId) {
        String query = String.format("SELECT * FROM " + table + " WHERE portfolio_id = '%s'", portfolioId);
        try {
            ResultSet resultSet = executeQueryStatement(query);
			List<Asset> list = new ArrayList<Asset>();
			while (resultSet.next()) {
				list.add(map(resultSet));
			}
			return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

	@Override
	protected Asset map(ResultSet rs) throws SQLException {
        return new Asset(
                rs.getInt("id"),
                stockDAO.findByPk(rs.getInt("stock_id")),
                portfolioDAO.findByPk(rs.getInt("portfolio_id")),
                rs.getDate("transaction_date"),
                rs.getLong("amount"),
                rs.getBigDecimal("price")
        );
	}

}
