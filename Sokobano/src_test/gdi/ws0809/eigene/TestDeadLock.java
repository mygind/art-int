package gdi.ws0809.eigene;

//DEPRECATED TEST CASES!!
//SEE DeadlockTest.java
//XXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXX
public class TestDeadLock {
	int _tiles[][] = {
			{4 ,4 ,4 ,4 ,4, 4 },
			{4 ,2 ,2 ,17,2, 4 },
			{4 ,2 ,4 ,17,2 ,4 },
			{4 ,2 ,25,17,2 ,4 },
			{4 ,2 ,4 ,2 ,2 ,2 },
			{4 ,2 ,17,2 ,2 ,2 },
			{4 ,4 ,4 ,4 ,4 ,4 }
	};
	public static final int TILE_FREE = 1;
	public static final int TILE_FLOOR = 2;
	public static final int TILE_WALL = 4;
	public static final int TILE_TARGET = 8;
	public static final int TILE_CRATE = 16;
	public static final int TILE_WORKER = 32;
	int _height = _tiles.length;
	int _width = _tiles[0].length;
	/*
	 * Shows if an Object is Crate or Wall
	 * @param tile a TILE representation of the object
	 * @return true if the object is Crate or Wall 
	 */
	public boolean isWallOrCrate (int tile){
		switch (tile){
		case TILE_FREE+TILE_CRATE: return true;
		case TILE_FREE+TILE_TARGET+TILE_CRATE: return true;
		case TILE_WALL : return true;
		default : return false;
		}
	}
	/*
	 * Looks for a Deadlock in Game
	 * @return true if game is in Deadlock
	 */
	public boolean findDeadlock(){
		for (int i =0; i<_height; i++){
			for (int j=0; j<_width;j++){
				if (_tiles[i][j] == TILE_CRATE+TILE_FREE){
					if( // this part of the loop checks if at least one crate is pushed in a corner
							(_tiles[i+1][j] == TILE_WALL && _tiles[i+1][j] == _tiles[i][j+1])||
							(_tiles[i][j+1] == TILE_WALL && _tiles[i][j+1] == _tiles[i-1][j])||
							(_tiles[i-1][j] == TILE_WALL && _tiles[i-1][j] == _tiles[i][j-1])||
							(_tiles[i][j-1] == TILE_WALL && _tiles[i][j-1] == _tiles[i+1][j])||
							/*
							 * this part of the loop checks if four objects build a square
							 * and at least one of them is a crate that is not on target
							 */
							(isWallOrCrate(_tiles[i][j+1]) && isWallOrCrate(_tiles[i-1][j+1]) && isWallOrCrate(_tiles[i-1][j]))||
							(isWallOrCrate(_tiles[i-1][j]) && isWallOrCrate(_tiles[i-1][j-1]) && isWallOrCrate(_tiles[i][j-1]))||
							(isWallOrCrate(_tiles[i][j-1]) && isWallOrCrate(_tiles[i+1][j-1]) && isWallOrCrate(_tiles[i+1][j]))||
							(isWallOrCrate(_tiles[i+1][j]) && isWallOrCrate(_tiles[i+1][j+1]) && isWallOrCrate(_tiles[i][j+1])))
					{System.out.println("Freeze deadlock found on Crate:["+i+"]["+j+"]");
						return true;}
				}
			}
		}
		System.out.println("No deadlock");
		return false;
	}


public static void main (String[] args){
	new TestDeadLock().findDeadlock();
}
}