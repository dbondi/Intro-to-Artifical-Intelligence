package hw1;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.AbstractMap.SimpleEntry;

/**
 * @author Dan Bondi
 * Created executeAStar,executeDFS,executeBFS
 */
/**
 * @author abhanshu 
 * This class is a template for implementation of 
 * HW1 for CS540 section 2
 */
/**
 * Data structure to store each node.
 */
class Location {
	private int x;
	private int y;
	private Location parent;

	public Location(int x, int y, Location parent) {
		this.x = x;
		this.y = y;
		this.parent = parent;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Location getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return x + " " + y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Location) {
			Location loc = (Location) obj;
			return loc.x == x && loc.y == y;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * (hash + x);
		hash = 31 * (hash + y);
		return hash;
	}
}

public class KingsKnightmare {
	//represents the map/board
	private static boolean[][] board;
	//represents the goal node
	private static Location king;
	//represents the start node
	private static Location knight;
	//y dimension of board
	private static int n;
	//x dimension of the board
	private static int m;
	//enum defining different algo types
	enum SearchAlgo{
		BFS, DFS, ASTAR;
	}

	public static void main(String[] args) {
		if (args != null && args.length > 0) {
			//loads the input file and populates the data variables
			SearchAlgo algo = loadFile(args[0]);
			if (algo != null) {
				switch (algo) {
					case DFS :
						executeDFS();
						break;
					case BFS :
						executeBFS();
						break;
					case ASTAR :
						executeAStar();
						break;
					default :
						break;
				}
			}
		}
	}

	/**
	 * Implementation of Astar algorithm for the problem
	 */
	private static void executeAStar() {
		PriorityQ<Location> queue = new PriorityQ<Location>();
		
		queue.add(knight,Math.abs(knight.getX()-king.getX())+Math.abs(knight.getY()-king.getY()));
		
		SimpleEntry<Location, Integer> temp;
		Location tempLoc;
		
		PriorityQ<Location> frontier = new PriorityQ<Location>();
		Stack<Location> expanded = new Stack<Location>();
		
		int i=0;
		frontier.add(knight,Math.abs(knight.getX()-king.getX())+Math.abs(knight.getY()-king.getY()));
		while(true){
		if(queue.isEmpty()){
			System.out.println("NOT REACHABLE");
			System.out.println("Expanded Nodes: "+i);
			return;
		}
		if(queue.peek().getKey().equals(king)){
			tempLoc = queue.peek().getKey();
			while(tempLoc.getParent()!=null){
				expanded.push(tempLoc);
				tempLoc=tempLoc.getParent();
			}
			expanded.push(tempLoc);
			
			while(!expanded.isEmpty()){
				System.out.println(expanded.pop().toString());
			}
			System.out.println("Expanded Nodes: "+i);
			return;
		}
		else {
		temp = queue.peek();
		queue.remove(temp.getKey());
		i++;
		
			int gValues = temp.getValue()-Math.abs(temp.getKey().getX()-king.getX())-Math.abs(temp.getKey().getY()-king.getY());
		
		
			if((temp.getKey().getX() + 2) < m && (temp.getKey().getY() + 1) < n){;
				if(!board[temp.getKey().getY() + 1][temp.getKey().getX() + 2]){
					Location newlocation = new Location(temp.getKey().getX() + 2,temp.getKey().getY() + 1,temp.getKey());
					
					int xChange = Math.abs(newlocation.getX()-king.getX());
					int yChange = Math.abs(newlocation.getY()-king.getY());
					
					int fValue = gValues + xChange + yChange + 3;
					
					if(!frontier.exists(newlocation)){
						queue.add(newlocation,fValue);
						frontier.add(newlocation,fValue);
					}
					else if(queue.exists(newlocation)){
						if(queue.getPriorityScore(newlocation)>fValue){
							queue.modifyEntry(newlocation,fValue);
							frontier.modifyEntry(newlocation,fValue);
						}
					}
					else if(frontier.exists(newlocation)){
						if(frontier.getPriorityScore(newlocation)>fValue){
							queue.add(newlocation,fValue);
							frontier.modifyEntry(newlocation,fValue);
						}
					}
					
				}
			
			if((temp.getKey().getX() + 1) < m && (temp.getKey().getY() + 2) < n){
				if(!board[temp.getKey().getY() + 2][temp.getKey().getX() + 1]){
					Location newlocation = new Location(temp.getKey().getX() + 1,temp.getKey().getY() + 2,temp.getKey());
					
					int xChange = Math.abs(newlocation.getX()-king.getX());
					int yChange = Math.abs(newlocation.getY()-king.getY());
					
					int fValue = gValues + xChange + yChange + 3;
					
					if(!frontier.exists(newlocation)){
						queue.add(newlocation,fValue);
						frontier.add(newlocation,fValue);
					}
					else if(queue.exists(newlocation)){
						if(queue.getPriorityScore(newlocation)>fValue){
							queue.modifyEntry(newlocation,fValue);
							frontier.modifyEntry(newlocation,fValue);
						}
					}
					else if(frontier.exists(newlocation)){
						if(frontier.getPriorityScore(newlocation)>fValue){
							queue.add(newlocation,fValue);
							frontier.modifyEntry(newlocation,fValue);
						}
					}
					
					
				}
				
			}
			if((temp.getKey().getX() - 1) >= 0 && (temp.getKey().getY() + 2) < n){
				if(!board[temp.getKey().getY() + 2][temp.getKey().getX() - 1]){
					Location newlocation = new Location(temp.getKey().getX() - 1,temp.getKey().getY() + 2,temp.getKey());
					
					int xChange = Math.abs(newlocation.getX()-king.getX());
					int yChange = Math.abs(newlocation.getY()-king.getY());
					
					int fValue = gValues + xChange + yChange + 3;
					
					if(!frontier.exists(newlocation)){
						queue.add(newlocation,fValue);
						frontier.add(newlocation,fValue);
					}
					else if(queue.exists(newlocation)){
						if(queue.getPriorityScore(newlocation)>fValue){
							queue.modifyEntry(newlocation,fValue);
							frontier.modifyEntry(newlocation,fValue);
						}
					}
					else if(frontier.exists(newlocation)){
						if(frontier.getPriorityScore(newlocation)>fValue){
							queue.add(newlocation,fValue);
							frontier.modifyEntry(newlocation,fValue);
						}
					}
					
					
				}
				
			}
			if((temp.getKey().getX() - 2) >= 0 && (temp.getKey().getY() + 1) < n){
				if(!board[temp.getKey().getY() + 1][temp.getKey().getX() - 2]){
					Location newlocation = new Location(temp.getKey().getX() - 2,temp.getKey().getY() + 1,temp.getKey());
					
					int xChange = Math.abs(newlocation.getX()-king.getX());
					int yChange = Math.abs(newlocation.getY()-king.getY());
					
					int fValue = gValues + xChange + yChange + 3;
					
					if(!frontier.exists(newlocation)){
						queue.add(newlocation,fValue);
						frontier.add(newlocation,fValue);
					}
					else if(queue.exists(newlocation)){
						if(queue.getPriorityScore(newlocation)>fValue){
							queue.modifyEntry(newlocation,fValue);
							frontier.modifyEntry(newlocation,fValue);
						}
					}
					else if(frontier.exists(newlocation)){
						if(frontier.getPriorityScore(newlocation)>fValue){
							queue.add(newlocation,fValue);
							frontier.modifyEntry(newlocation,fValue);
						}
					}
					
					
				}
			}
			if((temp.getKey().getX() - 2) >= 0 && (temp.getKey().getY() - 1) >= 0){
				if(!board[temp.getKey().getY() - 1][temp.getKey().getX() - 2]){
					Location newlocation = new Location(temp.getKey().getX() - 2,temp.getKey().getY() - 1,temp.getKey());
					
					int xChange = Math.abs(newlocation.getX()-king.getX());
					int yChange = Math.abs(newlocation.getY()-king.getY());
					
					int fValue = gValues + xChange + yChange + 3;
					
					if(!frontier.exists(newlocation)){
						queue.add(newlocation,fValue);
						frontier.add(newlocation,fValue);
					}
					else if(queue.exists(newlocation)){
						if(queue.getPriorityScore(newlocation)>fValue){
							queue.modifyEntry(newlocation,fValue);
							frontier.modifyEntry(newlocation,fValue);
						}
					}
					else if(frontier.exists(newlocation)){
						if(frontier.getPriorityScore(newlocation)>fValue){
							queue.add(newlocation,fValue);
							frontier.modifyEntry(newlocation,fValue);
						}
					}
					
					
				}
			}
			if((temp.getKey().getX() - 1) >= 0 && (temp.getKey().getY() - 2) >= 0){
				if(!board[temp.getKey().getY() - 2][temp.getKey().getX() - 1]){
					Location newlocation = new Location(temp.getKey().getX() - 1,temp.getKey().getY() - 2,temp.getKey());
					
					int xChange = Math.abs(newlocation.getX()-king.getX());
					int yChange = Math.abs(newlocation.getY()-king.getY());
					
					int fValue = gValues + xChange + yChange + 3;
					
					if(!frontier.exists(newlocation)){
						queue.add(newlocation,fValue);
						frontier.add(newlocation,fValue);
					}
					else if(queue.exists(newlocation)){
						if(queue.getPriorityScore(newlocation)>fValue){
							queue.modifyEntry(newlocation,fValue);
							frontier.modifyEntry(newlocation,fValue);
						}
					}
					else if(frontier.exists(newlocation)){
						if(frontier.getPriorityScore(newlocation)>fValue){
							queue.add(newlocation,fValue);
							frontier.modifyEntry(newlocation,fValue);
						}
					}
					
					
				}
			}
			if((temp.getKey().getX() + 1) < m && (temp.getKey().getY() - 2) >= 0){
				if(!board[temp.getKey().getY() - 2][temp.getKey().getX() + 1]){
					Location newlocation = new Location(temp.getKey().getX() + 1,temp.getKey().getY() - 2,temp.getKey());
					
					int xChange = Math.abs(newlocation.getX()-king.getX());
					int yChange = Math.abs(newlocation.getY()-king.getY());
					
					int fValue = gValues + xChange + yChange + 3;
					
					if(!frontier.exists(newlocation)){
						queue.add(newlocation,fValue);
						frontier.add(newlocation,fValue);
					}
					else if(queue.exists(newlocation)){
						if(queue.getPriorityScore(newlocation)>fValue){
							queue.modifyEntry(newlocation,fValue);
							frontier.modifyEntry(newlocation,fValue);
						}
					}
					else if(frontier.exists(newlocation)){
						if(frontier.getPriorityScore(newlocation)>fValue){
							queue.add(newlocation,fValue);
							frontier.modifyEntry(newlocation,fValue);
						}
					}
					
					
				}
			}
			if((temp.getKey().getX() + 2) < m && (temp.getKey().getY() - 1) >= 0){
				if(!board[temp.getKey().getY() - 1][temp.getKey().getX() + 2]){
					Location newlocation = new Location(temp.getKey().getX() + 2,temp.getKey().getY() - 1,temp.getKey());
					
					int xChange = Math.abs(newlocation.getX()-king.getX());
					int yChange = Math.abs(newlocation.getY()-king.getY());
					
					int fValue = gValues + xChange + yChange + 3;
					if(!frontier.exists(newlocation)){
						queue.add(newlocation,fValue);
						frontier.add(newlocation,fValue);
					}
					else if(queue.exists(newlocation)){
						if(queue.getPriorityScore(newlocation)>fValue){
							queue.modifyEntry(newlocation,fValue);
							frontier.modifyEntry(newlocation,fValue);
						}
					}
					else if(frontier.exists(newlocation)){
						if(frontier.getPriorityScore(newlocation)>fValue){
							queue.add(newlocation,fValue);
							frontier.modifyEntry(newlocation,fValue);
						}
					}
					
				}
				
			}		

		}
		}
		}
		
	}

	/**
	 * Implementation of BFS algorithm
	 */
		private static void executeBFS() {
			Queue<Location> queue = new LinkedList<>();
			
			queue.add(knight);
			Location temp;

			ArrayList<Location> frontier = new ArrayList<Location>();
			Stack<Location> expanded = new Stack<Location>();
			int i=0;
			frontier.add(knight);
			boolean goal = false;
			while(true){
			if(queue.isEmpty()){
				System.out.println("NOT REACHABLE");
				System.out.println("Expanded Nodes: " + i);
				return;
			}
			
			else {
			temp = queue.poll();
			i++;
			
				if((temp.getX() + 2) < m && (temp.getY() + 1) < n && !goal){;
					if(!board[temp.getY() + 1][temp.getX() + 2]){
						Location newtemp = new Location(temp.getX() + 2,temp.getY() + 1,temp);
						if(!frontier.contains(newtemp)){
							queue.add(newtemp);
							frontier.add(newtemp);
							if(newtemp.equals(king)){
								goal=true;
								temp = newtemp;
							}
						}
						
					}
				}
				if((temp.getX() + 1) < m && (temp.getY() + 2) < n && !goal){
					if(!board[temp.getY() + 2][temp.getX() + 1]){
						Location newtemp = new Location(temp.getX() + 1,temp.getY() + 2,temp);
						if(!frontier.contains(newtemp)){
							queue.add(newtemp);
							frontier.add(newtemp);
							if(newtemp.equals(king)){
								goal=true;
								temp = newtemp;
							}
						}
						
					}
					
				}
				if((temp.getX() - 1) >= 0 && (temp.getY() + 2) < n && !goal){
					if(!board[temp.getY() + 2][temp.getX() - 1]){
						Location newtemp = new Location(temp.getX() - 1,temp.getY() + 2,temp);
						if(!frontier.contains(newtemp)){
							queue.add(newtemp);
							frontier.add(newtemp);
							if(newtemp.equals(king)){
								goal=true;
								temp = newtemp;
							}
						}
						
					}
					
				}
				if((temp.getX() - 2) >= 0 && (temp.getY() + 1) < n && !goal){
					if(!board[temp.getY() + 1][temp.getX() - 2]){
						Location newtemp = new Location(temp.getX() - 2,temp.getY() + 1,temp);
						if(!frontier.contains(newtemp)){
							queue.add(newtemp);
							frontier.add(newtemp);
							if(newtemp.equals(king)){
								goal=true;
								temp = newtemp;
							}
						}
						
					}
				}
				if((temp.getX() - 2) >= 0 && (temp.getY() - 1) >= 0 && !goal){
					if(!board[temp.getY() - 1][temp.getX() - 2]){
						Location newtemp = new Location(temp.getX() - 2,temp.getY() - 1,temp);
						if(!frontier.contains(newtemp)){
							queue.add(newtemp);
							frontier.add(newtemp);
							if(newtemp.equals(king)){
								goal=true;
								temp = newtemp;
							}
						}
						
					}
				}
				if((temp.getX() - 1) >= 0 && (temp.getY() - 2) >= 0 && !goal){
					if(!board[temp.getY() - 2][temp.getX() - 1]){
						Location newtemp = new Location(temp.getX() - 1,temp.getY() - 2,temp);
						if(!frontier.contains(newtemp)){
							queue.add(newtemp);
							frontier.add(newtemp);
							if(newtemp.equals(king)){
								goal=true;
								temp = newtemp;
							}
						}
						
					}
				}
				if((temp.getX() + 1) < m && (temp.getY() - 2) >= 0 && !goal){
					if(!board[temp.getY() - 2][temp.getX() + 1]){
						Location newtemp = new Location(temp.getX() + 1,temp.getY() - 2,temp);
						if(!frontier.contains(newtemp)){
							queue.add(newtemp);
							frontier.add(newtemp);
							if(newtemp.equals(king)){
								goal=true;
								temp = newtemp;
							}
						}
						
					}
				}
				if((temp.getX() + 2) < m && (temp.getY() - 1) >= 0 && !goal){
					if(!board[temp.getY() - 1][temp.getX() + 2]){
						Location newtemp = new Location(temp.getX() + 2,temp.getY() - 1,temp);
						if(!frontier.contains(newtemp)){
							queue.add(newtemp);
							frontier.add(newtemp);
							if(newtemp.equals(king)){
								goal=true;
								temp = newtemp;
							}
						}
					}
					
				}		

			}
			if(goal==true){
				while(temp.getParent()!=null){
					expanded.push(temp);
					temp=temp.getParent();
				}
				expanded.push(temp);
				
				while(!expanded.isEmpty()){
					System.out.println(expanded.pop().toString());
				}
				System.out.println("Expanded Nodes: "+ i);
				return;
			}
			}
			
		}
	
	
	/**
	 * Implemention of DFS algorithm
	 */
	private static void executeDFS() {
		Stack<Location> stack = new Stack<Location>();
		
		stack.push(knight);
		Location temp;
		int i =0;
		boolean goal = false;
		ArrayList<Location> frontier = new ArrayList<Location>();
		Stack<Location> expanded = new Stack<Location>();
		while(true){
		if(stack.isEmpty()){
			System.out.println("NOT REACHABLE");
			System.out.println("Expanded Nodes: "+ i);
			return;
		}
		
		else {
		i++;
		temp = stack.pop();
			
			if((temp.getX() + 2) < m && (temp.getY() + 1) < n && !goal){;
				if(!board[temp.getY() + 1][temp.getX() + 2]){
					Location newtemp = new Location(temp.getX() + 2,temp.getY() + 1,temp);
					if(!frontier.contains(newtemp)){
						stack.push(newtemp);
						frontier.add(newtemp);
						if(newtemp.equals(king)){
							goal=true;
						}
					}
					
				}
			}
			if((temp.getX() + 1) < m && (temp.getY() + 2) < n && !goal){
				if(!board[temp.getY() + 2][temp.getX() + 1]){
					Location newtemp = new Location(temp.getX() + 1,temp.getY() + 2,temp);
					if(!frontier.contains(newtemp)){
						stack.push(newtemp);
						frontier.add(newtemp);
						if(newtemp.equals(king)){
							goal=true;
						}
					}
					
				}
				
			}
			if((temp.getX() - 1) >= 0 && (temp.getY() + 2) < n && !goal){
				if(!board[temp.getY() + 2][temp.getX() - 1]){
					Location newtemp = new Location(temp.getX() - 1,temp.getY() + 2,temp);
					if(!frontier.contains(newtemp)){
						stack.push(newtemp);
						frontier.add(newtemp);
						if(newtemp.equals(king)){
							goal=true;
						}
					}
					
				}
				
			}
			if((temp.getX() - 2) >= 0 && (temp.getY() + 1) < n && !goal){
				if(!board[temp.getY() + 1][temp.getX() - 2]){
					Location newtemp = new Location(temp.getX() - 2,temp.getY() + 1,temp);
					if(!frontier.contains(newtemp)){
						stack.push(newtemp);
						frontier.add(newtemp);
						if(newtemp.equals(king)){
							goal=true;
						}
					}
					
				}
			}
			if((temp.getX() - 2) >= 0 && (temp.getY() - 1) >= 0 && !goal){
				if(!board[temp.getY() - 1][temp.getX() - 2]){
					Location newtemp = new Location(temp.getX() - 2,temp.getY() - 1,temp);
					if(!frontier.contains(newtemp)){
						stack.push(newtemp);
						frontier.add(newtemp);
						if(newtemp.equals(king)){
							goal=true;
						}
					}
					
				}
			}
			if((temp.getX() - 1) >= 0 && (temp.getY() - 2) >= 0 && !goal){
				if(!board[temp.getY() - 2][temp.getX() - 1]){
					Location newtemp = new Location(temp.getX() - 1,temp.getY() - 2,temp);
					if(!frontier.contains(newtemp)){
						stack.push(newtemp);
						frontier.add(newtemp);
						if(newtemp.equals(king)){
							goal=true;
						}
					}
					
				}
			}
			if((temp.getX() + 1) < m && (temp.getY() - 2) >= 0 && !goal){
				if(!board[temp.getY() - 2][temp.getX() + 1]){
					Location newtemp = new Location(temp.getX() + 1,temp.getY() - 2,temp);
					if(!frontier.contains(newtemp)){
						stack.push(newtemp);
						frontier.add(newtemp);
						if(newtemp.equals(king)){
							goal=true;
						}
					}
					
				}
			}
			if((temp.getX() + 2) < m && (temp.getY() - 1) >= 0 && !goal){
				if(!board[temp.getY() - 1][temp.getX() + 2]){
					Location newtemp = new Location(temp.getX() + 2,temp.getY() - 1,temp);
					if(!frontier.contains(newtemp)){
						stack.push(newtemp);
						frontier.add(newtemp);
						if(newtemp.equals(king)){
							goal=true;
						}
					}
				}
				
			}		

		}
		if(goal){
			temp = stack.pop();
			
			while(temp.getParent()!=null){
				expanded.push(temp);
				temp=temp.getParent();
				
			}
			expanded.push(temp);
			while(!expanded.isEmpty()){
				System.out.println(expanded.pop().toString());
			}
			System.out.println("Expanded Nodes: "+i);
			return;
		}
		}
		
	}
	
	/**
	 * 
	 * @param filename
	 * @return Algo type
	 * This method reads the input file and populates all the 
	 * data variables for further processing
	 */
	private static SearchAlgo loadFile(String filename) {
		File file = new File(filename);
		try {
			Scanner sc = new Scanner(file);
			SearchAlgo algo = SearchAlgo.valueOf(sc.nextLine().trim().toUpperCase());
			n = sc.nextInt();
			m = sc.nextInt();
			sc.nextLine();
			board = new boolean[n][m];
			for (int i = 0; i < n; i++) {
				String line = sc.nextLine();
				for (int j = 0; j < m; j++) {
					if (line.charAt(j) == '1') {
						board[i][j] = true;
					} else if (line.charAt(j) == 'S') {
						knight = new Location(j, i, null);
					} else if (line.charAt(j) == 'G') {
						king = new Location(j, i, null);
					}
				}
			}
			sc.close();
			return algo;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}