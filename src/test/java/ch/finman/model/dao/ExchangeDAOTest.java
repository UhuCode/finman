/**
 * 
 */
package ch.finman.model.dao;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.finman.misc.ExchangeDAO;
import ch.finman.misc.ExchangeValue;
import ch.finman.model.builder.ExchangeValueBuilder;
import ch.finman.util.LogUtil;

/**
 * @author thomas
 *
 */
public class ExchangeDAOTest {
	
	private static final LogUtil logger = LogUtil.getLogger(ExchangeDAOTest.class);
	
	ExchangeValue vo;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		vo = ExchangeValueBuilder.create()
				.shortName("NYSE")
				.fullName("New York Stock Exchange")
				.timeZone("America/New_York")
				.build();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link ch.finman.misc.ExchangeDAO#create(ch.finman.misc.ExchangeValue)}.
	 */
	@Test
	public final void testCreate() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link ch.finman.misc.ExchangeDAO#findByPk(int)}.
	 */
	@Test
	public final void testFindByPk() {
		ExchangeDAO dao = new ExchangeDAO();
		ExchangeValue vo = dao.findByPk(1);
		logger.sayOut(vo.toString());
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link ch.finman.misc.ExchangeDAO#findByShortName(java.lang.String)}.
	 */
	@Test
	public final void testFindByShortName() {
		fail("Not yet implemented"); // TODO
	}

}
