/**
 * 
 */
package ch.finman.model.dao;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.finman.misc.CurrencyDAO;
import ch.finman.misc.CurrencyValue;
import ch.finman.misc.ExchangeValue;
import ch.finman.model.builder.CurrencyValueBuilder;
import ch.finman.util.LogUtil;

/**
 * @author thomas
 *
 */
public class CurrencyDAOTest {

	private static final LogUtil logger = LogUtil.getLogger(CurrencyDAOTest.class);
	
	CurrencyValue vo;
	CurrencyDAO dao;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		dao = new CurrencyDAO();
		vo = CurrencyValueBuilder.create()
				.symbol("AUD")
				.name("Australian Dollar")
				.build();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link ch.finman.misc.CurrencyDAO#create(ch.finman.misc.CurrencyValue)}.
	 */
	@Test
	public final void testCreate() {
		logger.sayOut("Creating Value...");
		dao.create(vo);
	}

	/**
	 * Test method for {@link ch.finman.misc.CurrencyDAO#findBySymbol(java.lang.String)}.
	 */
	@Test
	public final void testFindBySymbol() {
		logger.sayOut("Find By Symbol...");
		CurrencyValue cv = dao.findBySymbol("CHF");
		logger.sayOut(cv.toString());
	}

	/**
	 * Test method for {@link ch.finman.model.dao.AbstractDAO#findByPk(int)}.
	 */
	@Test
	public final void testFindByPk() {
		logger.sayOut("Find By PK...");
		CurrencyValue cv = dao.findByPk(1);
		logger.sayOut(cv.toString());
	}

	/**
	 * Test method for {@link ch.finman.model.dao.AbstractDAO#findAll()}.
	 */
	@Test
	public final void testFindAll() {
		logger.sayOut("Find All...");
		List<CurrencyValue> list = dao.findAll();
		Iterator<CurrencyValue> i = list.iterator();
		while(i.hasNext()) {
			CurrencyValue cv = i.next();
			logger.sayOut(cv.toString());
		}
	}

}
