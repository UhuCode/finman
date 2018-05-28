/**
 * 
 */
package ch.finman.misc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.finman.model.dao.AbstractDAO;
import ch.finman.sql.DatabaseController;
import ch.finman.util.LogUtil;
import yahoofinance.exchanges.ExchangeTimeZone;

/**
 * @author thomas
 *
 */
public class ExchangeDAO extends AbstractDAO<ExchangeValue> {
	
	public ExchangeDAO() {
		super("exchange", "id");
	}

	private static final LogUtil logger = LogUtil.getLogger(ExchangeDAO.class);
	
    public void create(ExchangeValue vo) {
        String query = String.format(
                "INSERT INTO exchange(short_name, full_name, timezone) SELECT '%s', '%s', '%s' WHERE NOT EXISTS (SELECT 1 FROM exchange WHERE short_name = '%s')",
                vo.getShortName(),
                vo.getFullName(),
                vo.getTimeZone().getDisplayName(),
                vo.getShortName()
        );

        try {
            DatabaseController.executeUpdateStatement(query);
        } catch (SQLException e) {
        	logger.sayErr("Unable to execute query: " + query);
            e.printStackTrace();
        }
    }

    public ExchangeValue findByShortName(String shortName) {
        String query = String.format("SELECT * FROM exchange WHERE short_name = '%s'", shortName);
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
	protected ExchangeValue map(ResultSet rs) throws SQLException {
        return new ExchangeValue(
                rs.getInt("id"),
                rs.getString("short_name"),
                rs.getString("full_name"),
                ExchangeTimeZone.get(rs.getString("timezone"))
        );
	}

   



}
