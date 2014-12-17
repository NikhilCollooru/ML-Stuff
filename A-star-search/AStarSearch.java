/**
 * Interface class for A* search algorithm.
 */
public interface AStarSearch {

	public SearchResult search(String initConfig, int modeFlag);
	/**
	 * check the configuration is a goal 
	 */
	public boolean checkGoal(String config);
	/**
	 * move the current configation with specific operation
	 */
	public String move(String config, char op);	
	/**
	 * compute the heuristic cost for current configuration
	 */
	public int getHeuristicCost(String config, int modeFlag);
	
}
