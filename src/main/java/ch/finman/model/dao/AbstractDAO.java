/**
 * 
 */
package ch.finman.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import ch.finman.misc.CurrencyValue;
import ch.finman.misc.ExchangeValue;
import ch.finman.sql.DatabaseController;
import ch.finman.util.LogUtil;
import yahoofinance.exchanges.ExchangeTimeZone;

/**
 * @author thomas
 *
 */
public abstract class AbstractDAO<T> {

	protected final String table;
	protected final String pkCol;
	protected String findByPkSql;
	protected PreparedStatement findByPkPs;
	protected String findAllSql;
	protected PreparedStatement findAllPs;

	protected AbstractDAO(String table, String pkCol) {
		this.table = table;
		this.pkCol = pkCol;
		findByPkSql = "SELECT * FROM " + table + " WHERE " + pkCol + " = ?";
		findAllSql = "SELECT * FROM " + table;
	}

	public T findByPk(int id) {
		try {
			Connection con = DatabaseController.getConnection();
			findByPkPs = con.prepareStatement(findByPkSql);
			findByPkPs.setInt(1, id);
			ResultSet rs = findByPkPs.executeQuery();
            if (rs.next()) {
            	return map(rs);
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public abstract T create(T vo);

	public List<T> findAll() {
		try {
			Connection con = DatabaseController.getConnection();
			findAllPs = con.prepareStatement(findAllSql);
			ResultSet rs = findAllPs.executeQuery();
			List<T> list = new ArrayList<T>();
			while (rs.next()) {
				list.add(map(rs));
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

    public synchronized static int executeUpdateStatement(String query) throws SQLException {
        Connection connection = DatabaseController.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        int result = preparedStatement.executeUpdate();
        connection.commit();
        return result;
    }

    public synchronized static long executeInsertStatement(String query) throws SQLException {
        Connection connection = DatabaseController.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        int result = preparedStatement.executeUpdate();
        connection.commit();
        if (result == 0) {
            throw new SQLException("Insert query failed, no rows affected.");
        }
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
        	return generatedKeys.getLong(1);
        } else {
        	throw new SQLException("Insert query failed, no ID obtained.");
        } 
    }

    public synchronized static ResultSet executeQueryStatement(String query) throws SQLException {
        Connection connection = DatabaseController.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        connection.commit();
        return resultSet;
    }

	protected abstract T map(ResultSet rs) throws SQLException;



}
