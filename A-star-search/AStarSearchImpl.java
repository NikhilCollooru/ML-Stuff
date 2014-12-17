import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Iterator;
import java.util.Comparator;
import java.util.ListIterator;



public class AStarSearchImpl implements AStarSearch {

	
	@Override	
	public SearchResult search(String initConfig, int modeFlag) {
		// TODO Add your code here
		State S = new State(initConfig,0,getHeuristicCost(initConfig, modeFlag),"");

		PriorityQueue<State> OPEN = new PriorityQueue<State>(10,S.comparator);
		Map<String,Integer> OPEN_MAP = new HashMap<String,Integer>();
		
		PriorityQueue<State> CLOSED = new PriorityQueue<State>(10,S.comparator);
		Map<String,Integer> CLOSED_MAP = new HashMap<String,Integer>();
		
		OPEN.add(S);
		OPEN_MAP.put(S.config, S.realCost+S.heuristicCost);
		
		int pop_count=0;
		while(OPEN.size() != 0)
		{
		  State temp_pop = OPEN.poll();
		  OPEN_MAP.remove(temp_pop.config);
		  
//		  System.out.print(temp_pop.config+" "+temp_pop.realCost +" "+temp_pop.heuristicCost+" "+temp_pop.opSequence+"\n");
		  
		  CLOSED.add(temp_pop);
		  CLOSED_MAP.put(temp_pop.config, temp_pop.realCost+temp_pop.heuristicCost);
		  
		  pop_count++;
//		  if(pop_count % 10000 == 0)
//				System.out.println(pop_count);
				
		  if(checkGoal(temp_pop.config))
		  {
		    SearchResult output = new SearchResult(temp_pop.config,temp_pop.opSequence,pop_count);
			return output;
		  }
		  int i;
		  Iterator<State> it_open, it_closed;
		  char[] chars = {'A','B','C','D','E','F','G','H'};
		  for(i=0; i<8; i++)
		  {
		    String succ_config = move(temp_pop.config,chars[i]);
		    State succ = new State( succ_config, temp_pop.realCost+1, getHeuristicCost(succ_config, modeFlag), temp_pop.opSequence + chars[i]);
			boolean in_open=false, in_closed=false;
			
			if(OPEN_MAP.containsKey(succ_config))
				in_open = true;

 
			if(CLOSED_MAP.containsKey(succ_config))
				in_closed = true;

			if( !in_open && !in_closed )
			{
               OPEN.add(succ);
			   OPEN_MAP.put(succ.config, succ.realCost+succ.heuristicCost);
			}   
			else
			{
			  if(in_open)
			  { 
			    if( OPEN_MAP.get(succ.config) > (succ.realCost+succ.heuristicCost) )
				{
			      it_open = OPEN.iterator();
			      while(it_open.hasNext())
			      {
				  	State temp = it_open.next();
			        if( succ.equals(temp) )
					{
				      it_open.remove();
					  break;
					}  
				  }	  
				  OPEN.add(succ);
			      OPEN_MAP.put(succ.config, succ.realCost + succ.heuristicCost);
			    }
			  }
			  else
			  {
			    if( CLOSED_MAP.get(succ.config) > (succ.realCost+succ.heuristicCost) )
				{
				  it_closed = CLOSED.iterator();
			      while(it_closed.hasNext())
			      {
				    State temp2 = it_closed.next();
			        if( succ.equals(temp2) )
			        {
				      it_closed.remove();
					  CLOSED_MAP.remove(temp2.config);	
				    }
				    break;
			      }
				  OPEN.add(succ);
				  OPEN_MAP.put(succ.config, succ.realCost + succ.heuristicCost);
				}
			  }	
            }
		  }
		}
		return null;
	}

	@Override
	public boolean checkGoal(String config) {
		// TODO Add your code here
		char config_arr[] = config.toCharArray();
		int[] blk_num = {6,7,8,11,12,15,16,17};		
		char initial = config_arr[6];
		for(int i:blk_num)
		{
		  if( config_arr[i] != initial)
		    return false;
		}
		return true;
	}

	@Override
	public String move(String config, char op) {
		// TODO Add your code here
		if( op >= 'A' && op < 'I')
		{
		  char config_arr[] = config.toCharArray();
		  int[][] rotations = new int[][]{
		   {2,1,6,3,4,5,11,7,8,9,10,15,12,13,14,20,16,17,18,19,22,21,0,23},
		   {0,3,2,8,4,5,6,7,12,9,10,11,17,13,14,15,16,21,18,19,20,23,22,1},
		   {0,1,2,3,10,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23},
		   {0,1,2,3,4,5,6,7,8,9,10,11,12,19,13,14,15,16,17,18,20,21,22,23},
		   {0,23,2,1,4,5,6,7,3,9,10,11,8,13,14,15,16,12,18,19,20,17,22,21},
		   {22,1,0,3,4,5,2,7,8,9,10,6,12,13,14,11,16,17,18,19,15,21,20,23},
		   {0,1,2,3,4,5,6,7,8,9,10,11,12,14,15,16,17,18,19,13,20,21,22,23},
		   {0,1,2,3,5,6,7,8,9,10,4,11,12,13,14,15,16,17,18,19,20,21,22,23}
		  };
		
		  int num = op -'A';
		  StringBuffer output_str = new StringBuffer();
		  for(int i:rotations[num])
		    output_str.append(config_arr[i]);
		  return output_str.toString();
		}
		else
		  return null;
	}

	@Override
	public int getHeuristicCost(String config, int modeFlag) {		
		// TODO Add your code here
		char config_arr[] = config.toCharArray();
		int[] blk_num = {6,7,8,11,12,15,16,17};
		int n1=0,n2=0,n3=0;
		for(int i : blk_num)
		{
		  if( config_arr[i] == '1')
		    n1++;
		  else if( config_arr[i] == '2')
		    n2++;
		  else 
            n3++;		  
		}
		
		if(modeFlag == 1)
		  return (8- Math.max(n1,Math.max(n2,n3)));
		else if(modeFlag == 2)
		  return 0;
		else if(modeFlag == 3)
		  return (Math.min(2, (8- Math.max(n1,Math.max(n2,n3)))));
		return 0;  
	}
	

}

