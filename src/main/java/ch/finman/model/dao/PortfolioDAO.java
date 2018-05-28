/**
 * 
 */
package ch.finman.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.finman.misc.ExchangeValue;
import ch.finman.misc.PortfolioValue;
import ch.finman.model.Portfolio;
import ch.finman.sql.DatabaseController;
import ch.finman.util.LogUtil;
import yahoofinance.exchanges.ExchangeTimeZone;

/**
 * @author thomas
 *
 */
public class PortfolioDAO extends AbstractDAO<Portfolio> {

	private static final LogUtil logger = LogUtil.getLogger(PortfolioDAO.class);
	
	public PortfolioDAO() {
		super("portfolio", "id");
	}

	
    public Portfolio create(Portfolio vo) {
        String query = String.format(
                "INSERT INTO " + table + "(short_name, full_name) SELECT '%s', '%s' WHERE NOT EXISTS (SELECT 1 FROM " + table + " WHERE short_name = '%s')",
                vo.getShortName(),
                vo.getFullName(),
                vo.getShortName()
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

    public Portfolio findByShortName(String shortName) {
        String query = String.format("SELECT * FROM " + table + " WHERE short_name = '%s'", shortName);
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
	protected Portfolio map(ResultSet rs) throws SQLException {
        return new Portfolio(
                rs.getInt("id"),
                rs.getString("short_name"),
                rs.getString("full_name")
        );
	}


	public boolean exists(String shortName) {
		Portfolio p = findByShortName(shortName);
		return (p == null) ? false : true;
	}

}
