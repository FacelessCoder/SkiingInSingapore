import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SkiingInSingapore 
{
	static int xGrid = 0;
	
	static int yGrid = 0;
	
	static boolean nodeAdded = false;
	
	static int longestPathLength = 0;
	
	static int largestDrop = 0;
	
	public static int[] loadMap(String filePath) throws FileNotFoundException
	{
		Scanner scanner = null;
		int[] map = null;
		try 
		{
			File file = new File(filePath);
			scanner = new Scanner(file);
			if (scanner.hasNextLine()) 
			{
				String nextLine = scanner.nextLine();
				String[] tokens = nextLine.split("\\s+");
				if (tokens.length == 2)
				{
					yGrid = Integer.parseInt(tokens[0]);
					xGrid = Integer.parseInt(tokens[1]);
				}
			}
			if (xGrid > 0 && yGrid > 0)
			{
				map = new int[yGrid * xGrid];
				int index = 0;
				while (scanner.hasNextLine()) 
				{
					String nextLine = scanner.nextLine();
					String[] tokens = nextLine.split("\\s+");
					for (String token : tokens) 
					{
						map[index] = Integer.parseInt(token);
						index++;
					}
				}
				if (index != (yGrid * xGrid))
				{
					map = null;
					System.out.println("The grid specified in the map file does not match the map");
				}
			}
			else 
			{
				System.out.println("The grid of the map is not specified in the map file");
			}
			return map;
			
		}
		finally
		{
			scanner.close();
		}
	}
	
	public static List<List<Integer>> buildPaths(int[] map)
	{
		List<List<Integer>> paths = new ArrayList<List<Integer>>();
		float mapSize = xGrid*yGrid;
		for (int position = 0; position < (yGrid * xGrid); position ++)
		{
			System.out.print("\rProcessing: "+ Math.round((position+1)/mapSize*10000.0)/100.0 + "%");
			traveseMap(map, xGrid, position, paths, null);
		}
		System.out.println("");
		return paths;
	}
	
	public static void traveseMap(int[] map, int xGrid, int position, List<List<Integer>> paths, List<Integer> currentPath)
	{
		if (position >= 0 && position < map.length)
		{
			int currentValue = map[position];
			for (int direction = 0; direction < 4; direction++)
			{
				int nextPosition = position;
				switch(direction) 
				{
					case 0:	nextPosition = position + 1;
							break;
					case 1: nextPosition = position + xGrid;
							break;
					case 2: nextPosition = position - 1;
							break;
					case 3: nextPosition = position - xGrid;
							break;
				}
				if (nextPosition >= 0 && nextPosition < map.length && !(direction == 2 && (position%xGrid) == 0 ) && !(direction == 0 && (nextPosition%xGrid) == 0)) 
				{
					int nextValue = map[nextPosition];
					if (currentValue > nextValue)
					{
						if (currentPath == null)
						{
							currentPath = new ArrayList<Integer>();								
							currentPath.add(position);
						}
						currentPath.add(nextPosition);
						nodeAdded = true;
						traveseMap(map, xGrid, nextPosition, paths, currentPath);
					}
				}	
			}
			if (currentPath != null)
			{				
				if (nodeAdded)
				{
					if (currentPath.size() > longestPathLength)
						longestPathLength = currentPath.size();
					paths.add(new ArrayList<Integer>(currentPath));
					nodeAdded = false;
				}
				currentPath.remove(currentPath.size() - 1);
			}
		}
	}
	
	public static List<List<Integer>> findLongestSteepestPaths(int[] map, List<List<Integer>> paths)
	{
		List<List<Integer>> longestPaths = new ArrayList<List<Integer>>();
		List<List<Integer>> longestSteepestPaths = new ArrayList<List<Integer>>();	
		for (List<Integer> path : paths)
		{
			if (path.size() == longestPathLength)
			{
				int drop = map[path.get(0)] - map[path.get(path.size()-1)];
				if (drop > largestDrop)
					largestDrop = drop;
				longestPaths.add(path);
			}
		}
		for (List<Integer> path : longestPaths)
		{
			int drop = map[path.get(0)] - map[path.get(path.size()-1)];
			if (drop == largestDrop)
			{
				longestSteepestPaths.add(path);
			}
		}
		return longestSteepestPaths;
	}
	
	public static void main(String[] args) throws FileNotFoundException, InterruptedException 
	{
		if (args != null && args.length != 0)
		{
			int[] map = loadMap(args[0]);
			if (map != null)
			{
				List<List<Integer>> paths = buildPaths(map);
				List<List<Integer>> longestSteepestPaths = findLongestSteepestPaths(map, paths);
				for (List<Integer> longestSteepestPath: longestSteepestPaths)
				{
					System.out.print("Longest path with the largest drop: ");
					for(Integer position : longestSteepestPath)
					{
						System.out.print(map[position] + " ");
					}
					System.out.println();
				}
				System.out.println("Length of longest path with the largest drop: " + longestPathLength);
				System.out.println("Size of the drop for longest path with the largest drop: " + largestDrop);
			}
		}
		else
		{
			System.out.println("Please run the program by keying in \"java SkiingSingapore <path of map file>\"");
		}
	} 
}

