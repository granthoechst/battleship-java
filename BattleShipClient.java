import java.util.*;

public class BattleShipClient
{
	private static Location lastMove = new Location(0,0,false);
	private static boolean upSuccess = true;
	private static boolean downSuccess;
	private static boolean leftSuccess;
	private static boolean rightSuccess;
	private static int shipCounter = 0;
	private static int otherShipCounter = 0;
	private static char[][]guesses = new char[10][10];
	private static boolean gameOver = false;
	private static ArrayList<Location> activeHits = new ArrayList<Location>(0);
	public static void main( String args[] )
	{
		char[][] grid = new char[10][10];
		char[][] grid2 = new char[10][10];
		for( int r = 0 ; r <= 9 ; r++ )
			for( int c = 0 ; c <= 9 ; c++ )
				grid[r][c] = '.';
		for( int r = 0 ; r <= 9 ; r++ )
			for( int c = 0 ; c <= 9 ; c++ )
				grid2[r][c] = '.';
		for( int r = 0 ; r <= 9 ; r++ )
			for( int c = 0 ; c <= 9 ; c++ )
				guesses[r][c] = '.';
		randomShipPlacement( grid , 'A' , 5 );
		randomShipPlacement( grid , 'B' , 4 );
		randomShipPlacement( grid , 'S' , 3 );
		randomShipPlacement( grid , 'C' , 3 );
		randomShipPlacement( grid , 'D' , 2 );
		do
		{
			for( int r = 0 ; r <= 9 ; r++ )
				for( int c = 0 ; c <= 9 ; c++ )
					grid2[r][c] = '.';
			humanFill(grid2);
		}
		while(humanIsDumb(grid2));
		while(!gameOver)
		{
			humanPlay(grid);
			compPlay(grid2);
			System.out.println();
			printGrid(guesses);
			System.out.println();
			printGrid(grid2);
		}
		if(otherShipCounter == 5)
		{	
			System.out.println();
			System.out.println("You won! Wupdee do.");
		}
		else
		{
			System.out.println();
			System.out.println("You just got outplayed by the computer.");
			System.out.println();
			printGrid(grid);
		}

		// Some testing code to check currently active hits

		/*for(int i = 0 ; i < 50 ; i++)
		{
			System.out.println();
			compPlay(grid);
			printGrid( grid );
			for(int j = 0 ; j < activeHits.size(); j++)
			{
				activeHits.get(j).printLocation();
			}
			System.out.print(">");
			lastMove.printLocation();
			System.out.println("UpSuccess = " + upSuccess);
			System.out.println("RightSuccess = " + rightSuccess);
			System.out.println("DownSuccess = " + downSuccess);
			System.out.println("LeftSuccess = " + leftSuccess);
		}*/
		
	}
	private static void push(Location hitPoint)
	{
		int r = hitPoint.getRow();
		int c = hitPoint.getColumn();
		boolean surr = hitPoint.getSurrounded();
		activeHits.add(0,new Location(r,c,surr));
	}
	private static Location check()
	{
		for(int i = 0 ; i < activeHits.size(); i++)
		{
			if(!(activeHits.get(i)).getSurrounded())
			{
				return activeHits.get(i);
			}
		}
		return null;
	}
	private static void pop(char ship , char rowCol , int position)
	{
		//System.out.println("Pop : Ship " + ship);
		int elements;
		switch(ship)
		{
			case 'D':
				elements = 2;
				break;
			case 'S':
				elements = 3;
				break;
			case 'C':
				elements = 3;
				break;
			case 'B':
				elements = 4;
				break;
			case 'A':
				elements = 5;
				break;
			default:
				elements = 0;
		}


		//System.out.println("Elements: " + elements);
		//System.out.println("ActiveHits : ");
		//for(int j = 0 ; j < activeHits.size(); j++)
		//{
			//activeHits.get(j).printLocation();
		//}
		int counter = elements;
		//int limit = activeHits.size();
		if(rowCol == 'r')
		{
			int i = 0;
			do
			{
				if(counter > 0 && (activeHits.get(i)).getRow() == position)
				{
					activeHits.remove(i);
					counter--;
				}
				else
					i++;
			}while(i < activeHits.size());
		}
		else
		{
			int i = 0;
			do
			{
				if(counter > 0 && (activeHits.get(i)).getColumn() == position)
				{
					activeHits.remove(i);
					counter--;
				}
				else
					i++;
			}while(i < activeHits.size());
		}
		//System.out.println("ActiveHits : ");
		//for(int j = 0 ; j < activeHits.size(); j++)
		//{
		//	activeHits.get(j).printLocation();
		//}
		//System.out.println("*");
		if(check() != null)
			lastMove.setLocation(check().getRow(), check().getColumn(), false);
		upSuccess = true;
		rightSuccess = false;
		downSuccess = false;
		leftSuccess = false;
	}
	private static boolean sunkCheck(char[][] grid , char ship)
	{
		for(int i = 0 ; i < 10 ; i++)
		{
			for(int j = 0 ; j < 10 ; j++)
			{
				if(grid[i][j] == ship)
					return false;
			}
		}
		return true;
	}
	public static void printGrid( char[][] grid )
	{
		for( int r = 0 ; r <= 9 ; r++ )
		{
			for( int c = 0 ; c <= 9 ; c++ )
				System.out.print(grid[r][c] + " " );
			System.out.println();
		}			
	}
	public static void randomShipPlacement( char[][] grid , char ship , int size )
	{
		// first step, what direction is it going to be placed
		// 0 is going to be vertical
		// 1 is going to be horizontal
		
		
		boolean canBePlace;
		do
		{
			canBePlace = true;
			int dir = (int)(Math.random()*2);
			// place ship vertical
			if( dir == 0 )
			{
				// lets find a place to put it, generate a random r/c
				int r = (int)(Math.random()*(10-size) );
				int c = (int)(Math.random()*10);
				// System.out.println("dir = " + dir + "    ship = " + ship + "    r = " + r + "   c = " + c );
				for( int row = r ; row < r+size ; row++ )
				{
					if( grid[row][c] != '.')
						canBePlace = false;
				}
				if( canBePlace )
				{
					for( int row = r ; row < r+size ; row++ )
						grid[row][c] = ship;
				}
			}
			else // if( dir == 1 )  - place ship horizontal
			{
				// lets find a place to put it, generate a random r/c
				int r = (int)(Math.random()*10);
				int c = (int)(Math.random()*(10-size) );
				// System.out.println("dir = " + dir + " 
				// ship = " + ship + "    r = " + r + "   c = " + c );
				for( int col = c ; col < c+size ; col++ )
				{
					if( grid[r][col] != '.')
						canBePlace = false;
				}
				if( canBePlace )
				{
					for( int col = c ; col < c+size ; col++ )
						grid[r][col] = ship;
				}
			}
		}
		while( canBePlace == false );
		
	}
	public static void randomCompTurn(char[][] playerGrid)
	{
		int r;
		int c;
		do
		{
			r = (int)(Math.random() * 10);
			c = (int)(Math.random() * 10);
			
		}while(playerGrid[r][c] == 'X' || playerGrid[r][c] == 'O');
		
		lastMove.setLocation(r,c,false);
		
		//lastMove[0] = r;
		//lastMove[1] = c;
		if(playerGrid[r][c] == '.')
		{
			playerGrid[r][c] = 'O';
		}
		else
		{
			playerGrid[r][c] = 'X';
			push(lastMove);
			//startingHit[0] = r;
			//startingHit[1] = c;
		}
	}
	public static void smartCompTurnUp(char[][] playerGrid)
	{
		int r = lastMove.getRow();
		int c = lastMove.getColumn();
		if(r!=0 && playerGrid[r-1][c] != 'X' && playerGrid[r-1][c] != 'O')
		{	
			lastMove.setLocation(r - 1, c, false);
			if(playerGrid[r - 1][c] == '.')
			{
				playerGrid[r - 1][c] = 'O';
				upSuccess = false;
				rightSuccess = true;
			}
			else
			{
				char temp = playerGrid[r - 1][c];
				playerGrid[r - 1][c] = 'X';
				push(lastMove);
				if(sunkCheck(playerGrid , temp))
				{
					pop(temp,'c',c);
					shipCounter++;
				}
			}
		}
		else
		{
			upSuccess = false;
			rightSuccess = true;
			compPlay(playerGrid);
		}
	}
	public static void smartCompTurnRight(char[][] playerGrid)
	{
		int r = lastMove.getRow();
		int c = lastMove.getColumn();
		if(c!=9 && playerGrid[r][c+1] != 'X' && playerGrid[r][c+1] != 'O')
		{	
			lastMove.setLocation(r, c + 1, false);
			if(playerGrid[r][c + 1] == '.')
			{
				playerGrid[r][c + 1] = 'O';
				rightSuccess = false;
				downSuccess = true;
			}
			else
			{
				char temp = playerGrid[r][c + 1];
				playerGrid[r][c + 1] = 'X';
				push(lastMove);
				if(sunkCheck(playerGrid , temp))
				{
					pop(temp,'r',r);
					shipCounter++;
				}
			}
		}
		else
		{
			rightSuccess = false;
			downSuccess = true;
			compPlay(playerGrid);
		}
	}
	public static void smartCompTurnDown(char[][] playerGrid)
	{
		int r = lastMove.getRow();
		int c = lastMove.getColumn();
		if(r!=9 && playerGrid[r+1][c] != 'X' && playerGrid[r+1][c] != 'O')
		{	
			lastMove.setLocation(r + 1, c, false);
			if(playerGrid[r + 1][c] == '.')
			{
				playerGrid[r + 1][c] = 'O';
				downSuccess = false;
				leftSuccess = true;
			}
			else
			{
				char temp = playerGrid[r + 1][c];
				playerGrid[r + 1][c] = 'X';
				push(lastMove);
				if(sunkCheck(playerGrid , temp))
				{
					pop(temp,'c',c);
					shipCounter++;
				}
			}
		}
		else
		{
			downSuccess = false;
			leftSuccess = true;
			compPlay(playerGrid);
		}
	}
	public static void smartCompTurnLeft(char[][] playerGrid)
	{
		int r = lastMove.getRow();
		int c = lastMove.getColumn();
		if(c!=0 && playerGrid[r][c-1] != 'X' && playerGrid[r][c-1] != 'O')
		{	
			lastMove.setLocation(r, c - 1, false);
			if(playerGrid[r][c - 1] == '.')
			{
				playerGrid[r][c - 1] = 'O';
				leftSuccess = false;
			}
			else
			{
				char temp = playerGrid[r][c - 1];
				playerGrid[r][c - 1] = 'X';
				push(lastMove);
				if(sunkCheck(playerGrid , temp))
				{
					pop(temp,'r',r);
					shipCounter++;
				}
			}
		}
		else
		{
			leftSuccess = false;
			lastMove.surround();
			for(int i = 0 ; i < activeHits.size(); i++)
			{
				if(activeHits.get(i).getRow() == lastMove.getRow() && 
					activeHits.get(i).getColumn() == lastMove.getColumn())
					activeHits.get(i).surround();
			}
			compPlay(playerGrid);
		}
	}
	public static void compPlay(char[][] grid)
	{
		if(activeHits.size() == 0)
		{
			randomCompTurn(grid);
		}
		else
		{
			if(grid[lastMove.getRow()][lastMove.getColumn()] == 'O' || 
				lastMove.getSurrounded())
			{
				lastMove.setLocation(check().getRow(), check().getColumn(), false);
				upSuccess = true;
				downSuccess = false;
				rightSuccess =false;
				leftSuccess = false;
			}
			if(upSuccess)
				smartCompTurnUp(grid);
			else if(rightSuccess)
				smartCompTurnRight(grid);
			else if(downSuccess)
				smartCompTurnDown(grid);
			else if(leftSuccess)
				smartCompTurnLeft(grid);
		}
		if(shipCounter == 5)
			gameOver = true;
	}
	public static void humanFill(char[][] grid)
	{
		Scanner r = new Scanner(System.in);
		Scanner d = new Scanner(System.in);
		int row;
		int column;
		String direction;
		do
		{
		System.out.println();
		System.out.println("Hi! Welcome to Battleship, by Grant Hoechst and Matt Loughney!");
		System.out.println("We wrote this during our Senior Year of High School.");
		System.out.println("Hope you enjoy!");
		System.out.println();
		System.out.println("Please enter the coordinates for your destroyer (1 - 10)");
		System.out.println("Row: ");
		row = r.nextInt();
		System.out.println("Column: ");
		column = r.nextInt();
		System.out.println("Direction: ");
		direction = d.nextLine();
		}while(!shipPlacement(row, column, grid, "destroyer", direction));
		printGrid(grid);
		
		do
		{
		System.out.println("Please enter the coordinates for your cruiser");
		System.out.println("Row: ");
		row = r.nextInt();
		System.out.println("Column: ");
		column = r.nextInt();
		System.out.println("Direction: ");
		direction = d.nextLine();
		}while(!shipPlacement(row, column, grid, "cruiser", direction));
		printGrid(grid);
		
		do
		{
		System.out.println("Please enter the coordinates for your submarine");
		System.out.println("Row: ");
		row = r.nextInt();
		System.out.println("Column: ");
		column = r.nextInt();
		System.out.println("Direction: ");
		direction = d.nextLine();
		}while(!shipPlacement(row, column, grid, "submarine", direction));
		printGrid(grid);
		
		do
		{
		System.out.println("Please enter the coordinates for your battleship");
		System.out.println("Row: ");
		row = r.nextInt();
		System.out.println("Column: ");
		column = r.nextInt();
		System.out.println("Direction: ");
		direction = d.nextLine();
		}while(!shipPlacement(row, column, grid, "battleship", direction));
		printGrid(grid);
		
		do
		{
		System.out.println("Please enter the coordinates for your aircraft carrier");
		System.out.println("Row: ");
		row = r.nextInt();
		System.out.println("Column: ");
		column = r.nextInt();
		System.out.println("Direction: ");
		direction = d.nextLine();
		}while(!shipPlacement(row, column, grid, "aircraft carrier", direction));
		printGrid(grid);
	}
	public static boolean shipPlacement(int r, int c, char[][] grid, String boatName, String direction)
	{
		if(r < 1 || r > 10 || c < 1 || c > 10)
		{
			System.out.println("You can't put your ship there. That's not even on the board!");
			return false;
		}
		char l;
		int length;
		if(boatName.compareToIgnoreCase("destroyer") == 0)
		{
			l = 'D';
			length = 2;
		}
		else if(boatName.compareToIgnoreCase("cruiser") == 0)
		{
			l = 'C';
			length = 3;
		}
		else if(boatName.compareToIgnoreCase("submarine") == 0)
		{
			l = 'S';
			length = 3;
		}
		else if(boatName.compareToIgnoreCase("battleship") == 0)
		{
			l = 'B';
			length = 4;
		}
		else
		{
			l = 'A';
			length = 5;
		}
		if(direction.compareToIgnoreCase("up") == 0)
		{
			if(l == 'D' && r > 1)
				for(int i = 0; i < 2; i++)
					grid[r - 1 - i][c - 1] = l;
			else if(l == 'C' && r > 2)
				for(int i = 0; i < 3; i++)
					grid[r - 1 - i][c - 1] = l;
			else if(l == 'S' && r > 2)
				for(int i = 0; i < 3; i++)
					grid[r - 1 - i][c - 1] = l;
			else if(l == 'B' && r > 3)
				for(int i = 0; i < 4; i++)
					grid[r - 1 - i][c - 1] = l;
			else if(l == 'A' && r > 4)
				for(int i = 0; i < 5; i++)
					grid[r - 1 - i][c - 1] = l;
			else
			{
				System.out.println("You can't put your ship there because it goes off the board! Please choose again.");
				return false;
			}
			return true;
		}
		else if(direction.compareToIgnoreCase("right") == 0)
		{
			if(l == 'D' && c < 10)
				for(int i = 0; i < 2; i++)
					grid[r - 1][c - 1 + i] = l;
			else if(l == 'C' && c < 9)
				for(int i = 0; i < 3; i++)
					grid[r - 1][c - 1 + i] = l;
			else if(l == 'S' && c < 9)
				for(int i = 0; i < 3; i++)
					grid[r - 1][c - 1 + i] = l;
			else if(l == 'B' && c < 8)
				for(int i = 0; i < 4; i++)
					grid[r - 1][c - 1 + i] = l;
			else if(l == 'A' && c < 7)
				for(int i = 0; i < 5; i++)
					grid[r - 1][c - 1 + i] = l;
			else
			{
				System.out.println("You can't put your ship there because it goes off the board! Please choose again.");
				return false;
			}
			return true;
		}
		else if(direction.compareToIgnoreCase("down") == 0)
		{
			if(l == 'D' && r < 10)
				for(int i = 0; i < 2; i++)
					grid[r - 1 + i][c - 1] = l;
			else if(l == 'C' && r < 9)
				for(int i = 0; i < 3; i++)
					grid[r - 1 + i][c - 1] = l;
			else if(l == 'S' && r < 9)
				for(int i = 0; i < 3; i++)
					grid[r - 1 + i][c - 1] = l;
			else if(l == 'B' && r < 8)
				for(int i = 0; i < 4; i++)
					grid[r - 1 + i][c - 1] = l;
			else if(l == 'A' && r < 7)
				for(int i = 0; i < 5; i++)
					grid[r - 1 + i][c - 1] = l;
			else
			{
				System.out.println("You can't put your ship there because it goes off the board! Please choose again.");
				return false;
			}
			return true;
		}
		else if(direction.compareToIgnoreCase("left") == 0)
		{
			if(l == 'D' && c > 1)
				for(int i = 0; i < 2; i++)
					grid[r - 1][c - 1 - i] = l;
			else if(l == 'C' && c > 2)
				for(int i = 0; i < 3; i++)
					grid[r - 1][c - 1 - i] = l;
			else if(l == 'S' && c > 2)
				for(int i = 0; i < 3; i++)
					grid[r - 1][c - 1 - i] = l;
			else if(l == 'B' && c > 3)
				for(int i = 0; i < 4; i++)
					grid[r - 1][c - 1 - i] = l;
			else if(l == 'A' && c > 4)
				for(int i = 0; i < 5; i++)
					grid[r - 1][c - 1 - i] = l;
			else
			{
				System.out.println("You can't put your ship there because it goes off the board! Please choose again.");
				return false;
			}
			return true;
		}
		else
		{
			System.out.println("Please type 'down,' 'right,' 'left,' or 'up.'");
			return false;
		}
	}
	public static void humanPlay(char[][] grid)
	{
		Scanner r = new Scanner(System.in);
		Scanner c = new Scanner(System.in);
		System.out.println("What row do you want to guess?");
		int row = r.nextInt();
		System.out.println("What column do you want to guess?");
		int column = c.nextInt();
		if(row < 1 || column < 1 || row > 10 || column >  10)
		{
			System.out.println("Sorry! That's off the board. Try again.");
			humanPlay(grid);
		}
		else if(grid[row - 1][column - 1] == '.')
		{
			grid[row - 1][column - 1] = 'O';
			guesses[row - 1][column - 1] = 'O';
			System.out.println("You missed.");
		}
		else if(grid[row - 1][column - 1] == 'X' || grid[row - 1][column - 1] == 'O')
		{
			System.out.println("You already guessed there!");
			humanPlay(grid);
		}
		else
		{
			System.out.println("You got a hit!");
			char temp = grid[row - 1][column - 1];
			grid[row - 1][column - 1] = 'X';
			guesses[row - 1][column - 1] = 'X';
			if(sunkCheck(grid, temp))
			{
				System.out.println();
				otherShipCounter++;
				if(temp == 'B')
					System.out.println("You sunk the computer's battleship!");
				else if(temp == 'C')
					System.out.println("You sunk the computer's cruiser!");
				else if(temp == 'S')
					System.out.println("You sunk the computer's submarine!");
				else if(temp == 'D')
					System.out.println("You sunk the computer's destroyer!");
				else
					System.out.println("You sunk the computer's aircraft carrier!");
			}
		}
		if(otherShipCounter == 5)
			gameOver = true;
	}
	public static boolean humanIsDumb(char[][] grid)
	{
		int w = 0;
		for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++)
			{
				if(grid[i][j] == '.')
					w++;
			}
		if(w == 83)
		{
			System.out.println("Yay, you didn't overlap your ships!");
			return false;
		}
		else
		{
			System.out.println("You cannot overlap your ships.");
			return true;
		}
	}
}