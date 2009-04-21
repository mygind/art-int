package gdi1sokoban.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

import gdi1sokoban.exceptions.IllegalFormatException;

// This solver does not longer work with the current version of level!
// The current solver is integrated into "level".
public class Solver extends Level {

	public static final int FLAG_CRATE = 4;
	
	public Solver(LinkedList<String> strings)
			throws IllegalFormatException {
		super(strings);
	} 
	
	public LinkedList<Position> findSolution() {
		LinkedList<Position> solution = new LinkedList<Position>();
		
		// Key des Startzustandes bilden und das Feld mit Flags versehen:
		int nCrates = 0;
		for (int x = 1; x < _width - 1; x++) {
			for (int y = 1; y < _height - 1; y++) {
				if (isType(x, y, TILE_CRATE)) {
					nCrates++;
				}
			}
		}
		
		int[] key = new int[nCrates + 1];
		key[0] = _workerX + (_workerY << 8);
		
		int i = 1;
		for (int x = 1; x < _width - 1; x++) {
			for (int y = 1; y < _height - 1; y++) {
				if (isType(x, y, TILE_CRATE)) {
					setFlag(x, y, FLAG_CRATE);
					key[i] = x + (y << 8);
					i++;
				}
			}
		}
		
		for (int depth = 10; depth < 10000; depth*=1.5) {
			System.out.println(depth);
			thisround = 0;
			if (findSolutionNode(solution, 0, depth, new HashMap<IntBuffer, LinkedList<int[]>>(), key, _workerX, _workerY))
				break;
			System.out.println("Erkannte Positionen: " + thisround);
		}
		
		// Cleanup:
		for (int x = 1; x < _width - 1; x++) {
			for (int y = 1; y < _height - 1; y++) {
				removeFlag(x, y, FLAG_CRATE);
			}
		}
		return solution;
	}
	
	public boolean findSolutionNode(LinkedList<Position> solution, int depth, int maxDepth, HashMap<IntBuffer, LinkedList<int[]>> keys, int[] key, int wx, int wy) {
	
		// Alle Crates auf Verschiebbarkeit testen:
		for (int i = 1; i < key.length; i++) {

		 	// Pruefe alle Verschiebungen auf Lösung:
        	if (findSolutionPush(solution, depth, maxDepth, keys, key, i, 1, 0, wx, wy)) return true;
        	if (findSolutionPush(solution, depth, maxDepth, keys, key, i, 0, 1, wx, wy)) return true;
        	if (findSolutionPush(solution, depth, maxDepth, keys, key, i, -1, 0, wx, wy)) return true;
        	if (findSolutionPush(solution, depth, maxDepth, keys, key, i, 0, -1, wx, wy)) return true;
		}
		
		// Es wurde hier und in den Unterbäumen keine Lösung gefunden:
		return false;
	}
	
	// Crate verschiebar:
	static int avoided = 0;
	static int done = 0;
	static int thisround = 0;
	public boolean findSolutionPush(LinkedList<Position> solution, int depth, int maxDepth, HashMap<IntBuffer, LinkedList<int[]>> keys, int[] parentKey, int index, int dirX, int dirY, int wx, int wy) {
		
		int x = parentKey[index] & 0xFF;
		int y = parentKey[index] >> 8;
		
		if (isType(x + dirX, y + dirY, TILE_WALL) ||
			isFlag(x + dirX, y + dirY, FLAG_CRATE)) return false;
		
		int length = existsPath(wx, wy, x - dirX, y - dirY);
		if ((length == -1) || (depth + length > maxDepth)) return false;

			// Verschiebung auf Spielfeld eintragen:
		    removeFlag(x, y, FLAG_CRATE);
		    setFlag(x + dirX, y + dirY, FLAG_CRATE);
		    
		    // Auf Deadlock testen:
		    if (!(!isType(x + dirX, y + dirY, TILE_TARGET) &&
		    	(isDeadCorner(x + dirX, y + dirY) || isDeadSquareA(x + dirX, y + dirY) || isDeadWall(x + dirX, y + dirY, x + dirX*2, y + dirY*2)))) {
				
		    	// Neuen Key bilden:
			    int[] key = copyKey(parentKey);
			    key = buildKey(key, index, x+dirX, y+dirY);
			    
			    // Verschiebung der Crate in Key eintragen:
			    key[0] = 0;//x + (y << 8);
			    
				// Testen, ob Key schon existiert:
			    IntBuffer keyBuffer = IntBuffer.wrap(key);
			    LinkedList<int[]> positions = keys.get(keyBuffer);
			   
			    //if ((positions == null) || (depth + length < positions.getFirst()[0] - 10) || (existsPath(positions.getFirst()[1] & 0xFF, positions.getFirst()[1] >> 8, x, y) != -1)) {
		    	/*if (positions == null) {
	    			positions = new LinkedList<int[]>();
	    			positions.add(new int[] {length + depth, x + (y << 8)});
	    		}
	    		else {
	    			positions.getFirst()[0]=length + depth;
	    			positions.getFirst()[1]=x + (y << 8);
	    		}*/
			    
			    boolean positionFound = false;
			    boolean positionIsOld = false;
			    
			    // Wenn es diesen Key schonmal gegeben hat:
			    if ((positions != null)) {
			    	
			    	// Prüfe, ob der Key auch diese Situation beschreibt:
			    	for (int[] position : positions) {
			    		positionFound = (existsPath(position[1] & 0xFF, position[1] >> 8, x, y) != -1);
			    		if (positionFound && ((depth + length) < (position[0] - maxDepth / 10))) {
			    			
			    			// Position existiert, aber veraltet:
			    			positionFound = false;
			    			positionIsOld = true;
			    			position[0] = length + depth;
			    			position[1] = x + (y << 8);
			    			break;
			    		}
			    		if (positionFound) {
			    			// Position existiert:
			    			break;
			    		}
			    	}
			    	
			    	// Nein, also neue Situation einfügen:
			    	if (!positionFound && !positionIsOld) {
			    		thisround++;
			    		positions.add(new int[] {length + depth, x + (y << 8)});
			    		keys.put(keyBuffer, positions);
			    	}
			    }
				// Neuen Key in keyMap eintragen:
			    else {
			    	thisround++;
			    	positions = new LinkedList<int[]>();
			    	positions.add(new int[] {length + depth, x + (y << 8)});
			    	keys.put(keyBuffer, positions);
			    }
		    		

		    	if (!positionFound) {

					// Auf Lösung testen:
					if (isType(x + dirX, y + dirY, TILE_TARGET) &&
				        isSolved(key)) {
				    	System.out.println("Lösung gefunden!");
				    	System.out.println("Züge: " + (length + depth));
						System.out.println("Züge getestet: " + done);
						System.out.println("Züge vermieden: " + avoided);
				    	solution.add(new Position(x - dirX, y - dirY));
				    	solution.add(new Position(x, y));
				    	return true; 
				    }

				    // Lösungspfad erweitern:
				    solution.addLast(new Position(x - dirX, y - dirY));
				    solution.add(new Position(x, y));
				    if (findSolutionNode(solution, depth + length, maxDepth, keys, key, x, y)) return true;
				    solution.removeLast();
				    solution.removeLast();
				    
				    done++;
			    }
			    else avoided++;
			} 
			setFlag(x, y, FLAG_CRATE);
			removeFlag(x + dirX, y + dirY, FLAG_CRATE);
		
		// Es wurde in diesem Unterbaum keine Lösung gefunden:
		return false;
	}
	
	public boolean isSolved(int[] key) {
		
		for (int i = 1; i < key.length; i++) {
			int x = key[i] & 0xFF;
			int y = key[i] >> 8;
				if (!isType(x, y, TILE_TARGET)) return false;
		}
		
		return true;
	}
	
	public int[] copyKey(int[] parentKey) {
		
		// ParentKey kopieren:
		return parentKey.clone();
	}
	
	public int[] buildKey(int[] key, int index, int x, int y) {
		
		// Finde Index des nächstgrößeren Elementes:
		int successor;
		for (successor = 1; successor < key.length; successor++) {
			int xcoord = key[successor] & 0xFF;
			int ycoord = key[successor] >> 8;
			if (xcoord > x) break;
			if ((xcoord == x) && (ycoord > y)) break;
			if ((xcoord == x) && (ycoord == y)) {
				key[successor] = x + (y << 8);
				return key;
			}
		}
		
		// Neuer Index ist kleiner als alter:
		if (index < successor) {
			
			System.arraycopy(key, index + 1, key, index, successor - index - 1);
			key[successor - 1] = x + (y << 8);
		}
		else if (index > successor) {
			System.arraycopy(key, successor, key, successor + 1, index - successor);
			key[successor ] = x + (y << 8);
		}
		else {
			key[successor] = x + (y << 8);
		}
		
		return key;
	}

	private boolean isDeadSquareA(int x, int y) {
		return ((isType(x, y+1, TILE_WALL)||isFlag(x, y+1, FLAG_CRATE)) && (isType(x-1, y, TILE_WALL)||isFlag(x-1, y, FLAG_CRATE)) && (isType(x-1, y+1, TILE_WALL)||isFlag(x-1, y+1, FLAG_CRATE)))||
			   ((isType(x-1, y, TILE_WALL)||isFlag(x-1, y, FLAG_CRATE))  && (isType(x-1, y-1, TILE_WALL)||isFlag(x-1, y-1, FLAG_CRATE)) && (isType(x, y-1, TILE_WALL)||isFlag(x, y-1, FLAG_CRATE)))||
			   ((isType(x, y-1, TILE_WALL)||isFlag(x, y-1, FLAG_CRATE))  && (isType(x+1, y-1, TILE_WALL)||isFlag(x+1, y-1, FLAG_CRATE)) && (isType(x+1, y, TILE_WALL)||isFlag(x+1, y, FLAG_CRATE)))||
			   ((isType(x+1, y, TILE_WALL)||isFlag(x+1, y, FLAG_CRATE))  && (isType(x+1, y+1, TILE_WALL)||isFlag(x+1, y+1, FLAG_CRATE)) && (isType(x, y+1, TILE_WALL)||isFlag(x, y+1, FLAG_CRATE)));
	}
	
	public int existsPath(int x, int y, int targetX, int targetY) {

		if (isType(targetX, targetY, TILE_WALL)) return -1;
		
        Position target = new Position(targetX, targetY);

        PriorityQueue<PathFinderNode> openQueue = new PriorityQueue<PathFinderNode>();
        HashMap<Long, PathFinderNode> openMap = new HashMap<Long, PathFinderNode>();
        
        LinkedList<PathFinderNode> cleanup = new LinkedList<PathFinderNode>();
        
        PathFinderNode start = new PathFinderNode(new Position(x, y), target);
        
        // Open set contains the initial node:
        openQueue.add(start);
        
        // The lightest node from the open set:
        PathFinderNode lightest = null;
        
        while (!openQueue.isEmpty()) {
        	
            // Get the open node with the lowest fScore:
            lightest = openQueue.poll();

            // Path found:
            if (lightest.getPosition().equals(target)) break;

            // Flag the node as closed / done:
            cleanup.add(lightest);
            setFlag(lightest.getPosition().getX(), lightest.getPosition().getY(), FLAG_CLOSED);
           
            // Visit all but closed neighbors:
            for (int i = 1; i <= 4; i++) {
            	
            	// Martin's fancy little trick of getting neighbor coordinates
            	x = lightest.getPosition().getX() + ((i - 2) % 2);
                y = lightest.getPosition().getY() + ((5 - i - 2) % 2);
            	 
            	if (!isFlag(x, y, FLAG_CLOSED) && !isType(x, y, TILE_WALL) && !isFlag(x, y, FLAG_CRATE)) {

                    PathFinderNode neighbor = null;
                    
                    int tentativeG = lightest.getGScore() + 1; // + distance(lightest, x, y);
                    
                    if (!isFlag(x, y, FLAG_OPEN)) {

                        neighbor = new PathFinderNode(new Position(x, y), target);
                        neighbor.setParent(lightest);
                        neighbor.setGScore(tentativeG);
                        
                        // Add neighbor to open set:
                        openQueue.add(neighbor);
                        openMap.put(x + ((long)y << 16), neighbor);
                        cleanup.add(neighbor);
                        setFlag(x, y, FLAG_OPEN);
                    }
                    else {
                    	neighbor = openMap.get(x + ((long)y << 16));
                    	
                    	if (tentativeG < neighbor.getGScore()) { 
                    		neighbor.setParent(lightest);
                    		neighbor.setGScore(tentativeG);
                    	}
                    }
                }
            }
        }
        
        // Cleanup:
        for (PathFinderNode node : cleanup) {
        	removeFlag(node.getPosition().getX(), node.getPosition().getY(), FLAG_OPEN + FLAG_CLOSED);
        }
        
        if (lightest.getPosition().equals(target))
        	return calcPathSize(lightest);
        
        // No path found:
        return -1;
	}
	
	private int calcPathSize(PathFinderNode node) {

		int result = 0;
		
		while (node.getParent() != null) {
			result++;
			node = node.getParent();
		}
		result++;

		return result;
	}
	
	public void play(LinkedList<Position> solution) {
		
		removeType(_workerX, _workerY, TILE_WORKER);
		
		for (Position p : solution) {
			
			int dirX =  p.getX()-_workerX;
			int dirY =  p.getY()-_workerY;
			
			if (isType(p.getX(), p.getY(), TILE_CRATE)) {
				removeType(p.getX(), p.getY(), TILE_CRATE);
				setType(p.getX() + dirX, p.getY() + dirY, TILE_CRATE);
			}
			
			_workerX = p.getX();
			_workerY = p.getY();
			
			// Visualisieren:
			for (int y = 0; y < _height; y++) {
				for (int x = 0; x < _width; x++) {
					if (x == _workerX && y == _workerY) System.out.print('@');
					else System.out.print(convertTileToChar(_tiles[y][x]));
				}
				System.out.println();
			}
			System.out.println();
			
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	public static void main(String[] args) {
		LinkedList<String> strings = new LinkedList<String>();
		
		/*strings.add("#######"); // 16 Moves
		strings.add("#.@ # #");
		strings.add("#$* $ #");
		strings.add("#   $ #");
		strings.add("# ..  #");
		strings.add("#  *  #");
		strings.add("#######");*/
		
		/*strings.add("##########"); // 28 Moves
		strings.add("#@   $  .#");
		strings.add("#  $. .$ #");
		strings.add("##########");*/
		
		/*strings.add("####  "); // 33 Moves
		strings.add("# .#  ");
		strings.add("#  ###");
		strings.add("#*@  #");
		strings.add("#  $ #");
		strings.add("#  ###");
		strings.add("####  ");*/
		
		/*strings.add(" #####    "); // 61 Moves
		strings.add("## @ #### ");
		strings.add("#  #  . ##");
		strings.add("# #      #");
	 	strings.add("# $$ #.  #");
	 	strings.add("##    ####");
		strings.add(" ##   #   ");
		strings.add("  #####   ");*/
		
		/*strings.add(" ############# "); // 60 < Moves < 70
		strings.add("##           # ");
		strings.add("# # $      $# #");
		strings.add("#    #.@.#    #");
		strings.add("## #$ . . $ # #");
		strings.add(" #   # # ##   #");
		strings.add(" #####    #####");
		strings.add("     ######    ");*/
		
		/*strings.add("     #####"); // 84 Moves
		strings.add(" #####   #");
		strings.add("##  +  # #");
	    strings.add("# $$.$$  #");
	    strings.add("#  #.# ###");
	    strings.add("##  .  #  ");
	    strings.add(" #######  ");*/
	    
	    strings.add("   ###      "); // 109 < Moves < 150
	    strings.add("  ## # #### ");
	    strings.add(" ##  ###  # ");
	    strings.add("## $      # ");
	    strings.add("#   @$ #  # ");
	    strings.add("### $###  # ");
	    strings.add("  #  #..  # ");
	    strings.add(" ## ##.# ## ");
	    strings.add(" #      ##  ");
	    strings.add(" #     ##   ");
	    strings.add(" #######    ");
		
	   /* strings.add("         #####"); // 170 < Moves < 225
	    strings.add("         #   #");
	    strings.add("########## # #");
	    strings.add("#.     #  $  #");
	    strings.add("#.  @  #    ##");
	    strings.add("#.# #######  #");
	    strings.add("#         $$ #");
	    strings.add("##  #####    #");
	    strings.add(" ####   ######");*/
		
		/*strings.add("#######  "); // 789 < Moves < 1228
		strings.add("#.... ###");
		strings.add("#..##$$ #");
		strings.add("#.    $ #");
		strings.add("#   $$$@#");
		strings.add("#####  ##");
		strings.add("  #  $ # ");
		strings.add("  #   ## ");
		strings.add("  #####  ");*/
		
		/*strings.add("     ##### ##################     "); // Moves < 620 
	 	strings.add("    #     #                  #    ");
	 	strings.add("   #  #  #  ################  #   ");
	 	strings.add("  #  #  #  #                #  #  ");
	 	strings.add(" #  #  #  #  ##############  #  # ");
	 	strings.add("#  #  #  #  #              #  #  #");
	 	strings.add("# #  #  #  #  ############  #  # #");
	 	strings.add("# # #  #  #  #            #  #   #");
	 	strings.add("# # # #  #  #  ##########  #  #  #");
	 	strings.add("# # # # #  #  #          #  #  # #");
	 	strings.add("# # # # # #  #  ########  #  #  # ");
	 	strings.add("# # # # # # #  #        #  #  #  #");
	 	strings.add("# # # # # # # #  ######  #  #  # #");
	 	strings.add("# # # # # # # # #      #  #  # # #");
	 	strings.add("# # # # # # # # # ####  #  # # # #");
	 	strings.add("# # # # # # # #       #  # # # # #");
	 	strings.add("# # # # # # #   ## ##  # # # # # #");
	 	strings.add("# # # # # #  ## ##   # # # # # # #");
	 	strings.add("# # # # #  #    @.$# # # # # # # #");
	 	strings.add("# # # #  #  #### # # # # # # # # #");
	 	strings.add("# # #  #  #      # # # # # # # # #");
	 	strings.add("# #  #  #  ######  # # # # # # # #");
	 	strings.add("#  #  #  #        #  # # # # # # #");
	 	strings.add(" #  #  #  ########  #  # # # # # #");
	 	strings.add("# #  #  #          #  #  # # # # #");
	 	strings.add("#  #  #  ##########  #  #  # # # #");
	 	strings.add("#   #  #            #  #  #  # # #");
	 	strings.add("# #  #  ############  #  #  #  # #");
	 	strings.add("#  #  #              #  #  #  #  #");
	 	strings.add(" #  #  ##############  #  #  #  # ");
	 	strings.add("  #  #                #  #  #  #  ");
	 	strings.add("   #  ################  #  #  #   ");
	 	strings.add("    #                  #     #    ");
	 	strings.add("     ################## #####     ");*/

		/*strings.add("     #######     "); // 49 < Moves
		strings.add("######     ######");
	    strings.add("#  . ..$#$.. .  #");
	    strings.add("#  $ $  .  $ $  #");
	    strings.add("###$####@####$###");
	    strings.add("#  $ $  .  $ $  #");
	    strings.add("#  . ..$#$.. .  #");
	    strings.add("######     ######");
	    strings.add("     #######     ");*/
	
		/*strings.add("            ####  "); 49 < Moves
		strings.add("           ##  ###");
		strings.add("          ##  @  #");
		strings.add("         ##  *$ .#");
		strings.add("        ##  **  ##");
		strings.add("       ##  **  ## ");
		strings.add("      ##  **  ##  ");
		strings.add("     ##  **  ##   ");
		strings.add("    ##  **  ##    ");
		strings.add("   ##  **  ##     ");
		strings.add("  ##  **  ##      ");
		strings.add(" ##  **  ##       ");
		strings.add("##  **  ##        ");
		strings.add("#  **  ##         ");
		strings.add("# **  ##          ");
		strings.add("#  ####           ");
		strings.add("####              ");*/
	   
	    /*strings.add("   ######          "); // 33 < Moves
	    strings.add("   #....###########");
	    strings.add(" ###**.*.         #");
	    strings.add(" #  .*.###### $ $ #");
	    strings.add(" # #.*.....#### ###");
	    strings.add(" # # #$##.*.   $  #");
	    strings.add(" # # # $ .*.# #$# #");
	    strings.add(" #  $   ##..#$#   #");
	    strings.add(" # $.##  ###  #$$ #");
	    strings.add(" ## ### $  # ##   #");
	    strings.add(" #      #  $ .### #");
	    strings.add(" ##### #  $# $$ # #");
	    strings.add("     # #   # $  # #");
	    strings.add("     # ##$##  # # #");
	    strings.add("     #      $$$ $ #");
	    strings.add("     #######     @#");
	    strings.add("           ########");*/

		/*strings.add("   ####                       "); // Godlike
		strings.add("  ##  #############           ");
		strings.add(" ##    .......... #           ");
		strings.add("##    #  ####$### ##          ");
		strings.add("#      # #      #  ##         ");
		strings.add("#     #  # $$$  # # #####     ");
		strings.add("#####  # . .# ### . . . ##    ");
		strings.add("    #   $. .#  # $$ $ $ @#    ");
		strings.add("   ####### ### #   #######    ");
		strings.add("   # $   $   # ## ###         ");
		strings.add("   #    $  $ #  # ## # ####   ");
		strings.add("   # $### ####  # # # ##  ####");
		strings.add("   #  $ $$$  # ## #  ##      #");
		strings.add("   ##        # ## #### $$$   #");
		strings.add("    ## ####### .....     #####");
		strings.add("    #  $         ######  #    ");
		strings.add("    #   ######  ##    ####    ");
		strings.add("    #####    ####             ");*/

		Solver blob = null;
		try {
			blob = new Solver(strings);
			LinkedList<String> result = blob.toStringList();
			assertEquals(strings.toString(), result.toString());
		} catch (Exception exception) {
			exception.printStackTrace();
			fail();
		}

		LinkedList<Position> solution = blob.findSolution();
		System.out.println(solution);
		blob.play(solution);
	}
}
