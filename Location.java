public class Location
{
	private int row;
	private int column;
	private boolean surrounded;
	
	public Location(int r , int c , boolean surr)
	{
		row = r;
		column = c;
		surrounded = surr;
	}
	
	public int getRow()
	{
		return row;
	}
	public int getColumn()
	{
		return column;
	}
	public boolean getSurrounded()
	{
		return surrounded;
	}
	
	public void setLocation(int r , int c , boolean surr)
	{
		row = r;
		column = c;
		surrounded = surr;
	}
	
	public void surround()
	{
		surrounded = true;
	}
	
	public void printLocation()
	{
		System.out.println("[" + row + " , " + column + "] " + surrounded);
		
	}
}