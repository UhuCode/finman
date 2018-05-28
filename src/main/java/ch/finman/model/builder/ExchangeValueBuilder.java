/**
 * 
 */
package ch.finman.model.builder;

import java.util.TimeZone;

import ch.finman.misc.ExchangeValue;
import yahoofinance.exchanges.ExchangeTimeZone;

/**
 * @author thomas
 *
 */
public class ExchangeValueBuilder {
	
	private int id;
	private String shortName;
	private String fullName;
	private TimeZone timeZone;
	
	
	private ExchangeValueBuilder() {
		
	}
	
	public static ExchangeValueBuilder create() {
		return new ExchangeValueBuilder();
	}
	
	public ExchangeValueBuilder id(int id) {
		this.id = id;
		return this;
	}

	public ExchangeValueBuilder shortName(String shortName) {
		this.shortName = shortName;
		return this;
	}

	public ExchangeValueBuilder fullName(String fullName) {
		this.fullName = fullName;
		return this;
	}

	public ExchangeValueBuilder timeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
		return this;
	}

	public ExchangeValueBuilder timeZone(String timeZoneStr) {
		TimeZone timeZone = ExchangeTimeZone.get(timeZoneStr);
		return timeZone(timeZone);
	}
	
	public ExchangeValue build() {
		ExchangeValue vo = new ExchangeValue();
		vo.setId(id);
		vo.setShortName(shortName);
		vo.setFullName(fullName);
		vo.setTimeZone(timeZone);
		return vo;
	}
	

}
