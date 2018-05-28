/**
 * 
 */
package ch.finman.model.builder;

import java.util.TimeZone;

import ch.finman.misc.CurrencyValue;
import ch.finman.misc.ExchangeValue;
import yahoofinance.exchanges.ExchangeTimeZone;

/**
 * @author thomas
 *
 */
public class CurrencyValueBuilder {
	
	private int id;
	private String symbol;
	private String name;
	
	
	private CurrencyValueBuilder() {
		
	}
	
	public static CurrencyValueBuilder create() {
		return new CurrencyValueBuilder();
	}
	
	public CurrencyValueBuilder id(int id) {
		this.id = id;
		return this;
	}

	public CurrencyValueBuilder symbol(String symbol) {
		this.symbol = symbol;
		return this;
	}

	public CurrencyValueBuilder name(String name) {
		this.name = name;
		return this;
	}

	public CurrencyValue build() {
		CurrencyValue vo = new CurrencyValue();
		vo.setId(id);
		vo.setSymbol(symbol);
		vo.setName(name);
		return vo;
	}
	

}
