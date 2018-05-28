/**
 * 
 */
package ch.finman.model.dao;

import java.util.List;

import ch.finman.model.Portfolio;

/**
 * @author thomas
 *
 */
public interface PortfolioDAOInterface {
	
	public Portfolio create(Portfolio portfolio);
	
	public Portfolio findByPk(int id);
	
	public List<Portfolio> findAll();
	
	public Portfolio findByShortName(String shortName);
	
	public Portfolio update(Portfolio portfolio);
	
	public boolean delete(Portfolio portfolio);

}
