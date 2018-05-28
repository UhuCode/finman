/**
 * 
 */
package ch.finman.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;

import ch.finman.core.StockManager;
import ch.finman.util.LogUtil;

/**
 * @author thomas
 *
 */
public class DatabaseController {
	
	//private static SimpleLogger logger = (SimpleLogger) LoggerFactory.getLogger(DatabaseController.class);
	private static LogUtil logger = LogUtil.getLogger(DatabaseController.class);
	private static Connection conn;
	
	private static final String DB_PROT = System.getProperty("ctxConnProt", "jdbc:mysql");
	private static final String DB_HOST = System.getProperty("ctxConnHost", "localhost");
	private static final String DB_PORT = System.getProperty("ctxConnPort", "3306");
	private static final String DB_NAME = System.getProperty("ctxConnName", "finman");
	private static final String DB_USER = System.getProperty("ctxConnUser", "finman");
	private static final String DB_PASS = System.getProperty("ctxConnPass", "finmanfidos");
	
	private DatabaseController() {
		
	}
	
	public synchronized static Connection getConnection() {
		try {
			if (conn != null && conn.isValid(0)) {
				return conn;
			} else {
				conn = connect();
				return conn;
			}
		} catch (SQLException ex) {
		    // handle any errors
		    logger.sayErr("SQLException: " + ex.getMessage());
		    logger.sayErr("SQLState: " + ex.getSQLState());
		    logger.sayErr("VendorError: " + ex.getErrorCode());
		}
		return null;
	}
	
	private static Connection connect() throws SQLException {
		Properties properties = new Properties();
		properties.setProperty("user", DB_USER);
		properties.setProperty("password", DB_PASS);
		//properties.setProperty("useSSL", "false");
		//properties.setProperty("autoReconnect", "true");
		properties.setProperty("useSSL", "true");
		properties.setProperty("verifyServerCertificate", "false");
		
		String connStr = DB_PROT + "://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
		
		logger.sayOut("Connecting to MySQL database: " + connStr);
		Connection c = DriverManager.getConnection(connStr, properties);
		logger.sayOut("Database Connection established. ");
		return c;
	}

}
