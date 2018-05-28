/**
 * 
 */
package ch.finman.sql;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;

import ch.finman.core.StockManager;
import ch.finman.util.LogUtil;

/**
 * @author thomas
 *
 */
public class DatabaseControllerTest {
	
	//private static SimpleLogger logger = (SimpleLogger) LoggerFactory.getLogger(DatabaseControllerTest.class);
	private static LogUtil logger = LogUtil.getLogger(DatabaseControllerTest.class);
	private Connection conn;
	private static final String DB_SCHEMA = "finman";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
//		logger.debug("setUp: getting Database connection...");
//		conn = DatabaseController.getConnection();
//		logger.debug("setUp: Database connection established.");
	}

	/**
	 * Test method for {@link ch.finman.sql.DatabaseController#getConnection()}.
	 */
	@Test
	public final void testGetConnection() {
		logger.sayOut("setUp: getting Database connection...");
		conn = DatabaseController.getConnection();
		logger.sayOut("setUp: Database connection established.");
		assertNotNull(conn);
		DatabaseMetaData schema;
		try {
			schema = conn.getMetaData();
			logger.sayOut("testGetConnection: Schema -> " + schema.getURL());
			//assertEquals(DB_SCHEMA, schema);
		} catch (SQLException e) {
			logger.sayErr("testGetConnection: Failed to get Schema.");
			e.printStackTrace();
		}
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		if (conn != null) {
			logger.sayOut("tearDown: trying to close connection");
			conn.close();
		}
	}


}
