/**
 * 
 */
package ch.finman.core;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

/**
 * @author thomas
 *
 */
public class StockItemTest {
	
	StockItem stockItem;
	
	private static final String STOCK_SYMBOL = "^SSMI";
	private static final BigDecimal STOCK_PRICE = BigDecimal.ZERO;
	private static final boolean STOCK_SHOW = true;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		stockItem = new StockItem(STOCK_SYMBOL, STOCK_PRICE);
		stockItem.setShow(STOCK_SHOW);
	}

	/**
	 * Test method for {@link ch.finman.core.StockItem#StockItem(java.lang.String, java.math.BigDecimal)}.
	 */
	@Test
	public final void testStockItem() {
		assertNotNull(stockItem);
	}

	/**
	 * Test method for {@link ch.finman.core.StockItem#getStockSymbol()}.
	 */
	@Test
	public final void testGetStockSymbol() {
		assertEquals(stockItem.getStockSymbol(), STOCK_SYMBOL);
	}

	/**
	 * Test method for {@link ch.finman.core.StockItem#getPricePaid()}.
	 */
	@Test
	public final void testGetPricePaid() {
		assertEquals(stockItem.getPricePaid(), STOCK_PRICE);
	}

	/**
	 * Test method for {@link ch.finman.core.StockItem#getShow()}.
	 */
	@Test
	public final void testGetShow() {
		assertTrue(stockItem.getShow());
	}

	/**
	 * Test method for {@link ch.finman.core.StockItem#setShow(boolean)}.
	 */
	@Test
	public final void testSetShow() {
		stockItem.setShow(false);
		assertFalse(stockItem.getShow());
	}

}
